package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


class Customer {
    // TODO: Update fields based on DB functions
//    private Map<String, Event> joinedEvents;
//    private Map<String, Event> hostedEvents;
    private Map<String, String> joinedEventKeys;
    private Map<String, String> hostedEventKeys;
    
    private String email;
    private String uid;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        joinedEventKeys = new HashMap<String, String>();
        hostedEventKeys = new HashMap<String, String>();
    }

    public Customer(String email, String uid) {
        this.email = email;
        this.uid = uid;
        joinedEventKeys = new HashMap<String, String>();
        hostedEventKeys = new HashMap<String, String>();
    }

//    @Exclude
//    public Map<String, Event> getJoinedEvents() {
//        return joinedEvents;
//    }
//    @Exclude
//    public Map<String, Event> getHostedEvents() {
//        return hostedEvents;
//    }

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

    public void setJoinedEventKeys(Map<String, String> joinedEventKeys) {
        this.joinedEventKeys = joinedEventKeys;
    }

    public void addJoinedEvent(String eventKey){
        joinedEventKeys.put(eventKey, eventKey);
    }
    public void addHostedEvent(String eventKey){
        hostedEventKeys.put(eventKey, eventKey);
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
