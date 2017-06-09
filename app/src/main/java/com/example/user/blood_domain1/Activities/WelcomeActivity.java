package com.example.user.blood_domain1.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.user.blood_domain1.Database.LoginDataBaseAdapter;
import com.example.user.blood_domain1.R;

public class WelcomeActivity extends Activity
{
    Button btnSignIn,btnSignUp;
    LoginDataBaseAdapter loginDataBaseAdapter;
    boolean signin=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcomeactivity);

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        btnSignIn = (Button) findViewById(R.id.button2);
        btnSignUp = (Button) findViewById(R.id.button);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intentSignUP = new Intent(WelcomeActivity.this, LogInActivity.class);
                startActivity(intentSignUP);
            }
        });

    }

    public void signIn(View V)
    {
        signin=true;
        setContentView(R.layout.login);

        // get the Refferences of views
        final  EditText editTextUserName=(EditText)findViewById(R.id.editText6);
        final  EditText editTextPassword=(EditText)findViewById(R.id.editText7);

        Button btnSignIn=(Button)findViewById(R.id.buttonlogin);

        // Set On ClickListener
        btnSignIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // get The User name and Password
                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                String storedPassword=LoginDataBaseAdapter.getSinlgeEntry(userName);

                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword))
                {
                    Toast.makeText(WelcomeActivity.this, "Congrats: Login Successfull", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(WelcomeActivity.this, SearchMapActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(WelcomeActivity.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(signin)
        {
            Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(i);
        }

    }
}