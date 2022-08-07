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

public class CustomerHomepageActivity extends AppCompatActivity implements UpdatesUI {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> upcomingEventList;
    private EventAdapter eventAdapter;
    private Map<String, Event> eventsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_homepage);

        // Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        upcomingEventList = new ArrayList<EventItem>();
        eventsMap = new HashMap<String, Event>();
        DatabaseFunctions.readAllEventsFromDatabase(db, eventsMap, this);

        Log.d("andre-testing", "onCreate finished.");
    }

    private void initList() {

        upcomingEventList = new ArrayList<EventItem>();

        for (String venueEventKey : eventsMap.keySet()) {
            Log.d("andre-testing-eventname", venueEventKey);
        }

        // TODO: Remove test code and reimplement after time functionality is added
        // for each event in eventsMap that is UPCOMING,
            // upcomingEventList.add(new EventItem(event.getName()));

        for (String venueEventKey : eventsMap.keySet()) {
            upcomingEventList.add(new EventItem(venueEventKey));
        }
    }

    // Called when DatabaseFunction is called
    public void updateUI() {
        Log.d("andre-testing", "update ui entered");

        // Create reference to upcoming events spinner
        Spinner upcomingEventsSpinner = findViewById(R.id.ctrUpcomingEventsSpinner);

        // Create ArrayList of upcoming events to show
        initList();

        // Create new EventAdapter to work with spinner
        eventAdapter = new EventAdapter(this, upcomingEventList);
        upcomingEventsSpinner.setAdapter(eventAdapter);

        /*upcomingEventsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                EventItem clickedItem = (EventItem)adapterView.getItemAtPosition(i);
                String venueEventLink = clickedItem.getVenueEventLink();
                Log.d("andre-testing-clicked", venueEventLink + "clicked");

                // TODO: Uncomment out below once EventActivity class is created

                 Intent intentToEventActivity = new Intent(CustomerHomepageActivity.this, EventActivity.class);
                 intentToEventActivity.putExtra("inter", venueEventLink);
                 startActivity(intentToEventActivity);
                 Log.d("zane", venueEventLink + "redirecting");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });*/
    }

    public void myEventsClicked(View v) {
        // TODO: Uncomment function once MyEventsActivity is implemented

        Intent intentToMyEvents = new Intent(this, MyEventsActivity.class);
        startActivity(intentToMyEvents);
    }

    public void goToUpcoming(View v){

        Spinner upcomingEventsSpinner = findViewById(R.id.ctrUpcomingEventsSpinner);

        // Create ArrayList of upcoming events to show
        initList();

        // Create new EventAdapter to work with spinner
        EventItem selectedItem = upcomingEventList.get(upcomingEventsSpinner.getSelectedItemPosition());
        String venueEventLink = selectedItem.getVenueEventLink();
        Intent intentToEventActivity = new Intent(this, EventActivity.class);
        intentToEventActivity.putExtra("inter", venueEventLink);
        startActivity(intentToEventActivity);

    }
}