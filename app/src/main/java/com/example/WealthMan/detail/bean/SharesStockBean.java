package com.example.WealthMan.detail.bean;


public class SharesStockBean {
    public SharesStockBean() {
    }

    public SharesStockBean(int ID, String stock, String shares, String price, String date, String buy_type) {
        this.stock = stock;
        this.shares = shares;
        this.price = price;
        this.date = date;
        this.buy_type = buy_type;
        this.ID = ID;
    }

    public String stock;
    public String shares;
    public String price;
    public String date;
    public String buy_type;
    public int ID;
}
