package com.example.b07project;

import java.util.Objects;

public class Venue {
    public String name;
    // Need to implement Event List<Event> events;

    public Venue(String name){ //, List<Event> events) {
        this.name = name;
        //this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return Objects.equals(name, venue.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
