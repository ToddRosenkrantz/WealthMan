package com.example.WealthMan.detail.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.WealthMan.DatabaseHelper;
import com.example.WealthMan.R;


public class NoteActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText noteEdit;
    Button noteSaveButton;
    private String symbolName = "AAPL";
    private int userid = 1;
    private SharedPreferences preference;
    public static final String MY_PREFS_FILE = "wealthman_prefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        db = new DatabaseHelper(this);
//        db.noteDisply();
        SharedPreferences preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
        final Intent intent = getIntent();
        symbolName = intent.getStringExtra("Symbol");
        userid = intent.getIntExtra("UserID",1);
//        startActivity(intent);
        noteEdit = (EditText)findViewById(R.id.editnote);
        noteSaveButton=(Button)findViewById(R.id.button_save);
        noteEdit.setText(db.noteDisply(userid,symbolName));

        noteSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String note = noteEdit.getText().toString().trim();
                db.editNote(note, userid, symbolName);
                Toast.makeText(NoteActivity.this,"sucssce",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
