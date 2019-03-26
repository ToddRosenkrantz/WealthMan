package com.example.WealthMan.detail.view;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.WealthMan.DatabaseHelper;
import com.example.WealthMan.detail.adapter.SharesListAdapter;
import com.example.WealthMan.detail.bean.SharesStockBean;

import java.util.ArrayList;
import java.util.List;

import com.example.WealthMan.R;

public class SharesListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rc;
    private EditText stockEt;
    private EditText sharesEt;
    private EditText priceEt;
    private TextView dateEt;
    private CheckBox boughtCb;
    private CheckBox soldCb;
    private static final String TAG = "SharesListActivity";
    public List<SharesStockBean> date = new ArrayList();
    private DatabaseHelper db;
    private Button button;
    private String buyType = "0";
    private SharesListAdapter sharesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shares_list);
        initDb();
        initView();
        initRc();
        initListen();
    }

    private void initListen() {
        button.setOnClickListener(this);
    }

    private void initDb() {
        db = new DatabaseHelper(this);
        date = db.queryAllSharesList();
        Log.i(TAG, "initDb: date size =  " + date.size());
    }

    private void initRc() {
        sharesListAdapter = new SharesListAdapter(this, date);
        rc.setAdapter(sharesListAdapter);
        rc.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initView() {
        rc = findViewById(R.id.rc);
        stockEt = findViewById(R.id.stock);
        sharesEt = findViewById(R.id.shares);
        priceEt = findViewById(R.id.price);
        dateEt = findViewById(R.id.date);
        boughtCb = findViewById(R.id.bought);
        soldCb = findViewById(R.id.sold);
        button = findViewById(R.id.insert);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.insert:
                String stock = stockEt.getText().toString().trim();
                boolean b = db.queryStockDate(new String[]{stock});
                if (!b) {
                    ContentValues value = new ContentValues();
                    String stockS = stockEt.getText().toString().trim();
                    String sharesS = sharesEt.getText().toString().trim();
                    String priceS = priceEt.getText().toString().trim();
                    String dateS = dateEt.getText().toString().trim();
                    value.put("stock", stockS);
                    value.put("shares", sharesS);
                    value.put("price", priceS);
                    value.put("date", dateS);
                    value.put("bought", buyType);
                    db.insertDateToTable(DatabaseHelper.SHARES_LIST_NAME, value);
                    sharesListAdapter.addDate(new SharesStockBean(stockS, sharesS, priceS, dateS, buyType));
                }
                break;
        }
    }
}
