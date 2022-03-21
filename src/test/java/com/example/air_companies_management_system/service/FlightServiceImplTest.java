package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.FlightNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    public static final String BRITISH_AIR_LINES = "British Air Lines";
    public static final String COMPLETED = "completed";
    private FlightService flightService;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private AirCompanyRepository airCompanyRepository;

    private Flight flight;
    private AirCompany britishAirLines;

    @BeforeEach
    void setUp() {
        flightService = new FlightServiceImpl(flightRepository, airCompanyRepository);
        britishAirLines = AirCompany.builder().name(BRITISH_AIR_LINES).build();
        flight = Flight.builder().flightStatus(FlightStatus.COMPLETED).airCompany(britishAirLines).build();
    }

    @Test
    void findFlightsByAirCompanyNameAndByStatus() {
        when(airCompanyRepository.existsAirCompanyByName(anyString())).thenReturn(true);
        when(flightRepository.findAllByAirCompany_NameAndFlightStatus(anyString(), any(FlightStatus.class)))
                .thenReturn(Optional.of(Set.of(flight)));
        final Set<Flight> result = flightService
                .findFlightsByAirCompanyNameAndByStatus(BRITISH_AIR_LINES, COMPLETED);
        assertThat(result.iterator().hasNext()).isTrue();
        assertThat(result.iterator().next().getAirCompany().getName()).isEqualTo(BRITISH_AIR_LINES);
        assertThat(result.iterator().next().getFlightStatus().getStatus()).isEqualToIgnoringCase(COMPLETED);
    }

    @Test
    void findFlightsByAirCompanyNameAndByStatusThrowsAirCompanyNotFoundExc() {
        when(airCompanyRepository.existsAirCompanyByName(anyString())).thenReturn(false);

        assertThrows(AirCompanyNotFoundException.class, () -> flightService
                .findFlightsByAirCompanyNameAndByStatus(BRITISH_AIR_LINES, COMPLETED));
        verifyNoMoreInteractions(airCompanyRepository);
        verifyNoInteractions(flightRepository);
    }

    @Test
    void findFlightsByAirCompanyNameAndByStatusThrowsFlightNotFoundExc() {
        when(airCompanyRepository.existsAirCompanyByName(anyString())).thenReturn(true);
        when(flightRepository.findAllByAirCompany_NameAndFlightStatus(anyString(), any(FlightStatus.class)))
                .thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> flightService
                .findFlightsByAirCompanyNameAndByStatus(BRITISH_AIR_LINES, COMPLETED));
        verifyNoMoreInteractions(airCompanyRepository);
        verifyNoMoreInteractions(flightRepository);
    }
}