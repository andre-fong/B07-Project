package com.example.b07project;

import java.util.List;
import java.util.Objects;

public class Venue {
    private String name;
    private List<Event> events;

    public Venue(String name, List<Event> events) {
        this.name = name;
        this.events = events;
    }

    public String getName() {
        return name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public boolean addEvent(Event event){
        if(this.events.contains(event)){
            System.out.println("Event already exists");
            return false;
        }
        return this.events.add(event);
    }

    public boolean removeEvent(Event event){
        if(this.events.contains(event)){
            return this.events.remove(event);
        }

        System.out.println("Event does not exist");
        return false;
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
