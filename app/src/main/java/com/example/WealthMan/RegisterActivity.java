//RegisterActivity.java


//package com.demo.easylearn;
package com.example.WealthMan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DatabaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    EditText mTextEmail;
    EditText mTextPin;
    Button mButtonRegister;
    TextView mTextViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText)findViewById(R.id.edittext_cnf_password);
        mTextEmail = (EditText)findViewById(R.id.user_email);
        mTextPin = (EditText)findViewById(R.id.recovery_pin);

        mButtonRegister = (Button)findViewById(R.id.button_register);
        mTextViewLogin = (TextView)findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent LoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(LoginIntent);
                finish();
            }
        });

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString().trim();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                String email = mTextEmail.getText().toString().trim();
                String pin = mTextPin.getText().toString().trim();

                if(pwd.equals(cnf_pwd)){
                    if (!db.checkUserExist(user)){
                        long val = db.addUser(user, pwd, email, pin);
                        if(val > 0){
                            Toast.makeText(RegisterActivity.this,"You have registered",Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(moveToLogin);
                            finish();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Registeration Error",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        Toast.makeText(RegisterActivity.this,"User Already Exists",Toast.LENGTH_SHORT).show();

                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}