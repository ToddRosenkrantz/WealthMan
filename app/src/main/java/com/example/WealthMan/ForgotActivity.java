package com.example.WealthMan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Dialog;


public class ForgotActivity extends AppCompatActivity {

    DatabaseHelper db;
    EditText mTextEmail;
    EditText mTextPin;
    Button mButtonForget;
    TextView mTextViewLogin;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_forgot);
/*

        "@+id/button_Ok"
*/

            db = new DatabaseHelper(this);
            mTextEmail = (EditText)findViewById(R.id.user_email);
            mTextPin = (EditText)findViewById(R.id.recovery_pin);
            mButtonForget = (Button)findViewById(R.id.button_Ok);

            mButtonForget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String requestEmail = mTextEmail.getText().toString().trim();
                    String requestpin = mTextPin.getText().toString().trim();
                    mTextEmail.getText().clear();
                    mTextPin.getText().clear();
                    String  result = db.checkpassword(requestEmail, requestpin);
                    Toast.makeText(ForgotActivity.this,result,Toast.LENGTH_LONG).show();
                    Intent LoginPage = new Intent(ForgotActivity.this,LoginActivity.class);
                    startActivity(LoginPage);
                }
            });


    }
}
