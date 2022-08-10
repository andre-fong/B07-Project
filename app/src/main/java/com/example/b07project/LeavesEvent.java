package com.example.b07project;

public interface LeavesEvent {
    void onLeaveEventSuccess(String uid, String eventKey);
    void onLeaveEventError(String errorMessage);
}