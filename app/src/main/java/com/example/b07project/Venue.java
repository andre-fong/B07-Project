package com.example.b07project;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.firebase.database.Exclude;

public class Venue {
    private String name;
    private Map<String, String> eventKeys;
    private Map<String, Event> events;

    public Venue(){
        eventKeys = new HashMap<String, String>();
        events = new HashMap<String, Event>();
    }

    public Venue(String name) {
        this.name = name;
        eventKeys = new HashMap<String, String>();
        events = new HashMap<String, Event>();
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getEventKeys() {
        return eventKeys;
    }

    @Exclude
    public Map<String, Event> getEvents(){
        return events;
    }

    public boolean addEvent(Event event){
        if(this.eventKeys.containsKey(event.getKey())){
            System.out.println("Event already exists");
            return false;
        }
        eventKeys.put(event.getKey(), event.getHostKey());
        return true;
    }

    public boolean removeEvent(Event event){
        if(this.eventKeys.containsKey(event.getKey())){
            this.eventKeys.remove(event.getKey());
            return true;
        }
        System.out.println("Event does not exist");
        return false;
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
