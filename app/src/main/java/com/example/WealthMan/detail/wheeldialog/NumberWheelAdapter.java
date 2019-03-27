package com.example.WealthMan.detail.wheeldialog;

import android.content.Context;


import com.example.WealthMan.detail.picker.adapters.AbstractWheelTextAdapter;

import java.util.List;

/**
 * Author: SQSong
 * Date: 2018/5/29
 * Description:
 */

public class NumberWheelAdapter extends AbstractWheelTextAdapter {
    private final List<String> mWeekList;



    public NumberWheelAdapter(Context context, List weekList) {
        super(context);
        this.mWeekList = weekList;
    }


    @Override
    public int getItemsCount() {
        return mWeekList == null ? 0 : mWeekList.size();
    }

    @Override
    protected CharSequence getItemText(int index) {
        if (index >= 0 && index < mWeekList.size()) {
            return  mWeekList.get(index);
        }
        return null;
    }
}
