package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventActivity extends AppCompatActivity implements UpdatesUI{
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<EventItem> upcomingEventList;
    private EventAdapter eventAdapter;
    private Map<String, Event> eventsMap;
    private Map<String, Customer> CustomerMap;
    private Customer c;
    private String eventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        TextView event_name = (TextView)findViewById(R.id.textView2);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        eventName = (String) b.get("inter");
        Log.d("zane", "displaying" + eventName);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        CustomerMap= new HashMap<String, Customer>();
        DatabaseFunctions.readCustomerFromDatabase(db, auth.getCurrentUser().getUid(), CustomerMap, this);

            /*if (venueEventKey == eventName){
                bool = true;
                break;
            }/

        }*/
        event_name.setText(eventName); //set the event name to the selected event
    }

    public void joinEvent(View view) {
        //triggers after onClick, update the user's joined events by adding this event

    }


    @Override
    public void updateUI() {
        Boolean bool = false;
        c = CustomerMap.get(auth.getCurrentUser().getUid());
        if (c.joinedEvents == null){
            bool = true;
        }
        Button button = (Button)findViewById(R.id.button2);
        ViewGroup layout = (ViewGroup) button.getParent();
        if (bool){ // inside if statement, add arguments that check if already joined
            //make the button disappear when event is already joined by the user
            layout.removeView(button);
        } //this is a null test

        /* //use this when functions for joined events is implemented

        for (String venueEventKey: c.joinedEvents.keySet()) {
            if (eventName == venueEventKey){
                bool = true;
                break;
            }
        }

         */
    }
}