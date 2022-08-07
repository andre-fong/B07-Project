package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyEventsActivity extends AppCompatActivity implements UpdatesUI {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private Map<String, Customer> CustomerMap;
    private Customer c;

    protected void createJoinedEventsSpinner() {
        // Create reference to upcoming events spinner
        Spinner upcomingEventsSpinner = findViewById(R.id.ctrjoinedEvents);

        // Create ArrayList of upcoming events to show and hosted events
        ArrayList<EventItem> upcomingEventList = getJoinedList();
        // Create new EventAdapter to work with spinner
        EventAdapter eventAdapter = new EventAdapter(this, upcomingEventList);
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
    };

    protected void createHostedEventsSpinner() {
        // Create reference to upcoming events spinner
        Spinner hostedEventsSpinner = findViewById(R.id.ctrhostedEvents);

        // Create ArrayList of upcoming events to show and hosted events
        ArrayList<EventItem> hostList = getHostedList();
        // Create new EventAdapter to work with spinner
        EventAdapter eventAdapter = new EventAdapter(this, hostList);
        hostedEventsSpinner.setAdapter(eventAdapter);

        hostedEventsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        CustomerMap = new HashMap<String, Customer>();
        DatabaseFunctions.readCustomerFromDatabase(db, auth.getCurrentUser().getUid(), CustomerMap, this);
    }

    @Override
    public void updateUI() {
        c = CustomerMap.get(auth.getCurrentUser().getUid());
        createJoinedEventsSpinner();
        createHostedEventsSpinner();
    }

    private ArrayList<EventItem> getHostedList() {
        if (c.hostedEvents == null) return new ArrayList<>();

        ArrayList<EventItem> hostedEventList = new ArrayList<>();

        for (String venueEventKey: c.hostedEvents.keySet()) {
            Log.d("victortest", venueEventKey);
            hostedEventList.add(new EventItem(venueEventKey));
        }
        return hostedEventList;
    }

    private ArrayList<EventItem> getJoinedList() {
        if (c.joinedEvents == null) return new ArrayList<>();

        ArrayList<EventItem> upcomingEventList = new ArrayList<>();
        for (String venueEventKey: c.joinedEvents.keySet()) {
            upcomingEventList.add(new EventItem(venueEventKey));
        }
        return upcomingEventList;
    }
}