package com.example.WealthMan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.data.LineData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    HashMap<String,ArrayList<IconBean>> map = new HashMap<String,ArrayList<IconBean>>();
    private ArrayList<IconBean> mIconBeenList = new ArrayList<>();

    List<String> chartType = new ArrayList<>();
    String symbol = "AAPL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        symbol = intent.getStringExtra("Symbol");

//        getWatchListData(symbol, "1d");
        chartType.add("1d");
        chartType.add("1m");
        chartType.add("1y");
        chartType.add("5y");
        for (String temp: chartType ) {
            getWatchListData(symbol,temp);
        }

    }



    public void getWatchListData(String syms, final String range){
        final RequestQueue queue = Volley.newRequestQueue(this);
        String url;
        if (syms == "1d")
            url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote,news,chart&range="+range+"&last=5";
        else
            url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote,news,chart&range="+range+"&last=5";
//        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("{}"))
                            Toast.makeText(TestActivity.this, "No stocks being tracked", Toast.LENGTH_LONG).show();
                        else {
                            getData(response, range);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TestActivity.this, "That didn't work! Do you have internet?", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getData(String jsonData, String range) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(APIInterface.Batches.class, new APIInterface.CompanyListDeserializer());
        APIInterface.Batches watchList = gsonBuilder.create().fromJson(jsonData, APIInterface.Batches.class);
        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(watchList);
//        String json = gsonPretty.toJson(watchList);
//        System.out.println("JSON = " + json);
        for (int index = 0; index < watchList.batches.size(); index++) {
//            String coSym = watchList.batches.get(index).coSym;
            ArrayList<Pair> chartData = new ArrayList<>();
            for (int i = 0 ; i < watchList.batches.get(index).chart.size(); i++){
//                float xValue = Float.valueOf(watchList.batches.get(index).chart.get(i).date.replace("-",""));
                float xValue = Float.valueOf(i+1);
                float yValue = (float)watchList.batches.get(index).chart.get(i).close;
                Pair data = new Pair(xValue, yValue);
                chartData.add(data);
            }
            IconBean bean = new IconBean(
                    symbol,
                    watchList.batches.get(index).quote.companyName,
                    watchList.batches.get(index).quote.latestPrice,
                    watchList.batches.get(index).quote.change,
                    chartData
            );
            mIconBeenList.add(bean);
//            System.out.println("range: " + range +" chart items: "+ bean.cData.size());
        }
        map.put(range,mIconBeenList);
    }
}
