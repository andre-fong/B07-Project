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

public class MyEventsActivity extends AppCompatActivity implements ReadsCustomer, ReadsEvent {
    private FirebaseDatabase db;
    private FirebaseAuth auth;

    // Create ArrayList for joined events and hosted events
    ArrayList<EventItem> joinList;
    ArrayList<EventItem> hostList;

    private Customer c;

    protected void createJoinedEventsSpinner() {
        // Create reference to upcoming events spinner
        Spinner joinedEventsSpinner = findViewById(R.id.ctrjoinedEvents);

        // Create ArrayList of upcoming events to show and hosted events
        updateJoinList();
        // Create new EventAdapter to work with spinner
        EventAdapter eventAdapter = new EventAdapter(this, joinList);
        joinedEventsSpinner.setAdapter(eventAdapter);
    }

    protected void createHostedEventsSpinner() {
        // Create reference to upcoming events spinner
        Spinner hostedEventsSpinner = findViewById(R.id.ctrhostedEvents);

        // Create ArrayList of upcoming events to show and hosted events
        updateHostList();
        // Create new EventAdapter to work with spinner
        EventAdapter eventAdapter = new EventAdapter(this, hostList);
        hostedEventsSpinner.setAdapter(eventAdapter);
    }

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


    private void updateJoinList() {
        Map<String, String> joinMap = c.getHostedEventKeys();
        if (joinMap == null) joinList = new ArrayList<EventItem>();     // may not needed

//        ArrayList<EventItem> joinedEventList = new ArrayList<>();

//        for (String eventKey : joinMap.keySet()) {
//            Log.d("andre-testing-joinedevents", eventKey);
//            DatabaseFunctions.readEventFromDatabase(db, eventKey, this);
//        }
//        return joinedEventList;
    }


    private void updateHostList() {
        Map<String, String> hostMap = c.getJoinedEventKeys();
        if (hostMap == null) hostList = new ArrayList<EventItem>();     // may not needed

//        ArrayList<EventItem> hostedEventList = new ArrayList<>();

        for (String eventKey : hostMap.keySet()) {
            Log.d("andre-testing-hostedevents", eventKey);
            DatabaseFunctions.readEventFromDatabase(db, eventKey, this);
        }
//        return hostedEventList;
    }

    @Override
    public void onCustomerReadSuccess(Customer c) {
        this.c = c;
        // Reset joinList and hostList
        joinList = new ArrayList<EventItem>();
        hostList = new ArrayList<EventItem>();

        // IMPORTANT: Maintain order of creating joined events, then hosted events
        createJoinedEventsSpinner();
        createHostedEventsSpinner();
    }

    @Override
    public void onCustomerReadError(String errorMessage) {
        Log.d("LOG ERROR", errorMessage);
    }

    @Override
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
    }
}
