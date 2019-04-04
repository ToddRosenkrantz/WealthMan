package com.example.WealthMan;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class TransactionEdit extends AppCompatActivity {
    Button svButton;
    TextView m_Symbol;
    EditText m_Date;
    EditText m_Price;
    EditText m_Qty;
    private DatePickerDialog mDatePickerDialog;

    DatabaseHelper db;
    public static final String MY_PREFS_FILE = "wealthman_prefs";
    private String symbolName = "AAPL";
    private int userid = 1;
    private String quantity;
    private String price;
    private String date;
    private int txID;

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

        svButton = (Button) findViewById((R.id.svButton));
        m_Symbol = (TextView) findViewById(R.id.log_symbol);
        m_Date = (EditText) findViewById(R.id.log_date);
        m_Price = (EditText) findViewById(R.id.log_price);
        m_Qty = (EditText) findViewById(R.id.log_qty);
        m_Symbol.setText(intent.getStringExtra("symbol"));
        m_Price.setText(intent.getStringExtra("price"));
        m_Qty.setText(intent.getStringExtra("quantity"));
        price = intent.getStringExtra("price");
        m_Date.setText(intent.getStringExtra("date"));
        txID = intent.getIntExtra("tx_id", -1);

        setDateTimeField();
        m_Date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        svButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("userid: "+ userid);
                System.out.println("symbol: " + symbolName);
                System.out.println(" price: " + m_Price.getText());
                System.out.println("   qty: " + m_Qty.getText());
                System.out.println("  date: " + m_Date.getText());
                Double s_price = 0.0;
                Double s_qty = 0.0;
                if(!m_Price.toString().isEmpty())
                    s_price = Double.valueOf(m_Price.getText().toString().trim());
                if(m_Qty != null)
                    s_qty = Double.valueOf(m_Qty.getText().toString().trim());
                db.uptateTx(txID, userid,symbolName,s_qty,s_price,m_Date.getText().toString().trim());
                finish();
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
//        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }
}
