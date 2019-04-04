package com.example.WealthMan;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Transaction {
    private int ID;
    private String symbol;
    private Double shareCount;
    private Double sharePrice;
    private String date;

    public Transaction(Integer ID, String symbol, Double shares, Double price, String datetime){
        this.ID = ID;
        this.symbol = symbol;
        shareCount = shares;
        sharePrice = price;
        date = datetime;
    }
    private Calendar convert (String datetime){
        Calendar cal = Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm a", Locale.ENGLISH);
        try {
            cal.setTime(format.parse(datetime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }
    public Integer getID(){ return ID;}
    public void setID(Integer id) {ID = id;}
    public String getSymbol(){ return symbol; }
    public void setSymbol(String sym){
        symbol = sym;
    }
    public Double getShares(){
        return shareCount;
    }
    public void setShares(Double num){
        shareCount = num;
    }
    public Double getPrice(){
        return sharePrice;
    }
    public void setPrice(Double price){
        sharePrice = price;
    }

    public String getDate(){
        return date;
    }
    public void setDate(String dt){ date = dt; }
}
