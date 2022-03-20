package com.example.air_companies_management_system.repository;

import com.example.air_companies_management_system.domain.Flight;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface FlightRepository extends CrudRepository<Flight, Long> {
    Optional<Set<Flight>> findAllByAirCompany_NameAndFlightStatus(String companyName, String flightStatus);
    Optional<Set<Flight>> findAllByFlightStatusAndStartedAtGreaterThanEqual(String flightStatus, LocalDateTime dateTime);
}
