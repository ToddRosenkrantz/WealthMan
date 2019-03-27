package com.example.WealthMan.detail.view;

import android.content.ContentValues;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WealthMan.DatabaseHelper;
import com.example.WealthMan.detail.adapter.SharesListAdapter;
import com.example.WealthMan.detail.bean.SharesStockBean;

import java.util.ArrayList;
import java.util.List;

import com.example.WealthMan.R;
import com.example.WealthMan.detail.picker.TimePickerDialog;
import com.example.WealthMan.detail.picker.ToolsUtil;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.data.Type;
import com.example.WealthMan.detail.picker.data.WheelCalendar;
import com.example.WealthMan.detail.wheeldialog.NumberWheelDialog;

public class SharesListActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimerSelcetCloseListener, CompoundButton.OnCheckedChangeListener {

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
    public static String TAG_PICK_TIME = "tag_pick_time";
    private long mStartMillis;
    private String startTime;

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
        dateEt.setOnClickListener(this);
        boughtCb.setOnCheckedChangeListener(this);
        soldCb.setOnCheckedChangeListener(this);
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
                if (b) {
                    ContentValues value = new ContentValues();
                    String stockS = stockEt.getText().toString().trim();
                    String sharesS = sharesEt.getText().toString().trim();
                    String priceS = priceEt.getText().toString().trim();
                    String dateS = dateEt.getText().toString().trim();
                    value.put("stock", stockS);
                    value.put("shares", sharesS);
                    value.put("price", priceS);
                    value.put("date", startTime);
                    value.put("bought", buyType);
                    db.insertDateToTable(DatabaseHelper.SHARES_LIST_NAME, value);
                    sharesListAdapter.addDate(new SharesStockBean(stockS, sharesS, priceS, dateS, buyType));
                }else {
                    Toast.makeText(SharesListActivity.this,"stock is erro",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.date:
                TimePickerDialog.newInstance(null, true, buildPickerConfig()).show(getSupportFragmentManager(), "start");
                TimePickerDialog.setListener(this);

                break;
        }
    }
    private PickerConfig buildPickerConfig() {
        long oneYear = 1L * 365 * 1000 * 60 * 60 * 24L;
        PickerConfig config = new PickerConfig();
        config.cyclic = true;
        long currentTime;
        if (mStartMillis == 0) {
            currentTime = ToolsUtil.getTodayMillis(false);
        } else {
            currentTime = mStartMillis;
        }
        config.mCurrentCalendar = new WheelCalendar(currentTime);
        config.mMaxCalendar = new WheelCalendar(ToolsUtil.getTodayMillis(false));
        config.mMinCalendar = new WheelCalendar(ToolsUtil.getTodayMillis(false) - oneYear);
        config.mType = Type.ALL;
        config.mWheelTVSize = 18;
        config.mWheelTVNormalColor = ContextCompat.getColor(this, R.color.colorCommonGrey);
        config.mWheelTVSelectorColor = ContextCompat.getColor(this, R.color.colorPrimary);
        config.mYear = "year";
        config.mMonth = "month";
        config.mDay = "day";
        config.mHour = "hour";
        config.mMinute = "minute";
        return config;
    }

    @Override
    public void onTimeClose(String tag, long startTimeMillis, String startTimeStr, long endTimeMillis, String endTimeStr) {
dateEt.setText(startTimeStr);
this.startTime = startTimeStr;
    }

    @Override
    public void onClose() {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
switch (buttonView.getId()){
    case R.id.bought:
        if (isChecked){
            buyType = "1";
        soldCb.setChecked(false);
        }
        break;
    case R.id.sold:
        if (isChecked){
            boughtCb.setChecked(false);
            buyType = "0";
        }
        break;
}
    }
}
