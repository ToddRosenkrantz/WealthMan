package com.example.WealthMan.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WealthMan.R;
import com.example.WealthMan.detail.bean.DetailLineBean;
import com.example.WealthMan.detail.okhttp.RequestManger;
import com.example.WealthMan.detail.okhttp.RequstCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DetailActivity extends AppCompatActivity implements RequstCallBack, View.OnClickListener {
    public ArrayList<DetailLineBean> dateBean = new ArrayList<>();
    public float maxAverage = 0;
    public float minAverage = 0;
    private static final String TAG = "DetailActivity";
    private DiscountView discountView;
    private View one_day_view;
    private View one_day_line;
    private View one_week_line;
    private View one_week_view;
    private View one_month_line;
    private View one_month_view;
    private View one_year_line;
    private View one_year_view;
    private View five_year;
    private View five_year_line;
    private View max_view;
    private View max_lin;
    public static final String SYMBOL_NAME = "com.example.WealthMan.detail.view.DetailActivity";
    private String symbolName = "aapl";
    public static final String baseUrl = "https://api.iextrading.com/1.0/stock/market/batch";
    private String dayType = "1d";
    private ProgressBar progressBar;
    private TextView name;
    private String[] dataList={"appl"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeInflateView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv1 = findViewById(R.id.tv1);
        tv1.setText(Html.fromHtml("<a  href='https://api.iextrading.com/1.0/stock/aapl/article/5022287287028639'>Apple: Stress-Valuation</a>"));
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv2 = findViewById(R.id.tv2);
        tv2.setText(Html.fromHtml("<a href='https://api.iextrading.com/1.0/stock/aapl/article/8242749460030043'>Apple's Highest-Ever EPS Shows A New Trend</a>"));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tv3 = findViewById(R.id.tv3);
        tv3.setText(Html.fromHtml("<a href='https://api.iextrading.com/1.0/stock/aapl/article/5750047012237294'>The diversity challenge facing tech</a>"));
        tv3.setMovementMethod(LinkMovementMethod.getInstance());


        //data list
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1,dataList);//适配器
        ListView listView = (ListView) findViewById(R.id.dataList); //找到ListView布局
        listView.setAdapter(adapter);

        initView();
        initListen();
        requstDate(baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=" + dayType);
        loading(true);
    }

    private void beforeInflateView() {
        Intent intent = getIntent();
        if (intent != null) {
            symbolName = intent.getStringExtra(SYMBOL_NAME);
            symbolName = "aapl";
        }
    }


    private void initListen() {
        one_day_view.setOnClickListener(this);
        one_week_view.setOnClickListener(this);
        one_month_view.setOnClickListener(this);
        one_year_view.setOnClickListener(this);
        five_year.setOnClickListener(this);
    }

    private void requstDate(String url) {
        RequestManger.getInstance(this).requestDate(url, new HashMap(), DetailLineBean.class);
    }

    private void initView() {
        discountView = findViewById(R.id.discount_view);
        one_day_view = findViewById(R.id.one_day_view);
        one_day_line = findViewById(R.id.one_day_line);
        one_week_line = findViewById(R.id.one_week_line);
        one_week_view = findViewById(R.id.one_week_view);
        one_month_line = findViewById(R.id.one_month_line);
        one_month_view = findViewById(R.id.one_month_view);
        one_year_line = findViewById(R.id.one_year_line);
        one_year_view = findViewById(R.id.one_year_view);
        five_year = findViewById(R.id.five_year);
        five_year_line = findViewById(R.id.five_year_line);
        max_view = findViewById(R.id.max_view);
        max_lin = findViewById(R.id.max_lin);
        progressBar = findViewById(R.id.progress_bar);
        name = findViewById(R.id.name);
    }

    @Override
    public void onSuccess(Object date) {
        DetailLineBean detailLineBean = (DetailLineBean) date;
        if (date == null && (((DetailLineBean) date).AAPL == null || ((DetailLineBean) date).AAPL.chart == null))
            return;

        DetailLineBean.Detaildate aapl = detailLineBean.AAPL;
        List<DetailLineBean.Detaildate.DetailLineDate> chart = aapl.chart;
        int textColor;
        if (aapl.quote.changePercent > 0) {
            textColor = getResources().getColor(android.R.color.holo_red_light);
        } else {

            textColor = getResources().getColor(R.color.colorPrimary);
        }
        float v = aapl.quote.changePercent * 100;
        name.setText(aapl.quote.companyName + "close :" + aapl.quote.close + "( " + v + "%)");
        name.setTextColor(textColor);
        List<DetailLineBean.Detaildate.DetailLineDate> newchart = new ArrayList<>();
        DetailLineBean.Detaildate.DetailLineDate detailLineDate = chart.get(0);
        float firstClose = detailLineDate.close;
        Random random = new Random(4);
        for (DetailLineBean.Detaildate.DetailLineDate newDetailLineBean : chart) {
            float average = newDetailLineBean.close;
            Log.i(TAG, "onSuccess:  average = " + average);

            if (maxAverage == 0 && minAverage == 0) {
                maxAverage = average;
                minAverage = average;
            } else if (average > maxAverage) {
                maxAverage = average;
                if (Math.abs(average - firstClose) > 5) {
                    maxAverage = firstClose + random.nextFloat();
                    newDetailLineBean.close = maxAverage;
                }
            } else if (average < minAverage) {
                minAverage = average;
                if (Math.abs(minAverage - firstClose) > 5) {
                    minAverage = firstClose - random.nextFloat();
                    newDetailLineBean.close = minAverage;
                }
            }
            newchart.add(newDetailLineBean);
        }
        discountView.setDate(newchart, maxAverage, minAverage);
        loading(false);
    }

    @Override
    public void onError(Object date) {
        Toast.makeText(this, "加载失败,请求超时", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        String url = "";
        switch (v.getId()) {
            case R.id.one_day_view:
                one_day_line.setVisibility(View.VISIBLE);
                one_week_line.setVisibility(View.GONE);
                one_month_line.setVisibility(View.GONE);
                one_year_line.setVisibility(View.GONE);
                five_year_line.setVisibility(View.GONE);
                max_lin.setVisibility(View.GONE);
                url = baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=" + dayType;
                break;
            case R.id.one_week_view:
                one_day_line.setVisibility(View.GONE);
                one_week_line.setVisibility(View.VISIBLE);
                one_month_line.setVisibility(View.GONE);
                one_year_line.setVisibility(View.GONE);
                five_year_line.setVisibility(View.GONE);
                max_lin.setVisibility(View.GONE);
                url = baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=1m";
                break;
            case R.id.one_month_view:
                one_day_line.setVisibility(View.GONE);
                one_week_line.setVisibility(View.GONE);
                one_month_line.setVisibility(View.VISIBLE);
                one_year_line.setVisibility(View.GONE);
                five_year_line.setVisibility(View.GONE);
                max_lin.setVisibility(View.GONE);
                url = baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=3m";
                break;
            case R.id.one_year_view:
                one_day_line.setVisibility(View.GONE);
                one_week_line.setVisibility(View.GONE);
                one_month_line.setVisibility(View.GONE);
                one_year_line.setVisibility(View.VISIBLE);
                five_year_line.setVisibility(View.GONE);
                max_lin.setVisibility(View.GONE);
                url = baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=1y";
                break;
            case R.id.five_year:
                one_day_line.setVisibility(View.GONE);
                one_week_line.setVisibility(View.GONE);
                one_month_line.setVisibility(View.GONE);
                one_year_line.setVisibility(View.GONE);
                five_year_line.setVisibility(View.VISIBLE);
                max_lin.setVisibility(View.GONE);
                url = baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=1y";
                break;
            case R.id.max_view:
                one_day_line.setVisibility(View.GONE);
                one_week_line.setVisibility(View.GONE);
                one_month_line.setVisibility(View.GONE);
                one_year_line.setVisibility(View.GONE);
                five_year_line.setVisibility(View.GONE);
                max_lin.setVisibility(View.VISIBLE);
                url = baseUrl + "?symbols=" + symbolName + "&types=quote,chart&range=1y";
                break;
        }
        requstDate(url);
        loading(true);
    }

    public void loading(boolean isLoading) {
        if (isLoading) {
            discountView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            discountView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }

    }
}
