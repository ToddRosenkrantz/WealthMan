package com.example.WealthMan;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Transaction {
    private String symbol;
    private int shareCount;
    private Double sharePrice;
    private String boughtSold;
    private Calendar date;

    public Transaction(String symbol, Integer shares, Double price, String b_s, String datetime){
        this.symbol = symbol;
        shareCount = shares;
        sharePrice = price;
        boughtSold = b_s;
        date = convert(datetime);
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
    public String getSymbol(){
        return symbol;
    }
    public void setSymbol(String sym){
        symbol = sym;
    }
    public Integer getShares(){
        return shareCount;
    }
    public void setShares(Integer num){
        shareCount = num;
    }
    public Double getPrice(){
        return sharePrice;
    }
    public void setPrice(Double price){
        sharePrice = price;
    }
    public String getBoughtSold(){
        return boughtSold;
    }
    public void setBoughtSold(String type){
        switch (type){
            case "B":
            case "b":
            case "S":
            case "s":
                boughtSold = type;
                break;
            default:
                Log.e("Transaction", "Unkonwn Transaction type");

        }
    }
    public Calendar getDate(){
        return date;
    }
    public void setDate(String dt){
        date = convert(dt);
    }
}
