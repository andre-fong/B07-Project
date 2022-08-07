package com.example.b07project;

public interface ReadsEvent {
    void onEventReadSuccess(Event event);
    void onEventReadError(String message);
}
