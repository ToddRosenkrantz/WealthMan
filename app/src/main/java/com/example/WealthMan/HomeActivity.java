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

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        replaceFragment(HomeFragment.getInstance(), R.id.flayout_content);

        (findViewById(R.id.btn_stock)).setOnClickListener(this);
        (findViewById(R.id.btn_capital_gain)).setOnClickListener(this);
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
                break;

            case R.id.btn_capital_gain:
                replaceFragment(CapitalGainFragment.getInstance(), R.id.flayout_content);
                break;
        }
    }
}
