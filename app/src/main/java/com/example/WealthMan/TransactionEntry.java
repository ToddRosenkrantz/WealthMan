package com.example.WealthMan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TransactionEntry extends AppCompatActivity {
    Button svButton;
    EditText m_Symbol;
    EditText m_Date;
    EditText m_Price;
    EditText m_Qty;
    private DatePickerDialog mDatePickerDialog;

    DatabaseHelper db;
    public static final String MY_PREFS_FILE = "wealthman_prefs";
    private String symbolName = "AAPL";
    private int userid = 1;
    private SharedPreferences preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_entry);
        db = new DatabaseHelper(this);
        // data to populate the RecyclerView with
        ArrayList<Transaction> tList;
        preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        final Intent intent = getIntent();
        userid = preference.getInt("UserID", 1);
        symbolName = intent.getStringExtra("Symbol");
        svButton = (Button) findViewById((R.id.svButton));
        m_Symbol = (EditText) findViewById(R.id.log_symbol);
        m_Date = (EditText) findViewById(R.id.log_date);
        m_Price = (EditText) findViewById(R.id.log_price);
        m_Qty = (EditText) findViewById(R.id.log_qty);

        setDateTimeField();
        m_Date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

    }
    private void setDateTimeField() {

        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                m_Date.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
}
