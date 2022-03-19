package com.example.air_companies_management_system.repository;

import com.example.air_companies_management_system.domain.Flight;
import org.springframework.data.repository.CrudRepository;

public interface FlightRepository extends CrudRepository<Flight, Long> {
}
