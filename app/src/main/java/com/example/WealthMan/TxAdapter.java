package com.example.WealthMan;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TxAdapter extends RecyclerView.Adapter<TxAdapter.ViewHolder> {

    private List<Transaction> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

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
            holder.mySymbolView.setBackgroundColor(Color.argb(41,223, 108, 88));
            holder.mySharesView.setBackgroundColor(Color.argb(41,223, 108, 88));
            holder.myPriceView.setBackgroundColor(Color.argb(41,223, 108, 88));
            holder.myDateView.setBackgroundColor(Color.argb(41,223, 108, 88));
        }
        holder.mySymbolView.setText(mData.get(position).getSymbol());
        holder.mySharesView.setText(mData.get(position).getShares().toString());
        holder.myPriceView.setText(mData.get(position).getPrice().toString());
        holder.myDateView.setText(mData.get(position).getDate());
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int p=getLayoutPosition();
                    System.out.println("LongClick: "+ getItem(p).getID());

                    return true;// returning true instead of false, works for me
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Transaction getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
