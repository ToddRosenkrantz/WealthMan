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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.WealthMan.APIInterface.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity {
    EditText mTextURI;
    Button mButtonOk;

    DatabaseHelper db;
    boolean dbsuccess = true;
    public static final String MY_PREFS_FILE = "wealthman_prefs";

//    final TextView mTextView = (TextView)findViewById(R.id.text);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        int userid = intent.getIntExtra("UserId", 0);
        db = new DatabaseHelper(this);
        SharedPreferences preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        long now = System.currentTimeMillis();
        long updateDue = preference.getLong("updateDue", 0);
//        if (false) {  // Calc next time to update
        System.out.println("After Read Prefs: Now = "+ now + " , Due = " + updateDue);
        if (updateDue < now) {  // Calc next time to update
            System.out.println("Symbol Update due");
            updateDue = now + TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
            if (updateSymbols()) {
                editor.putLong("updateDue", updateDue);   // Store new time to update
                editor.commit();
                System.out.println("At commit: Now = "+ now + " , Due = " + updateDue);
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
        setContentView(R.layout.activity_home);

        mButtonOk = (Button) findViewById(R.id.button);
        mTextURI = (EditText) findViewById(R.id.url_to_fetch);
//        mTextURI.setText(db.getWatchList());
        final TextView mTextView = (TextView) findViewById(R.id.text);
        mTextURI.append("");
        final RequestQueue queue = Volley.newRequestQueue(this);

        mTextView.setMovementMethod(new ScrollingMovementMethod());
// Gson
        final GsonBuilder gsonBuilder = new GsonBuilder();
//        String symbols = mTextURI.getText().toString().trim();
        String symbols = db.getWatchList().trim();
        // Instantiate the RequestQueue.
//                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=chart&range=1m&last=5";
//                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=quote,news,chart&range=6m";
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + symbols + "&types=quote,news,chart&last=5";
//          Sql query should be like SELECT GROUP_CONCAT(symbol SEPARATOR ',')
//              or SELECT GROUP_CONCAT(symbol)

//                String url ="https://api.iextrading.com/1.0/stock/"+ symbols +"/quote";
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("{}"))
                            mTextView.setText(mTextView.getText() + ("No Data returned.  Did you enter a valid stock symbol?"));
                        else {
                            mTextView.setText("");
                            gsonBuilder.registerTypeAdapter(Batches.class, new CompanyListDeserializer());
                            Batches myData = gsonBuilder.create().fromJson(response, Batches.class);
                            for (int index = 0; index < myData.batches.size(); index++) {
                                mTextView.append(Integer.toString(index));
                                mTextView.append(" ");
                                mTextView.append(myData.batches.get(index).quote.symbol);
                                mTextView.append(" ");
                                mTextView.append(Float.toString(myData.batches.get(index).quote.latestPrice));
                                mTextView.append(" ");
                                if (myData.batches.get(index).quote.change < 0) {
                                    //mTextView.setTextColor(Color.parseColor("#FF0000"));
                                    mTextView.append(Float.toString(myData.batches.get(index).quote.change));
                                } else {
                                    //mTextView.setTextColor(Color.parseColor("#00FF00"));
                                    mTextView.append(Float.toString(myData.batches.get(index).quote.change));
                                }
                                mTextView.append("\n");
                                Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                                String json = gsonPretty.toJson(myData);

                                //System.out.println("JSON = " + json);
                                //mTextView.append(json);

                                //mTextView.setTextColor(Color.parseColor("#000000"));
                            }
                            //mTextView.setText("Response is: " + response);

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work! Do you have an internet connection?");
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

                Intent intent = new Intent(getApplicationContext(), com.example.WealthMan.detail.view.DetailActivity.class);
                String symbol = mTextURI.getText().toString().trim();
                intent.putExtra("SYMBOL_NAME", symbol);
                startActivity(intent);
            }
        });
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
                    stockSym[] mySyms = gsonSymbols.create().fromJson(response,stockSym[].class);
                    // Make two DB entries, one for symbol-symbol lookup
                    // Other for name-symbol lookup
                    System.out.println("Length = " + mySyms.length);
                    long val = 0;
                    for (int i = 0 ; i < mySyms.length ; i++) {
                        val += db.addSymbol(mySyms[i].symbol,mySyms[i].symbol);
                        val += db.addSymbol(mySyms[i].name,mySyms[i].symbol);
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
        }
    }
}
