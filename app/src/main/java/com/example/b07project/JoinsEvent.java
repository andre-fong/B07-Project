package com.example.b07project;

public interface JoinsEvent {
    void onJoinEventSuccess(String uid, String eventKey);
    void onJoinEventError(String errorMessage);
}
