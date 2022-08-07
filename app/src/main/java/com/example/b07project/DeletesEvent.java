package com.example.b07project;

public interface DeletesEvent {
    void onDeleteEventSuccess(String eventKey);
    void onDeleteEventError(String errorMessage);
}
