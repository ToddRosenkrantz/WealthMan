package com.example.WealthMan;

import android.util.Pair;

import java.util.ArrayList;

public class IconBean {
    public String symbol;
    public String companyName;
    public double latestPrice;
    public double change;
    public ArrayList<Pair> cData;


    public String getsymbol() {
        return symbol;
    }

    public void setsymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getcompanyName() {
        return companyName;
    }

    public void setcompanyName(String companyName) {
        this.companyName = companyName;
    }


    public double getlatestPrice() {
        return latestPrice;
    }

    public void setlatestPrice(float latestPrice) {
        this.latestPrice = latestPrice;
    }

    public double getchange() {
        return change;
    }

    public void setchange(float change) {
        this.change = change;
    }

    public IconBean(String symbol,String companyName,double latestPrice,double change, ArrayList<Pair> cdata){
        this.symbol=symbol;
        this.companyName=companyName;
        this.latestPrice=latestPrice;
        this.change=change;
        this.cData = cdata;
    }

}