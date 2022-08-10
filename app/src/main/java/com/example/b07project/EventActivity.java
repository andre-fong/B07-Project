package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class EventActivity extends AppCompatActivity implements ReadsCustomer, JoinsEvent{
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

        this.setTitle("Viewing event: ");

        DatabaseFunctions.readCustomerFromDatabase(db, auth.getCurrentUser().getUid(), this);
    }

    public void joinEvent(View view) {
        // TODO: Add customer to event (update customer fields and event fields)
        //triggers after onClick, update the user's joined events by adding this event
        DatabaseFunctions.joinEvent(db, auth.getCurrentUser().getUid(),eventKey, this);
    }

    @Override
    public void onCustomerReadSuccess(Customer c) {
        Boolean inEvent = false;
        Map<String, String> keyMap = c.getJoinedEventKeys();

        if (keyMap != null) {
            if (keyMap.containsKey(eventKey))
                inEvent = true;
        }

        if (inEvent){ // inside if statement, add arguments that check if already joined
            TextView msg = (TextView) findViewById(R.id.ctrJoined);
            msg.setVisibility(View.VISIBLE);
        }
        else {
            Button button = (Button)findViewById(R.id.ctrJoinEventButton);
            button.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCustomerReadError(String errorMessage) {
        Log.d("error", errorMessage);
    }

    @Override
    public void onJoinEventSuccess(String uid, String eventKey) {
        Toast.makeText(EventActivity.this, "Joined Successfully", Toast.LENGTH_SHORT).show();
        Button button = (Button)findViewById(R.id.ctrJoinEventButton);
        ViewGroup layout = (ViewGroup) button.getParent();
        layout.removeView(button);
        TextView msg = (TextView) findViewById(R.id.ctrJoined) ;
        msg.setVisibility(View.VISIBLE);
    }

    @Override
    public void onJoinEventError(String errorMessage) {
        Log.d("error", errorMessage);
    }
}