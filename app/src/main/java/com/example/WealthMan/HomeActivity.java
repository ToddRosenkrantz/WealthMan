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
import android.app.DownloadManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
import com.example.WealthMan.APIInterface;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity {
    EditText mTextURI;
    Button mButtonOk;
//    final TextView mTextView = (TextView)findViewById(R.id.text);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
/*        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
            long now  = System.currentTimeMillis();
            if (preference.getLong("updateDue", 0) < now){
                // calculate next update time
                long thirtyDays = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
                // update TABLE and update SharedPrefs
                // update call
                if(updateSymbols())
                    // Success means we can update the sharedPreference
                    preference.edit().putLong("updateDue", thirtyDays);
                else
                    // WE had an error
                    Toast.makeText(HomeActivity.this, "Error updating Stock Symbols", Toast.LENGTH_LONG).show();

            }*/

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mButtonOk = (Button)findViewById(R.id.button);
        mTextURI = (EditText)findViewById(R.id.url_to_fetch);
        final TextView mTextView = (TextView)findViewById(R.id.text);
        mTextURI.append("");
        final RequestQueue queue = Volley.newRequestQueue(this);

        mTextView.setMovementMethod(new ScrollingMovementMethod());
// Gson
        final GsonBuilder gsonBuilder = new GsonBuilder();
// ...
        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String symbols = mTextURI.getText().toString().trim();
                // Instantiate the RequestQueue.
//                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=chart&range=1m&last=5";
//                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=quote,news,chart&range=6m";
                String url ="https://api.iextrading.com/1.0/stock/market/batch?symbols="+ symbols +"&types=quote,news,chart&last=5";
//          Sql query should be like SELECT GROUP_CONCAT(symbol SEPARATOR ',')
//              or SELECT GROUP_CONCAT(symbol)

//                String url ="https://api.iextrading.com/1.0/stock/"+ symbols +"/quote";
// Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
//                                mTextView.setText("Response is: "+ response.substring(0,1000));
                                if (response.equals("{}"))
                                    mTextView.setText(mTextView.getText() + ("No Data returned.  Did you enter a valid stock symbol?"));
                                else {
                                    mTextView.setText("");
                                    gsonBuilder.registerTypeAdapter(Batches.class, new CompanyListDeserializer());
                                    Batches myData = gsonBuilder.create().fromJson(response, Batches.class);
                                    for (int index = 0; index < myData.batches.size(); index++) {
                                        mTextView.append(Integer.toString(index));
                                        mTextView.append("\t");
                                        mTextView.append(myData.batches.get(index).quote.symbol);
                                        mTextView.append("\t");
                                        mTextView.append(Float.toString(myData.batches.get(index).quote.latestPrice));
                                        mTextView.append("\t");
                                        if(myData.batches.get(index).quote.change < 0){
                                            mTextView.setTextColor(Color.parseColor("#FF0000"));
                                            mTextView.append(Float.toString(myData.batches.get(index).quote.change));
                                        } else {
                                            mTextView.setTextColor(Color.parseColor("#00FF00"));
                                            mTextView.append(Float.toString(myData.batches.get(index).quote.change));
                                        }
                                        mTextView.append("\n");
                                        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
                                        String json = gsonPretty.toJson(myData);

                                    System.out.println("JSON = " + json);
                                    mTextView.append(json);

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
            }
        });
    }
    public boolean updateSymbols() {
        String symbolUrl = "https://api.iextrading.com/1.0/ref-data/symbols";
        GsonBuilder gsonSymbols = new GsonBuilder();
        boolean success = false;
        StringRequest symbolRequest = new StringRequest(Request.Method.GET, symbolUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // parse data here
                GsonBuilder gsonBuilder = new GsonBuilder();
                stockSym[] mySyms = gsonBuilder.create().fromJson(response,stockSym[].class);

                // update table here
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "That didn't work! Do you have an internet connection?", Toast.LENGTH_LONG).show();
            }
        });
        return success;
    }
    public void getData(String jsonData) {
        GsonBuilder gsonBuilder = new GsonBuilder();
//        QuoteList myData = gsonBuilder.create().fromJson(jsonData, QuoteList.class);
        gsonBuilder.registerTypeAdapter(Batches.class, new CompanyListDeserializer());
        Batches myData = gsonBuilder.create().fromJson(jsonData, Batches.class);
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(myData);
        String json = gsonPretty.toJson(myData);
        System.out.println("JSON = " + json);
//        int index = 0;
        for (int index = 0; index < myData.batches.size(); index++) {
            System.out.println(index + "\t" + myData.batches.get(index).quote.symbol + "\t" + myData.batches.get(index).quote.latestPrice + "\t" + myData.batches.get(index).quote.change);
        }
    }
}
