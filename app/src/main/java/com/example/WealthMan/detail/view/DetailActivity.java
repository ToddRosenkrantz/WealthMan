package com.example.WealthMan.detail.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.WealthMan.R;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView tv1=findViewById(R.id.tv1);
        tv1.setText(Html.fromHtml("<a  href='https://api.iextrading.com/1.0/stock/aapl/article/5022287287028639'>Apple: Stress-Valuation</a>"));
        tv1.setMovementMethod(LinkMovementMethod.getInstance());
        TextView tv2=findViewById(R.id.tv2);
        tv2.setText(Html.fromHtml("<a href='https://api.iextrading.com/1.0/stock/aapl/article/8242749460030043'>Apple's Highest-Ever EPS Shows A New Trend</a>" ));
        tv2.setMovementMethod(LinkMovementMethod.getInstance());

        TextView tv3=findViewById(R.id.tv3);
        tv3.setText(Html.fromHtml("<a href='https://api.iextrading.com/1.0/stock/aapl/article/5750047012237294'>The diversity challenge facing tech</a>" ));
        tv3.setMovementMethod(LinkMovementMethod.getInstance());

    }
}
