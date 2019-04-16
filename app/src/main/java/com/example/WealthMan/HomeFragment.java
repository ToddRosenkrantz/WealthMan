package com.example.WealthMan;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.google.gson.GsonBuilder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.content.Context.MODE_PRIVATE;

/**
 * 首页fragment
 *
 * @author gjc
 * @version 1.0.0
 * @since 2019-04-09
 */
public class HomeFragment extends Fragment {

    public static final String MY_PREFS_FILE = "wealthman_prefs";
    private static HomeFragment instance;
    private ArrayList<IconBean> mIconBeenList = new ArrayList<>();
    private ArrayList<stockValue> portfolioValue = new ArrayList<>();
    private ListView lv;
    private String latestprice;
    private IconAdapter sa;
    DatabaseHelper db;
    private EditText p_start;
    private EditText p_end;
    private TextView totalPorfolioValue;
    private DatePickerDialog sDatePickerDialog, eDatePickerDialog;
    private APIInterface.Batches qList;
    public double sumValue = 0.0;
    public double sumCost = 0.0;
    public String sValue;
    public String sCost;
    int userid;
    public final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    String start_date = "2014-04-01";
    ZoneId zoneId = ZoneId.systemDefault() ;  // Or ZoneOffset.UTC or ZoneId.systemDefault()
    String today = LocalDate.now( zoneId ).toString() ;
    AlertDialog.Builder confirmExit;


    /*
     * Change to type CustomAutoCompleteView instead of AutoCompleteTextView
     * since we are extending to customize the view and disable filter
     * The same with the XML view, type will be CustomAutoCompleteView
     */
    CustomAutoCompleteView myAutoComplete;
    // adapter for auto-complete
    ArrayAdapter<NameSymbol> myAdapter;
    boolean dbsuccess = true;

    public static HomeFragment getInstance(){
        if (instance == null) {
            instance = new HomeFragment();
        }
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        //        final int userid = intent.getIntExtra("UserID", 1);
        SharedPreferences preference = getContext().getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        final int userid = preference.getInt("UserID", 1);
/*        Calendar newDate = Calendar.getInstance();
        try {
            newDate.setTime(sdf.parse(start_date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date startDate = newDate.getTime();*/
//        String today = sdf.format(startDate);

        DecimalFormatSymbols my_format = new DecimalFormatSymbols();
        my_format.setGroupingSeparator(',');
        my_format.setDecimalSeparator('.');

        final DecimalFormat decimalFormat = new DecimalFormat("$#,###.00", my_format);
        final DecimalFormat percentFormat = new DecimalFormat("0.00%");


//        System.out.println("UserID: " + userid);
        db = new DatabaseHelper(getContext());

        final TextView mTextView = (TextView) view.findViewById(R.id.text);
        totalPorfolioValue = (TextView) view.findViewById(R.id.total_value);
        final TextView totalPorfolioCost = (TextView) view.findViewById(R.id.total_cost);
        final TextView totalGainLoss = (TextView) view.findViewById(R.id.gain_loss);
        p_start = (EditText) view.findViewById(R.id.periodStart);
        start_date = db.getMinDate(userid);
        System.out.println("Min Date: " + start_date);
        if (start_date == null|| start_date.isEmpty())
            start_date = sdf.format(new Date());
        p_start.setText(start_date);
        p_start.setFocusable(false);
        p_end = (EditText) view.findViewById(R.id.periodEnd);
        p_end.setText(today);
        p_end.setFocusable(false);
        p_start.setVisibility(View.VISIBLE);
        p_end.setVisibility(View.VISIBLE);
        view.findViewById(R.id.labelStart).setVisibility(View.VISIBLE);
        view.findViewById(R.id.labelEnd).setVisibility(View.VISIBLE);


        setupApp();
        setStartPeriodDate();
        setEndPeriodDate();

        String symbols = db.getWatchList(userid).trim();
        String portfolioSymbols = db.getPortfolioSymbols(userid, p_start.getText().toString(), p_end.getText().toString());

        // ALL findViewById must be after the following line!
        getWatchListData(symbols);
        getPortfolioData(portfolioSymbols, userid);

        lv = (ListView)view.findViewById(R.id.lv);
        //为listview添加adapter
        lv.setAdapter(new IconAdapter(getContext(),mIconBeenList));
        sa = (IconAdapter) lv.getAdapter();

//  This is the section for the Search bar autocomplete
        try {
            // autocompletetextview is in activity_home.xml
            myAutoComplete = (CustomAutoCompleteView) view.findViewById(R.id.myautocomplete);

            myAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                    RelativeLayout rl = (RelativeLayout) arg1;
                    LinearLayout Lin1 = (LinearLayout) rl.getChildAt(0);
                    TextView tv = (TextView) Lin1.getChildAt(0);
                    TextView sym = (TextView) Lin1.getChildAt(1);
                    //                String companyToSend = tv.getText().toString();
                    String stringToSend = sym.getText().toString();
                    myAutoComplete.setText("");
//                    myAutoComplete.setText(tv.getText().toString());
                    Log.e("MAIN", stringToSend);
                    nextActivity(stringToSend, userid);
                }

            });
            // add the listener so it will tries to suggest while the user types
            myAutoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(this, getActivity()));
            // ObjectItemData has no value at first
            NameSymbol[] ObjectItemData = new NameSymbol[0];
            // set the custom ArrayAdapter
            myAdapter = new AutocompleteCustomArrayAdapter(getContext(), R.layout.list_view_row_item, ObjectItemData);
            myAutoComplete.setAdapter(myAdapter);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//  End of Search Bar
// Begin various listeners
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                IconBean stock = mIconBeenList.get(position);
                nextActivity(stock.symbol, userid);
            }
        });
        totalPorfolioValue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                getWatchListData(db.getWatchList(userid).trim());
                getPortfolioData(db.getPortfolioSymbols(userid, p_start.getText().toString(), p_end.getText().toString()), userid);
//                System.out.println("Portfolio items = " + portfolioValue.size());
//                System.out.println("Final C: " +sumCost + " , Final V: " + sumValue);
                sValue = decimalFormat.format(sumValue);
                sCost = decimalFormat.format((sumCost));
                double sumGainLoss = ((sumValue - sumCost)/sumCost);
                String sGainLoss;
                if(Double.isNaN(sumGainLoss))
                    sGainLoss = "No Data";
                else
                    sGainLoss = percentFormat.format(sumGainLoss);
                totalPorfolioValue.setText("Total Value: "+ sValue);
                totalPorfolioCost.setText("Cost: " + sCost);
                totalGainLoss.setText("Performance: " + sGainLoss);
                if(sumGainLoss < 0){
                    totalPorfolioValue.setBackgroundColor(Color.argb(41,223, 108, 88));
                    totalGainLoss.setBackgroundColor(Color.argb(41,223, 108, 88));
                    totalPorfolioCost.setBackgroundColor(Color.argb(41,223, 108, 88));

                }
                else {
                    totalPorfolioValue.setBackgroundColor(Color.argb(41, 156, 223, 88));
                    totalGainLoss.setBackgroundColor(Color.argb(41, 156, 223, 88));
                    totalPorfolioCost.setBackgroundColor(Color.argb(41, 156, 223, 88));
                }
                sa.notifyDataSetChanged();

//                return false;
            }
        });
        p_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cldr = myDates(p_start.getText().toString());
                System.out.println("Starting with this string: " + p_start.getText().toString() + " with a year of: " + cldr.YEAR);
                sDatePickerDialog.updateDate(cldr.get(Calendar.YEAR),cldr.get(Calendar.MONTH) ,cldr.get(Calendar.DAY_OF_MONTH));
                sDatePickerDialog.show();
                getPortfolioData(db.getPortfolioSymbols(userid, p_start.getText().toString(), p_end.getText().toString()), userid);
              //  return false;
            }
        });
        p_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cldr = myDates(p_start.getText().toString());
                eDatePickerDialog.updateDate(cldr.get(Calendar.YEAR),cldr.get(Calendar.MONTH) ,cldr.get(Calendar.DAY_OF_MONTH));
                eDatePickerDialog.show();
                getPortfolioData(db.getPortfolioSymbols(userid, p_start.getText().toString(), p_end.getText().toString()), userid);
//                return false;
            }
        });

//        System.out.println("Portfolio items = " + portfolioValue.size());
//        System.out.println("Final C: " +sumCost + " , Final V: " + sumValue);
        sValue = decimalFormat.format(sumValue);
        sCost = decimalFormat.format((sumCost));
        Double sumGainLoss = sumValue - sumCost;
        String sGainLoss = decimalFormat.format(sumGainLoss);
        totalPorfolioValue.setText("Touch to update");
        totalPorfolioCost.setText("Cost ");
        totalGainLoss.setText("Performance ");
    }

// End Oncreate, begin various support functions

    /*  Not Implemented YET!
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    
        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
    
        //noinspection SimplifiableIfStatement
        if (id == R.id.test_activity) {
            Intent intent = new Intent(getContext(),TestActivity.class);
    //        Intent intent = new Intent(getContext(),TransactionLogActivity.class);
            intent.putExtra("Symbol","F");
            intent.putExtra("UserID",userid);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */
    public void nextActivity(String symbol, Integer ID){
        Intent intent = new Intent(getContext(),com.example.WealthMan.detail.view.DetailActivity.class);
//        Intent intent = new Intent(getContext(),TransactionLogActivity.class);
        intent.putExtra("Symbol",symbol);
        intent.putExtra("UserID",ID);
//        System.out.println("UserID: " + ID);
        startActivity(intent);
    }

    public void setupApp(){
        SharedPreferences preference = getContext().getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        long now = System.currentTimeMillis();
        long updateDue = preference.getLong("updateDue", 0);
//        if (false) {  // Calc next time to update
//        System.out.println("After Read Prefs: Now = " + now + " , Due = " + updateDue);
        if (updateDue < now) {  // Calc next time to update
//            System.out.println("Symbol Update due");
            updateDue = now + TimeUnit.MILLISECONDS.convert(30, TimeUnit.DAYS);
            if (updateSymbols()) {
                editor.putLong("updateDue", updateDue);   // Store new time to update
                editor.apply();
//                System.out.println("At commit: Now = " + now + " , Due = " + updateDue);
//                System.out.println("Update was saved");
            } else
                Toast.makeText(getContext(), "Error updating Stock Symbols", Toast.LENGTH_LONG).show();
        }
    }
    public void getWatchListData(String syms){
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote,news,chart&range=1m&last=5";
//        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("{}"))
                            Toast.makeText(getContext(), "No stocks being tracked", Toast.LENGTH_LONG).show();
                        else {
                            getData(response);
                            sa.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "That didn't work! Do you have internet?", Toast.LENGTH_LONG).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public boolean updateSymbols() {
        final RequestQueue queue = Volley.newRequestQueue(getContext());
//        System.out.println("Updating Symbols now");
        String symbolUrl = "https://api.iextrading.com/1.0/ref-data/symbols";
        final GsonBuilder gsonSymbols = new GsonBuilder();
        dbsuccess = false;
        StringRequest symbolRequest = new StringRequest(Request.Method.GET, symbolUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        APIInterface.stockSym[] mySyms = gsonSymbols.create().fromJson(response, APIInterface.stockSym[].class);
                        List<Pair> SymbolList = new ArrayList<Pair>();
//                        System.out.println("Length = " + mySyms.length);
                        long val = 0;
                        for (int i = 0; i < mySyms.length; i++) {
                            Pair temp = new Pair(mySyms[i].name, mySyms[i].symbol);
                            SymbolList.add(temp);
                        }
                        db.populateSymbols(SymbolList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "That didn't work! Do you have an internet connection?", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(symbolRequest);
        return true;
    }

    public void getData(String jsonData) {
        mIconBeenList.clear();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(APIInterface.Batches.class, new APIInterface.CompanyListDeserializer());
        APIInterface.Batches watchList = gsonBuilder.create().fromJson(jsonData, APIInterface.Batches.class);
//        Gson gsonPretty = new GsonBuilder().setPrettyPrinting().create();
//        System.out.println(watchList);
//        String json = gsonPretty.toJson(watchList);
//        System.out.println("JSON = " + json);
        for (int index = 0; index < watchList.batches.size(); index++) {
//            String coSym = watchList.batches.get(index).coSym;
            ArrayList<Pair> chartData = new ArrayList<>();
            for (int i = 0 ; i < watchList.batches.get(index).chart.size(); i++){
//                float xValue = Float.valueOf(watchList.batches.get(index).chart.get(i).date.replace("-",""));
                float xValue = Float.valueOf(i+1);
                float yValue = (float)watchList.batches.get(index).chart.get(i).close;
                Pair data = new Pair(xValue, yValue);
                chartData.add(data);
            }
            IconBean symbol = new IconBean(
                    watchList.batches.get(index).quote.symbol,
                    watchList.batches.get(index).quote.companyName,
                    watchList.batches.get(index).quote.latestPrice,
                    watchList.batches.get(index).quote.change,
                    chartData
            );
            mIconBeenList.add(symbol);
        }
    }
    private void setStartPeriodDate() {

        Calendar newCalendar = Calendar.getInstance();
        sDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date startDate = newDate.getTime();
                String fdate = sdf.format(startDate);

                p_start.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        eDatePickerDialog.getDatePicker().setMinDate(sdf.parse(p_start.getText().toString()));

    }
    private void setEndPeriodDate() {

        Calendar newCalendar = Calendar.getInstance();
        eDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date startDate = newDate.getTime();
                String fdate = sdf.format(startDate);
                p_end.setText(fdate);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
//        eDatePickerDialog.getDatePicker().setMinDate(sdf.parse(p_start.getText().toString()));

    }

    public void getPortfolioData(String syms, int userid){
        if (syms == ""){
            sumValue = 0.0;
            sumCost = 0.0;
            Toast.makeText(getContext(), "No assets in that date range", Toast.LENGTH_LONG).show();
        }
        final int uid = userid;
        final RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote,news,chart&range=1m&last=5";
//        String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + syms + "&types=quote";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ArrayList<stockValue> tempPortfolio = new ArrayList<>();
                        if (response.equals("{}"))
                            Toast.makeText(getActivity(), "No assets in that date range", Toast.LENGTH_LONG).show();
                        else {
                            sumCost = 0.0;
                            sumValue = 0.0;
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.registerTypeAdapter(APIInterface.Batches.class, new APIInterface.CompanyListDeserializer());
                            qList = gsonBuilder.create().fromJson(response, APIInterface.Batches.class);
                            for (int i = 0; i < qList.batches.size(); i++) {
//                                System.out.print(qList.batches.get(i).quote.symbol);
                                stockValue tempStock = db.getValue(uid, qList.batches.get(i).quote.symbol, p_start.getText().toString(), p_end.getText().toString());
//                                tempStock = db.getValue(userid, qList.batches.get(i).quote.symbol);
//                                System.out.print(uid+","+ qList.batches.get(i).quote.symbol + "," +p_start.getText().toString() + "," +p_end.getText().toString());
                                tempStock.setCurrentPrice(qList.batches.get(i).quote.delayedPrice);
                                if (tempStock.shares > 0){
                                    sumCost += tempStock.getExtendedPrice();
                                    sumValue += tempStock.getCurrentValue();
                                }
                                tempPortfolio.add(tempStock);
//                                System.out.println(tempStock.getSymbol() + "   C: " +sumCost + " , V: " + sumValue);
                            }
                            portfolioValue = tempPortfolio;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "That didn't work! Do you have internet?", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    /*
        public void getPortfolioData(int uid){
            final int userid = uid;
            String symbols = db.getPortfolioSymbols(userid);
            final RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://api.iextrading.com/1.0/stock/market/batch?symbols=" + symbols + "&types=quote";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("{}"))
                                Toast.makeText(getContext(), "No stocks being tracked", Toast.LENGTH_LONG).show();
                            else {
    //                            System.out.print(response);
                                updatePortfolioData(response, userid);
                                System.out.println("Done Port update");
                                System.out.println("Get Data Final C: " +sumCost + " , Final V: " + sumValue);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext(), "That didn't work! Do you have internet?", Toast.LENGTH_LONG).show();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    */
/*  Moved to HomeActivity after making this a fragment
    public void onBackPressed(){
        confirmExit = new AlertDialog.Builder(getActivity());
//        Toast.makeText(this, "You Long clicked " + adapter.getItem(p).getID() + " on row number " + p, Toast.LENGTH_SHORT).show();
//        removeAt(p, tList.indexOf(adapter.getItem(p)), adapter.getItem(p).getID());
        confirmExit.setMessage("Logout from Stock Recording System?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        //Creating dialog box
        AlertDialog alert = confirmExit.create();
        //Setting the title manually
        alert.show();
    }
*/
public Calendar myDates(String date){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    Calendar newDate = Calendar.getInstance();
    System.out.println("String: " + date +" Year: " + newDate.get(Calendar.YEAR) + " Month: " + newDate.get(Calendar.MONTH) + " DoM: " + newDate.get(Calendar.DAY_OF_MONTH));
    try {
        newDate.setTime(sdf.parse(date));
    } catch (ParseException e) {
        e.printStackTrace();
    }
    return newDate;
}
    public static class stockValue {
        private String symbol;
        private Double shares;
        private Double extendedPrice;
        private Double currentPrice;

        public stockValue(){
            symbol = "";
            shares = 0.0;
            extendedPrice = 0.0;
            currentPrice = 0.0;
        }

        public stockValue(String sym, Double shares, Double extPrice){
            symbol = sym;
            this.shares = shares;
            extendedPrice = extPrice;
        }

        public void setShares(Double shares){ this.shares = shares;}
        public double getShares(){ return shares; }
        public void setSymbol(String symbol){ this.symbol = symbol; }
        public String getSymbol(){ return symbol; }
        public double getExtendedPrice(){return extendedPrice;}
        public double getCurrentValue(){return currentPrice * shares;}
        public void setCurrentPrice(Double price) {currentPrice = price;}
    }
}
