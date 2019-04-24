package com.example.WealthMan;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.WealthMan.detail.adapter.SearchListAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.example.WealthMan.LoginActivity.MY_PREFS_FILE;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private static SearchFragment instance;
    private DatabaseHelper db;
    private TextView svStartDate;
    private TextView svEndDate;
    private TextView tv_stock_date;
    private RecyclerView rvContent;
    private SearchListAdapter mAdapter;
    private int userid;
    private String mSelectStartTime;
    private String mSelectEndTime;
    private ArrayList<Transaction> mData = new ArrayList<>();
    private TextView sv_symbol;
    private ArrayList<Transaction> popList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    public static SearchFragment getInstance() {
        if (instance == null) {
            instance = new SearchFragment();
        }
        return instance;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        db = new DatabaseHelper(getContext());
        SharedPreferences preference = getActivity().getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        userid = preference.getInt("UserID", -1);

        svStartDate = view.findViewById(R.id.tv_start_date);
        sv_symbol = view.findViewById(R.id.tv_total_gain);
        sv_symbol.setOnClickListener(this);
        svEndDate = view.findViewById(R.id.tv_end_date);
//        tv_stock_date = view.findViewById(R.id.tv_stock_date);
        (view.findViewById(R.id.btn_search)).setOnClickListener(this);

        svStartDate.setOnClickListener(this);
        svEndDate.setOnClickListener(this);
        rvContent = view.findViewById(R.id.rv_content);
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new SearchListAdapter(getContext(), mData);
        rvContent.setAdapter(mAdapter);
        String minDate = db.getMinDate(userid, "translog");
        svStartDate.setText(minDate);
//        mSelectStartTime = minDate;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String current = df.format(new Date());
        svEndDate.setText(current);
        if (minDate == null)
            svStartDate.setText(current);
//        mSelectEndTime = current;
        Log.e("TAG", "min == " + minDate);
        Log.e("TAG", "current == " + current);
        mSelectStartTime = svStartDate.getText().toString();
        mSelectEndTime = svEndDate.getText().toString();
        doSearch();
    }

    /**
     * 查询数据库
     */
    private void doSearch() {
        if (db != null) {
            String startDate = svStartDate.getText().toString().trim();
            String endDate = svEndDate.getText().toString().trim();
            String symbol = sv_symbol.getText().toString().trim();
            ArrayList<Transaction> searchResults = db.getShareDataNew(userid, startDate, endDate, (TextUtils.isEmpty(symbol) ? null : symbol));
            if (searchResults.size() == 0) {
                Toast.makeText(getContext(), "No result.", Toast.LENGTH_SHORT).show();
                mAdapter.clearData();
                return;
            }
            mData.clear();
            mData.addAll((Collection<? extends Transaction>) searchResults);
            if (TextUtils.isEmpty(symbol)) {
                popList.addAll(mData);
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
            Log.e("TAG", searchResults.size() + "");
            DecimalFormat decimalFormat = new DecimalFormat("$#,###.00");
/*
            Double totalGain = db.getTotalGain(userid, startDate, endDate);
            if (totalGain <= 0) {
                totalGain = Math.abs(totalGain);
            } else {
                totalGain = totalGain * (-1);
            }
*/
//            String str = decimalFormat.format(TotalGains(gainData));
//            tvTotal.setText(str);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_total_gain:  //show popwindow
                showListPopWindow();
                break;
            case R.id.btn_search:
                String startDate = svStartDate.getText().toString().trim();
                String endDate = svEndDate.getText().toString().trim();
                if (TextUtils.isEmpty(startDate) || TextUtils.isEmpty(endDate)) {
                    Toast.makeText(getContext(), "Please select date!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSelectStartTime = svStartDate.getText().toString();
                mSelectEndTime = svEndDate.getText().toString();
                if (mSelectStartTime.compareTo(mSelectEndTime) > 0) {
                    Toast.makeText(getContext(), "Invalid date, select again!", Toast.LENGTH_SHORT).show();
                    return;
                }

                doSearch();
                break;
            case R.id.tv_start_date:
                Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog mDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        final Date startDate = newDate.getTime();
                        String fdate = sd.format(startDate);

                        svStartDate.setText(fdate);

                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePickerDialog.show();
                break;
            case R.id.tv_end_date:
                Calendar cala = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        final Date startDate = newDate.getTime();
                        String fdate = sd.format(startDate);
                        svEndDate.setText(fdate);

                    }
                }, cala.get(Calendar.YEAR), cala.get(Calendar.MONTH), cala.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
        }
    }

    private void showListPopWindow() {
        ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
        if (mData == null) {
            Toast.makeText(getContext(), "date null", Toast.LENGTH_SHORT).show();
        } else {
            List<String> list = removeDuplicateString(popList);
            listPopupWindow.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list));
            listPopupWindow.setAnchorView(sv_symbol);
            listPopupWindow.setModal(true);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sv_symbol.setText(list.get(position));
                    listPopupWindow.dismiss();
                }
            });
            listPopupWindow.show();
        }
    }
// TODO Instead of doing this in a function, use proper DB Query with 'DISTINCT"
    private List<String> removeDuplicateString(ArrayList<Transaction> mData) {
        List<String> list = new ArrayList<>();
        list.add("");
        for (Transaction transaction : mData) {
            list.add(transaction.getSymbol());
        }
        Set<String> set = new LinkedHashSet<>();
        set.addAll(list);

        list.clear();

        list.addAll(set);
        return list;
    }
}
