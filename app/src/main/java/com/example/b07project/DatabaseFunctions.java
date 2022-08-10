package com.example.b07project;

import android.provider.ContactsContract;
import android.util.Log;


import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public abstract class DatabaseFunctions {

    /**
     * Checks if currently logged in user is admin in database. Calls back to callbackSrc with Boolean result or error message.
     * @param db          instance of FirebaseDatabase
     * @param auth        instance of FirebaseAuth
     * @param callbackSrc class to callback to after database operation completion
     */

    public static void loggedInAsAdmin(FirebaseDatabase db, FirebaseAuth auth, ChecksAdmin callbackSrc){
        DatabaseReference adminRef = db.getReference("/admins/" + auth.getCurrentUser().getUid());
        adminRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callbackSrc.onCheckAdminSuccess(true);
                return;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if(error.getCode() == -3){
                    callbackSrc.onCheckAdminSuccess(false);
                    return;
                }

                callbackSrc.onCheckAdminError("request at " + adminRef.toString().substring(adminRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Queries database for a single customer. Calls back to callbackSrc with created Customer instance or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param uid         uid of customer
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void readCustomerFromDatabase(FirebaseDatabase db, String uid, ReadsCustomer callbackSrc) {
        //Reference to specific customer
        DatabaseReference customerRef = db.getReference("/customers/" + uid);
        customerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot customerSnap) {
                //Read from database
                Customer customer = customerSnap.getValue(Customer.class);
                //Set uid field in customer
                if (!customer.addUid(uid)) {
                    callbackSrc.onCustomerReadError("must @Exclude getter for Uid in Customer class");
                    return;
                }
                if(customer.getJoinedEventKeys() == null){
                    if(customer.getHostedEventKeys() == null){
                        callbackSrc.onCustomerReadSuccess(customer);
                        return;
                    }
                    for(String hostedEventKey : customer.getHostedEventKeys().keySet()){
                        DatabaseReference hostedEventRef = db.getReference("/events/" + hostedEventKey);
                        hostedEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            //Add hosted Event object corresponding to eventKey to venue
                            @Override
                            public void onDataChange(@NonNull DataSnapshot hostedEventSnap) {
                                Event hostedEvent = hostedEventSnap.getValue(Event.class);
                                //Set key field in event
                                if (!hostedEvent.addKey(hostedEventKey)) {
                                    callbackSrc.onCustomerReadError("must @Exclude getter for eventKey in Event class");
                                    return;
                                }
                                customer.addHostedEvent(hostedEvent);
                                if(customer.getHostedEvents().size() == customer.getHostedEventKeys().size()){
                                    callbackSrc.onCustomerReadSuccess(customer);
                                    return;
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                callbackSrc.onCustomerReadError("request at " + hostedEventRef.toString().substring(hostedEventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                                return;
                            }
                        });
                    }
                }
                for(String joinedEventKey : customer.getJoinedEventKeys().keySet()){
                    DatabaseReference joinedEventRef = db.getReference("/events/" + joinedEventKey);
                    joinedEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        //Add joined Event object corresponding to eventKey to venue
                        @Override
                        public void onDataChange(@NonNull DataSnapshot joinedEventSnap) {
                            Event joinedEvent = joinedEventSnap.getValue(Event.class);
                            //Set key field in event
                            if (!joinedEvent.addKey(joinedEventKey)) {
                                callbackSrc.onCustomerReadError("must @Exclude getter for eventKey in Event class");
                                return;
                            }
                            customer.addJoinedEvent(joinedEvent);
                            if(customer.getJoinedEvents().size() == customer.getJoinedEventKeys().size()){
                                if(customer.getHostedEventKeys() == null){
                                    callbackSrc.onCustomerReadSuccess(customer);
                                    return;
                                }
                                for(String hostedEventKey : customer.getHostedEventKeys().keySet()){
                                    DatabaseReference hostedEventRef = db.getReference("/events/" + hostedEventKey);
                                    hostedEventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        //Add hosted Event object corresponding to eventKey to venue
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot hostedEventSnap) {
                                            Event hostedEvent = hostedEventSnap.getValue(Event.class);
                                            //Set key field in event
                                            if (!hostedEvent.addKey(hostedEventKey)) {
                                                callbackSrc.onCustomerReadError("must @Exclude getter for eventKey in Event class");
                                                return;
                                            }
                                            customer.addHostedEvent(hostedEvent);
                                            if(customer.getHostedEvents().size() == customer.getHostedEventKeys().size()){
                                                callbackSrc.onCustomerReadSuccess(customer);
                                                return;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            callbackSrc.onCustomerReadError("request at " + hostedEventRef.toString().substring(hostedEventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                                            return;
                                        }
                                    });
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            callbackSrc.onCustomerReadError("request at " + joinedEventRef.toString().substring(joinedEventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                            return;
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                callbackSrc.onCustomerReadError("request at " + customerRef.toString().substring(customerRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Queries database for a single event. Calls back to callbackSrc with created Event instance or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param eventKey    key of event (venueName-eventName)
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void readEventFromDatabase(FirebaseDatabase db, String eventKey, ReadsEvent callbackSrc) {
        //Reference to specific event
        DatabaseReference eventRef = db.getReference("/events/" + eventKey);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot eventSnap) {
                //Read from database
                Event event = eventSnap.getValue(Event.class);
                //Set key field in event
                if (!event.addKey(eventKey)) {
                    callbackSrc.onEventReadError("must @Exclude getter for eventKey in Event class");
                    return;
                }
                callbackSrc.onEventReadSuccess(event);
                return;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                callbackSrc.onEventReadError("request at " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Queries database for a single venue. Calls back to callbackSrc with created Venue or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param name        name of venue
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void readVenueFromDatabase(FirebaseDatabase db, String name, ReadsVenue callbackSrc) {
        //Reference to specific venue
        DatabaseReference venueRef = db.getReference("/venues/" + name);
        venueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot venueSnap) {
                Log.d("venue", name);
                //Read from database
                Venue venue = venueSnap.getValue(Venue.class);
                //No events
                if(venue.getEventKeys() == null){
                    callbackSrc.onVenueReadSuccess(venue);
                    return;
                }
                //For every event
                for(String eventKey : venue.getEventKeys().keySet()){
                    DatabaseReference eventRef = db.getReference("/events/" + eventKey);
                    eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        //Add Event object corresponding to eventKey to venue
                        @Override
                        public void onDataChange(@NonNull DataSnapshot eventSnap) {
                            Event event = eventSnap.getValue(Event.class);
                            //Set key field in event
                            if (!event.addKey(eventKey)) {
                                callbackSrc.onVenueReadError("must @Exclude getter for eventKey in Event class");
                                return;
                            }
                            venue.addToEvents(event);
                            if(venue.getEvents().size() == venue.getEventKeys().size()){
                                callbackSrc.onVenueReadSuccess(venue);
                                return;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            callbackSrc.onVenueReadError("request at " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                            return;
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                callbackSrc.onVenueReadError("request at " + venueRef.toString().substring(venueRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Queries database for a all events. Calls back to callbackSrc with map of created Event instances or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void readAllEventsFromDatabase(FirebaseDatabase db, ReadsAllEvents callbackSrc) {
        //Reference to all events
        DatabaseReference eventsRef = db.getReference("/events");
        //Map where events are stored
        Map<String, Event> eventMap = new HashMap<String, Event>();
        eventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot eventsSnap) {
                long numEvents = eventsSnap.getChildrenCount();
                //Read events from database
                for (DataSnapshot eventSnap : eventsSnap.getChildren()) {
                    Event event = eventSnap.getValue(Event.class);
                    //Set eventKey field in event
                    if (!event.addKey(eventSnap.getKey())) {
                        callbackSrc.onAllEventsReadError("must @Exclude getter for eventKey in Event class");
                        return;
                    }
                    eventMap.put(event.getKey(), event);
                    if(eventMap.size() == numEvents){
                        callbackSrc.onAllEventsReadSuccess(eventMap);
                        return;
                    }
                }
                callbackSrc.onAllEventsReadSuccess(eventMap);
                return;
            }
            @Override
            public void onCancelled(DatabaseError error) {
                callbackSrc.onAllEventsReadError("request at " + eventsRef.toString().substring(eventsRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Queries database for a all venues. Calls back to callbackSrc with map of created Venue instances or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void readAllVenuesFromDatabase(FirebaseDatabase db, ReadsAllVenues callbackSrc) {
        //Reference to all venues
        DatabaseReference venuesRef = db.getReference("/venues");
        //Map where venues are stored
        Map<String, Venue> venueMap = new HashMap<String, Venue>();
        venuesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot venuesSnap) {
                long numVenues = venuesSnap.getChildrenCount();
                //Read venues from database
                for (DataSnapshot venueSnap : venuesSnap.getChildren()) {
                    Venue venue = venueSnap.getValue(Venue.class);
                    venueMap.put(venue.getName(), venue);
                    if(venueMap.size() == numVenues){
                        callbackSrc.onAllVenuesReadSuccess(venueMap);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                callbackSrc.onAllVenuesReadError("request at " + venuesRef.toString().substring(venuesRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Creates customer in the database. Calls back to callbackSrc with passed Customer instance or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param customer    customer to create
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void createCustomer(FirebaseDatabase db, Customer customer, CreatesCustomer callbackSrc) {
        DatabaseReference customersRef = db.getReference("/customers/");
        customersRef.child(customer.getUid()).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callbackSrc.onCreateCustomerSuccess(customer);
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackSrc.onCreateCustomerError("failed to create customer with uid " + customer.getUid() + " at path " + customersRef.toString().substring(customersRef.getRoot().toString().length()) + ": " + e.getMessage());
                return;
            }
        });
    }

    /**
     * Adds customer to event's joined customers and adds event to customer's joined events. Calls back to callbackSrc with passed uid and eventKey or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param uid         uid of customer to joining event
     * @param eventKey    key of event that customer is joining (venueName-eventName)
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void joinEvent(FirebaseDatabase db, String uid, String eventKey, JoinsEvent callbackSrc) {
        DatabaseReference eventRef = db.getReference("/events/" + eventKey);
        eventRef.child("customerKeys/" + uid).setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //If completes, operation is valid. Validation done in rules
                eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot eventSnap) {
                        eventRef.child("curCustomers").setValue(eventSnap.child("curCustomers").getValue(Integer.class) + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference customerJoinedEventsRef = db.getReference("/customers/" + uid + "/joinedEventKeys");
                                customerJoinedEventsRef.child(eventKey).setValue(eventSnap.child("hostKey").getValue(String.class)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callbackSrc.onJoinEventSuccess(uid, eventKey);
                                        return;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callbackSrc.onJoinEventError("failed to add event with eventKey " + eventKey + "to customer's joined events with uid " + uid + " at path " + customerJoinedEventsRef.toString().substring(customerJoinedEventsRef.getRoot().toString().length()) + ": " + e.getMessage());
                                        return;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onJoinEventError("failed to add customer with uid " + uid + " to event's customerKeys with eventKey " + eventKey +" at path " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + ": " + e.getMessage());
                                return;
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callbackSrc.onJoinEventError("request at " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                        return;
                    }
                });
            }
        });
    }

    /**
     * Removes customer from event's joined customers and removes event from customer's joined events. Calls back to callbackSrc with passed uid and eventKey or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param uid         uid of customer to joining event
     * @param eventKey    key of event that customer is leaving (venueName-eventName)
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void leaveEvent(FirebaseDatabase db, String uid, String eventKey, LeavesEvent callbackSrc) {
        DatabaseReference eventRef = db.getReference("/events/" + eventKey);
        eventRef.child("customerKeys/" + uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //If successful, operation is valid. Validation done in rules
                eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot eventSnap) {
                        eventRef.child("curCustomers").setValue(eventSnap.child("curCustomers").getValue(Integer.class) - 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                DatabaseReference customerJoinedEventsRef = db.getReference("/customers/" + uid + "/joinedEventKeys");
                                customerJoinedEventsRef.child(eventKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callbackSrc.onLeaveEventSuccess(uid, eventKey);
                                        return;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callbackSrc.onLeaveEventError("failed to remove event with eventKey " + eventKey + "from customer's joined events with uid " + uid + " at path " + customerJoinedEventsRef.toString().substring(customerJoinedEventsRef.getRoot().toString().length()) + ": " + e.getMessage());
                                        return;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onLeaveEventError("failed to remove customer with uid " + uid + " from event's customerKeys with eventKey " + eventKey +" at path " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + ": " + e.getMessage());
                                return;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callbackSrc.onLeaveEventError("request at " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                        return;
                    }
                });
            }
        });
    }

    /**
     * Creates event in the database. Calls back to callbackSrc with passed Event instance or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param event       event to create
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void createEvent(FirebaseDatabase db, Event event, CreatesEvent callbackSrc) {
        DatabaseReference eventRef = db.getReference("/events/" + event.getKey());
        //Create event in database
        eventRef.setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                //If successful, operation is valid. Validation done in rules
                DatabaseReference hostRef = db.getReference("/customers/" + event.getHostKey());
                //Add event to host's joined events
                hostRef.child("joinedEventKeys").child(event.getKey()).setValue(event.getHostKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Add event to host's hosted events
                        hostRef.child("hostedEventKeys").child(event.getKey()).setValue(event.getHostKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Add eventKey to venue's list of events
                                DatabaseReference venueRef = db.getReference("/venues/" + event.getVenueKey() + "/eventKeys");
                                venueRef.child(event.getKey()).setValue(event.getHostKey()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callbackSrc.onCreateEventSuccess(event);
                                        return;
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        callbackSrc.onCreateEventError("failed to add event with eventKey " + event.getKey() + "to venues's events with venueName " + event.getVenueKey() + " at path " + venueRef.toString().substring(venueRef.getRoot().toString().length()) + ": " + e.getMessage());
                                        return;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onCreateEventError("failed to add event with eventKey " + event.getKey() + "to customer's hosted events with uid " + event.getHostKey() + " at path " + hostRef.toString().substring(hostRef.getRoot().toString().length()) + ": " + e.getMessage());
                                return;
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbackSrc.onCreateEventError("failed to add event with eventKey " + event.getKey() + "to customer's joined events with uid " + event.getHostKey() + " at path " + hostRef.toString().substring(hostRef.getRoot().toString().length()) + ": " + e.getMessage());
                        return;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackSrc.onCreateEventError("failed to add event with eventKey " + event.getKey() + "to events at path " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + ": " + e.getMessage());
                return;
            }
        });
    }

    /**
     * Removes event from database and all references to it in customers' joined/hosted events. Calls back to callbackSrc with eventKey or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param eventKey    key of event to delete (venueName-eventName)
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void deleteEvent(FirebaseDatabase db, String eventKey, DeletesEvent callbackSrc) {
        DatabaseReference eventRef = db.getReference("/events/" + eventKey);
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot eventSnap) {
                String hostKey = eventSnap.child("hostKey").getValue(String.class);
                DatabaseReference hostRef = db.getReference("/customers/" + hostKey + "/hostedEventKeys/" + eventKey);
                hostRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        String venueKey = eventSnap.child("venueKey").getValue(String.class);
                        final int[] customerCount = {(int) eventSnap.child("customerKeys").getChildrenCount()};
                        //Remove events from customer's joined events
                        for (DataSnapshot customerInEvent : eventSnap.child("customerKeys").getChildren()) {
                            DatabaseReference customerRef = db.getReference("/customers/" + customerInEvent.getKey());
                            customerRef.child("joinedEventKeys").child(eventKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    customerCount[0]--;
                                    //Done removing from customers' joined events
                                    if(customerCount[0] == 0){
                                        //Remove from venue's list of events
                                        DatabaseReference venueRef = db.getReference("/venues/" + venueKey + "/eventKeys");
                                        venueRef.child(eventKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //Remove from host's hosted events
                                                DatabaseReference customerRef = db.getReference("/customers/" + customerInEvent.getKey());
                                                customerRef.child("hostedEventKeys").child(eventKey).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        //Remove event from database
                                                        eventRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                callbackSrc.onDeleteEventSuccess(eventKey);
                                                                return;
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                callbackSrc.onDeleteEventError(e.getMessage());
                                                                return;
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        callbackSrc.onDeleteEventError("failed to delete event with eventKey " + eventKey + "from events at path " + eventRef.toString().substring(eventRef.getRoot().toString().length()) + ": " + e.getMessage());
                                                        return;
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                callbackSrc.onDeleteEventError("failed to remove event with eventKey " + eventKey + "from customer's hosted events with uid " + hostKey + " at path " + customerRef.toString().substring(customerRef.getRoot().toString().length()) + ": " + e.getMessage());
                                                return;
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callbackSrc.onDeleteEventError(e.getMessage());
                                    return;
                                }
                            });
                        }
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callbackSrc.onDeleteEventError("request at " + eventKey.toString().substring(eventRef.getRoot().toString().length()) + " cancelled. " + error.getMessage());
                return;
            }
        });
    }

    /**
     * Creates venue in the database. Calls back to callbackSrc with passed Venue instance or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param venue       venue to create
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void createVenue(FirebaseDatabase db, Venue venue, CreatesVenue callbackSrc) {
        DatabaseReference venueRef = db.getReference("/venues/");
        venueRef.child(venue.getName()).setValue(venue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callbackSrc.onCreateVenueSuccess(venue);
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackSrc.onCreateVenueError("failed to remove venue with name " + venue.getName() + "to venues at path " + venueRef.toString().substring(venueRef.getRoot().toString().length()) + ": " + e.getMessage());
                return;
            }
        });
    }

//    /**
//     * Removes venue from database and all events associated with it. Calls back to callbackSrc with venueName or error message.
//     *
//     * @param db          instance of FirebaseDatabase
//     * @param venueName    key of venue to delete
//     * @param callbackSrc class to callback to after database operation completion
//     */
//    public static void deleteVenue(FirebaseDatabase db, String venueName, DeletesVenue callbackSrc) {
//        DatabaseReference venueRef = db.getReference("/venues/" + venueName);
//        venueRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot venueSnap) {
//                //Delete venue
//                venueRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        //For every event at the venue
//                        for (DataSnapshot eventAtVenue : venueSnap.getChildren()){
//                            String hostKey = eventAtVenue.getValue(String.class);
//                            //Delete event
//                            db.getReference("/events/" + eventAtVenue.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    //For every customer at event
//                                    for (DataSnapshot customerInEvent : eventAtVenue.child("customerKeys").getChildren()) {
//                                        DatabaseReference customerRef = db.getReference("/customers/" + customerInEvent.getKey());
//                                        //Remove event from customer's joined events
//                                        customerRef.child("joinedEventKeys").child(eventAtVenue.getKey()).removeValue().addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                callbackSrc.onDeleteVenueError(e.getMessage());
//                                                return;
//                                            }
//                                        });
//                                    }
//                                    //Remove event from host's hosted events
//                                    db.getReference("/customers/" + hostKey + "/hostedEvents").removeValue().addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            callbackSrc.onDeleteVenueError(e.getMessage());
//                                            return;
//                                        }
//                                    });
//                                    //Events not removed from customers' joined/hosted events yet
//                                }
//                            });
//                        }
//                        //Events have not been removed from database yet.
//                        callbackSrc.onDeleteVenueSuccess(venueName);
//                        return;
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        callbackSrc.onDeleteVenueError(e.getMessage());
//                        return;
//                    }
//                });
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                callbackSrc.onDeleteVenueError(error.getMessage());
//                return;
//            }
//        });
//    }

}
