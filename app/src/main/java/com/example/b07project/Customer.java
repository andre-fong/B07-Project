package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Customer {
    private Map<String, Event> joinedEvents;
    private Map<String, Event> hostedEvents;
    private Map<String, String> joinedEventKeys;
    private Map<String, String> hostedEventKeys;
    private String email;
    private String uid;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        joinedEvents = new HashMap<String, Event>();
        hostedEvents = new HashMap<String, Event>();
    }

    public Customer(String email, String uid) {
        this.email = email;
        this.uid = uid;
        joinedEvents = new HashMap<String, Event>();
        hostedEvents = new HashMap<String, Event>();
    }

    @Exclude
    public Map<String, Event> getJoinedEvents() {
        return joinedEvents;
    }
    @Exclude
    public Map<String, Event> getHostedEvents() {
        return hostedEvents;
    }

    public Map<String, String> getJoinedEventKeys() { return joinedEventKeys; }

    public Map<String, String> getHostedEventKeys() {
        return hostedEventKeys;
    }

    public String getEmail() {
        return email;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setJoinedEvents(Map<String, Event> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public void addJoinedEvent(Event event){
        joinedEvents.put(event.getName(), event);
    }
    public void addHostedEvent(Event event){
        hostedEvents.put(event.getName(), event);
    }
    //For Customers created by reading from database
    public boolean addUid(String uid){
        if(this.uid == null){
            this.uid = uid;
            return true;
        }
        return false;
    }
}
