package com.example.b07project;

import android.util.Log;

public class EventItem {
    private String eventItemName;
    private String venueEvent;

    public EventItem(String venueEvent) {
        // venueEvent is the key for an event in database, with the format "venueName-eventName"
        String[] name = venueEvent.split("-");

        if (name.length != 2)
            Log.d("invalidConstructor", "invalid venueEvent String in EventItem");

        eventItemName = name[1] + " @ " + name[0];

        this.venueEvent = venueEvent;
    }

    // Returns Customer readable text with the format "event @ venue"
    public String getEventItemName() {
        return eventItemName;
    }

    // Returns DB usable link with the format "venue-event"
    public String getVenueEventLink() {
        return venueEvent;
    }
}
