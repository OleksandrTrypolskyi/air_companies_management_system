package com.example.air_companies_management_system.domain;

public enum FlightStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    DELAYED("Delayed"),
    PENDING("Pending");

    private String status;

    FlightStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
