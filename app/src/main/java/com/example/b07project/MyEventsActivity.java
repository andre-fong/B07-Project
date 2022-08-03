package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class MyEventsActivity extends AppCompatActivity implements UpdatesUI {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private Map<String, Customer> CustomerMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        DatabaseFunctions.readCustomerFromDatabase(db, auth.getCurrentUser().getUid(), CustomerMap, this);
    }

    @Override
    public void updateUI() {
        Customer c = CustomerMap.get(auth.getCurrentUser().getUid());
        
    }
}