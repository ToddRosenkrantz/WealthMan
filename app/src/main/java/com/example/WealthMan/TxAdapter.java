package com.example.WealthMan;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TxAdapter extends RecyclerView.Adapter<TxAdapter.ViewHolder> {

    private List<Transaction> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    TxAdapter(Context context, ArrayList<Transaction> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.transaction_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        String animal = mData.get(position).getSymbol();
        if (mData.get(position).getShares() <0){
            holder.itemView.setBackgroundColor(Color.argb(41,223, 108, 88));
//            holder.mySymbolView.setBackgroundColor(Color.argb(41,223, 108, 88));
//            holder.mySharesView.setBackgroundColor(Color.argb(41,223, 108, 88));
//            holder.myPriceView.setBackgroundColor(Color.argb(41,223, 108, 88));
//            holder.myDateView.setBackgroundColor(Color.argb(41,223, 108, 88));
        }
        else {
              holder.itemView.setBackgroundColor(Color.argb(41,156, 223, 88));
//            holder.mySymbolView.setBackgroundColor(Color.argb(41,156, 223, 88));
//            holder.mySharesView.setBackgroundColor(Color.argb(41,156, 223, 88));
//            holder.myPriceView.setBackgroundColor(Color.argb(41,156, 223, 88));
//            holder.myDateView.setBackgroundColor(Color.argb(41,156, 223, 88));
        }
        holder.mySymbolView.setText(mData.get(position).getSymbol());
        String shares = String.format("%.1f", mData.get(position).getShares());
        holder.mySharesView.setText(shares);
        String price = String.format("%.2f", mData.get(position).getPrice());
        holder.myPriceView.setText(price);
        holder.myDateView.setText(mData.get(position).getDate());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder{
//        TextView myTextView;
        TextView mySymbolView;
        TextView mySharesView;
        TextView myPriceView;
        TextView myDateView;

        ViewHolder(View itemView) {
            super(itemView);
//            myTextView = itemView.findViewById(R.id.tvAnimalName);
            mySymbolView = itemView.findViewById(R.id.stock);
            mySharesView = itemView.findViewById(R.id.shares);
            myPriceView = itemView.findViewById(R.id.price);
            myDateView = itemView.findViewById(R.id.date);
        }
    }

    // convenience method for getting data at click position
    Transaction getItem(int id) {
        return mData.get(id);
    }

    public void update(ArrayList<Transaction> data){
        System.out.println("Before update size: " + mData.size());
        mData.clear();
        mData = data;
        this.notifyDataSetChanged();
        System.out.println("After update size: " + mData.size());
    }

}
