package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Flight;

import java.util.Optional;
import java.util.Set;

public interface FlightService {
    Optional<Set<Flight>> findFlightsByAirCompanyNameAndByStatus(String companyName, String status);
    Optional<Set<Flight>> findActiveFlightsStartedMoreThanDayAgo();
    Flight addNew(Flight flight);
    Flight changeFlightStatus(String status);
    Optional<Set<Flight>> findCompletedFlightsWithDelay();
}
