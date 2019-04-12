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

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    AlertDialog.Builder confirmExit;
    Button stocks;
    Button gains;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }
    public void onBackPressed(){
        confirmExit = new AlertDialog.Builder(this);
//        Toast.makeText(this, "You Long clicked " + adapter.getItem(p).getID() + " on row number " + p, Toast.LENGTH_SHORT).show();
//        removeAt(p, tList.indexOf(adapter.getItem(p)), adapter.getItem(p).getID());
        confirmExit.setMessage("Logout from Stock Recording System?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert = confirmExit.create();
        //Setting the title manually
        alert.show();
    }
    private void initView() {
        replaceFragment(HomeFragment.getInstance(), R.id.flayout_content);

        stocks = findViewById(R.id.btn_stock);
        stocks.setOnClickListener(this);
        gains = findViewById(R.id.btn_capital_gain);
        gains.setOnClickListener(this);
    }

    protected void replaceFragment(Fragment baseFragment, @IdRes int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, baseFragment, baseFragment.getClass().getCanonicalName());
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_stock:
                replaceFragment(HomeFragment.getInstance(), R.id.flayout_content);
                stocks.setTextColor(Color.rgb(255, 255, 255));
                gains.setTextColor(Color.rgb(170, 170, 170));
                break;

            case R.id.btn_capital_gain:
                replaceFragment(CapitalGainFragment.getInstance(), R.id.flayout_content);
                stocks.setTextColor(Color.rgb(170, 170, 170));
                gains.setTextColor(Color.rgb(255, 255, 255));
                break;
        }
    }
}
