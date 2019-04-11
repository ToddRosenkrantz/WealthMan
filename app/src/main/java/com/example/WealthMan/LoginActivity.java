package com.example.WealthMan;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//LoginActivity
public class LoginActivity extends AppCompatActivity {
    //EditText mTextUsername;
    EditText mTextEmail;
    EditText mTextPassword;
    Button mButtonLogin;
    TextView mTextViewRegister;
    TextView mTextViewForgot;
    public static final String MY_PREFS_FILE = "wealthman_prefs";

    DatabaseHelper db;
//    ViewGroup progressView;
//    protected boolean isProgressShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

/*
        Dialog dialog = new Dialog(this,android.R.style.Theme_Translucent_NoTitleBar);
        View v = this.getLayoutInflater().inflate(R.layout.progress_bar,null);
        dialog.setContentView(v);
        dialog.show();
*/

        db = new DatabaseHelper(this);
       //mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextEmail = (EditText)findViewById(R.id.edittext_useremail);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mButtonLogin = (Button)findViewById(R.id.button_login);
        mTextViewRegister = (TextView)findViewById(R.id.textview_register);
        mTextViewForgot = (TextView)findViewById(R.id.textview_forgot);

        mTextViewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
        mTextViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotIntent = new Intent(LoginActivity.this,ForgotActivity.class);
                startActivity(forgotIntent);
            }
        });

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mTextEmail.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
                //System.out.println("setOnClickListener:::"+email);
                mTextEmail.getText().clear();
                mTextPassword.getText().clear();
                Boolean res = db.checkUser(email, pwd);
                if(res)
                {
                    Intent HomePage = new Intent(LoginActivity.this,HomeActivity.class);
                    int userid = db.getUserId(email);
                    SharedPreferences preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
                    SharedPreferences.Editor editor = preference.edit();
                    editor.putInt("UserID", userid);   // Store new time to update
                    editor.commit();
                    System.out.println("User ID from DB: " + userid);
                    HomePage.putExtra ("UserID", userid);
                    finish();
//                    startActivity(HomePage);
                }
                else if(email.length()==0
                        || pwd.length() == 0)
                {
                    Toast.makeText(LoginActivity.this,
                            "Please Enter ALL fields", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                }


                //
            }
        });
    }

/*    public void showProgressingView() {

        if (!isProgressShowing) {
            View view=findViewById(R.id.progressBar1);
            view.bringToFront();
        }
    }

    public void hideProgressingView() {
        View v = this.findViewById(android.R.id.content).getRootView();
        ViewGroup viewGroup = (ViewGroup) v;
        viewGroup.removeView(progressView);
        isProgressShowing = false;
    }*/
}
