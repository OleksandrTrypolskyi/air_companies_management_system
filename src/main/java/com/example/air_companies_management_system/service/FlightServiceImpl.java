package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.FlightNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.FlightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirCompanyRepository airCompanyRepository;

    public FlightServiceImpl(FlightRepository flightRepository, AirCompanyRepository airCompanyRepository) {
        this.flightRepository = flightRepository;
        this.airCompanyRepository = airCompanyRepository;
    }

    @Override
    public Set<Flight> findFlightsByAirCompanyNameAndByStatus(String airCompanyName, String flightStatus) {
        if (!airCompanyRepository.existsAirCompanyByName(airCompanyName)) {
            log.error("AirCompany with name: " + airCompanyName + " does not exist in DB. " +
                    "FlightServiceImpl.findFlightsByAirCompanyNameAndByStatus() failed.");
            throw new AirCompanyNotFoundException("Air company with name:" + airCompanyName + " does not exists.");
        } else {
            FlightStatus status = convertStringToFlightStatus(flightStatus);

            final Optional<Set<Flight>> flightsFromDB = flightRepository
                    .findAllByAirCompany_NameAndFlightStatus(airCompanyName, status);
            if(flightsFromDB.isPresent()) {
                log.info("Flights with Air Company name: " + airCompanyName + " and status: " + flightStatus +
                " were retrieved from DB. " +
                        "FlightServiceImpl.findFlightsByAirCompanyNameAndByStatus() successful.");
                return flightsFromDB.get();
            } else {
                log.error("Flight with Air Company name: " + airCompanyName + " and status: " + flightStatus +
                        " does not exist in DB. " +
                        "FlightServiceImpl.findFlightsByAirCompanyNameAndByStatus() failed.");
                throw new FlightNotFoundException(
                        "Flight with Air Company name: " + airCompanyName + " and status: " + flightStatus +
                                "does not exist");
            }
        }
    }

    @Override
    public Set<Flight> findActiveFlightsStartedMoreThanDayAgo() {
        return null;
    }

    @Override
    public Flight addNew(Flight flight) {
        return null;
    }

    @Override
    public Flight changeFlightStatus(String status) {
        return null;
    }

    @Override
    public Set<Flight> findCompletedFlightsWithDelay() {
        return null;
    }

    private FlightStatus convertStringToFlightStatus(String flightStatus) {
        return FlightStatus.valueOf(flightStatus.toUpperCase());
    }
}
