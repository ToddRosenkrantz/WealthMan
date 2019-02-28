package com.example.WealthMan;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

class MainActivity extends AppCompatActivity {
    private ArrayList<IconBean> mIconBeenList = new ArrayList<>();
    private ListView lv;
    private String latestprice;
    private IconAdapter sa;
    private DatabaseHelper DatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativity_test_watchlist);
        lv = (ListView)findViewById(R.id.lv);
        //为listview添加adapter
        lv.setAdapter(new IconAdapter(this,mIconBeenList));

        }


}
