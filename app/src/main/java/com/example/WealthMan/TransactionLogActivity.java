package com.example.WealthMan;

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

public class TransactionLogActivity extends AppCompatActivity implements TxAdapter.ItemClickListener {
    TxAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String m_Text = "";
    DatabaseHelper db;
    public static final String MY_PREFS_FILE = "wealthman_prefs";

    private String symbolName = "AAPL";
    private int userid = 1;

    private Double m_Shares = 0.0;
    private Double m_Price = 0.0;
    private String m_Date = "";

    private SharedPreferences preference;


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
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if (tList != null) {
            adapter = new TxAdapter(this, tList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Launch Add Activity", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                addTransaction(symbolName);
            }
        });
    }

    /*    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_transaction, menu);
            return true;
        }*/
    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

    }

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
    public void addTransaction(String sym) {
/*        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LinearLayout layout = new LinearLayout(TransactionLogActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        final EditText Shares_Box = new EditText(TransactionLogActivity.this);
        Shares_Box.setHint("Shares");
        Shares_Box.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        layout.addView(Shares_Box);
        final EditText Price_Box = new EditText(TransactionLogActivity.this);
        Price_Box.setHint("Price");
        Price_Box.setInputType(2002);
        layout.addView(Price_Box);
        final EditText Date_Box = new EditText(TransactionLogActivity.this);
        Date_Box.setHint("Date");
        Date_Box.setInputType(4);
        layout.addView(Date_Box);
        builder.setTitle("Shares Log Entry");

// Set up the input
//        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(layout);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Shares = Double.parseDouble(Shares_Box.getText().toString());
                m_Price = Double.parseDouble(Price_Box.getText().toString());
                m_Date = Date_Box.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();*/
        final Intent intent = getIntent();
//        final int userid = preference.getInt("UserID", 1);
//        symbolName = intent.getStringExtra("Symbol");
        nextActivity(symbolName, userid);
    }
    public void nextActivity(String symbol, Integer ID){
        Intent intent = new Intent(this,TransactionEntry.class);
//        Intent intent = new Intent(HomeActivity.this,TransactionLogActivity.class);
        intent.putExtra("Symbol",symbol);
        intent.putExtra("UserID",ID);
//        System.out.println("UserID: " + ID);
        startActivity(intent);
    }
}
