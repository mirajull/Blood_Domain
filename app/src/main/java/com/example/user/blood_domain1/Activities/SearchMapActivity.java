package com.example.user.blood_domain1.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.blood_domain1.Activities.MapsActivity;
import com.example.user.blood_domain1.R;

public class SearchMapActivity extends AppCompatActivity {

    EditText eAddress;
    Button search;
    Spinner sblood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__map);
        sblood= (Spinner) findViewById(R.id.ssearchblood);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sblood.setAdapter(adapter);

        search = (Button)findViewById(R.id.button3);
        eAddress = (EditText)findViewById(R.id.eAddress);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("address",eAddress.getText().toString());
                i.putExtra("bloodgroup",sblood.getSelectedItem().toString());
                startActivity(i);
            }
        });

    }
}
