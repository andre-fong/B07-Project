package com.example.b07project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class VenueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ReadsCustomer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        Spinner venue_spinner = findViewById(R.id.venue_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venue_spinner.setAdapter(adapter);
        venue_spinner.setOnItemSelectedListener(this);

    }

    // Copy the following code onto the previous page to open Venues Page
//    public void goToVenuePage(){
//        Intent intent = new Intent(this, VenueActivity.class);
//        startActivity(intent);
//    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //Called by DatabaseFunctions
    public void updateUI(){
        //TODO update ui
    }

    @Override
    public void onCustomerReadSuccess(Customer c) {
//        Log.d("readCustomer", customerMap.toString());
        Log.d("readCustomer", "uid: " + c.getUid() + "joinedEvents: " + c.getJoinedEvents().toString() + "hostedEvents: " + c.getHostedEvents().toString());
    }

    @Override
    public void onCustomerReadError(String errorMessage) {

    }
}