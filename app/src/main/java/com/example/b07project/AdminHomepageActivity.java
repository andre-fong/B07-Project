package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.Map;

public class AdminHomepageActivity extends AppCompatActivity implements ReadsAllVenues {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        // TODO: Add working spinner
        Spinner venue_spinner = findViewById(R.id.ctrEventsInVenueSpinner);
    }

    public void goToAddVenueActivity(View view){
        Intent intent = new Intent(this, AddVenueActivity.class);
        startActivity(intent);

    @Override
    public void onAllVenuesReadSuccess(Map<String, Venue> venueMap) {

    }


    @Override
    public void onAllVenuesReadError(String message) {
        Log.d("andre-testing-readallvenueserror", message);
    }
    

    public void addVenue(View view){
        // TODO: Link to AddVenueActivity
    }
}