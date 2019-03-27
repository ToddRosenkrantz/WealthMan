package com.example.WealthMan.detail.picker;

import android.view.View;

import com.example.WealthMan.detail.picker.listener.OnDataSelectedListener;


/**
 * Created by jerry on 2018/3/24.
 */

public class AllTypeDialog extends AbstractTimerFragment implements OnDataSelectedListener {

    @Override
    public void initEvent() {
        super.initEvent();
        timeOfOtherTv.setVisibility(View.GONE);
        radioGroup.setVisibility(View.VISIBLE);
        if (mPickerConfig != null) {
            startMillis = ToolsUtil.getTodayMillis(true);
            endMillis = mPickerConfig.mCurrentCalendar.milliSeconds;
//            startTimeRb.setText(ToolsUtil.convertTimeLongToString(mZeroCalendar, "yyyy-MM-dd"));
//            endTimeRb.setText(ToolsUtil.convertTimeLongToString(mCurrentCalendar, "yyyy-MM-dd"));
        }
        detectShowHourMinute();
    }

    @Override
    public void onDataSelected(long timeMillis, String timeStr) {
        if (mSelectType == 0) {
            startMillis = timeMillis; // ToolsUtil.getDayZeroMillis(timeMillis);
            startTimeStr = timeStr;
            // setTitleTime(startTimeRb, startMillis);
             } else {
//            long todayMillis = ToolsUtil.getTodayMillis(true);
//            if (timeMillis >= todayMillis) {
//                endMillis = System.currentTimeMillis();
//            } else {
//                endMillis = timeMillis + 24 * 60 * 60 * 1000/* - 1*/;
//            }
            endMillis = timeMillis;
             endTimeStr = timeStr;
            // setTitleTime(endTimeRb, endMillis);
        }
        detectShowHourMinute();
    }

}
