package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Flight;
import com.example.air_companies_management_system.domain.FlightStatus;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.FlightNotFoundException;
import com.example.air_companies_management_system.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FlightControllerTest {

    public static final String BRITISH_AIR_LINES = "British Air Lines";
    public static final String COMPLETED = "COMPLETED";
    public static final String API_V_1_FLIGHTS = "/api/v1/flights";
    public static final String FIND_ACTIVE_FLIGHTS_STARTED_MORE_THAN_DAY_AGO = "/findActiveFlightsStartedMoreThanDayAgo";
    private FlightController flightController;
    @Mock
    private FlightService flightService;
    private MockMvc mockMvc;

    private Flight flight;
    private AirCompany britishAirLines;

    @BeforeEach
    void setUp() {
        britishAirLines = AirCompany.builder().name(BRITISH_AIR_LINES).build();
        flight = Flight.builder().flightStatus(FlightStatus.COMPLETED).airCompany(britishAirLines)
                .startedAt(LocalDateTime.now().minusDays(2)).build();
        flightController = new FlightController(flightService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(flightController)
                .setControllerAdvice(ExceptionHandlerController.class)
                .build();
    }

    @Test
    void getFlightsByCompanyNameAndStatus() throws Exception {
        when(flightService.findFlightsByAirCompanyNameAndByStatus(anyString(), anyString()))
                .thenReturn(Set.of(flight));

        mockMvc.perform(get(API_V_1_FLIGHTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airCompanyName", BRITISH_AIR_LINES)
                        .param("flightStatus", COMPLETED))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].airCompany.name", is(BRITISH_AIR_LINES)))
                .andExpect(jsonPath("$[0].flightStatus", is(COMPLETED)));

        verify(flightService, times(1))
                .findFlightsByAirCompanyNameAndByStatus(anyString(), anyString());

    }

    @Test
    void getFlightsByCompanyNameAndStatusThrowsAirCompanyNotFoundExc() throws Exception {
        when(flightService.findFlightsByAirCompanyNameAndByStatus(anyString(), anyString()))
                .thenThrow(AirCompanyNotFoundException.class);

        final MvcResult mvcResult = mockMvc.perform(get(API_V_1_FLIGHTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airCompanyName", BRITISH_AIR_LINES)
                        .param("flightStatus", COMPLETED))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(AirCompanyNotFoundException.class);

        verify(flightService, times(1))
                .findFlightsByAirCompanyNameAndByStatus(anyString(), anyString());
    }

    @Test
    void getFlightsByCompanyNameAndStatusThrowsFlightNotFoundExc() throws Exception {
        when(flightService.findFlightsByAirCompanyNameAndByStatus(anyString(), anyString()))
                .thenThrow(FlightNotFoundException.class);

        final MvcResult mvcResult = mockMvc.perform(get(API_V_1_FLIGHTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airCompanyName", BRITISH_AIR_LINES)
                        .param("flightStatus", COMPLETED))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(FlightNotFoundException.class);

        verify(flightService, times(1))
                .findFlightsByAirCompanyNameAndByStatus(anyString(), anyString());
    }

    @Test
    void findActiveFlightsStartedMoreThanDayAgo() throws Exception {
        when(flightService.findActiveFlightsStartedMoreThanDayAgo())
                .thenReturn(Set.of(flight));

        mockMvc.perform(get(API_V_1_FLIGHTS + FIND_ACTIVE_FLIGHTS_STARTED_MORE_THAN_DAY_AGO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].airCompany.name", is(BRITISH_AIR_LINES)))
                .andExpect(jsonPath("$[0].flightStatus", is(COMPLETED)));

        verify(flightService, times(1)).findActiveFlightsStartedMoreThanDayAgo();
    }

    @Test
    void findActiveFlightsStartedMoreThanDayAgoThrowsFlightNotFoundExc() throws Exception {
        when(flightService.findActiveFlightsStartedMoreThanDayAgo())
                .thenThrow(FlightNotFoundException.class);

        final MvcResult mvcResult = mockMvc.perform(get(API_V_1_FLIGHTS + FIND_ACTIVE_FLIGHTS_STARTED_MORE_THAN_DAY_AGO)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        assertThat(mvcResult.getResolvedException()).isInstanceOf(FlightNotFoundException.class);

        verify(flightService, times(1)).findActiveFlightsStartedMoreThanDayAgo();
    }
}