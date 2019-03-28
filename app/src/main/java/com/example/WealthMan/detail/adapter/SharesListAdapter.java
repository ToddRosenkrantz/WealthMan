package com.example.WealthMan.detail.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.WealthMan.DatabaseHelper;
import com.example.WealthMan.R;
import com.example.WealthMan.TransactionActivity;
import com.example.WealthMan.detail.bean.SharesStockBean;
import com.example.WealthMan.detail.view.SharesListActivity;

import java.util.List;

public class SharesListAdapter extends RecyclerView.Adapter<SharesListAdapter.SharesListViewHolder> {


    private List<SharesStockBean> sharesStockBeans;
    private final LayoutInflater mInflater;

    SharesListActivity mContext ;

    public  void  MyBaseAdapter(SharesListActivity context) {

        this.mContext = context;
    }
    //private DatabaseHelper db = new DatabaseHelper();
    public SharesListAdapter(/*Context*/SharesListActivity context, List<SharesStockBean> sharesStocklist) {
        this.sharesStockBeans = sharesStocklist;
        /* db = new DatabaseHelper(this); */
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public SharesListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new SharesListViewHolder(mInflater.inflate(R.layout.shares_rc_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull SharesListViewHolder sharesListViewHolder, int i) {
        SharesStockBean sharesStockBean = sharesStockBeans.get(i);
        sharesListViewHolder.stock.setText(sharesStockBean.stock);
        sharesListViewHolder.price.setText(sharesStockBean.price);
        sharesListViewHolder.date.setText(sharesStockBean.date);
        sharesListViewHolder.buy_type.setText(sharesStockBean.buy_type);
        sharesListViewHolder.shares.setText(sharesStockBean.shares);
    }

    public void addDate(SharesStockBean date) {
        sharesStockBeans.add(date);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return sharesStockBeans.size();
    }

    public class SharesListViewHolder extends RecyclerView.ViewHolder {

        public View itemView;
        public final TextView stock;
        public final TextView shares;
        public final TextView date;
        public final TextView price;
        public final TextView buy_type;



        public SharesListViewHolder(View itemView) {

            super(itemView);
            this.itemView = itemView;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharesStockBean sharesStockBean = sharesStockBeans.get(getAdapterPosition());
                    Log.e("的撒娇地计算WHATever!!!!", String.valueOf(sharesStockBean.ID));

                    mContext.delete(sharesStockBean.ID);
                    Log.e("WHATever!!!!", String.valueOf(sharesStockBean.ID));
                }
            });
            stock = itemView.findViewById(R.id.stock);
            shares = itemView.findViewById(R.id.shares);
            date = itemView.findViewById(R.id.date);
            price = itemView.findViewById(R.id.price);
            buy_type = itemView.findViewById(R.id.buy_type);

        }
    }
}
