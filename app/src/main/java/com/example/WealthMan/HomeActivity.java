package com.example.WealthMan;
/*
Small test to see what is needed to use API to get stock information

https://iextrading.com/developer/docs/#batch-requests
next steps:
    parse JSON
    display needed data
    create chart from JSON data
    improve input
    FIX crash when stock symbol is bad

 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.WealthMan.APIInterface.*;
import com.example.WealthMan.detail.view.DetailActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity {
    private ArrayList<IconBean> mIconBeenList = new ArrayList<>();
    private ListView lv;
    private String latestprice;
    private IconAdapter sa;
    private DatabaseHelper DatabaseHelper;
    //public String [] data = {"apple","apple","orange","watermelon","peat","grape","pineapple","strawberry","cherry","mango"};
    EditText mTextURI;
    Button mButtonOk;
//    Button mButtonTest;
    ArrayList<WatchListData> wl_data = new ArrayList<>();

    DatabaseHelper db;
    boolean dbsuccess = true;
    public static final String MY_PREFS_FILE = "wealthman_prefs";

//    final TextView mTextView = (TextView)findViewById(R.id.text);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        final int userid = intent.getIntExtra("UserId", 1);
        db = new DatabaseHelper(this);
        SharedPreferences preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        long now = System.currentTimeMillis();
        long updateDue = preference.getLong("updateDue", 0);
//        if (false) {  // Calc next time to update
        System.out.println("After Read Prefs: Now = " + now + " , Due = " + updateDue);
        if (updateDue < now) {  // Calc next time to update
            System.out.println("Symbol Update due");
            updateDue = now + TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
            if (updateSymbols()) {
                editor.putLong("updateDue", updateDue);   // Store new time to update
                editor.commit();
                System.out.println("At commit: Now = " + now + " , Due = " + updateDue);
//                System.out.println("Update was saved");
            } else
                Toast.makeText(HomeActivity.this, "Error updating Stock Symbols", Toast.LENGTH_LONG).show();
        } else
            System.out.println("No Symbol Update is due...");
        if (!preference.getBoolean("setupDone", false)) {
            long res = db.createWatchlist();  //special one time add
            if (res > 0) {
                editor.putBoolean("setupDone", true);
                editor.commit();
            } else
                Toast.makeText(HomeActivity.this, "Database Error creating Watch List", Toast.LENGTH_LONG).show();
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ativity_test_watchlist);
        lv = (ListView)findViewById(R.id.lv);
        //为listview添加adapter
        lv.setAdapter(new IconAdapter(this,mIconBeenList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                IconBean stock = mIconBeenList.get(position);
                Intent intent = new Intent(HomeActivity.this,com.example.WealthMan.detail.view.DetailActivity.class);
                intent.putExtra("Symbol",stock.symbol);
                intent.putExtra("UserID",userid);
                startActivity(intent);
            }
        });


/*
//        IconAdapter adapter = new IconAdapter(
//                MainActivity.this,R.layout.lv_item,data);
//        ListView listView = lv;
///////////////////*/
//        setContentView(R.layout.activity_home);

        mButtonOk = (Button) findViewById(R.id.button);
//        mButtonTest = (Button) findViewById(R.id.button2);
        mTextURI = (EditText) findViewById(R.id.url_to_fetch);
//        mTextURI.setText(db.getWatchList());
        final TextView mTextView = (TextView) findViewById(R.id.text);
       mTextURI.append("");
        final RequestQueue queue = Volley.newRequestQueue(this);

////        mTextView.setMovementMethod(new ScrollingMovementMethod());
// Gson

//        String symbols = mTextURI.getText().toString().trim();
        String symbols = db.getWatchList().trim();
        // Instantiate the RequestQueue.
//                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=chart&range=1m&last=5";
//                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=quote,news,chart&range=6m";
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + symbols + "&types=quote";
//          Sql query should be like SELECT GROUP_CONCAT(symbol SEPARATOR ',')
//              or SELECT GROUP_CONCAT(symbol)

//                String url ="https://api.iextrading.com/1.0/stock/"+ symbols +"/quote";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("{}"))
                            Toast.makeText(HomeActivity.this, "Nothing Returned", Toast.LENGTH_LONG).show();
    ////                        mTextView.setText(mTextView.getText() + ("No Data returned.  Did you enter a valid stock symbol?"));
                        else {
        ////                    mTextView.setText("");
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(Batches.class, new CompanyListDeserializer());
                            Batches myData = gsonBuilder.create().fromJson(response, Batches.class);
                            Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                            String json = gsonPretty.toJson(myData);
                            System.out.println(json);
                            initData(myData);

                            //System.out.println("JSON = " + json);
/*                            for (int index = 0; index < myData.batches.size(); index++) {
         ////                       mTextView.append(Integer.toString(index));
         ////                       mTextView.append(" ");
         ////                       mTextView.append(myData.batches.get(index).quote.symbol);
         ////                       mTextView.append(" ");
         ////                       mTextView.append(Float.toString(myData.batches.get(index).quote.latestPrice));
         ////                       mTextView.append(" ");
                                if (myData.batches.get(index).quote.change < 0) {
                                    //mTextView.setTextColor(Color.parseColor("#FF0000"));
         ////                           mTextView.append(Float.toString(myData.batches.get(index).quote.change));
                                } else {
                                    //mTextView.setTextColor(Color.parseColor("#00FF00"));
         ////                           mTextView.append(Float.toString(myData.batches.get(index).quote.change));
                                }
         ////                       mTextView.append("\n");
                                //mTextView.append(json);

                                //mTextView.setTextColor(Color.parseColor("#000000"));
                            }*/
                            //mTextView.setText("Response is: " + response);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
         ////       mTextView.setText("That didn't work! Do you have an internet connection?");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);


        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = getIntent();
//                String str = intent.getStringExtra("Symbol");
                // Enable the following to go to Detail Activity and retrieve the Symbol with the above lines

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                String symbol = mTextURI.getText().toString().trim();
                intent.putExtra("Symbol", symbol);
                intent.putExtra("UserID", userid);
                startActivity(intent);
            }
        });
//        mButtonTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = getIntent();
////                String str = intent.getStringExtra("Symbol");
//                // Enable the following to go to Detail Activity and retrieve the Symbol with the above lines
//
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                String symbol = mTextURI.getText().toString().trim();
//                intent.putExtra("Symbol", symbol);
//                intent.putExtra("UserID", userid);
//                startActivity(intent);
//            }
//        });



//        mButtonOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = getIntent();
////                String str = intent.getStringExtra("Symbol");
//                // Enable the following to go to Detail Activity and retrieve the Symbol with the above lines
//
//                Intent intent = new Intent(getApplicationContext(), com.example.WealthMan.detail.view.DetailActivity.class);
//                String symbol = mTextURI.getText().toString().trim();
//                intent.putExtra(DetailActivity.SYMBOL_NAME, symbol);
//                intent.putExtra("Symbol", symbol);
//                intent.putExtra("UserID", userid);
//                startActivity(intent);
//            }
//        });

    }

    public boolean updateSymbols() {

        final RequestQueue queue = Volley.newRequestQueue(this);
        System.out.println("Updating Symbols now");
        String symbolUrl = "https://api.iextrading.com/1.0/ref-data/symbols";
        final GsonBuilder gsonSymbols = new GsonBuilder();
        dbsuccess = false;
        StringRequest symbolRequest = new StringRequest(Request.Method.GET, symbolUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        stockSym[] mySyms = gsonSymbols.create().fromJson(response, stockSym[].class);
                        // Make two DB entries, one for symbol-symbol lookup
                        // Other for name-symbol lookup
                        System.out.println("Length = " + mySyms.length);
                        long val = 0;
                        for (int i = 0; i < mySyms.length; i++) {
                            val += db.addSymbol(mySyms[i].symbol, mySyms[i].symbol);
                            val += db.addSymbol(mySyms[i].name, mySyms[i].symbol);
                            //System.out.println(mySyms[i].name +"   ,  " +mySyms[i].symbol);
                        }
//                if (val <= mySyms.length) {
//                    Toast.makeText(HomeActivity.this, "Symbol Database Error", Toast.LENGTH_LONG).show();
//                    dbsuccess = false;
//                }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "That didn't work! Do you have an internet connection?", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(symbolRequest);
        return true;
    }

    public void getData(String jsonData) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Batches.class, new CompanyListDeserializer());
        Batches watchList = gsonBuilder.create().fromJson(jsonData, Batches.class);
//        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(watchList);
//        String json = gsonPretty.toJson(watchList);
//        System.out.println("JSON = " + json);
        for (int index = 0; index < watchList.batches.size(); index++) {
            System.out.println(index + "\t" + watchList.batches.get(index).quote.symbol + "\t" + watchList.batches.get(index).quote.latestPrice + "\t" + watchList.batches.get(index).quote.change);
            for (int i = 0; i < watchList.batches.size(); i++) {
                WatchListData temp = new WatchListData();
                temp.setChange(watchList.batches.get(i).quote.change);
                temp.setPrice(watchList.batches.get(i).quote.latestPrice);
                temp.setName(watchList.batches.get(i).quote.companyName);
                temp.setSymbol(watchList.batches.get(i).quote.symbol);
                wl_data.add(temp);
            }
        }
    }

    private void initData(Batches jsonData) {

        for (int i = 0 ; i < jsonData.batches.size(); i++) {
//            WatchListData temp = new WatchListData();
//            temp.setChange(jsonData.quotes.get(i).change);
//            temp.setPrice(jsonData.quotes.get(i).latestPrice);
//            temp.setName(jsonData.quotes.get(i).companyName);
//            temp.setSymbol(jsonData.quotes.get(i).symbol);
//            System.out.println();
            IconBean symbol = new IconBean(
                    jsonData.batches.get(i).quote.symbol.trim(),
                    jsonData.batches.get(i).quote.companyName.trim(),
                    jsonData.batches.get(i).quote.latestPrice,
                    jsonData.batches.get(i).quote.change
                );
//            IconBean symbol = new IconBean("FB","FaceBook",7.77,0.89);
            mIconBeenList.add(symbol);
        }
//        symbol = new IconBean("AAPL","Apple Inc.",140.63,-0.09);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        mIconBeenList.add(symbol);
//        System.out.println(jsonData);
//        private View show_list(){
//        List<String> data_list = new ArrayList<>(Arrays.asList(data));
//        ArrayAdapter<String> data_adapter = new ArrayAdapter<>(this,R.layout.lv_item,data_list);
//        ListView data_view = (ListView)this.findViewById(R.id.list_view);
//        data_view.setAdapter(data_adapter);
//        return data_view;
    }
}
