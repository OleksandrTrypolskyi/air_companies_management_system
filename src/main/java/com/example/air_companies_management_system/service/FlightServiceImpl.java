package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.FlightNotFoundException;
import com.example.air_companies_management_system.exception.FlightStatusNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.FlightRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Transactional
@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirCompanyRepository airCompanyRepository;

    public FlightServiceImpl(FlightRepository flightRepository, AirCompanyRepository airCompanyRepository) {
        this.flightRepository = flightRepository;
        this.airCompanyRepository = airCompanyRepository;
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public Set<Flight> findActiveFlightsStartedMoreThanDayAgo() {
        final Optional<Set<Flight>> optional = flightRepository
                .findAllByFlightStatusAndStartedAtLessThanEqual(FlightStatus.ACTIVE,
                        LocalDateTime.now().minusHours(24));

        if(optional.isPresent() && !optional.get().isEmpty()) {
            log.info("Flights with Active status and started more than 24 hours ago were retrieved from DB. " +
                    "FlightServiceImpl.findActiveFlightsStartedMoreThanDayAgo() successful.");
            return optional.get();
        } else {
            log.error("Flight with Active status and started more than 24 hours ago does not exist in DB. " +
                    "FlightServiceImpl.findActiveFlightsStartedMoreThanDayAgo() failed.");
            throw new FlightNotFoundException(
                    "Flight with Active status and started more than 24 hours ago does not exist in DB.");
        }
    }

    @Override
    public Flight addNew(Flight flight) {
        flight.setCreatedAt(LocalDateTime.now());
        flight.setFlightStatus(FlightStatus.PENDING);
        final Flight savedFlight = flightRepository.save(flight);
        log.info("Flight with id: " + savedFlight.getId() + " and status: "
                + savedFlight.getFlightStatus().getStatus() +
                " was saved to DB. " +
                "FlightServiceImpl.addNew() successful.");
        return savedFlight;
    }

    @Override
    public Flight changeFlightStatus(Long flightId, String status) {
        Flight flight = getFlight(flightId, flightRepository.findById(flightId));
        FlightStatus flightStatus = getFlightStatus(status);

        if(flightStatus.equals(FlightStatus.DELAYED)) flight.setDelayStartedAt(LocalDateTime.now());
        if(flightStatus.equals(FlightStatus.ACTIVE)) flight.setStartedAt(LocalDateTime.now());
        if(flightStatus.equals(FlightStatus.COMPLETED)) flight.setEndedAt(LocalDateTime.now());

        flight.setFlightStatus(flightStatus);
        final Flight savedFlight = flightRepository.save(flight);

        log.info("Flight with id: " + savedFlight.getId() + " and status: "
                + savedFlight.getFlightStatus().getStatus() +
                " was updated and saved to DB with new status. " +
                "FlightServiceImpl.changeFlightStatus() successful.");

        return savedFlight;
    }

    @Override
    public Set<Flight> findCompletedFlightsWithDelay() {
        return null;
    }

    private Flight getFlight(Long flightId, Optional<Flight> optionalFlight) {
        if(optionalFlight.isPresent()) {
            log.info("Flight with id=" + flightId + " was retrieved from DB.");
            return optionalFlight.get();
        } else {
            log.error("Flight with id=" + flightId + " does not exist in DB. " +
                    "FlightServiceImpl.changeFlightStatus()  failed.");
            throw new FlightNotFoundException("Flight with id=" + flightId + " does not exist in DB.");
        }
    }

    private FlightStatus getFlightStatus(String status) {
        final boolean validStatus = Arrays.stream(FlightStatus.values())
                .anyMatch(fS -> fS.getStatus().equalsIgnoreCase(status));
        if(validStatus) {
            return convertStringToFlightStatus(status);
        } else {
            throw new FlightStatusNotFoundException("Flight status " + status +
                    " is not Valid. Please use: 'ACTIVE', 'COMPLETED', 'DELAYED', 'PENDING'");
        }
    }

    private FlightStatus convertStringToFlightStatus(String flightStatus) {
        return FlightStatus.valueOf(flightStatus.toUpperCase());
    }
}
