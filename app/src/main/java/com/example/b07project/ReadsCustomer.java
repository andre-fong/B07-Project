package com.example.b07project;

import java.util.Map;

public interface ReadsCustomer {
    void onCustomerReadSuccess(Customer c);
    void onCustomerReadError(String errorMessage);
}
