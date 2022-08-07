package com.example.b07project;

public class VenueItem {
    private String venueName;

    public VenueItem(String venueName) {
        // venueName is the key of the venue in the database, with the form "[venue name]"
        this.venueName = venueName;
    }

    // Returns venue name
    public String getVenueName() {
        return venueName;
    }
}
