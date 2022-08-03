package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class Customer {
    public Map<String, Event> joinedEvents;
    public Map<String, Event> hostedEvents;
    private String email;
    @Exclude
    private String uid;

    public Customer() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Customer(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }


//    public Map<String, Event> getJoinedEvents() {
//        return joinedEvents;
//    }
//    public Map<String, Event> getHostedEvents() {
//        return hostedEvents;
//    }
    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
    //    public void joinEvent(String name, Event event){
//        this.joinedEvents.put(name, event);
//    }
//    public void leaveEvent(Event event){
//        joinedEvents.remove(event);
//    }

    //For Customers created by reading from database
    public boolean addUid(String uid){
        if(this.uid == null){
            this.uid = uid;
            return true;
        }
        return false;
    }
}
