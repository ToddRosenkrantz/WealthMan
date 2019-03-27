package com.example.WealthMan.detail.picker;

import android.content.Context;
import android.view.View;


import com.example.WealthMan.R;
import com.example.WealthMan.detail.picker.adapters.NumericWheelAdapter;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.data.source.TimeRepository;
import com.example.WealthMan.detail.picker.listener.OnDataSelectedListener;
import com.example.WealthMan.detail.picker.utils.PickerContants;
import com.example.WealthMan.detail.picker.utils.Utils;
import com.example.WealthMan.detail.picker.wheel.OnWheelChangedListener;
import com.example.WealthMan.detail.picker.wheel.OnWheelScrollListener;
import com.example.WealthMan.detail.picker.wheel.WheelView;

import java.util.Calendar;

/**
 * Created by jzxiang on 16/4/20.
 */
public class TimeWheel implements OnWheelScrollListener {

    Context mContext;
    private View mView;
    WheelView year, month, day, hour, minute;
    NumericWheelAdapter mYearAdapter, mMonthAdapter, mDayAdapter, mHourAdapter, mMinuteAdapter;

    StringBuffer mBuffer;
    PickerConfig mPickerConfig;
    TimeRepository mRepository;
    OnDataSelectedListener mListener;

    OnWheelChangedListener yearListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateMonths();
        }
    };
    OnWheelChangedListener monthListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateDays();
        }
    };
    OnWheelChangedListener dayListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateHours();
        }
    };
    OnWheelChangedListener minuteListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateMinutes();
        }
    };

    public  TimeWheel(View view, Context context, PickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
        mView = view;
        mRepository = new TimeRepository(pickerConfig);
        mContext = context;
        initialize(view);
    }

    public void initialize(View view) {
        if (view == null) return;

        initView(view);
        initYear();
        initMonth();
        initDay();
        initHour();
        initMinute();
    }

    void initView(View view) {
        year = (WheelView) view.findViewById(R.id.year);
        month = (WheelView) view.findViewById(R.id.month);
        day = (WheelView) view.findViewById(R.id.day);
        hour = (WheelView) view.findViewById(R.id.hour);
        minute = (WheelView) view.findViewById(R.id.minute);

        switch (mPickerConfig.mType) {
            case ALL:
                break;
            case YEAR_MONTH_DAY:
                Utils.hideViews(hour, minute);
                break;
            case YEAR_MONTH:
                Utils.hideViews(day, hour, minute);
                break;
            case MONTH_DAY_HOUR_MIN:
                Utils.hideViews(year);
                break;
            case HOURS_MINS:
                Utils.hideViews(year, month, day);
                break;
            case YEAR:
                Utils.hideViews(month, day, hour, minute);
                break;
        }

        year.addChangingListener(yearListener);
        year.addChangingListener(monthListener);
        year.addChangingListener(dayListener);
        year.addChangingListener(minuteListener);
        month.addChangingListener(monthListener);
        month.addChangingListener(dayListener);
        month.addChangingListener(minuteListener);
        day.addChangingListener(dayListener);
        day.addChangingListener(minuteListener);
        hour.addChangingListener(minuteListener);
    }

    void initYear() {
        int minYear = mRepository.getMinYear();
        int maxYear = mRepository.getMaxYear();

        mYearAdapter = new NumericWheelAdapter(mContext, minYear, maxYear, PickerContants.FORMAT, mPickerConfig.mYear);
        mYearAdapter.setConfig(mPickerConfig);
        year.setViewAdapter(mYearAdapter);
        year.setCurrentItem(mRepository.getDefaultCalendar().year - minYear);
        year.addScrollingListener(this);
    }

    void initMonth() {
        updateMonths();
        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        month.setCurrentItem(mRepository.getDefaultCalendar().month - minMonth);
        month.setCyclic(mPickerConfig.cyclic);
        month.addScrollingListener(this);
    }

    void initDay() {
        updateDays();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        int minDay = mRepository.getMinDay(curYear, curMonth);
        day.setCurrentItem(mRepository.getDefaultCalendar().day - minDay);
        day.setCyclic(mPickerConfig.cyclic);
        day.addScrollingListener(this);
    }

    void initHour() {
        updateHours();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        hour.setCurrentItem(mRepository.getDefaultCalendar().hour - minHour);
        hour.setCyclic(mPickerConfig.cyclic);
        hour.addScrollingListener(this);
    }

    void initMinute() {
        updateMinutes();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();
        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);

        minute.setCurrentItem(mRepository.getDefaultCalendar().minute - minMinute);
        minute.setCyclic(mPickerConfig.cyclic);
        minute.addScrollingListener(this);
    }

    void updateMonths() {
        if (month.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        int maxMonth = mRepository.getMaxMonth(curYear);
        mMonthAdapter = new NumericWheelAdapter(mContext, minMonth, maxMonth, PickerContants.FORMAT, mPickerConfig.mMonth);
        mMonthAdapter.setConfig(mPickerConfig);
        month.setViewAdapter(mMonthAdapter);

        if (mRepository.isMinYear(curYear)) {
            month.setCurrentItem(0, false);
        }
    }

    void updateDays() {
        if (day.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, curMonth);

        int maxDay = mRepository.getMaxDay(curYear, curMonth);
        int minDay = mRepository.getMinDay(curYear, curMonth);
        mDayAdapter = new NumericWheelAdapter(mContext, minDay, maxDay, PickerContants.FORMAT, mPickerConfig.mDay);
        mDayAdapter.setConfig(mPickerConfig);
        day.setViewAdapter(mDayAdapter);

        if (mRepository.isMinMonth(curYear, curMonth)) {
            day.setCurrentItem(0, true);
        }

        int dayCount = mDayAdapter.getItemsCount();
        if (day.getCurrentItem() >= dayCount) {
            day.setCurrentItem(dayCount - 1, true);
        }
    }

    void updateHours() {
        if (hour.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        int maxHour = mRepository.getMaxHour(curYear, curMonth, curDay);

        mHourAdapter = new NumericWheelAdapter(mContext, minHour, maxHour, PickerContants.FORMAT, mPickerConfig.mHour);
        mHourAdapter.setConfig(mPickerConfig);
        hour.setViewAdapter(mHourAdapter);

        if (mRepository.isMinDay(curYear, curMonth, curDay))
            hour.setCurrentItem(0, false);
    }

    void updateMinutes() {
        if (minute.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
        int maxMinute = mRepository.getMaxMinute(curYear, curMonth, curDay, curHour);

        mMinuteAdapter = new NumericWheelAdapter(mContext, minMinute, maxMinute, PickerContants.FORMAT, mPickerConfig.mMinute);
        mMinuteAdapter.setConfig(mPickerConfig);
        minute.setViewAdapter(mMinuteAdapter);

        if (mRepository.isMinHour(curYear, curMonth, curDay, curHour))
            minute.setCurrentItem(0, false);
    }

    public int getCurrentYear() {
        return year.getCurrentItem() + mRepository.getMinYear();
    }

    public int getCurrentMonth() {
        int curYear = getCurrentYear();
        return month.getCurrentItem() + mRepository.getMinMonth(curYear);
    }

    public int getCurrentDay() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        return day.getCurrentItem() + mRepository.getMinDay(curYear, curMonth);
    }

    public int getCurrentHour() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        return hour.getCurrentItem() + mRepository.getMinHour(curYear, curMonth, curDay);
    }

    public int getCurrentMinute() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        return minute.getCurrentItem() + mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        int currentYear = getCurrentYear();
        int currentMonth = getCurrentMonth();
        int currentDay = getCurrentDay();
        int currentHour = getCurrentHour();
        int currentMinute = getCurrentMinute();
        if (mBuffer == null) {
            mBuffer = new StringBuffer();
        } else {
            mBuffer.setLength(0);
        }
        mBuffer.append(currentYear).append("-").append(configTime(currentMonth)).append("-").append(configTime(currentDay))
                .append(" ").append(configTime(currentHour)).append(":").append(configTime(currentMinute));
        if (mListener != null) {
            mListener.onDataSelected(getTimeMillis(), mBuffer.toString());
        }
    }

    public void setOnDataSelectedListener(OnDataSelectedListener listener) {
        this.mListener = listener;
    }

    private long getTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        calendar.set(Calendar.YEAR, getCurrentYear());
        calendar.set(Calendar.MONTH, getCurrentMonth() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, getCurrentDay());
        calendar.set(Calendar.HOUR_OF_DAY, getCurrentHour());
        calendar.set(Calendar.MINUTE, getCurrentMinute());
        return calendar.getTimeInMillis();
    }

    protected String configTime(int time) {
        String timeStr = "" + time;
        if (time < 10) {
            timeStr = "0" + time;
        }
        return timeStr;
    }

    public void release() {
        mView = null;
        year.stopScrolling();
        month.stopScrolling();
        day.stopScrolling();
        hour.stopScrolling();
        minute.stopScrolling();
    }

}
