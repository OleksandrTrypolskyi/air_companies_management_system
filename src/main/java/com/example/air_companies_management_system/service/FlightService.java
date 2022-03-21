package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Flight;

import java.util.Optional;
import java.util.Set;

public interface FlightService {
    Set<Flight> findFlightsByAirCompanyNameAndByStatus(String airCompanyName, String flightStatus);
    Set<Flight> findActiveFlightsStartedMoreThanDayAgo();
    Flight addNew(Flight flight);
    Flight changeFlightStatus(String status);
    Set<Flight> findCompletedFlightsWithDelay();
}
