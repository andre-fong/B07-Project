package com.example.b07project;

public interface CreatesVenue {
    void onCreateVenueSuccess(Venue venue);
    void onCreateVenueError(String errorMessage);
}
