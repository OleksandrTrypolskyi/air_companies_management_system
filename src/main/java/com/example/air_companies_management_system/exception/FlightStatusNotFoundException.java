package com.example.air_companies_management_system.exception;

public class FlightStatusNotFoundException extends RuntimeException {
    public FlightStatusNotFoundException(String message) {
        super(message);
    }
}
