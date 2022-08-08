package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdminVenueActivity extends AppCompatActivity implements ReadsVenue{
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> eventsInVenueList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_venue);

        // Get venue name passed from AdminHomepageActivity
        Intent intent = getIntent();
        String venueName = intent.getStringExtra("venueName");

        // Set TextView header to venue name
        TextView venueNameText = (TextView) findViewById(R.id.AdminVenueName);
        venueNameText.setText(venueName);

        eventsInVenueList = new ArrayList<EventItem>();

        DatabaseFunctions.readVenueFromDatabase(db, venueName, this);

        // Create spinner with events listed under current venue
        Spinner eventsInVenueSpinner = (Spinner) findViewById(R.id.AdminEventSpinner);
        eventsInVenueSpinner.setAdapter(eventAdapter);
    }

    public void deleteEvent(View v){
        // TODO Implement this function
        // Deletes the event selected by adminEventSpinner (be careful will no event selected)
    }

    @Override
    public void onVenueReadSuccess(Venue venue) {
        // Convert all eventKeys to Event objects
    }

    @Override
    public void onVenueReadError(String message) {

    }
}