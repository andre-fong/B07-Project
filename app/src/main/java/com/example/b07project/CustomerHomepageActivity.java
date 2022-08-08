package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

// import DatabaseFunctions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerHomepageActivity extends AppCompatActivity implements ReadsAllEvents, ReadsAllVenues {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> upcomingEventList;
    private ArrayList<VenueItem> venuesList;
    private EventAdapter eventAdapter;
    private VenueAdapter venueAdapter;
//    private Map<String, Event> eventsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        // Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        upcomingEventList = new ArrayList<EventItem>();
        venuesList = new ArrayList<VenueItem>();

        DatabaseFunctions.readAllEventsFromDatabase(db, this);

        Log.d("andre-testing", "onCreate finished.");
    }


    // Executes when success with reading all events
    // Reads returned eventMap and updates spinner to display events
    @Override
    public void onAllEventsReadSuccess(Map<String, Event> eventMap) {
//        upcomingEventList = new ArrayList<EventItem>();               // may need after

        // TODO: Implement time for viewing upcoming events

        for (Event event : eventMap.values()) {
            // if event is upcoming:
            upcomingEventList.add(new EventItem(event));
            Log.d("andre-testing", "EVENT read from db: " + event.getKey());
        }

        // Create new EventAdapter to work with spinner
        eventAdapter = new EventAdapter(this, upcomingEventList);

        // Get spinner obj
        Spinner upcomingEventsSpinner = findViewById(R.id.ctrUpcomingEventsSpinner);

        // Update event spinner to display upcoming events
        upcomingEventsSpinner.setAdapter(eventAdapter);
    }


    // Executes when error with reading all events
    @Override
    public void onAllEventsReadError(String errorMessage) {
        Log.d("andre-testing-error", errorMessage);
    }


    @Override
    public void onAllVenuesReadSuccess(Map<String, Venue> venueMap) {
        for (Venue venue : venueMap.values()) {
            venuesList.add(new VenueItem(venue));
            Log.d("andre-testing", "VENUE read from db: " + venue.getName());
        }

        // Create new VenueAdapter to work with spinner
        venueAdapter = new VenueAdapter(this, venuesList);

        // Get spinner obj
        Spinner venuesSpinner = findViewById(R.id.ctrVenuesSpinner);

        // Update venue spinner to display venues
        venuesSpinner.setAdapter(venueAdapter);
    }


    @Override
    public void onAllVenuesReadError(String errorMessage) {
        Log.d("andre-testing-error", errorMessage);
    }


    // Send customer to EventActivity given selected EVENT (after clicking go)
    public void goToUpcoming(View v){

        // Get spinner obj
        Spinner upcomingEventsSpinner = findViewById(R.id.ctrUpcomingEventsSpinner);

        // Get EventItem selected
        EventItem selectedItem = upcomingEventList.get(upcomingEventsSpinner.getSelectedItemPosition());
        String venueEventLink = selectedItem.getVenueEventLink();
        Intent intentToEventActivity = new Intent(this, EventActivity.class);
        intentToEventActivity.putExtra("inter", venueEventLink);
        startActivity(intentToEventActivity);
    }


    // Send customer to VenueActivity given selected VENUE (after clicking go)
    public void goToVenue(View v) {

        // Get spinner obj
        Spinner venuesSpinner = findViewById(R.id.ctrVenuesSpinner);

        // Get VenueItem selected
        VenueItem selectedItem = venuesList.get(venuesSpinner.getSelectedItemPosition());
        String venueName = selectedItem.getVenueName();
        Intent intentToVenueActivity = new Intent(this, VenueActivity.class);
        intentToVenueActivity.putExtra("venueName", venueName);
        startActivity(intentToVenueActivity);
    }

    
    // Sends customer to MyEventsActivity
    public void myEventsClicked(View v) {
        Intent intentToMyEvents = new Intent(this, MyEventsActivity.class);
        startActivity(intentToMyEvents);
    }
}