package com.example.b07project;

import java.util.Map;

public interface ReadsAllEvents {
    void onAllEventsReadSuccess(Map<String, Event> eventMap);
    void onAllEventsReadError(String errorMessage);
}
