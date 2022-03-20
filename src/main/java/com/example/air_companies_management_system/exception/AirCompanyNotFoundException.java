package com.example.air_companies_management_system.exception;

public class AirCompanyNotFoundException extends RuntimeException {
    public AirCompanyNotFoundException(String message) {
        super(message);
    }
}
