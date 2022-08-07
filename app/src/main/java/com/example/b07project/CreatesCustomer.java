package com.example.b07project;

import java.util.Map;

public interface CreatesCustomer {
    void onCreateCustomerSuccess(Customer customer);
    void onCreateCustomerError(String errorMessage);
}
