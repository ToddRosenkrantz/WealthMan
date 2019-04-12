package com.example.WealthMan.detail.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.WealthMan.R;
import com.example.WealthMan.Transaction;
import com.example.WealthMan.detail.bean.SharesStockBean;
import com.example.WealthMan.detail.view.SharesListActivity;

import java.text.DecimalFormat;
import java.util.List;

public class GainLossAdapter extends RecyclerView.Adapter<GainLossAdapter.GainLossViewHolder> {


    private List<Transaction> sharesStockBeans;
    private final LayoutInflater mInflater;

    //private DatabaseHelper db = new DatabaseHelper();
    public GainLossAdapter(/*Context*/Context context, List<Transaction> sharesStocklist) {
        this.sharesStockBeans = sharesStocklist;
        /* db = new DatabaseHelper(this); */
        this.mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public GainLossViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GainLossViewHolder(mInflater.inflate(R.layout.gain_loss_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull GainLossViewHolder holder, int i) {
        Transaction bean = sharesStockBeans.get(i);
        holder.tvName.setText(bean.getSymbol());
        DecimalFormat decimalFormat = new DecimalFormat("$#,###.00");
        double gain = bean.getPrice();
        if (gain <= 0) {
            gain = Math.abs(gain);
        } else {
            gain = gain * (-1);
        }
        String str = decimalFormat.format(gain);
        holder.tvGain.setText(str);
    }

    public void addDate(Transaction date) {
        sharesStockBeans.add(date);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sharesStockBeans.size();
    }

    public class GainLossViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public final TextView tvName;
        public final TextView tvGain;


        public GainLossViewHolder(View itemView) {

            super(itemView);
            this.itemView = itemView;

            tvName = itemView.findViewById(R.id.tv_name);
            tvGain = itemView.findViewById(R.id.tv_gain);

        }
    }
}
