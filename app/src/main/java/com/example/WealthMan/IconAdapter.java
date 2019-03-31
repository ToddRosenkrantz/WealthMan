
package com.example.WealthMan;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class IconAdapter extends BaseAdapter {

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

            viewHolder.symbol =(TextView) convertView.findViewById(R.id.symbol);
            viewHolder.companyName =(TextView) convertView.findViewById(R.id.companyName);
            viewHolder.latestPrice =(TextView) convertView.findViewById(R.id.latestPrice);
            viewHolder.change =(TextView) convertView.findViewById(R.id.change);
            viewHolder.chart = (LineChart) convertView.findViewById(R.id.chart);




            convertView.setTag(viewHolder);//设置标签标签

        }else {
            viewHolder= (ViewHolder) convertView.getTag();

        }
        //从list取出对象
        IconBean bean=mlist.get(position);
        //设置item的内容
        viewHolder.symbol.setText(bean.getsymbol());
        viewHolder.companyName.setText(bean.getcompanyName());
        viewHolder.latestPrice.setText(Double.toString(bean.getlatestPrice()));
        viewHolder.change.setText(Double.toString(bean.getchange()));
        List<Entry> entries = new ArrayList<>();
        for (int i = 0 ; i < bean.cData.size(); i++){
            entries.add(new Entry((float)bean.cData.get(i).first, (float)bean.cData.get(i).second));
//            System.out.println(entries.get(i).getY() +", " + entries.get(i).getX());
        }
        LineDataSet dataSet = new LineDataSet(entries, "Label");
//        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setLineWidth(1f);
        dataSet.setColor(Color.rgb(69,139,0));
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(69,139,0));
        dataSet.setValueTextColor(Color.rgb(1,1,1));
        dataSet.setDrawValues(false);
        LineData lineData = new LineData(dataSet);
        viewHolder.chart.setData(lineData);
        viewHolder.chart.setTouchEnabled(false);
        Legend legend = viewHolder.chart.getLegend();
        legend.setEnabled(false);
        viewHolder.chart.getXAxis().setDrawAxisLine(false);
        viewHolder.chart.getXAxis().setDrawGridLines(false);
        viewHolder.chart.getXAxis().setDrawLabels(false);
        viewHolder.chart.getAxisRight().setDrawAxisLine(false);
        viewHolder.chart.getAxisRight().setDrawGridLines(false);
        viewHolder.chart.getAxisRight().setDrawLabels(false);
        viewHolder.chart.getAxisLeft().setDrawAxisLine(false);
        viewHolder.chart.getAxisLeft().setDrawGridLines(false);
        viewHolder.chart.getAxisLeft().setDrawLabels(false);
        viewHolder.chart.getDescription().setEnabled(false);
        viewHolder.chart.invalidate();
        viewHolder.chart.setVisibility(View.VISIBLE);
        if (bean.getchange()>=0){
            viewHolder.change.setTextColor(Color.parseColor("#458B00"));
        }
        else {
            viewHolder.change.setTextColor(Color.parseColor("#FF0000"));
        }


        return convertView;

    }
    public static class ViewHolder{
        TextView symbol;
        TextView companyName;
        TextView latestPrice;
        TextView change;
        LineChart chart;
    }
    IconAdapter(Context context, List<IconBean> list){
        this.mContext = context;
        this.mlist = list;
        mLayoutInflater= LayoutInflater.from(context);
    }


}