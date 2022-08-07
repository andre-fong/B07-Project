package com.example.b07project;

public interface CreatesEvent {
    void onCreateEventSuccess(Event event);
    void onCreateEventError(String errorMessage);
}
