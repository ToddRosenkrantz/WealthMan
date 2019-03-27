package com.example.WealthMan.detail.picker;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Author: SQSong
 * Date: 2018/8/10
 * Description:
 */
public class DateFormatUtil {
    public static long getTodayMillis(boolean zero) {
        Calendar calendar = Calendar.getInstance();
        if (zero) {
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTimeInMillis();
    }

    public static String getCurrentTimeLastTime(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i); //向前走一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentFormatDate = format.format(date);

        return currentFormatDate;
    }

    public static long getCurrentTimeLastTimeSteam(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i); //向前走一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
    public static String getCurrentTimeStartTime(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i); //向前走一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String currentFormatDate = format.format(date);
        return currentFormatDate;
    }

    public static String getCurrentTimeTamp(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i); //向前走一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        String timeInMillis = calendar.getTimeInMillis() / 1000 + "";
        return timeInMillis;
    }
    public static String getLastTimeTamp(int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, i); //向前走一天
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        String timeInMillis = calendar.getTimeInMillis() / 1000 + "";
        return timeInMillis;
    }
    public static long getTomorrowMillis(boolean zero) {
        Calendar calendar = Calendar.getInstance();
        if (zero) {
            int tomorrow = calendar.get(Calendar.DAY_OF_MONTH) + 1;
            calendar.set(Calendar.DAY_OF_MONTH, tomorrow);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTimeInMillis();
    }

    public static long getDayZeroMillis(long dayMillis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(dayMillis);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static long getTodayEndMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    @SuppressLint("WrongConstant")
    public static long getTimesMonth(boolean isStart) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        if (!isStart) {
            cal.set(Calendar.HOUR_OF_DAY, 24);
        }
        return cal.getTimeInMillis();
    }

    /*
     * 获取天数
     * */
    public static int getDaysOfMonth(String data) {
        int num = 0;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(data));
            num = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return num;
    }

    public static long getYesterdayMillis(boolean zero, int hour) {
        Calendar calendar = Calendar.getInstance();
        if (zero) {
            int yesterday = calendar.get(Calendar.DAY_OF_MONTH) - 1;
            calendar.set(Calendar.DAY_OF_MONTH, yesterday);
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTimeInMillis();
    }
}
