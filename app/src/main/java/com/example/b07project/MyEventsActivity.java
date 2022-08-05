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
        Spinner upcomingEventsSpinner = findViewById(R.id.CtrjoinedEvents);

        // Create ArrayList of upcoming events to show
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
        Log.d("victortest", "updateUI entered");
        for (Customer c : CustomerMap.values()) {
            Log.d("megajuice", c.getUid());
        }
        c = CustomerMap.get(auth.getCurrentUser().getUid());
        Log.d("victortest", c.getUid());
        createJoinedEventsSpinner();
    }


    private ArrayList<EventItem> getJoinedList() {
        ArrayList<EventItem> upcomingEventList = new ArrayList<>();
        for (String e: c.joinedEvents.values()) {
            String eventName = e.getName();
            String venueName = e.getVenue().getName();
            upcomingEventList.add(new EventItem(venueName + "-" + eventName));
            Log.d("victortest", eventName + venueName);
        }
        return upcomingEventList;
    }
}