package com.example.b07project;

public class VenueItem {
    private Venue venue;

    public VenueItem(Venue venue) {
        this.venue = venue;
    }

    // Returns venue name
    public String getVenueName() {
        return venue.getName();
    }
}
