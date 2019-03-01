
package com.example.WealthMan.detail.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.WealthMan.detail.bean.DetailLineBean;

import java.util.ArrayList;
import java.util.List;

public class DiscountView extends View {

    private Paint linPaint;
    public long maxValue = 100;
    public long minValue = 50;
    private List<DetailLineBean.Detaildate.DetailLineDate> detailLineBeans = new ArrayList<>();
    private int width;
    private int height;
    private float maxAverage;
    private float minAverage;
    private static final String TAG = "DiscountView";

    public DiscountView(Context context) {
        super(context);
        initPaint();
    }

    public DiscountView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }


    public DiscountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        int proj_green = Color.rgb(66,151,64);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        linPaint = new Paint();
        linPaint.setColor(proj_green);
        linPaint.setAntiAlias(true);
        linPaint.setStrokeWidth(3);
        linPaint.setTextSize(30);
        linPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        Log.i(TAG, "onSizeChanged:  height = " + height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawTimeAndMoney(canvas);
        drawFrame(canvas);
    }

    private void drawTimeAndMoney(Canvas canvas) {
        int size = detailLineBeans.size();
        if (size > 0) {

            //time
            int averageSize = size / 4;
            int secondSize = 0 + averageSize;
            int threeSize = secondSize + averageSize;
            int fourSize = threeSize + averageSize;
            String firstTime = detailLineBeans.get(0).date;
            String secondTime = detailLineBeans.get(secondSize).date;
            String threeTime = detailLineBeans.get(threeSize).date;
            String fourTime = detailLineBeans.get(fourSize).date;
            String fiveTime = detailLineBeans.get(size - 1).date;
            if (!TextUtils.isEmpty(detailLineBeans.get(0).date) && detailLineBeans.get(0).date.equals(detailLineBeans.get(1).date)) {
                firstTime = detailLineBeans.get(0).minute;
                secondTime = detailLineBeans.get(secondSize).minute;
                threeTime = detailLineBeans.get(threeSize).minute;
                fourTime = detailLineBeans.get(fourSize).minute;
                fiveTime = detailLineBeans.get(size - 1).minute;
            }

            int averageX = (width - 30 - 100) / 4;
            canvas.drawText(firstTime, 30, height - linPaint.getStrokeWidth() - 30, linPaint);
            canvas.drawText(secondTime, 30 + averageX, height - linPaint.getStrokeWidth() - 30, linPaint);
            canvas.drawText(threeTime, 30 + 2 * averageX, height - linPaint.getStrokeWidth() - 30, linPaint);
            canvas.drawText(fourTime, 30 + 3 * averageX, height - linPaint.getStrokeWidth() - 30, linPaint);
            //line
            Path path = new Path();
            float maxLinHeight = height - linPaint.getStrokeWidth();

            float average = detailLineBeans.get(0).close;
            float v = ((maxAverage - average) / (maxAverage - minAverage)) * maxLinHeight;
            path.moveTo(30, v);
            for (int i = 0; i < detailLineBeans.size(); i += 3) {
                float linAverage = detailLineBeans.get(i).close;
                float moveY = ((maxAverage - linAverage) / (maxAverage - minAverage)) * maxLinHeight - 30;

                path.lineTo(30 + i * ((width - 30 - 100) / detailLineBeans.size()), moveY);
                Log.i(TAG, "drawTimeAndMoney:  x = " + (30 + i * ((width - 30 - 100) / detailLineBeans.size())) + "y = " + moveY);
            }
            canvas.drawPath(path, linPaint);
            //money
            float moneyAverage = (maxAverage - minAverage) / 4;
            float moneyAverageHeight = (height - linPaint.getStrokeWidth()) / 4;
            canvas.drawText(maxAverage + "", width - 200, 60, linPaint);
            canvas.drawText(maxAverage - moneyAverage + "", width - 200, 60 + moneyAverageHeight, linPaint);
            canvas.drawText(maxAverage - 2 * moneyAverage + "", width - 200, 60 + 2 * moneyAverageHeight, linPaint);
            canvas.drawText(maxAverage - 3 * moneyAverage + "", width - 200, 60 + 3 * moneyAverageHeight, linPaint);
            canvas.drawText(maxAverage - 4 * moneyAverage + "", width - 200, 60 + 4 * moneyAverageHeight, linPaint);
        }


    }

    private void drawFrame(Canvas canvas) {
        canvas.drawLine(30, height - linPaint.getStrokeWidth(), width - 30 - 100, height - linPaint.getStrokeWidth(), linPaint);
    }

    public void setDate(List<DetailLineBean.Detaildate.DetailLineDate> lineDate, float maxAverage, float minAverage) {
        if (lineDate == null) return;
        detailLineBeans = lineDate;
        this.maxAverage = maxAverage;
        this.minAverage = minAverage;
        invalidate();
    }
}
