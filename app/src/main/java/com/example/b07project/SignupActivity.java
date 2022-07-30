package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Get username data from MainActivity
        Intent i = getIntent();
        EditText t = (EditText)findViewById(R.id.ctrEmailSignup);
        t.setText(i.getStringExtra("Email"));
    }


}