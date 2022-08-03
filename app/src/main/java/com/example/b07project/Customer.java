package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Customer {
//    public Map<String, Event> joinedEvents;
//    public Map<String, Event> hostedEvents;
    public String email;
    @Exclude
    public String uid;

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
//    public void joinEvent(String name, Event event){
//        this.joinedEvents.put(name, event);
//    }
//    public void leaveEvent(Event event){
//        joinedEvents.remove(event);
//    }
}
