package com.example.b07project;

public interface CreatesVenue {
    void onCreateVenueComplete(Venue venue);
    void onCreateVenueError(String errorMessage);
}
