package com.example.b07project;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Event {
    private String key;
    private String name;
    private String venueKey;
    private Map<String, Boolean> customerKeys;
    private String hostKey;
    private int maxCustomers;
    private int curCustomers;
    private long startTime;
    private long endTime;

    //for database
    public Event(){

    }

    public Event(String name, String venueKey, String hostKey, int maxCustomers, long startTime, long endTime) {
        this.key = venueKey + "-" + name;
        this.name = name;
        this.venueKey = venueKey;
        this.hostKey = hostKey;
        this.maxCustomers = maxCustomers;
        this.customerKeys = new HashMap<String, Boolean>();
        customerKeys.put(hostKey, true);
        curCustomers = 1;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Exclude
    public String getKey(){
        return this.key;
    }
    public String getName() {
        return this.name;
    }
    public String getVenueKey() {
        return venueKey;
    }
    public String getHostKey() {
        return hostKey;
    }
    public int getMaxCustomers() {
        return maxCustomers;
    }
    public Map<String, Boolean> getCustomerKeys() {
        return customerKeys;
    }

    public void addCustomer(String uid) {
        if (this.customerKeys.size() == this.maxCustomers) return;
        this.customerKeys.put(uid, false);
        //add the customer to the database
    }
    public void deleteCustomer(String uid) {
        this.customerKeys.remove(uid);
        //remove customer from the database
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return key.equals(event.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, venueKey);
    }

    //For Events created by reading from database
    public boolean addKey(String key){
        if(this.key == null){
            this.key = key;
            return true;
        }
        return false;
    }

    public int getCurCustomers() {
        return curCustomers;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
