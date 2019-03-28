package com.example.WealthMan.detail.picker;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WealthMan.R;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.data.Type;
import com.example.WealthMan.detail.picker.data.WheelCalendar;
import com.example.WealthMan.detail.picker.listener.OnDataSelectedListener;
import com.example.WealthMan.detail.picker.wheel.WheelView;


/**
 * Created by Administrator on 2017/11/23.
 */

public class TimePickerDialog extends DialogFragment implements OnDataSelectedListener, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String SHOW_CONFIG = "show_config";
    private static final String IS_SINGLE = "is_single";
    private static final String PICKER_CONFIG = "picker_config";

    public PickerConfig mPickerConfig;

    public RadioButton startTimeRb;

    public TextView cancelTv;
    public TextView confirmTv;
    public TextView text;


    public RadioButton endTimeRb;

    public RadioGroup radioGroup;


    public WheelView yearView;


    public WheelView monthView;

    public WheelView dayView;


    public WheelView hourView;


    public WheelView minuteView;

    public LinearLayout linearWheel;


    public TextView timeOfOtherTv;
    LinearLayout wheelContainer;

    private long endMillis;
    private long startMillis;
    private String endTimeStr;
    private String mShowConfig;

    private String startTimeStr;
    private int mSelectType = 0;
    private TimeWheel mTimeWheel;
    private boolean isSingleChoice;

    private OnTimerSelectListener mListener;
    private  static  OnTimerSelcetCloseListener closeListener;
    private static final String TAG = "TimePickerDialog";

    public static TimePickerDialog newInstance(String showConfig, boolean isSingle, PickerConfig pickerConfig) {
        TimePickerDialog dialog = new TimePickerDialog();
        Bundle bundle = new Bundle();
        bundle.putString(SHOW_CONFIG, showConfig);
        bundle.putBoolean(IS_SINGLE, isSingle);
        bundle.putParcelable(PICKER_CONFIG, pickerConfig);
        dialog.setArguments(bundle);
        return dialog;
    }
    private void initView(View inflate) {
     cancelTv =  inflate.findViewById(R.id.cancel_tv);
     confirmTv =  inflate.findViewById(R.id.confirm_tv);
        startTimeRb =  inflate.findViewById( R.id.start_time_rb);
        text =  inflate.findViewById( R.id.text);
        endTimeRb =  inflate.findViewById( R.id.end_time_rb);
        radioGroup =  inflate.findViewById( R.id.radio_group);
        yearView =  inflate.findViewById( R.id.year);
        monthView =  inflate.findViewById( R.id.month);
        dayView =  inflate.findViewById( R.id.day);
        hourView =  inflate.findViewById( R.id.hour);
        minuteView =  inflate.findViewById( R.id.minute);
        linearWheel =  inflate.findViewById( R.id.linear_wheel);
        timeOfOtherTv =  inflate.findViewById( R.id.time_of_other_tv);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mShowConfig = bundle.getString(SHOW_CONFIG);
            isSingleChoice = bundle.getBoolean(IS_SINGLE);
            mPickerConfig = bundle.getParcelable(PICKER_CONFIG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);

        View view = inflater.inflate(R.layout.dialog_select_item, container, false);
        initView(view);
        configTimeWheel(view);

        initEvent();
        return view;
    }

    private void configTimeWheel(View view) {
        if (isSingleChoice) {
            text.setVisibility(View.GONE);
            endTimeRb.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mShowConfig) && mShowConfig.split("|").length == 5) {
            String[] showConfig = mShowConfig.split("|");
            setViewVisibility("1".equals(showConfig[0]), yearView);
            setViewVisibility("1".equals(showConfig[1]), monthView);
            setViewVisibility("1".equals(showConfig[2]), dayView);
            setViewVisibility("1".equals(showConfig[3]), hourView);
            setViewVisibility("1".equals(showConfig[4]), minuteView);
        }
        if (mPickerConfig == null) {
            mPickerConfig = buildPickerConfig();
        }
        mTimeWheel = new TimeWheel(view, getActivity().getApplicationContext(), mPickerConfig);
        mTimeWheel.setOnDataSelectedListener(this);

        if (mPickerConfig.mType == Type.YEAR_MONTH_DAY) {
            ViewGroup.LayoutParams layoutParams = wheelContainer.getLayoutParams();
            layoutParams.width = ToolsUtil.getWindowHeight(getActivity()) - 100;
            wheelContainer.setLayoutParams(layoutParams);
        }
    }

    private void setViewVisibility(boolean show, View view) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void initEvent() {
        cancelTv.setOnClickListener(this);
        confirmTv.setOnClickListener(this);
        radioGroup.check(R.id.start_time_rb);
        radioGroup.setOnCheckedChangeListener(this);

        startMillis = /*isSingleChoice ? */mPickerConfig.mCurrentCalendar.milliSeconds/* : mPickerConfig.mMinCalendar.milliSeconds*/; // ToolsUtil.getTodayMillis(true);
        endMillis = mPickerConfig.mMaxCalendar.milliSeconds; // ToolsUtil.getTodayMillis(false);
        endTimeStr = ToolsUtil.convertTimeLongToString(endMillis);
        startTimeStr = ToolsUtil.convertTimeLongToString(startMillis);

        setTitleTime(startTimeRb, startTimeStr);
        setTitleTime(endTimeRb, endTimeStr);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = ToolsUtil.getWindowHeight(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_tv:
                dismiss();
                break;
            case R.id.confirm_tv:
                if (!isSingleChoice && endMillis <= startMillis) {
                    Toast.makeText(getContext(), "结束时间必须大于起始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mListener != null) {
                    mListener.onTimeSelected(startMillis, startTimeStr, endMillis, endTimeStr);
                }
                String tag = this.getTag();
                if (closeListener != null) {
                    closeListener.onTimeClose( tag,startMillis, startTimeStr, endMillis, endTimeStr);
                    Log.i(TAG, "onClick: "+tag);
                }

                dismiss();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.start_time_rb) {
            mSelectType = 0;
        } else {
            mSelectType = 1;
        }
    }

    @Override
    public void onDataSelected(long timeMillis, String timeStr) {
        if (mSelectType == 0) {
            startMillis = timeMillis;
            startTimeStr = ToolsUtil.formatLongTime(startMillis, "M-dd-yyyy HH:mm");
            setTitleTime(startTimeRb, timeStr);
            } else {
//            endMillis = timeMillis;
            long todayMillis = ToolsUtil.getTodayMillis(true);
            if (timeMillis >= todayMillis) {
                endMillis = System.currentTimeMillis();
            } else {
                endMillis = timeMillis + 24 * 60 * 60 * 1000 - 1;
            }
            endTimeStr = ToolsUtil.formatLongTime(endMillis, "M-dd-yyyy HH:mm");
           setTitleTime(endTimeRb, timeStr);
        }
    }

    private void setTitleTime(RadioButton radioButton, String timeStr) {
        if (TextUtils.isEmpty(timeStr) || timeStr.length() < 11 || radioButton == null) return;

        if (mPickerConfig != null && mPickerConfig.mType == Type.YEAR_MONTH_DAY) {
            radioButton.setText(timeStr.substring(0, 11));
        } else {
            radioButton.setText(timeStr);
        }
    }

    private PickerConfig buildPickerConfig() {
        long fiveYears = 5L * 365 * 1000 * 60 * 60 * 24L;
        PickerConfig config = new PickerConfig();
        config.cyclic = true;
        config.mCurrentCalendar = new WheelCalendar(System.currentTimeMillis());
        config.mMaxCalendar = new WheelCalendar(System.currentTimeMillis());
        config.mMinCalendar = new WheelCalendar(System.currentTimeMillis() - fiveYears);
        config.mType = Type.YEAR_MONTH_DAY;
        config.mWheelTVSize = 16;
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTimerSelectListener) {
            mListener = (OnTimerSelectListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mTimeWheel != null) {
            mTimeWheel.release();
            mTimeWheel = null;
        }
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mListener != null) {
            mListener.onDismiss();
        }
    }

    public interface OnTimerSelectListener {
        void onTimeSelected(long startTimeMillis, String startTimeStr, long endTimeMillis, String endTimeStr);

        void onDismiss();
    }

    public static void  setListener(OnTimerSelcetCloseListener listener) {
       closeListener = listener;
    }

    public interface OnTimerSelcetCloseListener {
        //时间选择结束回调
        void onTimeClose(String tag, long startTimeMillis, String startTimeStr, long endTimeMillis, String endTimeStr);

        //dialog关闭
        void onClose();
    }
}
