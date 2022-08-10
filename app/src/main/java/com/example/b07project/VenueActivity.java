package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VenueActivity extends AppCompatActivity implements ReadsVenue {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> eventsInVenueList;
    private EventAdapter eventAdapter;
    private String venueName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // Get venue name passed from CustomerHomepageActivity
        Intent i = getIntent();
        String venueName = i.getStringExtra("venueName");

        // Set TextView header to venue name
        TextView venueNameText = (TextView) findViewById(R.id.ctrVenueName);
        venueNameText.setText(venueName);

        eventsInVenueList = new ArrayList<EventItem>();

        this.setTitle("Viewing venue: ");

        DatabaseFunctions.readVenueFromDatabase(db, venueName, this);
    }

    // TODO: Change below code based on updated DB functions

    @Override
    public void onVenueReadSuccess(Venue venue) {
        Log.d("andre-testing", "venuereadsuccess entered");
        venueName = venue.getName();

        Map<String, Event> events = venue.getEvents();

        if (events == null || events.size() == 0) {
            Toast.makeText(this, "No events in " + venue.getName(), Toast.LENGTH_SHORT).show();
            return;
        }

        // Enable button if there exist events
        Button toEvent = (Button) findViewById(R.id.ctrVenueEvent);
        toEvent.setEnabled(true);

        for (Event event : events.values()) {
            eventsInVenueList.add(new EventItem(event));
            Log.d("andre-testing-addeventinvenue", event.getKey());
        }

        eventAdapter = new EventAdapter(this, eventsInVenueList);

        // Create spinner with events listed under current venue
        Spinner eventsInVenueSpinner = (Spinner) findViewById(R.id.ctrEventsInVenueSpinner);
        eventsInVenueSpinner.setAdapter(eventAdapter);
    }


    @Override
    public void onVenueReadError(String message) {
        Log.d("error", message);
    }


    // Send customer to schedule a new event
    public void goToScheduleEventPage(View v){
        Intent intent = new Intent(this, ScheduleEventActivity.class);
        intent.putExtra("venueName", venueName);
        startActivity(intent);
    }
    
    
    public void goToVenueEvents(View view) {
        Spinner venueEventsSpinner = findViewById(R.id.ctrEventsInVenueSpinner);
        Log.d("lalala", "tatata");

        EventItem selectedItem = eventsInVenueList.get(venueEventsSpinner.getSelectedItemPosition());
        String venueEventLink = selectedItem.getVenueEventLink();
        Intent intentToEventActivity = new Intent(this, EventActivity.class);
        intentToEventActivity.putExtra("inter", venueEventLink);
        startActivity(intentToEventActivity);
    }
}