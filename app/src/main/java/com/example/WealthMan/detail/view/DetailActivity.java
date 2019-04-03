
package com.example.WealthMan.detail.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WealthMan.DatabaseHelper;
import com.example.WealthMan.HomeActivity;
import com.example.WealthMan.R;
import com.example.WealthMan.TransactionActivity;
import com.example.WealthMan.TransactionLogActivity;
import com.example.WealthMan.detail.view.SharesListActivity;
import com.example.WealthMan.detail.bean.DetailLineBean;
import com.example.WealthMan.detail.okhttp.RequestManger;
import com.example.WealthMan.detail.okhttp.RequstCallBack;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
public class DetailActivity extends AppCompatActivity implements RequstCallBack, View.OnClickListener {

    //+++++++++++Watch button+++++
    Button watch;
    AlertDialog.Builder builder;
    DatabaseHelper db;
    int ID;
    //+++++++++++++++++++++++
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
    private String[] dataList = {"appl"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeInflateView();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        db = new DatabaseHelper(this);

        // +++++++++++++++++ LOGIC FOR BUY/SELL BUTTON +++++++++++++++++++++++++
        Button buyOrSell = findViewById(R.id.buyOrSell);
        buyOrSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(DetailActivity.this,com.example.WealthMan.detail.view.SharesListActivity.class);
                Intent intent = new Intent(DetailActivity.this, TransactionLogActivity.class);
                intent.putExtra("Symbol",symbolName);
                intent.putExtra("UserID",ID);
                startActivity(intent);
            }

        });

        // +++++++++++++++++ LOGIC FOR WATCH/UNWATCH BUTTON +++++++++++++++++++++++++
        TextView tv = findViewById(R.id.watchOrUnwatch);
        if(db.checkSymbol(ID, symbolName)==1){
            tv.setText("Unwatch");
        }else{
            tv.setText("Watch");
        }
        watch = findViewById(R.id.watchOrUnwatch);
        builder = new AlertDialog.Builder(this);
        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.checkSymbol(ID, symbolName) == 0) {
                    db.addWatch(ID, symbolName);//add to watchlist
                    watch.setText("Unwatch");
                    Toast.makeText(DetailActivity.this,
                            "Added to Watch List", Toast.LENGTH_SHORT).show();
                } else {
                    builder.setMessage("Remove from watch list?")
                            .setCancelable(false)
                            .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    db.remWatch(ID, symbolName);//remove from watchlist
                                    watch.setText("Watch");
                                    Toast.makeText(DetailActivity.this,
                                            "Removed from Watch List", Toast.LENGTH_SHORT).show();
                                    //finish();
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.show();
                }
            }
        });
        // +++++++++++++++++ LOGIC FOR WATCH/UNWATCH BUTTON +++++++++++++++++++++++++


        //        new list
        TextView tv1 = findViewById(R.id.tv1);
        tv1.setMovementMethod(new ScrollingMovementMethod());
        tv1.setText(Html.fromHtml("<a  href='https://www.cnbc.com/quotes/?symbol="+symbolName+"'> "+symbolName+" : CNBC NEWS</a>"));
        tv1.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tv2 = findViewById(R.id.tv2);
        tv2.setText(Html.fromHtml("<a href='https://www.marketwatch.com/investing/stock/"+symbolName+"'> "+symbolName+"'s Marketwatch Investing Stocks</a>"));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tv3 = findViewById(R.id.tv3);
        tv3.setText(Html.fromHtml("<a href='https://finance.yahoo.com/quote/"+symbolName+"'>"+symbolName+": YAHOO FINANCE</a>"));
        tv3.setMovementMethod(LinkMovementMethod.getInstance());


        initView();
        initListen();
        requstDate(baseUrl + "?symbols=" + symbolName + "&types=quote,news,chart&range=" + dayType);
//        Logger.addLogAdapter(new AndroidLogAdapter());
//        Logger.d(baseUrl + "?symbols=" + symbolName + "&types=quote,news,chart&range=" + dayType);


    }

    private void beforeInflateView() {
        Intent intent = getIntent();
        if (intent != null) {
            symbolName = intent.getStringExtra("Symbol");
            symbolName = symbolName.toUpperCase();
            ID = intent.getIntExtra("UserID",1);
            Logger.addLogAdapter(new AndroidLogAdapter());
            Logger.d(symbolName);
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
        DetailLineBean.Detaildate detailLineBean = (DetailLineBean.Detaildate) date;
        if (date == null )
            return;

        if (detailLineBean == null) {
            Toast.makeText(this, "data bean is error", Toast.LENGTH_SHORT).show();
            return;
        }
        List<DetailLineBean.Detaildate.DetailLineDate> chart = detailLineBean.chart;
        int textColor;
        if (detailLineBean.quote.changePercent > 0) {
            textColor = getResources().getColor(android.R.color.holo_red_light);
        } else {

            textColor = getResources().getColor(R.color.colorPrimary);
        }
        float v = detailLineBean.quote.changePercent * 100;
        name.setText(detailLineBean.quote.companyName + "close :" + detailLineBean.quote.close + "( " + v + "%)");
        //information
        TextView symbols = findViewById(R.id.symbols);
        symbols.setText("Symbol: " + detailLineBean.quote.symbol);

        TextView cpName = findViewById(R.id.cpName);
        cpName.setText("Company name: " + detailLineBean.quote.companyName);

        TextView openPrice = findViewById(R.id.openPrice);
        openPrice.setText("Open: " + detailLineBean.quote.open);

        TextView latestVolume = findViewById(R.id.latestVolume);
        latestVolume.setText("Volume: " + detailLineBean.quote.latestVolume);

        TextView change = findViewById(R.id.change);
        change.setText("Change:     " + detailLineBean.quote.change);

        TextView changePercent = findViewById(R.id.changePercent);
        changePercent.setText("Change Percent: " + detailLineBean.quote.changePercent);


//        name.setTextColor(textColor);

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
        Toast.makeText(this, "Error: Request timed out", Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent Refresh = new Intent(DetailActivity.this, HomeActivity.class);
            Refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(Refresh);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
