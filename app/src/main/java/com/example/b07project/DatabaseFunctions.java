package com.example.b07project;

import android.provider.ContactsContract;


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
                callbackSrc.onCheckAdminError(error.getMessage());
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

                callbackSrc.onCustomerReadSuccess(customer);
                return;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callbackSrc.onCustomerReadError(databaseError.getMessage());
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
            public void onCancelled(DatabaseError databaseError) {
                callbackSrc.onEventReadError(databaseError.getMessage());
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
                //Read from database
                Venue venue = venueSnap.getValue(Venue.class);
                //For every event
                for(String eventKey : venue.getEventKeys().keySet()){
                    DatabaseReference eventRef = db.getReference("/events/" + eventKey);
                    eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        //Add Event object corresponding to eventKey to venue
                        @Override
                        public void onDataChange(@NonNull DataSnapshot eventSnap) {
                            venue.addEvent(eventSnap.getValue(Event.class));
                            //Done adding all events
                            if(venue.getEvents().size() == venue.getEventKeys().size()){
                                callbackSrc.onVenueReadSuccess(venue);
                                return;
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            callbackSrc.onVenueReadError(error.getMessage());
                            return;
                        }
                    });
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                callbackSrc.onVenueReadError(error.getMessage());
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
                long size = eventsSnap.getChildrenCount();
                //Read events from database
                for (DataSnapshot eventSnap : eventsSnap.getChildren()) {
                    Event event = eventSnap.getValue(Event.class);
                    //Set eventKey field in event
                    if (!event.addKey(eventSnap.getKey())) {
                        callbackSrc.onAllEventsReadError("must @Exclude getter for eventKey in Event class");
                        return;
                    }
                    //Add event to map
                    eventMap.put(event.getKey(), event);
                    return;
                }
                callbackSrc.onAllEventsReadSuccess(eventMap);
                return;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callbackSrc.onAllEventsReadError(databaseError.getMessage());
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
                //Read venues from database
                for (DataSnapshot venueSnap : venuesSnap.getChildren()) {
                    Venue venue = venueSnap.getValue(Venue.class);
                    //For every event at venue
                    for(String eventKey : venue.getEventKeys().keySet()){
                        DatabaseReference eventRef = db.getReference("/events/" + eventKey);
                        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            //Add Event object corresponding to eventKey to venue
                            @Override
                            public void onDataChange(@NonNull DataSnapshot eventSnap) {
                                venue.addEvent(eventSnap.getValue(Event.class));
                                //Done adding all events to specific venue
                                if(venue.getEvents().size() == venue.getEventKeys().size()){
                                    //Add venue to map
                                    venueMap.put(venue.getName(), venue);
                                    //Done adding all completed venues to venueMap
                                    if(venueMap.size() == venuesSnap.getChildrenCount()){
                                        callbackSrc.onAllVenuesReadSuccess(venueMap);
                                        return;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                callbackSrc.onAllVenuesReadError(error.getMessage());
                                return;
                            }
                        });
                    }
                }
                callbackSrc.onAllVenuesReadSuccess(venueMap);
                return;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callbackSrc.onAllVenuesReadError(databaseError.getMessage());
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
                callbackSrc.onCreateCustomerError(e.getMessage());
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
                                        callbackSrc.onJoinEventError(e.getMessage());
                                        return;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onJoinEventError(e.getMessage());
                                return;
                            }
                        });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callbackSrc.onJoinEventError(error.getMessage());
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
                                        callbackSrc.onLeaveEventError(e.getMessage());
                                        return;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onLeaveEventError(e.getMessage());
                                return;
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callbackSrc.onLeaveEventError(error.getMessage());
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
                                        callbackSrc.onCreateEventError(e.getMessage());
                                        return;
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onCreateEventError(e.getMessage());
                                return;
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbackSrc.onCreateEventError(e.getMessage());
                        return;
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackSrc.onCreateEventError(e.getMessage());
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
                String venueKey = eventSnap.child("venueKey").getValue(String.class);
                eventRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for (DataSnapshot customerInEvent : eventSnap.child("customerKeys").getChildren()) {
                            DatabaseReference customerRef = db.getReference("/customers/" + customerInEvent.getKey());
                            customerRef.child("joinedEventKeys").child(eventKey).removeValue().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    callbackSrc.onDeleteEventError(e.getMessage());
                                    return;
                                }
                            });
                        }
                        db.getReference("/customers/" + hostKey + "/hostedEvents").removeValue().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onDeleteEventError(e.getMessage());
                                return;
                            }
                        });
                        DatabaseReference venueRef = db.getReference("/venues/" + venueKey);
                        venueRef.child(eventKey).removeValue().addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callbackSrc.onDeleteEventError(e.getMessage());
                                return;
                            }
                        });
                        //Events not removed from customers' joined/hosted events or from venue yet
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
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callbackSrc.onDeleteEventError(error.getMessage());
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
        DatabaseReference customersRef = db.getReference("/venues/");
        customersRef.child(venue.getName()).setValue(venue).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                callbackSrc.onCreateVenueSuccess(venue);
                return;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callbackSrc.onCreateVenueError(e.getMessage());
                return;
            }
        });
    }

    /**
     * Removes venue from database and all events associated with it. Calls back to callbackSrc with venueName or error message.
     *
     * @param db          instance of FirebaseDatabase
     * @param venueName    key of venue to delete
     * @param callbackSrc class to callback to after database operation completion
     */
    public static void deleteVenue(FirebaseDatabase db, String venueName, DeletesVenue callbackSrc) {
        DatabaseReference venueRef = db.getReference("/venues/" + venueName);
        venueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot venueSnap) {
                //Delete venue
                venueRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //For every event at the venue
                        for (DataSnapshot eventAtVenue : venueSnap.getChildren()){
                            String hostKey = eventAtVenue.getValue(String.class);
                            //Delete event
                            db.getReference("/events/" + eventAtVenue.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    //For every customer at event
                                    for (DataSnapshot customerInEvent : eventAtVenue.child("customerKeys").getChildren()) {
                                        DatabaseReference customerRef = db.getReference("/customers/" + customerInEvent.getKey());
                                        //Remove event from customer's joined events
                                        customerRef.child("joinedEventKeys").child(eventAtVenue.getKey()).removeValue().addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                callbackSrc.onDeleteVenueError(e.getMessage());
                                                return;
                                            }
                                        });
                                    }
                                    //Remove event from host's hosted events
                                    db.getReference("/customers/" + hostKey + "/hostedEvents").removeValue().addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            callbackSrc.onDeleteVenueError(e.getMessage());
                                            return;
                                        }
                                    });
                                    //Events not removed from customers' joined/hosted events yet
                                }
                            });
                        }
                        //Events have not been removed from database yet.
                        callbackSrc.onDeleteVenueSuccess(venueName);
                        return;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callbackSrc.onDeleteVenueError(e.getMessage());
                        return;
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callbackSrc.onDeleteVenueError(error.getMessage());
                return;
            }
        });
    }

}

