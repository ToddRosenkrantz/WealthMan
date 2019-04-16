//RegisterActivity.java


//package com.demo.easylearn;
package com.example.WealthMan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PatternMatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
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
    public static final String MY_PREFS_FILE = "wealthman_prefs";


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

                // Validation occurs here
                int reg_error;
                if(user.length()==0
                        || pwd.length() == 0
                        || cnf_pwd.length() == 0
                        || email.length() == 0
                        || pin.length() == 0) reg_error = 1;
                else if (!pwd.equals(cnf_pwd)) reg_error = 2;
                else if (pin.length() < 4 || pin.length() > 8) reg_error = 3;
                else if (pwd.length() < 6 || pwd.length() > 12) reg_error = 4;
                else if (db.checkUserExist(email)) reg_error = 5;
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) reg_error = 6;
                else reg_error = 0;

                switch (reg_error){
                    case 0:             // Everything is good => add user to database
                        long val = db.addUser(user, pwd, email, pin);
                        if(val > 0){
                            int userid = db.getUserId(email);
                            long result = db.createWatchlist(userid);  //special one time add
                            SharedPreferences preference = getSharedPreferences(MY_PREFS_FILE, MODE_PRIVATE);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putInt("UserID", userid);   // Store new time to update
                            editor.commit();
                            if (result > 0) {
                                editor.putBoolean("setupDone", true);
                                editor.commit();
                            } else
                                Toast.makeText(RegisterActivity.this, "Database Error creating initial Watch List", Toast.LENGTH_LONG).show();
                            Toast.makeText(RegisterActivity.this,"You have registered",Toast.LENGTH_SHORT).show();
                            Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(moveToLogin);
                            finish();
                        }
                        else
                            Toast.makeText(RegisterActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:             // Need all fields entered
                        Toast.makeText(RegisterActivity.this,
                                "Please Enter ALL fields",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:             // Passwords must match
                        Toast.makeText(RegisterActivity.this,"" +
                                "Passwords do not match",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:             // PIN must be between 4 and 8 chars in length
                        Toast.makeText(RegisterActivity.this,
                                "PIN must be between 4 and 8 digits", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:             // Password must be between 6 and 12 chars in length
                        Toast.makeText(RegisterActivity.this,
                                "Passwords must be between 6 and 12 characters long", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:             // User must not be in database
                        Toast.makeText(RegisterActivity.this,
                                "That user already exists",Toast.LENGTH_SHORT).show();
                        break;
                    case 6:             // Email must not be valid format
                        Toast.makeText(RegisterActivity.this,
                                "Please enter a valid email address",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}