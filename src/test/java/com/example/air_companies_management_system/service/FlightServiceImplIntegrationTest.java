package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.FlightRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FlightServiceImplIntegrationTest {

    public static final String COMPLETED = "Completed";
    public static final String DELAYED = "Delayed";
    public static final String ACTIVE = "Active";
    private FlightService flightService;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirCompanyRepository airCompanyRepository;
    private Flight flight;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();;
        flightService = new FlightServiceImpl(flightRepository, airCompanyRepository);
        flight = Flight.builder().flightStatus(FlightStatus.PENDING).build();
        flight = flightRepository.save(flight);
    }

    @AfterEach
    void tearDown() {
        flightRepository.deleteAll();
    }

    @Test
    void changeFlightStatusToCompleted() {
        Long flightId = flight.getId();
        final Flight resultFlight = flightService.changeFlightStatus(flightId, COMPLETED);
        assertThat(resultFlight.getFlightStatus().getStatus()).isEqualToIgnoringCase(COMPLETED);
        assertThat(resultFlight.getEndedAt()).isNotNull();
    }

    @Test
    void changeFlightStatusToDelayed() {
        Long flightId = flight.getId();
        final Flight resultFlight = flightService.changeFlightStatus(flightId, DELAYED);
        assertThat(resultFlight.getFlightStatus().getStatus()).isEqualToIgnoringCase(DELAYED);
        assertThat(resultFlight.getDelayStartedAt()).isNotNull();
    }

    @Test
    void changeFlightStatusToActive() {
        Long flightId = flight.getId();
        final Flight resultFlight = flightService.changeFlightStatus(flightId, ACTIVE);
        assertThat(resultFlight.getFlightStatus().getStatus()).isEqualToIgnoringCase(ACTIVE);
        assertThat(resultFlight.getStartedAt()).isNotNull();
    }
}