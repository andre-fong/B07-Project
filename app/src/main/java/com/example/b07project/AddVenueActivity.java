package com.example.b07project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddVenueActivity extends AppCompatActivity implements CreatesVenue{
    private FirebaseDatabase db;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_venue);

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        this.setTitle("Adding venue (ADMIN): ");
    }

    public void addVenue(View v){
        // TODO Implement this function
        // Adds a venue to the database (which should update on customer and admin homepages)
        TextView VenueName = (TextView) findViewById(R.id.editTextVenueName);
        String name = VenueName.getText().toString();

        if (name.matches("")){
            Toast.makeText(AddVenueActivity.this, "Cannot add empty Venue Name", Toast.LENGTH_SHORT).show();
        }
        else{
            Venue venue = new Venue(name);
            DatabaseFunctions.createVenue(db, venue, this);
        }
    }

    @Override
    public void onCreateVenueSuccess(Venue venue) {
        Toast.makeText(AddVenueActivity.this, "Venue Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateVenueError(String errorMessage) {
        Log.d("Joe-testing-createvenueerror", errorMessage);
        Toast.makeText(AddVenueActivity.this, "Venue Not Added due to Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }
}