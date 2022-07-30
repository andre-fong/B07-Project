package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Welcome to App!");

        // Create invisible clickable buttons
        Button adminButton = (Button)findViewById(R.id.ctrAdminLink);
        Button signupButton = (Button)findViewById(R.id.ctrCreateAccount);
        adminButton.setBackgroundColor(Color.TRANSPARENT);
        signupButton.setBackgroundColor(Color.TRANSPARENT);
    }

    // Start AdminLoginActivity
    public void adminLogin(View v) {
        // TODO: Intent to AdminLoginActivity
    }

    // Start SignupActivity
    public void createAcc(View v) {
        String email = ((TextView)findViewById(R.id.ctrEmailField)).getText().toString();
        Intent i = new Intent(this, SignupActivity.class);
        i.putExtra("Email", email);
        startActivity(i);
    }

    // Authentication
    public void submitReq(View v) {
        String email = ((TextView)findViewById(R.id.ctrEmailField)).getText().toString();
        String pwd = ((TextView)findViewById(R.id.ctrPasswordField)).getText().toString();

        // TODO: Firebase pros only

        // If email and pwd valid:
            // Create new Customer obj
            // Start MainAppActivity
        // Else
            // Toast.alert Invalid login credentials
    }
}