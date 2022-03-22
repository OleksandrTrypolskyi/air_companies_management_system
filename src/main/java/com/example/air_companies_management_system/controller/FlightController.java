package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RequestMapping("/api/v1/flights")
@RestController
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/getFlights")
    public Set<Flight> getFlightsByCompanyNameAndStatus(@RequestParam String airCompanyName,
                                                        @RequestParam String flightStatus) {
        log.info("GET request to FlightController.getFlightsByCompanyNameAndStatus() endpoint. " +
                "With parameters airCompanyName=" + airCompanyName + " flightStatus=" + flightStatus);
        return flightService.findFlightsByAirCompanyNameAndByStatus(airCompanyName, flightStatus);
    }

    @GetMapping("/findActiveFlightsStartedMoreThanDayAgo")
    public Set<Flight> findActiveFlightsStartedMoreThanDayAgo() {
        log.info("GET request to FlightController.findActiveFlightsStartedMoreThanDayAgo() endpoint.");
        return flightService.findActiveFlightsStartedMoreThanDayAgo();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Flight addNew(@RequestBody Flight flight) {
        log.info("POST request to FlightController.addNew() endpoint.");
        return flightService.addNew(flight);
    }

    @PutMapping("/changeFlightStatus")
    public Flight changeFlightStatus(@RequestParam Long flightId,
                                     @RequestParam String flightStatus) {
        log.info("GET request to FlightController.changeFlightStatus() endpoint. " +
                "With parameters flightId=" + flightId + " flightStatus=" + flightStatus);
        return flightService.changeFlightStatus(flightId, flightStatus);
    }
}
