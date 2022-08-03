package com.example.b07project;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public abstract class DatabaseFunctions {

    /**
     * Queries database for a single customer and stores results in provided map if they exist. Optionally updates UI when complete.
     * Map is of the form {@code <uid:customer>}
     * @param db instance of FirebaseDatabase
     * @param uid uid of customer
     * @param customerMap map where customer is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after customer is loaded into customerMap
     */
    public static void readCustomerFromDatabase(FirebaseDatabase db, String uid, Map<String, Customer> customerMap, UpdatesUI activity){
        //Reference to specific customer
        DatabaseReference customerRef = db.getReference("/customer/" + uid);
        customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Customer is not in database
                if(!dataSnapshot.exists()){
                    return;
                }
                //Read customer from database and add uid
                Customer customer = dataSnapshot.getValue(Customer.class);
                if(customer.addUid(uid) == false){
                    Log.d("readCustomer", "error reading customer from database");
                    return;
                }
                customerMap.put(uid, customer);
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readCustomer", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for all customers and stores results in provided map. Optionally updates UI when complete.
     * Map is of the form {@code <uid:customer>}
     * @param db instance of FirebaseDatabase
     * @param customersMap map where customers are stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after customers are loaded into customersMap
     */
    public static void readAllCustomersFromDatabase(FirebaseDatabase db, Map<String, Customer> customersMap, UpdatesUI activity){
        //Reference to customers
        DatabaseReference customersRef = db.getReference("/customers/");
        customersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //For all customers
                for(DataSnapshot customerData : dataSnapshot.getChildren()){
                    //Read customer from database and add key
                    Customer customer = customerData.getValue(Customer.class);
                    String uid = customerData.getKey();
                    if(customer.addUid(uid) == false){
                        Log.d("readCustomers", "error reading customers from database");
                        return;
                    }
                    customersMap.put(uid, customerData.getValue(Customer.class));
                }
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readCustomers", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for a single event the customer has joined or is hosting and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param ref reference to query
     * @param eventName name of event in form venueName-eventName
     * @param eventMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after event is loaded into eventMap
     */
    private static void readCustomerEventFromDatabase(DatabaseReference ref, String eventName, Map<String, Event> eventMap, UpdatesUI activity){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Event is not in database
                if(!dataSnapshot.exists()){
                    return;
                }
                //Read event from database and add name
                Event event = dataSnapshot.getValue(Event.class);
                if(event.addName(eventName) == false){
                    Log.d("readCustomerEvent", "error reading customer's event from database");
                    return;
                }
                eventMap.put(eventName, event);
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readCustomerEvent", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for a single event the customer has joined and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param db instance of FirebaseDatabase
     * @param uid uid of customer
     * @param eventName name of event in form venueName-eventName
     * @param eventMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after event is loaded into eventMap
     */
    public static void readCustomerJoinedEventFromDatabase(FirebaseDatabase db, String uid, String eventName, Map<String, Event> eventMap, UpdatesUI activity){
        //Reference to specific customer
        DatabaseReference customerEventRef = db.getReference("/customer/" + uid + "/joinedEvents/" + eventName);
        readCustomerEventFromDatabase(customerEventRef, eventName, eventMap, activity);
    }

    /**
     * Queries database for a single event the customer is hosting and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param db instance of FirebaseDatabase
     * @param uid uid of customer
     * @param eventName name of event
     * @param eventMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after event is loaded into eventMap
     */
    public static void readCustomerHostedEventFromDatabase(FirebaseDatabase db, String uid, String eventName, Map<String, Event> eventMap, UpdatesUI activity){
        //Reference to specific customer
        DatabaseReference customerEventRef = db.getReference("/customer/" + uid + "/hostedEvents/" + eventName);
        readCustomerEventFromDatabase(customerEventRef, eventName, eventMap, activity);
    }

    /**
     * Queries database for all events the customer has joined or is hosting and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param ref reference to query
     * @param eventName name of event in form venueName-eventName
     * @param eventsMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after events are loaded into eventMap
     */
    private static void readAllCustomerEventsFromDatabase(DatabaseReference ref, String eventName, Map<String, Event> eventsMap, UpdatesUI activity){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //For all events
                for(DataSnapshot eventData : dataSnapshot.getChildren()){
                    //Read event from database and add name
                    Event event = eventData.getValue(Event.class);
                    if(event.addName(eventName) == false){
                        Log.d("readCustomerEvents", "error reading customer's events from database");
                        return;
                    }
                    eventsMap.put(eventName, eventData.getValue(Event.class));
                }
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readCustomerEvents", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for all events the customer has joined and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param db instance of FirebaseDatabase
     * @param uid uid of customer
     * @param eventsMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after events are loaded into eventMap
     */
    public static void readAllCustomerJoinedEventsFromDatabase(FirebaseDatabase db, String uid, String eventName, Map<String, Event> eventsMap, UpdatesUI activity){
        //Reference to specific customer
        DatabaseReference customerEventRef = db.getReference("/customer/" + uid + "/joinedEvents");
        readCustomerEventFromDatabase(customerEventRef, eventName, eventsMap, activity);
    }

    /**
     * Queries database for all events the customer is hosting and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param db instance of FirebaseDatabase
     * @param uid uid of customer
     * @param eventsMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after events are loaded into eventMap
     */
    public static void readAllCustomerHostedEventsFromDatabase(FirebaseDatabase db, String uid, String eventName, Map<String, Event> eventsMap, UpdatesUI activity){
        //Reference to specific customer
        DatabaseReference customerEventRef = db.getReference("/customer/" + uid + "/hostedEvents");
        readCustomerEventFromDatabase(customerEventRef, eventName, eventsMap, activity);
    }

    /**
     * Queries database for a single event and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param db instance of FirebaseDatabase
     * @param venueName name of venue
     * @param eventName name of event
     * @param eventMap map where event is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after event is loaded into eventMap
     */
    public static void readEventFromDatabase(FirebaseDatabase db, String venueName, String eventName, Map<String, Event> eventMap, UpdatesUI activity){
        //Reference to specific event
        DatabaseReference eventRef = db.getReference("/venues/" + venueName + "/" + eventName);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Event is not in database
                if(!dataSnapshot.exists()){
                    return;
                }
                //Read event from database and add name
                Event event = dataSnapshot.getValue(Event.class);
                if(event.addName(eventName) == false){
                    Log.d("readEvent", "error reading event from database");
                    return;
                }
                eventMap.put(eventName, event);
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readEvent", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for all events and stores results in provided map. Optionally updates UI when complete.
     * Map is of the form {@code <venueName-eventName:event>}
     * @param db instance of FirebaseDatabase
     * @param eventsMap map where events are stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after events are loaded into eventsMap
     */
    public static void readAllEventsFromDatabase(FirebaseDatabase db, Map<String, Event> eventsMap, UpdatesUI activity){
        //Reference to events
        DatabaseReference eventsRef = db.getReference("/venues/events");
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //For all events
                for(DataSnapshot eventData : dataSnapshot.getChildren()){
                    //Read event from database and add key
                    Event event = eventData.getValue(Event.class);
                    String name = eventData.getKey();
                    if(event.addName(name) == false){
                        Log.d("readEvents", "error reading events from database");
                        return;
                    }
                    eventsMap.put(name, eventData.getValue(Event.class));
                }
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readEvents", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for a single venue and stores results in provided map if it exists. Optionally updates UI when complete.
     * Map is of the form {@code <venueName:venue>}
     * @param db instance of FirebaseDatabase
     * @param venueName name of venue
     * @param venueMap map where venue is stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after venue is loaded into venueMap
     */
    public static void readVenueFromDatabase(FirebaseDatabase db, String venueName, Map<String, Venue> venueMap, UpdatesUI activity){
        //Reference to specific venue
        DatabaseReference eventRef = db.getReference("/venues/" + venueName);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Venue is not in database
                if(!dataSnapshot.exists()){
                    return;
                }
                //Read venue from database and add name
                Venue venue = dataSnapshot.getValue(Venue.class);
                if(venue.addName(venueName) == false){
                    Log.d("readVenue", "error reading venue from database");
                    return;
                }
                venueMap.put(venueName, venue);
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readVenue", "The read failed: " + databaseError.getCode());
            }
        });
    }

    /**
     * Queries database for all venues and stores results in provided map. Optionally updates UI when complete.
     * Map is of the form {@code <venueName:venue>}
     * @param db instance of FirebaseDatabase
     * @param venuesMap map where venues are stored
     * @param activity if non-null, activity's implementation of updateUI method will be called after venues are loaded into venuesMap
     */
    public static void readAllVenuesFromDatabase(FirebaseDatabase db, Map<String, Venue> venuesMap, UpdatesUI activity){
        //Reference to venues
        DatabaseReference venuesRef = db.getReference("/venues");
        venuesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //For all venues
                for(DataSnapshot venueData : dataSnapshot.getChildren()){
                    //Read venue from database and add key
                    Venue venue = venueData.getValue(Venue.class);
                    String name = venueData.getKey();
                    if(venue.addName(name) == false){
                        Log.d("readVenues", "error reading venues from database");
                        return;
                    }
                    venuesMap.put(name, venueData.getValue(Venue.class));
                }
                if(activity != null){
                    activity.updateUI();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("readVenues", "The read failed: " + databaseError.getCode());
            }
        });
    }
}
