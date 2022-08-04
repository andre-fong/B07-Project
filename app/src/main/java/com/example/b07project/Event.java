package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Objects;

public class Event {
    private String name;
    private String venue;
    private Customer host;
    private int maxParticipants;
    private List<Customer> participants;

    //for reading from the database
    public Event(){
    }
    //for writing to the database
    public Event(String name, String v, Customer c, int maxParticipants, List<Customer> participants) {
        this.name = name;
        this.venue = v;
        this.maxParticipants = maxParticipants;
        this.host = c;
        this.participants = participants;
    }

    @Exclude
    public String getName() {
        return this.name;
    }

    public String getVenue() {
        return venue;
    }

    public Customer getHost() {
        return host;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public List<Customer> getParticipants() {
        return participants;
    }

    public void addCustomer(Customer c) {
        if (this.participants.size() == this.maxParticipants) return;
        this.participants.add(c);
        //add the particpant to the database
    }

    public void deleteCustomer(Customer c) {
        this.participants.remove(c);
        //remove particpant from the database
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name, event.name) && Objects.equals(venue, event.venue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, venue);
    }

    //For Customers created by reading from database
    public boolean addName(String name){
        if(this.name == null){
            this.name = name;
            return true;
        }
        return false;
    }
}
