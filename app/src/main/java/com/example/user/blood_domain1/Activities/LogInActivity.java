package com.example.user.blood_domain1.Activities;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.user.blood_domain1.Database.LoginDataBaseAdapter;
import com.example.user.blood_domain1.Models.User;
import com.example.user.blood_domain1.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LogInActivity extends Activity {
    EditText editTextUserName,editTextPassword,editTextConfirmPassword,editTextAddress;
    Button btnCreateAccount;
    Spinner sblood;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page2);


        // get Instance  of Database Adapter
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        // Get Refferences of Views
        editTextUserName=(EditText)findViewById(R.id.editText);
        editTextPassword=(EditText)findViewById(R.id.editText4);
        editTextConfirmPassword=(EditText)findViewById(R.id.editText5);
        editTextAddress=(EditText)findViewById(R.id.editText3);

        sblood= (Spinner) findViewById(R.id.sblood);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sblood.setAdapter(adapter);


        btnCreateAccount=(Button)findViewById(R.id.buttoncreate);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                String address = editTextAddress.getText().toString();

                // check if any of the fields are vaccant
                if (userName.equals("") || password.equals("") || confirmPassword.equals("") || address.equals("")) {
                    Toast.makeText(getApplicationContext(), "Field Vacant", Toast.LENGTH_LONG).show();
                    return;
                }
                // check if both password matches
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    // Save the Data in Database
                    Geocoder geoCoder = new Geocoder(LogInActivity.this, Locale.getDefault());
                    try {
                        List<Address> addresses = geoCoder.getFromLocationName(address, 1);
                        if (addresses.size() > 0) {
                            Double lat = (double) (addresses.get(0).getLatitude());
                            Double lang = (double) (addresses.get(0).getLongitude());

                            Log.d("lat-long", "" + lat + "......." + lang);

                            User user = new User(userName, password, address, lat, lang,sblood.getSelectedItem().toString());
                            String msg= LoginDataBaseAdapter.insertEntry(user);
                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                            startActivity(i);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

                }
                );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
        startActivity(i);
    }
}
