package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AdminHomepageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, UpdatesUI{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_homepage);

        // This should be the same spinner as in VenueActivity (updates made to this spinner should transfer onto VenueActivity)

        Spinner venue_spinner = findViewById(R.id.venue_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.events, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venue_spinner.setAdapter(adapter);
        venue_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
    }

    public void addVenue(View view){
        // Should go to AddVenue Activity
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void updateUI() {

    }
}