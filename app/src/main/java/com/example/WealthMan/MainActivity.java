package com.example.WealthMan;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<IconBean> mIconBeenList = new ArrayList<>();
    private ListView lv;
    private String latestprice;
    private IconAdapter sa;
    private DatabaseHelper DatabaseHelper;
    //public String [] data = {"apple","apple","orange","watermelon","peat","grape","pineapple","strawberry","cherry","mango"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ativity_test_watchlist);
        lv = (ListView)findViewById(R.id.lv);
        //为listview添加adapter
        lv.setAdapter(new IconAdapter(this,mIconBeenList));
        initData();

//        IconAdapter adapter = new IconAdapter(
//                MainActivity.this,R.layout.lv_item,data);
//        ListView listView = lv;


    }


    private void initData() {
        IconBean symbol=new IconBean("FB","FaceBook",7.77,7.99);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
        mIconBeenList.add(symbol);
//        private View show_list(){
//        List<String> data_list = new ArrayList<>(Arrays.asList(data));
//        ArrayAdapter<String> data_adapter = new ArrayAdapter<>(this,R.layout.lv_item,data_list);
//        ListView data_view = (ListView)this.findViewById(R.id.list_view);
//        data_view.setAdapter(data_adapter);
//        return data_view;
    }


}