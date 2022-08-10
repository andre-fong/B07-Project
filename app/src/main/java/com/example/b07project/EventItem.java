package com.example.b07project;

import android.util.Log;

public class EventItem {
    private Event event;

    public EventItem(Event event) {
        this.event = event;
    }

    // Returns Customer readable text with the format "event @ venue"
    public String getEventItemName() {
        return event.getName() + " @ " + event.getVenueKey();
    }

    // Returns DB usable link with the format "venue-event"
    public String getVenueEventLink() {
        return event.getKey();
    }
}
