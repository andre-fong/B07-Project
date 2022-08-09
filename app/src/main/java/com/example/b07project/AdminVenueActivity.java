package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class AdminVenueActivity extends AppCompatActivity implements ReadsVenue, DeletesEvent {
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
    }

    // Delete selected venue upon button click
    public void deleteEvent(View v){
        // Get spinner obj
        Spinner eventsInVenue = findViewById(R.id.AdminEventSpinner);

        // Get VenueItem selected
        EventItem selectedItem = eventsInVenueList.get(eventsInVenue.getSelectedItemPosition());
        String eventKey = selectedItem.getVenueEventLink();

        DatabaseFunctions.deleteEvent(db, eventKey, this);
    }


    @Override
    public void onDeleteEventSuccess(String eventKey) {
        Toast.makeText(this, eventKey + " deleted", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDeleteEventError(String errorMessage) {
        Toast.makeText(this, "Could not delete event: " + errorMessage, Toast.LENGTH_SHORT).show();
        Log.d("andre-testing-deleteeventfail", errorMessage);
    }


    @Override
    public void onVenueReadSuccess(Venue venue) {
        eventsInVenueList = new ArrayList<EventItem>();

        // Popup notif if venue has no events
        if (venue.getEvents().size() == 0) {
            Toast.makeText(this, venue.getName() + " has no events!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Event event : venue.getEvents().values()) {
            eventsInVenueList.add(new EventItem(event));
        }
        eventAdapter = new EventAdapter(this, eventsInVenueList);
        Spinner eventsInVenueSpinner = (Spinner) findViewById(R.id.ctrEventsInVenueSpinner);
        eventsInVenueSpinner.setAdapter(eventAdapter);
    }

    @Override
    public void onVenueReadError(String message) {
        Log.d("andre-testing-readvenueerror", message);
        Toast.makeText(this, "Error reading venue: " + message, Toast.LENGTH_SHORT).show();
    }
}