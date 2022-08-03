package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.common.data.DataBufferObserverSet;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class VenueActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UpdatesUI {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        Spinner venue_spinner = findViewById(R.id.venue_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venue_spinner.setAdapter(adapter);
        venue_spinner.setOnItemSelectedListener(this);
        DatabaseFunctions.readAllVenuesFromDatabase(FirebaseDatabase.getInstance(), new HashMap<String, Venue>(), this);
    }

    // Copy the following code onto the previous page to open Venues Page
//    public void goToVenuePage(){
//        Intent intent = new Intent(this, VenueActivity.class);
//        startActivity(intent);
//    }

    public void goToScheduleEventPage(View v){
        Intent intent = new Intent(this, ScheduleEventActivity.class);
        startActivity(intent);
    }

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
}