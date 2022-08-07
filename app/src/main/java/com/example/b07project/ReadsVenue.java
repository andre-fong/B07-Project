package com.example.b07project;

public interface ReadsVenue {
    void onVenueReadSuccess(Venue venue);
    void onVenueReadError(String message);
}
