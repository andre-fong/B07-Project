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

public class CustomerHomepageActivity extends AppCompatActivity implements ReadsAllEvents {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> upcomingEventList;
    private EventAdapter eventAdapter;
//    private Map<String, Event> eventsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        // Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        upcomingEventList = new ArrayList<EventItem>();
//        eventsMap = new HashMap<String, Event>();
        DatabaseFunctions.readAllEventsFromDatabase(db, this);

        Log.d("andre-testing", "onCreate finished.");
    }


    // Executes when success with reading all events
    // Reads returned eventMap and updates spinner to display events
    public void onAllEventsReadSuccess(Map<String, Event> eventMap) {
        upcomingEventList = new ArrayList<EventItem>();

        for (Event event : eventMap.values()) {
            Log.d("andre-testing-eventname", event.getKey());
        }

        // TODO: Implement time for viewing upcoming events

        for (Event event : eventMap.values()) {
            // if event is upcoming:
            upcomingEventList.add(new EventItem(event));
        }

        // Create new EventAdapter to work with spinner
        eventAdapter = new EventAdapter(this, upcomingEventList);

        // Get spinner obj
        Spinner upcomingEventsSpinner = findViewById(R.id.ctrUpcomingEventsSpinner);

        upcomingEventsSpinner.setAdapter(eventAdapter);
    }


    // Executes when error with reading all events
    public void onAllEventsReadError(String errorMessage) {
        Log.d("andre-testing", errorMessage);
    }


    // Send customer to EventActivity given selected event after clicking button
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

    // Sends customer to MyEventsActivity
    public void myEventsClicked(View v) {
        Intent intentToMyEvents = new Intent(this, MyEventsActivity.class);
        startActivity(intentToMyEvents);
    }
}