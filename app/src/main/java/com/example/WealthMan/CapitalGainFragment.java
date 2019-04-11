package com.example.WealthMan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WealthMan.detail.adapter.GainLossAdapter;
import com.example.WealthMan.detail.picker.TimePickerDialog;
import com.example.WealthMan.detail.picker.ToolsUtil;
import com.example.WealthMan.detail.picker.YearMonthDayDialog;
import com.example.WealthMan.detail.picker.config.PickerConfig;
import com.example.WealthMan.detail.picker.data.Type;
import com.example.WealthMan.detail.picker.data.WheelCalendar;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import static android.content.Context.MODE_PRIVATE;

/**
 * capital gain fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-04-09
 */
public class CapitalGainFragment extends Fragment implements View.OnClickListener {

    private static CapitalGainFragment instance;
    private TextView tvStartDate;
    private TextView tvEndDate;
    private TextView tvTotal;
    private RecyclerView rvContent;
    private long mStartMillis;
    public static final String MY_PREFS_FILE = "wealthman_prefs";
    private SharedPreferences preference;
    private int userid;
    private DatabaseHelper db;
    private List<Transaction> mData = new ArrayList<>();
    private GainLossAdapter mAdapter;
    private long startMillis;
    private String mSelectStartTime;
    private String mSelectEndTime;
    private DatePickerDialog mDatePickerDialog;

    public static CapitalGainFragment getInstance() {
        if (instance == null) {
            instance = new CapitalGainFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_capital_gain, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        db = new DatabaseHelper(getContext());
        preference = getActivity().getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        userid = preference.getInt("UserID", -1);

        tvStartDate = view.findViewById(R.id.tv_start_date);
        tvEndDate = view.findViewById(R.id.tv_end_date);
        tvTotal = view.findViewById(R.id.tv_total_gain);
        (view.findViewById(R.id.btn_search)).setOnClickListener(this);
        tvStartDate.setOnClickListener(this);
        tvEndDate.setOnClickListener(this);
        rvContent = view.findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new GainLossAdapter(getContext(), mData);
        rvContent.setAdapter(mAdapter);
        String minDate = db.getMinDate(userid);
        tvStartDate.setText(minDate);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String current = df.format(new Date());
        tvEndDate.setText(current);
        Log.e("TAG", "min == " + minDate);
        Log.e("TAG", "current == " + current);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_search:
                String startDate = tvStartDate.getText().toString().trim();
                String endDate = tvEndDate.getText().toString().trim();
                if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                    Toast.makeText(getContext(), "Please select date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mSelectStartTime.compareTo(mSelectEndTime) > 0) {
                    Toast.makeText(getContext(), "Invalid date, select again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                doSearch();
                break;

            case R.id.tv_start_date:
                Calendar newCalendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        final Date startDate = newDate.getTime();
                        String fdate = sd.format(startDate);

                        tvStartDate.setText(fdate);
                        mSelectStartTime = fdate;

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
                break;

            case R.id.tv_end_date:
                Calendar endCalendar = Calendar.getInstance();
                mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        final Date startDate = newDate.getTime();
                        String fdate = sd.format(startDate);

                        tvEndDate.setText(fdate);
                        mSelectEndTime = fdate;

                    }
                }, endCalendar.get(Calendar.YEAR), endCalendar.get(Calendar.MONTH), endCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
                break;
        }
    }

    private PickerConfig buildStartPickerConfig() {
        String minDate = db.getMinDate(userid);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        try {
            Date date = df.parse(minDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            startMillis = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PickerConfig config = new PickerConfig();
        config.cyclic = true;
        long currentTime;
        if (mStartMillis == 0) {
            currentTime = ToolsUtil.getTodayMillis(false);
        } else {
            currentTime = mStartMillis;
        }
        config.mCurrentCalendar = new WheelCalendar(currentTime);
        config.mMaxCalendar = new WheelCalendar(ToolsUtil.getTodayMillis(false));
        config.mMinCalendar = new WheelCalendar(startMillis);
        config.mType = Type.YEAR_MONTH_DAY;
        config.mWheelTVSize = 18;
        config.mWheelTVNormalColor = ContextCompat.getColor(getActivity(), R.color.colorCommonGrey);
        config.mWheelTVSelectorColor = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        config.mYear = "year";
        config.mMonth = "month";
        config.mDay = "day";
        return config;
    }

    /**
     * 选择时间监听
     */
    private class MyTimerSelectClose implements TimePickerDialog.OnTimerSelcetCloseListener {
        private final int Index;

        public MyTimerSelectClose(int i) {
            this.Index = i;
        }

        @Override
        public void onTimeClose(String tag, long startTimeMillis, String startTimeStr, long endTimeMillis, String endTimeStr) {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//            Date date = new Date(startTimeMillis);
//            String forDate = df.format(date);
//            if (0 == Index) {
//                tvStartDate.setText(forDate);
//                mSelectStartMillis = startTimeMillis;
//            } else {
//                tvEndDate.setText(forDate);
//                mSelectEndMillis = startTimeMillis;
//            }
        }

        @Override
        public void onClose() {

        }
    }

    /**
     * 查询数据库
     */
    private void doSearch() {
        if (db != null) {
            String startDate = tvStartDate.getText().toString().trim();
            String endDate = tvEndDate.getText().toString().trim();
            ArrayList<?> gainData = db.getGainData(userid, startDate, endDate);
            if (gainData.size() == 0) {
                Toast.makeText(getContext(), "No result.", Toast.LENGTH_SHORT).show();
                return;
            }
            mData.clear();
            mData.addAll((Collection<? extends Transaction>) gainData);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            Log.e("TAG", gainData.size() + "");
            DecimalFormat decimalFormat = new DecimalFormat("0.0000");
            Double totalGain = db.getTotalGain(userid, startDate, endDate);
            String str = decimalFormat.format(totalGain);
            tvTotal.setText(str);
        }

    }
}
