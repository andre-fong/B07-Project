package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

// import DatabaseFunctions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomerHomepageActivity extends AppCompatActivity {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> upcomingEventList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        // Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        // Create reference to upcoming events spinner
        Spinner upcomingEventsSpinner = findViewById(R.id.ctrUpcomingEventsSpinner);

        // Create ArrayList of upcoming events to show
        initList();

        // Create new EventAdapter to work with spinner
        eventAdapter = new EventAdapter(this, upcomingEventList);
        upcomingEventsSpinner.setAdapter(eventAdapter);

        upcomingEventsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EventItem clickedItem = (EventItem)adapterView.getItemAtPosition(i);
                String venueEventLink = clickedItem.getVenueEventLink();

                // TODO: Uncomment out below once EventActivity class is created

                // Intent intentToEventActivity = new Intent(this, EventActivity.class);
                // intentToEventActivity.putExtra("venueEventLink", venueEventLink);
                // startActivity(intentToEventActivity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
    }

    private void initList() {
        upcomingEventList = new ArrayList<>();

        // TODO: implement InitList() below once DatabaseFunctions are implemented

        // Map<String, Event> eventsMap = new HashMap<String, Event>();
        // DatabaseFunctions.readAllEventsFromDatabase(db, eventsMap);

        // for each event in eventsMap that is UPCOMING,
            // upcomingEventList.add(new EventItem(event.getName()));
    }
}