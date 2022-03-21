package com.example.air_companies_management_system.repository;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class FlightRepositoryIntegrationTest {

    public static final String BRITISH_AIR_LINES = "British Air Lines";
    public static final String COMPLETED = "Completed";
    public static final String FRANCE_AIR_LINES = "France Air Lines";
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private AirCompanyRepository airCompanyRepository;

    private AirCompany britishAirLines;
    private AirCompany franceAirLines;

    private Set<Flight> flights;

    @BeforeEach
    void setUp() {
        flightRepository.deleteAll();
        airCompanyRepository.deleteAll();
        britishAirLines = AirCompany.builder().name(BRITISH_AIR_LINES).build();
        franceAirLines = AirCompany.builder().name(FRANCE_AIR_LINES).build();
        airCompanyRepository.save(britishAirLines);
        airCompanyRepository.save(franceAirLines);

        flights = new HashSet<>();
        flights.add(Flight.builder().flightStatus(FlightStatus.ACTIVE).airCompany(britishAirLines)
                .startedAt(LocalDateTime.now().minusDays(2)).build());
        flights.add(Flight.builder().flightStatus(FlightStatus.COMPLETED).airCompany(franceAirLines)
                .startedAt(LocalDateTime.now().minusHours(10)).build());
        flights.add(Flight.builder().flightStatus(FlightStatus.ACTIVE).airCompany(franceAirLines)
                .startedAt(LocalDateTime.now().minusHours(10)).build());
        flights.add(Flight.builder().flightStatus(FlightStatus.DELAYED).airCompany(britishAirLines)
                .startedAt(LocalDateTime.now().minusDays(3)).build());
        flights.add(Flight.builder().flightStatus(FlightStatus.ACTIVE).airCompany(britishAirLines)
                .startedAt(LocalDateTime.now().minusDays(3)).build());
        flights.add(Flight.builder().flightStatus(FlightStatus.PENDING).airCompany(franceAirLines).build());
        flights.add(Flight.builder().flightStatus(FlightStatus.COMPLETED).airCompany(britishAirLines).build());
        flightRepository.saveAll(flights);
    }

    @AfterEach
    void tearDown() {
        airCompanyRepository.deleteAll();
        flightRepository.deleteAll();
    }

    private static Stream<Arguments> provideArgumentsForFindAllByAirCompany_NameAndFlightStatus() {
        return Stream.of(
                Arguments.of(BRITISH_AIR_LINES, FlightStatus.COMPLETED),
                Arguments.of(FRANCE_AIR_LINES, FlightStatus.PENDING),
                Arguments.of(BRITISH_AIR_LINES, FlightStatus.DELAYED),
                Arguments.of(BRITISH_AIR_LINES, FlightStatus.ACTIVE)
        );
    }

    @ParameterizedTest
    @MethodSource("provideArgumentsForFindAllByAirCompany_NameAndFlightStatus")
    void findAllByAirCompany_NameAndFlightStatus(String airCompanyName, FlightStatus flightStatus) {
        final Set<Flight> expectedResult = flights.stream()
                .filter(flight -> flight.getAirCompany().getName().equals(airCompanyName)
                        && flight.getFlightStatus().getStatus().equals(flightStatus.getStatus()))
                .collect(Collectors.toSet());

        final Set<Flight> actualResult = flightRepository
                .findAllByAirCompany_NameAndFlightStatus(airCompanyName, flightStatus)
                .get();

        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void findAllByFlightStatusAndStartedAtGreaterThanEqual() {
        final Set<Flight> expectedResult = flights.stream()
                .filter(flight -> flight.getFlightStatus().equals(FlightStatus.ACTIVE)
                        && flight.getStartedAt().isBefore(LocalDateTime.now().minusHours(24)))
                .collect(Collectors.toSet());
        final Set<Flight> actualResult = flightRepository.findAllByFlightStatusAndStartedAtLessThanEqual(FlightStatus.ACTIVE,
                LocalDateTime.now().minusHours(24)).get();

        assertThat(actualResult).isEqualTo(expectedResult);
    }
}