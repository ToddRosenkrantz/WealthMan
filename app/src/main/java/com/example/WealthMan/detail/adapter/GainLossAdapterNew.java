package com.example.WealthMan.detail.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.WealthMan.R;
import com.example.WealthMan.Transaction;

import java.text.DecimalFormat;
import java.util.List;

public class GainLossAdapterNew extends RecyclerView.Adapter<GainLossAdapterNew.GainLossViewHolder> {


    private List<Transaction> sharesStockBeans;
    private final LayoutInflater mInflater;

    //private DatabaseHelper db = new DatabaseHelper();
    public GainLossAdapterNew(/*Context*/Context context, List<Transaction> sharesStocklist) {
        this.sharesStockBeans = sharesStocklist;
        /* db = new DatabaseHelper(this); */
        this.mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public GainLossViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GainLossViewHolder(mInflater.inflate(R.layout.gain_loss_item_new, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull GainLossViewHolder holder, int i) {
        Transaction bean = sharesStockBeans.get(i);
        holder.stock.setText(bean.getSymbol());
        DecimalFormat decimalFormat = new DecimalFormat("$#,###.00");
        double gain = bean.getPrice();
        if (gain >= 0) {
//            gain = Math.abs(gain);
            holder.itemView.setBackgroundColor(Color.argb(41, 156, 223, 88));
        } else {
//            gain = gain * (-1);
            holder.itemView.setBackgroundColor(Color.argb(41, 223, 108, 88));
        }
        String str = decimalFormat.format(gain);
        holder.price.setText(str);
        holder.quantity.setText(bean.getShares() + "");
        holder.date.setText(bean.getDate());
    }

    public void addDate(Transaction date) {
        sharesStockBeans.add(date);
        notifyDataSetChanged();
    }

    public void clearData() {
        sharesStockBeans.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sharesStockBeans.size();
    }

    public class GainLossViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public final TextView stock;
        public final TextView quantity;
        public final TextView price;
        public final TextView date;


        public GainLossViewHolder(View itemView) {

            super(itemView);
            this.itemView = itemView;

            stock = itemView.findViewById(R.id.stock);
            quantity = itemView.findViewById(R.id.quantity);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);

        }
    }
}
