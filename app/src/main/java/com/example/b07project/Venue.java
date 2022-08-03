package com.example.b07project;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;

import com.google.firebase.database.Exclude;

public class Venue {
    @Exclude
    private String name;
    private List<Event> events;

    public Venue(){
    }

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

    //For Venues created by reading from database
    public boolean addName(String name){
        if(this.name == null){
            this.name = name;
            return true;
        }
        return false;
    }
}
