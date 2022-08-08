package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class VenueActivity extends AppCompatActivity implements ReadsVenue, ReadsEvent {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> eventsInVenueList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        // Get venue name passed from CustomerHomepageActivity
        Intent i = getIntent();
        String venueName = i.getStringExtra("venueName");

        // Set TextView header to venue name
        TextView venueNameText = (TextView) findViewById(R.id.ctrVenueName);
        venueNameText.setText(venueName);

        eventsInVenueList = new ArrayList<EventItem>();

        DatabaseFunctions.readVenueFromDatabase(db, venueName, this);

        // Create spinner with events listed under current venue
        Spinner eventsInVenueSpinner = (Spinner) findViewById(R.id.ctrEventsInVenueSpinner);
        eventsInVenueSpinner.setAdapter(eventAdapter);
    }

    // TODO: Change below code based on updated DB functions

    @Override
    public void onVenueReadSuccess(Venue venue) {
        // Convert all eventKeys to Event objects

    }


    @Override
    public void onVenueReadError(String message) {
        Log.d("andre-testing-error", message);
    }


    @Override
    public void onEventReadSuccess(Event event) {

    }


    @Override
    public void onEventReadError(String message) {
        Log.d("andre-testing-error", message);
    }


    // Send customer to schedule a new event
    public void goToScheduleEventPage(View v){
        Intent intent = new Intent(this, ScheduleEventActivity.class);
        startActivity(intent);
    }


    // Add Event to
}