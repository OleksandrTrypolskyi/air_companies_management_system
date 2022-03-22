package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.FlightNotFoundException;
import com.example.air_companies_management_system.exception.FlightStatusNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
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
        flight = Flight.builder().flightStatus(FlightStatus.COMPLETED).airCompany(britishAirLines)
                .startedAt(LocalDateTime.now().minusDays(2)).build();
    }

    @Test
    void findFlightsByAirCompanyNameAndByStatus() {
        when(airCompanyRepository.existsAirCompanyByName(anyString())).thenReturn(true);
        when(flightRepository.findAllByAirCompany_NameAndFlightStatus(anyString(), any(FlightStatus.class)))
                .thenReturn(Optional.of(Set.of(flight)));
        final Set<Flight> result = flightService
                .findFlightsByAirCompanyNameAndByStatus(BRITISH_AIR_LINES, COMPLETED);
        verify(airCompanyRepository, times(1)).existsAirCompanyByName(anyString());
        verify(flightRepository, times(1))
                .findAllByAirCompany_NameAndFlightStatus(anyString(), any());
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


    @Test
    void findActiveFlightsStartedMoreThanDayAgo() {
        when(flightRepository
                .findAllByFlightStatusAndStartedAtLessThanEqual(any(FlightStatus.class), any(LocalDateTime.class)))
                .thenReturn(Optional.of(Set.of(flight)));
        final Set<Flight> result = flightService.findActiveFlightsStartedMoreThanDayAgo();
        verify(flightRepository, times(1))
                .findAllByFlightStatusAndStartedAtLessThanEqual(any(FlightStatus.class), any(LocalDateTime.class));
        assertThat(result.iterator().hasNext()).isTrue();
        assertThat(result.iterator().next().getStartedAt().isBefore(LocalDateTime.now().minusHours(24)))
                .isTrue();
    }

    @Test
    void findActiveFlightsStartedMoreThanDayAgoThrowsFlightNotFoundExc() {
        when(flightRepository
                .findAllByFlightStatusAndStartedAtLessThanEqual(any(FlightStatus.class), any(LocalDateTime.class)))
                .thenThrow(FlightNotFoundException.class);
        assertThrows(FlightNotFoundException.class, () -> flightService.findActiveFlightsStartedMoreThanDayAgo());
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void addNew() {
        flight.setFlightStatus(FlightStatus.PENDING);
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        final Flight savedFlight = flightService.addNew(flight);
        assertThat(savedFlight.getFlightStatus()).isEqualTo(FlightStatus.PENDING);
        verify(flightRepository, times(1)).save(any(Flight.class));
    }

    @Test
    void changeFlightStatus() {
        Flight flight = Flight.builder().id(3L).flightStatus(FlightStatus.PENDING).build();
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));
        flight.setFlightStatus(FlightStatus.COMPLETED);
        when(flightRepository.save(any(Flight.class))).thenReturn(flight);
        final Flight resultFlight = flightService.changeFlightStatus(3L, COMPLETED);
        assertThat(resultFlight.getFlightStatus().getStatus()).isEqualToIgnoringCase(COMPLETED);
        assertThat(resultFlight.getEndedAt()).isNotNull();
    }

    @Test
    void changeFlightStatusThrowsFlightNotFoundExc() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> flightService.changeFlightStatus(3L, COMPLETED));
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void changeFlightStatusThrowsFlightStatusNotFoundExc() {
        when(flightRepository.findById(anyLong())).thenReturn(Optional.of(flight));
        assertThrows(FlightStatusNotFoundException.class, () -> flightService.changeFlightStatus(3L, "foo"));
        verifyNoMoreInteractions(flightRepository);
    }

    @Test
    void findCompletedFlightsWithDelay() {
        final Set<Flight> flights = initTestDataForFindCompletedFlightsWithDelay();
        when(flightRepository.findAllByFlightStatus(any())).thenReturn(Optional.of(flights));

        final Set<Flight> result = flightService.findCompletedFlightsWithDelay();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void findCompletedFlightsWithDelayThrowsFlightNotFoundExc() {
        when(flightRepository.findAllByFlightStatus(any())).thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> flightService.findCompletedFlightsWithDelay());
    }

    private Set<Flight> initTestDataForFindCompletedFlightsWithDelay() {
        Set<Flight> flights = new HashSet<>();
        flights.add(Flight.builder().flightStatus(FlightStatus.COMPLETED)
                .startedAt(LocalDateTime.of(2022, Month.MARCH, 2, 5, 0))
                .endedAt(LocalDateTime.of(2022, Month.MARCH, 3, 5, 0))
                .estimatedFlightTime(Duration.ofHours(20))
                .build());
        flights.add(Flight.builder().flightStatus(FlightStatus.COMPLETED)
                .startedAt(LocalDateTime.of(2022, Month.MARCH, 2, 5, 0))
                .endedAt(LocalDateTime.of(2022, Month.MARCH, 2, 15, 0))
                .estimatedFlightTime(Duration.ofHours(8))
                .build());
        flights.add(Flight.builder().flightStatus(FlightStatus.COMPLETED)
                .startedAt(LocalDateTime.of(2022, Month.MARCH, 2, 5, 0))
                .endedAt(LocalDateTime.of(2022, Month.MARCH, 2, 10, 0))
                .estimatedFlightTime(Duration.ofHours(8))
                .build());
        return flights;
    }
}