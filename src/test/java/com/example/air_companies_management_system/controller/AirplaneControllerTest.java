package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Airplane;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.AirplaneNotFoundException;
import com.example.air_companies_management_system.service.AirplaneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AirplaneControllerTest {

    private AirplaneController airplaneController;
    @Mock
    private AirplaneService airplaneService;
    private MockMvc mockMvc;
    private AirCompany airCompanyIdOne;
    private AirCompany airCompanyIdTwo;
    private Airplane airplane;

    @BeforeEach
    void setUp() {
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

        mockMvc.perform(patch("/api/v1/airplanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airPlaneId", "3")
                        .param("airCompanyId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(airplaneService).changeAirCompany(anyLong(), anyLong());
    }

    @Test
    void changeAirCompanyThrowsAirCompanyNotFoundExc() throws Exception {
        when(airplaneService.changeAirCompany(anyLong(), anyLong()))
                .thenThrow(AirCompanyNotFoundException.class);

        mockMvc.perform(patch("/api/v1/airplanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airPlaneId", "3")
                        .param("airCompanyId", "2"))
                .andExpect(status().isBadRequest());

        verify(airplaneService).changeAirCompany(anyLong(), anyLong());
    }

    @Test
    void changeAirCompanyThrowsAirplaneNotFoundExc() throws Exception {
        when(airplaneService.changeAirCompany(anyLong(), anyLong()))
                .thenThrow(AirplaneNotFoundException.class);

        mockMvc.perform(patch("/api/v1/airplanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airPlaneId", "3")
                        .param("airCompanyId", "2"))
                .andExpect(status().isBadRequest());

        verify(airplaneService).changeAirCompany(anyLong(), anyLong());
    }

    @Test
    void changeAirCompanyThrowsNumberFormatExc() throws Exception {
        mockMvc.perform(patch("/api/v1/airplanes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("airPlaneId", "jdk")
                        .param("airCompanyId", "jre"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(airplaneService);
    }
}