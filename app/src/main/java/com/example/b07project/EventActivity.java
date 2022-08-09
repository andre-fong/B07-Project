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

import java.util.Map;

public class EventActivity extends AppCompatActivity implements ReadsCustomer{
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private String eventKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        TextView event_name = (TextView)findViewById(R.id.textView2);
        Intent i = getIntent();
        Bundle b = i.getExtras();
        eventKey = (String) b.get("inter");
        Log.d("zane", "displaying" + eventKey);
        event_name.setText(eventKey); //set the event name to the selected event
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        DatabaseFunctions.readCustomerFromDatabase(db, auth.getCurrentUser().getUid(), this);
    }

    public void joinEvent(View view) {
        // TODO: Add customer to event (update customer fields and event fields)
        //triggers after onClick, update the user's joined events by adding this event
    }

    @Override
    public void onCustomerReadSuccess(Customer c) {
        Boolean bool = false;
        Map<String, String> keyMap = c.getJoinedEventKeys();

        // Check if joined events contains current event
        if (keyMap.containsKey(eventKey)) {
            bool = true;
        }

        Button button = (Button)findViewById(R.id.ctrJoinEventButton);
        ViewGroup layout = (ViewGroup) button.getParent();

        if (bool){ // inside if statement, add arguments that check if already joined
            //make the button disappear when event is already joined by the user
            layout.removeView(button);
            TextView msg = (TextView) findViewById(R.id.ctrJoined) ;
            msg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCustomerReadError(String errorMessage) {
        Log.d("error", errorMessage);
    }
}