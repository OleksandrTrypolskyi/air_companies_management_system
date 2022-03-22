package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Airplane;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.AirplaneNotFoundException;
import com.example.air_companies_management_system.service.AirplaneService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AirplaneControllerTest {

    public static final String API_V_1_AIRPLANES = "/api/v1/airplanes";
    public static final String SERIAL_NUMBER = "Serial number";
    public static final String AIRPLANE_ID = "airplaneId";
    public static final String AIR_COMPANY_ID = "airCompanyId";
    private AirplaneController airplaneController;
    @Mock
    private AirplaneService airplaneService;
    private MockMvc mockMvc;
    private AirCompany airCompanyIdOne;
    private AirCompany airCompanyIdTwo;
    private Airplane airplane;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        airplaneController = new AirplaneController(airplaneService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(airplaneController)
                .setControllerAdvice(ExceptionHandlerController.class)
                .build();
        airCompanyIdOne = AirCompany.builder().id(1L).build();
        airCompanyIdTwo = AirCompany.builder().id(2L).build();
        airplane = Airplane.builder().id(3L).airCompany(airCompanyIdOne).build();
    }

    @Test
    void changeAirCompany() throws Exception {
        when(airplaneService.changeAirCompany(anyLong(), anyLong()))
                .thenReturn(airplane);

        mockMvc.perform(patch(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(AIRPLANE_ID, "3")
                        .param(AIR_COMPANY_ID, "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(airplaneService).changeAirCompany(anyLong(), anyLong());
    }

    @Test
    void changeAirCompanyThrowsAirCompanyNotFoundExc() throws Exception {
        when(airplaneService.changeAirCompany(anyLong(), anyLong()))
                .thenThrow(AirCompanyNotFoundException.class);

        mockMvc.perform(patch(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(AIRPLANE_ID, "3")
                        .param(AIR_COMPANY_ID, "2"))
                .andExpect(status().isBadRequest());

        verify(airplaneService).changeAirCompany(anyLong(), anyLong());
    }

    @Test
    void changeAirCompanyThrowsAirplaneNotFoundExc() throws Exception {
        when(airplaneService.changeAirCompany(anyLong(), anyLong()))
                .thenThrow(AirplaneNotFoundException.class);

        mockMvc.perform(patch(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param(AIRPLANE_ID, "3")
                        .param(AIR_COMPANY_ID, "2"))
                .andExpect(status().isBadRequest());

        verify(airplaneService).changeAirCompany(anyLong(), anyLong());
    }

    @Test
    void changeAirCompanyThrowsNumberFormatExc() throws Exception {
        mockMvc.perform(patch(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airPlaneId", "jdk")
                        .param(AIR_COMPANY_ID, "jre"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(airplaneService);
    }

    @Test
    void addNewAndAssignAirCompany() throws Exception {
        when(airplaneService.addNewAndAssignAirCompany(any(Airplane.class), anyLong()))
                .thenReturn(airplane);
        final Airplane newAirplane = Airplane.builder()
                .airCompany(airCompanyIdOne)
                .factorySerialNumber(SERIAL_NUMBER)
                .build();
        mockMvc.perform(post(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAirplane)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.airCompany.id", is(1)));
        verify(airplaneService, times(1))
                .addNewAndAssignAirCompany(any(Airplane.class), anyLong());
        verify(airplaneService, times(0))
                .addNew(any(Airplane.class));
    }

    @Test
    void addNewAndNotAssignAirCompany() throws Exception {
        airplane.setAirCompany(null);
        when(airplaneService.addNew(any(Airplane.class)))
                .thenReturn(airplane);
        final Airplane newAirplane = Airplane.builder()
                .factorySerialNumber(SERIAL_NUMBER)
                .build();
        mockMvc.perform(post(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAirplane)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.airCompany", is(nullValue())));
        verify(airplaneService, times(0))
                .addNewAndAssignAirCompany(any(Airplane.class), anyLong());
        verify(airplaneService, times(1))
                .addNew(any(Airplane.class));
    }

    @Test
    void addNewThrowsAirCompanyNotFoundExc() throws Exception {
        when(airplaneService.addNew(any(Airplane.class)))
                .thenThrow(AirCompanyNotFoundException.class);
        final Airplane newAirplane = Airplane.builder()
                .factorySerialNumber(SERIAL_NUMBER)
                .build();
        final MvcResult mvcResult = mockMvc.perform(post(API_V_1_AIRPLANES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAirplane)))
                .andExpect(status().isBadRequest())
                .andReturn();
        assertThat(mvcResult.getResolvedException())
                .isInstanceOf(AirCompanyNotFoundException.class);
        verify(airplaneService, times(0))
                .addNewAndAssignAirCompany(any(Airplane.class), anyLong());
        verify(airplaneService, times(1))
                .addNew(any(Airplane.class));
    }
}