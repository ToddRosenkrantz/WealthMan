package com.example.WealthMan;
// Many thanks to http://simpledeveloper.com/how-to-handle-click-events-in-android-recyclerviews/
// Elisha Chirchir

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

//public class TransactionLogActivity extends AppCompatActivity implements TxAdapter.ItemClickListener {
public class TransactionLogActivity extends AppCompatActivity{
    TxAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String m_Text = "";
    public DatabaseHelper db;
    public static final String MY_PREFS_FILE = "wealthman_prefs";

    private String symbolName = "AAPL";
    private int userid = 1;

    private SharedPreferences preference;
    public ArrayList<Transaction> tList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        db = new DatabaseHelper(this);
        // data to populate the RecyclerView with
        ArrayList<Transaction> tList;
        preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        final Intent intent = getIntent();
        userid = preference.getInt("UserID", 1);
        symbolName = intent.getStringExtra("Symbol");
        tList = (ArrayList<Transaction>) db.getShareData(userid, symbolName);
        /*
        ArrayList<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");
        animalNames.add("Dog");
        animalNames.add("Cat");
        animalNames.add("Ocelot");
        animalNames.add("Racoon");
        animalNames.add("Parrot");
        animalNames.add("Guinea Pig");
        animalNames.add("Hamster1");
        animalNames.add("Hamster2");
        animalNames.add("Hamster3");
        animalNames.add("Hamster4");
        animalNames.add("Hamster5");
        animalNames.add("Hamster6");
        animalNames.add("Hamster7");
*/

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.rvTransactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (tList != null) {
            adapter = new TxAdapter(this, tList);
//            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener
                .OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //handle click events here
                processClick(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                //handle longClick if any
                processLongClick(position);
            }
        }));
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Launch Add Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addTransaction(symbolName);
                adapter.notifyDataSetChanged();
            }
        });
    }
    /*    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_transaction, menu);
            return true;
        }*/
/*
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position).getID() + " on row number " + position, Toast.LENGTH_SHORT).show();
        editTx(position);
        adapter.notifyDataSetChanged();
    }
*/

    /*
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    */
    public void processClick(int p){
        editTx(p);
        Toast.makeText(this, "You clicked " + adapter.getItem(p).getID() + " on row number " + p, Toast.LENGTH_SHORT).show();
    }
    public void processLongClick(int p){
        Toast.makeText(this, "You Long clicked " + adapter.getItem(p).getID() + " on row number " + p, Toast.LENGTH_SHORT).show();
//        removeAt(p, tList.indexOf(adapter.getItem(p)), adapter.getItem(p).getID());
        removeAt(p, adapter.getItem(p).getID());
    }

    public void addTransaction(String sym) {
        Intent intent = new Intent(this,TransactionEntry.class);
        intent.putExtra("Symbol",sym);
        intent.putExtra("UserID",userid);
        startActivity(intent);
        updateList();
        adapter.notifyDataSetChanged();
    }
    public void editTx(Integer position){
        Intent intent = new Intent(this, TransactionEdit.class);
        Transaction temp = adapter.getItem(position);
        intent.putExtra("tx_id", temp.getID());
        intent.putExtra("symbol", temp.getSymbol());
        intent.putExtra("quantity", temp.getShares().toString());
        intent.putExtra("price", temp.getPrice().toString());
        intent.putExtra("date" , temp.getDate());
        startActivity(intent);
        updateList();
        adapter.notifyDataSetChanged();
    }
    public ArrayList<Transaction> updateList(){
        return (ArrayList<Transaction>) db.getShareData(userid, symbolName);
    }
//    public void removeAt(int p, int i, int id){
    public void removeAt(int p, int id){
//        System.out.println("position: " + p + " ,index: " + i + " ,ID#: " + id);
        System.out.println("position: " + p +  " ,ID#: " + id);
        db.deleteTx(id);
//        tList.remove(tList.indexOf(p));
        adapter.notifyItemRemoved(p);
//        adapter.notifyItemRangeChanged(p, tList.size());
    }
}
