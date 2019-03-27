package com.example.WealthMan.detail.picker;

import android.view.View;

/**
 * Created by jerry on 2018/3/24.
 */

public class YearMonthDayDialog extends AbstractTimerFragment {
    @Override
    public void initEvent() {
        super.initEvent();
        timeOfOtherTv.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.GONE);
        timeOfOtherTv.setText(startTimeStr);
    }

    @Override
    public void onDataSelected(long timeMillis, String timeStr) {
        long differ = 24L * 60 * 60 * 1000;
        startMillis = timeMillis;
        startTimeStr = ToolsUtil.convertTimeLongToString(timeMillis, "yyyy-MM-dd");

        endMillis = startMillis + differ;
        endTimeStr = ToolsUtil.convertTimeLongToString(endMillis, "yyyy-MM-dd");
        if (timeOfOtherTv != null) {
            timeOfOtherTv.setText(startTimeStr);
        }
    }
}
