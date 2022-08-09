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
import java.util.Map;

public class AdminVenueActivity extends AppCompatActivity implements ReadsVenue, ReadsAllEvents{
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> eventsInVenueList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_venue);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get venue name passed from AdminHomepageActivity
        Intent intent = getIntent();
        String venueName = intent.getStringExtra("venueName");

        // Set TextView header to venue name
        TextView venueNameText = (TextView) findViewById(R.id.AdminVenueName);
        venueNameText.setText(venueName);

        DatabaseFunctions.readVenueFromDatabase(db, venueName, this);

        // Create spinner with events listed under current venue
        eventsInVenueList = new ArrayList<EventItem>();
        DatabaseFunctions.readAllEventsFromDatabase(db, this);
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
        Log.d("andre-testing-readallvenueserror", message);
    }

    @Override
    public void onAllEventsReadSuccess(Map<String, Event> eventMap) {
        for (Event event : eventMap.values()) {
            eventsInVenueList.add(new EventItem(event));
            Log.d("andre-testing", "EVENT read from db: " + event.getName());
        }

        eventAdapter = new EventAdapter(this, eventsInVenueList);
        Spinner eventsInVenueSpinner = (Spinner) findViewById(R.id.AdminEventSpinner);
        eventsInVenueSpinner.setAdapter(eventAdapter);
    }

    @Override
    public void onAllEventsReadError(String errorMessage) {
        Log.d("andre-testing-readalleventserror", errorMessage);
    }
}