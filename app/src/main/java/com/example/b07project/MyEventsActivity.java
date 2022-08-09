package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class MyEventsActivity extends AppCompatActivity implements ReadsCustomer {
    private FirebaseDatabase db;
    private FirebaseAuth auth;

    // Create ArrayList for joined events and hosted events
    ArrayList<EventItem> joinList;
    ArrayList<EventItem> hostList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // Get database and authentication instances
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//        CustomerMap = new HashMap<String, Customer>();
//        c = CustomerMap.get(auth.getCurrentUser().getUid());
        DatabaseFunctions.readCustomerFromDatabase(db, auth.getCurrentUser().getUid(), this);
    }


    @Override
    public void onCustomerReadSuccess(Customer c) {
        // Reset joinList and hostList
        joinList = new ArrayList<EventItem>();
        hostList = new ArrayList<EventItem>();
        Spinner joinedEventsSpinner = findViewById(R.id.ctrjoinedEvents);
        Spinner hostedEventsSpinner = findViewById(R.id.ctrhostedEvents);

        Map<String, Event> joinMap = c.getJoinedEvents();

        for (Event event : joinMap.values()) {
            joinList.add(new EventItem(event));
        }

        Map<String, Event> hostMap = c.getHostedEvents();

        for (Event event : hostMap.values()) {
            hostList.add(new EventItem(event));
        }

        EventAdapter eventAdapter = new EventAdapter(this, joinList);
        joinedEventsSpinner.setAdapter(eventAdapter);
        EventAdapter eventAdapter2 = new EventAdapter(this, hostList);
        hostedEventsSpinner.setAdapter(eventAdapter2);

        // Reactivate button if list of events are not empty
        Button goToHosted = (Button) findViewById(R.id.ctrHostedEventsButton);
        Button goToJoined = (Button) findViewById(R.id.ctrJoinedEventsButton);

        if (joinList.size() > 0)
            goToJoined.setEnabled(true);
        if (hostList.size() > 0)
            goToHosted.setEnabled(true);
    }

    @Override
    public void onCustomerReadError(String errorMessage) {
        Log.d("LOG ERROR", errorMessage);
    }

    public void goToJoined(View view) {

        Spinner joinedEventsSpinner = findViewById(R.id.ctrjoinedEvents);

        // Get EventItem selected
        EventItem selectedItem = joinList.get(joinedEventsSpinner.getSelectedItemPosition());
        String joinedEventLink = selectedItem.getVenueEventLink();
        Intent intentToEventActivity = new Intent(this, EventActivity.class);
        intentToEventActivity.putExtra("inter", joinedEventLink);
        startActivity(intentToEventActivity);
    }

    public void goToHosted(View view) {
        Spinner hostedEventsSpinner = findViewById(R.id.ctrhostedEvents);

        // Get EventItem selected
        EventItem selectedItem = hostList.get(hostedEventsSpinner.getSelectedItemPosition());
        String hostedEventLink = selectedItem.getVenueEventLink();
        Intent intentToEventActivity = new Intent(this, EventActivity.class);
        intentToEventActivity.putExtra("inter", hostedEventLink);
        startActivity(intentToEventActivity);
    }

   /* @Override
    public void onEventReadSuccess(Event event) {

        // IMPORTANT: Maintain order of adding to join list before host list
        if (joinList.size() < c.getJoinedEventKeys().size()) {
            addEventItemToList(event, joinList);
            Log.d("andre-testing-addjoinedeventtolist", event.getKey());
        }
        else if (hostList.size() < c.getHostedEventKeys().size()) {
            addEventItemToList(event, hostList);
            Log.d("andre-testing-addhostedeventtolist", event.getKey());
        }
        else {
            Log.d("andre-testing", "FINISHED WRITING EVENT LISTS");
        }
    }


    @Override
    public void onEventReadError(String message) {
        Log.d("LOG ERROR", message);
    }


    // Helper fcn. used in onEventReadSuccess
    // Add EventItem to corresponding ArrayList
    public void addEventItemToList(Event event, ArrayList<EventItem> eventList) {
        eventList.add(new EventItem(event));
    }*/
}
