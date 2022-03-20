package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.service.AirCompanyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AirCompanyControllerTest {

    public static final long ID_1L = 1L;
    public static final long ID_2L = 2L;
    public static final String API_V_1_AIR_COMPANIES = "/api/v1/airCompanies";
    public static final int ID_1_INT = 1;
    public static final String FRANCE_AIR_LINES = "France Air Lines";
    public static final String BIG_COMPANY = "Big company";
    @Mock
    private AirCompanyService airCompanyService;
    @InjectMocks
    private AirCompanyController airCompanyController;
    private MockMvc mockMvc;
    private AirCompany airCompany;
    private Set<AirCompany> airCompanies;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders
                .standaloneSetup(airCompanyController)
                .setControllerAdvice(ExceptionHandlerController.class)
                .build();
        airCompanies = new LinkedHashSet<>();
        airCompany = AirCompany
                .builder()
                .id(ID_1L)
                .name(FRANCE_AIR_LINES)
                .companyType(BIG_COMPANY)
                .foundedAt(LocalDateTime.now())
                .build();
        airCompanies.add(airCompany);
        airCompanies.add(AirCompany
                .builder()
                .id(ID_2L)
                .name("British Air Lines")
                .companyType(BIG_COMPANY)
                .foundedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void findAll() throws Exception {
        when(airCompanyService.findAll()).thenReturn(airCompanies);
        mockMvc.perform(get(API_V_1_AIR_COMPANIES)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(ID_1_INT)))
                .andExpect(jsonPath("$[1].id", is(2)));
        verify(airCompanyService, times(ID_1_INT)).findAll();
    }

    @Test
    void findAllThrowsAirCompanyNotFound() throws Exception {
        when(airCompanyService.findAll()).thenThrow(AirCompanyNotFoundException.class);
        mockMvc.perform(get(API_V_1_AIR_COMPANIES)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(airCompanyService, times(ID_1_INT)).findAll();
    }

    @Test
    void findByID() throws Exception {
        when(airCompanyService.findById(anyLong())).thenReturn(airCompany);
        mockMvc.perform(get(API_V_1_AIR_COMPANIES + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(ID_1_INT)))
                .andExpect(jsonPath("$.name", is(FRANCE_AIR_LINES)))
                .andExpect(jsonPath("$.companyType", is(BIG_COMPANY)));
        verify(airCompanyService, times(1)).findById(anyLong());
    }

    @Test
    void findByIDThrowsAirCompanyNotFoundExc() throws Exception {
        when(airCompanyService.findById(anyLong())).thenThrow(AirCompanyNotFoundException.class);
        mockMvc.perform(get(API_V_1_AIR_COMPANIES + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(airCompanyService, times(1)).findById(anyLong());
    }

    @Test
    void findByIDThrowsNumberFormatExc() throws Exception {
        mockMvc.perform(get(API_V_1_AIR_COMPANIES + "/sdf")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(airCompanyService);
    }

    @Test
    void save() throws Exception {
        when(airCompanyService.saveOrUpdate(any(AirCompany.class))).thenReturn(airCompany);
        final AirCompany newAirCompany = AirCompany
                .builder()
                .name(FRANCE_AIR_LINES)
                .companyType(BIG_COMPANY)
                .build();
        mockMvc.perform(post(API_V_1_AIR_COMPANIES)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAirCompany)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ID_1_INT)))
                .andExpect(jsonPath("$.name", is(FRANCE_AIR_LINES)))
                .andExpect(jsonPath("$.companyType", is(BIG_COMPANY)));
        verify(airCompanyService, times(1))
                .saveOrUpdate(any(AirCompany.class));
    }

    @Test
    void update() throws Exception {
        when(airCompanyService.saveOrUpdate(any(AirCompany.class))).thenReturn(airCompany);
        final AirCompany airCompanyForUpdate = AirCompany
                .builder()
                .id(1L)
                .name(FRANCE_AIR_LINES)
                .companyType(BIG_COMPANY)
                .build();
        mockMvc.perform(put(API_V_1_AIR_COMPANIES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(airCompanyForUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(ID_1_INT)))
                .andExpect(jsonPath("$.name", is(FRANCE_AIR_LINES)))
                .andExpect(jsonPath("$.companyType", is(BIG_COMPANY)));
        verify(airCompanyService, times(1))
                .saveOrUpdate(any(AirCompany.class));
    }

    @Test
    void delete() throws Exception {
        doNothing().when(airCompanyService).deleteById(anyLong());
        mockMvc.perform(MockMvcRequestBuilders.delete(API_V_1_AIR_COMPANIES + "/1"))
                .andExpect(status().isOk());
        verify(airCompanyService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteThrowsAirCompanyNotFoundExc() throws Exception {
        doThrow(AirCompanyNotFoundException.class).when(airCompanyService).deleteById(anyLong());
        mockMvc.perform(MockMvcRequestBuilders.delete(API_V_1_AIR_COMPANIES + "/1"))
                .andExpect(status().isBadRequest());
        verify(airCompanyService, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteThrowsNumberFormatExc() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(API_V_1_AIR_COMPANIES + "/asdf"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(airCompanyService);
    }
}