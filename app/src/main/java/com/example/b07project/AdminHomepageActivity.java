package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Map;

public class AdminHomepageActivity extends AppCompatActivity implements ReadsAllVenues {
    private FirebaseDatabase db;
    private FirebaseAuth auth;
    private ArrayList<VenueItem> venuesList;
    private VenueAdapter venueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        // TODO: Add working spinner
//        Spinner venue_spinner = findViewById(R.id.ctrEventsInVenueSpinner);
        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        venuesList = new ArrayList<VenueItem>();

        DatabaseFunctions.readAllVenuesFromDatabase(db, this);
    }

    public void goToAddVenueActivity(View view) {
        Intent intent = new Intent(this, AddVenueActivity.class);
        startActivity(intent);
    }

    @Override
    public void onAllVenuesReadSuccess(Map<String, Venue> venueMap) {
        for (Venue venue : venueMap.values()) {
            venuesList.add(new VenueItem(venue));
            Log.d("andre-testing", "VENUE read from db: " + venue.getName());
        }

        // Create new VenueAdapter to work with spinner
        venueAdapter = new VenueAdapter(this, venuesList);

        // Get spinner obj
        Spinner venuesSpinner = findViewById(R.id.AdminVenueSpinner);

        // Update venue spinner to display venues
        venuesSpinner.setAdapter(venueAdapter);
    }

    @Override
    public void onAllVenuesReadError(String message) {
        Log.d("andre-testing-readallvenueserror", message);
    }

    // Send customer to VenueActivity given selected VENUE (after clicking go)
    public void goToAdminVenue(View v) {

        // Get spinner obj
        Spinner venuesSpinner = findViewById(R.id.AdminVenueSpinner);

        // Get VenueItem selected
        VenueItem selectedItem = venuesList.get(venuesSpinner.getSelectedItemPosition());
        String venueName = selectedItem.getVenueName();
        Intent intentToAdminVenueActivity = new Intent(this, AdminVenueActivity.class);
        intentToAdminVenueActivity.putExtra("venueName", venueName);
        startActivity(intentToAdminVenueActivity);
    }
}