package com.example.WealthMan.detail.picker;

import android.view.View;


/**
 * Created by jerry on 2018/3/24.
 */

public class YearMonthDialog extends AbstractTimerFragment {

    @Override
    public void initEvent() {
        super.initEvent();
        timeOfOtherTv.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);
        timeOfOtherTv.setText(startTimeStr);
    }

    @Override
    public void onDataSelected(long timeMillis, String timeStr) {
        long differ = ToolsUtil.getDaysOfMonth(timeStr) * 24L * 60 * 60 * 1000;
        startMillis = timeMillis;
        startTimeStr = ToolsUtil.convertTimeLongToString(timeMillis, "yyyy-MM");

        endMillis = startMillis + differ;
        endTimeStr = ToolsUtil.convertTimeLongToString(endMillis, "yyyy-MM");
        timeOfOtherTv.setText(startTimeStr);
    }
}
