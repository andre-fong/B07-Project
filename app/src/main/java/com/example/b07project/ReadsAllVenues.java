package com.example.b07project;

import java.util.Map;

public interface ReadsAllVenues {
    void onAllVenuesReadSuccess(Map<String, Venue> venueMap);
    void onAllVenuesReadError(String errorMessage);
}
