package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

    public void attemptSignup(View v) {
        String email = ((TextView)findViewById(R.id.ctrEmailSignup)).getText().toString();
        String pwd = ((TextView)findViewById(R.id.ctrPasswordSignup)).getText().toString();

        //signup(email, pwd)
    }



}