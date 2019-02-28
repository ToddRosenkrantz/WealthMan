package com.example.WealthMan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

 class IconAdapter extends BaseAdapter {

    public Context mContext;
    public List<IconBean> mlist;
    public LayoutInflater mLayoutInflater;
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {

        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){ // 如果为空，就表示是第一次加载，还没有加入到缓存中
            convertView = mLayoutInflater.inflate(R.layout.lv_item,null);

            viewHolder.mTextView =(TextView) convertView.findViewById(R.id.symbol);
            viewHolder.mTextView =(TextView) convertView.findViewById(R.id.companyName);
            viewHolder.mTextView =(TextView) convertView.findViewById(R.id.latestPrice);
            viewHolder.mTextView =(TextView) convertView.findViewById(R.id.change);


            convertView.setTag(viewHolder);//设置标签标签

        }else {
            viewHolder= (ViewHolder) convertView.getTag();

        }
        //从list取出对象
        IconBean bean=mlist.get(position);
        //设置item的内容
        viewHolder.mTextView.setText(bean.getsymbol());
        viewHolder.mTextView.setText(bean.getcompanyName());
        viewHolder.mTextView.setText(bean.getlatestPrice());
        viewHolder.mTextView.setText(bean.getchange());

        return convertView;

    }
    public static class ViewHolder{
        public TextView mTextView;
    }
    public IconAdapter(Context context, List<IconBean> list){
        this.mContext = context;
        this.mlist = list;
        mLayoutInflater= LayoutInflater.from(context);
    }
}

