package com.example.WealthMan.detail.picker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.WealthMan.R;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.data.Type;
import com.example.WealthMan.detail.picker.data.WheelCalendar;
import com.example.WealthMan.detail.picker.listener.OnDataSelectedListener;
import com.example.WealthMan.detail.picker.wheel.WheelView;


/**
 * Created by jerry on 2018/3/26.
 */

public abstract class AbstractTimerFragment extends Fragment implements OnDataSelectedListener, RadioGroup.OnCheckedChangeListener {
    private TimeWheel mTimeWheel;

    public PickerConfig mPickerConfig;

   public RadioButton startTimeRb;


    public TextView text;


    public RadioButton endTimeRb;

    public RadioGroup radioGroup;


    public WheelView year;


    public WheelView month;

    public WheelView day;


    public WheelView hour;


    public WheelView minute;

    public LinearLayout linearWheel;


    public TextView timeOfOtherTv;

    public int mSelectType;
    public long startMillis;
    public String startTimeStr;
    public long endMillis;
    public String endTimeStr;
    public long currentMillis;
    public String currentTimeStr;
    private boolean isFragmentFinish;
    protected boolean isHourMinuteShowing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.all_type_dialog, container, false);
      initView(inflate);


        configTimeWheel(inflate);
        initEvent();
        return inflate;
    }

    private void initView(View inflate) {

        startTimeRb =  inflate.findViewById( R.id.start_time_rb);
        text =  inflate.findViewById( R.id.text);
        endTimeRb =  inflate.findViewById( R.id.end_time_rb);
        radioGroup =  inflate.findViewById( R.id.radio_group);
        year =  inflate.findViewById( R.id.year);
        month =  inflate.findViewById( R.id.month);
        day =  inflate.findViewById( R.id.day);
        hour =  inflate.findViewById( R.id.hour);
        minute =  inflate.findViewById( R.id.minute);
        linearWheel =  inflate.findViewById( R.id.linear_wheel);
        timeOfOtherTv =  inflate.findViewById( R.id.time_of_other_tv);

    }

    public void initEvent() {
        ViewGroup.LayoutParams layoutParams = linearWheel.getLayoutParams();
        layoutParams.width = ToolsUtil.getWindowHeight(getActivity()) - 100;
        linearWheel.setLayoutParams(layoutParams);
        radioGroup.setOnCheckedChangeListener(this);
    }


    public void configTimeWheel(View view) {
        mPickerConfig = buildPickerConfig();
        mTimeWheel = new TimeWheel(view, getActivity().getApplicationContext(), mPickerConfig);
        mTimeWheel.setOnDataSelectedListener(this);

        detectShowHourMinute();
    }

    private PickerConfig buildPickerConfig() {
        PickerConfig config = new PickerConfig();
        long sevenDay = 7 * 1000 * 60 * 60 * 24L;
        if (this instanceof AllTypeDialog) {
            config.mType = Type.ALL;
            config.mCurrentCalendar = new WheelCalendar(System.currentTimeMillis());
            config.mMaxCalendar = new WheelCalendar(System.currentTimeMillis());
            config.mMinCalendar = new WheelCalendar(ToolsUtil.getTodayMillis(true) - sevenDay);
            currentMillis = config.mCurrentCalendar.milliSeconds;
            startMillis = ToolsUtil.getTodayMillis(true);
            endMillis = config.mCurrentCalendar.milliSeconds;
            currentTimeStr = ToolsUtil.convertTimeLongToString(currentMillis);
            startTimeStr = ToolsUtil.convertTimeLongToString(startMillis);
            endTimeStr = ToolsUtil.convertTimeLongToString(endMillis);
        }
        if (this instanceof YearMonthDayDialog) {
            config.mType = Type.YEAR_MONTH_DAY;
            config.mCurrentCalendar = new WheelCalendar(ToolsUtil.getTodayMillis(true));
            config.mMaxCalendar = new WheelCalendar(ToolsUtil.getTodayEndMillis());
            config.mMinCalendar = new WheelCalendar(ToolsUtil.getTodayMillis(true) - sevenDay);

            currentTimeStr = ToolsUtil.convertTimeLongToString(config.mCurrentCalendar.milliSeconds, "yyyy-MM-dd");
            currentMillis = config.mCurrentCalendar.milliSeconds;

            long differ = 24L * 60 * 60 * 1000;
            startMillis = currentMillis;
            startTimeStr = ToolsUtil.convertTimeLongToString(startMillis, "yyyy-MM-dd");

            endMillis = startMillis + differ;
            endTimeStr = ToolsUtil.convertTimeLongToString(endMillis, "yyyy-MM-dd");
        }
        if (this instanceof YearMonthDialog) {
            config.mType = Type.YEAR_MONTH;
            config.mCurrentCalendar = new WheelCalendar(ToolsUtil.getTimesMonth(true));
            config.mMaxCalendar = new WheelCalendar(ToolsUtil.getTimesMonth(false));
            long time = 2 * 28 * 24L * 60 * 60 * 1000;
            config.mMinCalendar = new WheelCalendar(ToolsUtil.getTimesMonth(true) - time);

            currentTimeStr = ToolsUtil.convertTimeLongToString(config.mCurrentCalendar.milliSeconds, "yyyy-MM");
            currentMillis = config.mCurrentCalendar.milliSeconds;

            long differ = ToolsUtil.getDaysOfMonth(currentTimeStr) * 24L * 60 * 60 * 1000;
            startMillis = currentMillis;
            startTimeStr = ToolsUtil.convertTimeLongToString(startMillis, "yyyy-MM");

            endMillis = startMillis + differ;
            endTimeStr = ToolsUtil.convertTimeLongToString(endMillis, "yyyy-MM");
        }
        config.cyclic = true;
        config.mWheelTVSize = 18;
        config.mWheelTVNormalColor = ContextCompat.getColor(getContext(), R.color.colorCommonGrey);
        config.mWheelTVSelectorColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        config.mYear = "年";
        config.mMonth = "月";
        config.mDay = "日";
        config.mHour = "时";
        config.mMinute = "分";
        return config;
    }

    @Override
    public void onDataSelected(long timeMillis, String timeStr) {
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.start_time_rb) {
            mSelectType = 0;
        } else {
            mSelectType = 1;
        }
        detectShowHourMinute();
    }

    protected void detectShowHourMinute() {
        if (!(this instanceof AllTypeDialog) || isFragmentFinish) return;

        String startTimeStr = ToolsUtil.formatLongTime(startMillis, "yyyy-MM-dd");
        String endTimeStr = ToolsUtil.formatLongTime(endMillis, "yyyy-MM-dd");
        if (startTimeStr != null && startTimeStr.equals(endTimeStr)) {
            hour.setVisibility(View.VISIBLE);
            minute.setVisibility(View.VISIBLE);
            isHourMinuteShowing = true;
        } else {
            hour.setVisibility(View.GONE);
            minute.setVisibility(View.GONE);
            isHourMinuteShowing = false;
        }

        setTitleTime(startTimeRb, startMillis);
        setTitleTime(endTimeRb, endMillis);

        if (!isHourMinuteShowing) {
            startMillis = ToolsUtil.getDayZeroMillis(startMillis); // timeMillis;
            long todayMillis = ToolsUtil.getTodayMillis(true);
            if (endMillis < todayMillis) {
                endMillis = ToolsUtil.getDayZeroMillis(endMillis) + 24 * 60 * 60 * 1000 - 1;
            }
        }
        /*startMillis = ToolsUtil.getDayZeroMillis(startMillis); // timeMillis;
        long todayMillis = ToolsUtil.getTodayMillis(true);
        if (endMillis < todayMillis) {
            endMillis = ToolsUtil.getDayZeroMillis(endMillis) + 24 * 60 * 60 * 1000 - 1;
        }*/
    }

    private void setTitleTime(RadioButton radioButton, long timeMillis) {
        if (radioButton == null || timeMillis <= 0) return;

        String pattern = isHourMinuteShowing ? "yyyy-MM-dd HH:mm" : "yyyy-MM-dd";
        radioButton.setText(ToolsUtil.formatLongTime(timeMillis, pattern));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        isFragmentFinish = true;
    }

}
