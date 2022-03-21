package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Slf4j
@RequestMapping("/api/v1/flights")
@RestController
public class FlightController {
    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public Set<Flight> getFlightsByCompanyNameAndStatus(@RequestParam String airCompanyName,
                                                        @RequestParam String flightStatus
    ) {
        log.info("GET request to FlightController.getFlightsByCompanyNameAndStatus() endpoint. " +
                "With parameters airCompanyName=" + airCompanyName + " flightStatus=" + flightStatus);
        return flightService.findFlightsByAirCompanyNameAndByStatus(airCompanyName, flightStatus);
    }
}
