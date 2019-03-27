package com.example.WealthMan.detail.wheeldialog;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.example.WealthMan.BuildConfig;
import com.example.WealthMan.R;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.wheel.OnWheelScrollListener;
import com.example.WealthMan.detail.picker.wheel.WheelView;

import java.util.List;

/**
 * Author: SQSong
 * Date: 2018/5/29
 * Description:
 */


public class NumberWheelDialog extends DialogFragment implements OnWheelScrollListener, View.OnClickListener {
    private static final String TAG = "NumberWheelDialog";
    public static List arrlist;
    private static OnNumberCheckedListener NumberClick;
    private WheelView weekWheel;
    public int chooseNumber;
    private TextView cancelTv;
    private TextView confirmTv;
    private String clickNumber;
    private String FRAGMENT_TAG;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        FRAGMENT_TAG = getTag();
        Log.i(TAG, "onCreateView: " + FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.dialog_week_picker, container, false);
        initView(view);
        //初始化滚轮
        initEvent();
        initListen();
        return view;
    }

    private void initListen() {
        cancelTv.setOnClickListener(this);
        confirmTv.setOnClickListener(this);
    }

    private void initView(View view) {
        weekWheel = view.findViewById(R.id.week_wheel);
        cancelTv = view.findViewById(R.id.cancel_tv);
        confirmTv = view.findViewById(R.id.confirm_tv);
    }

    public static NumberWheelDialog getInstanc(List arrlit) {
        NumberWheelDialog dialogFragment = new NumberWheelDialog();
        arrlist = arrlit;
        return dialogFragment;
    }

    /**
     * 初始化滚轮
     */
    private void initEvent() {
//        weekWheel.setCyclic(true);
        weekWheel.addScrollingListener(this);
        NumberWheelAdapter adapter = new NumberWheelAdapter(getContext(), arrlist);
        adapter.setConfig(buildPickerConfig());
        weekWheel.setViewAdapter(adapter);
    }


    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        int currentItem = wheel.getCurrentItem();
        if (currentItem >= 0 && arrlist != null && currentItem < arrlist.size()) {
            clickNumber = (String) arrlist.get(currentItem);
            // Toast.makeText(getContext(), mWeekMonthData.showStr, Toast.LENGTH_SHORT).show();
        }
    }

    private PickerConfig buildPickerConfig() {
        PickerConfig config = new PickerConfig();
        config.cyclic = true;
        config.mWheelTVSize = 18;
        config.mWheelTVNormalColor = ContextCompat.getColor(getContext(), R.color.colorCommonGrey);
        config.mWheelTVSelectorColor = ContextCompat.getColor(getContext(), R.color.colorPrimary);
        return config;
    }

    @Override
    public void onStart() {
        super.onStart();
        //得到dialog对应的window
        Window window = getDialog().getWindow();
        if (window != null) {
            //得到LayoutParams
            WindowManager.LayoutParams params = window.getAttributes();

            //修改gravity
            params.gravity = Gravity.BOTTOM;
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_tv:
                if (NumberClick != null) {
                    NumberClick.onDateChecked(clickNumber, FRAGMENT_TAG);
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.i(TAG, "onClick: 没有注册滚轮点击 ");
                    }
                }
                dismiss();
                break;
            case R.id.cancel_tv:
                dismiss();
                break;
        }
    }

    public static void setOncNumberClick(OnNumberCheckedListener oncNumberClick) {
        NumberClick = oncNumberClick;
    }

    public interface OnNumberCheckedListener {
        void onDateChecked(String clickItem, String tag);

        void onDismiss();
    }
}
