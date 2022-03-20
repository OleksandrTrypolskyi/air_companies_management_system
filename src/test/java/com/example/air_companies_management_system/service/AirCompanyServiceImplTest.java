package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirCompanyServiceImplTest {

    public static final String FRANCE_AIR_LINES = "France Air Lines";
    public static final long ID_1L = 1L;
    private AirCompanyService airCompanyService;
    @Mock
    private AirCompanyRepository airCompanyRepository;
    private AirCompany airCompany;
    private Set<AirCompany> airCompanies;

    @BeforeEach
    void setUp() {
        airCompanyService = new AirCompanyServiceImpl(airCompanyRepository);
        airCompanies = new HashSet<>();
        airCompany = AirCompany
                .builder()
                .id(ID_1L)
                .name(FRANCE_AIR_LINES)
                .companyType("Big company")
                .foundedAt(LocalDateTime.now())
                .build();
        airCompanies.add(airCompany);
        airCompanies.add(AirCompany
                .builder()
                .id(2L)
                .name("British Air Lines")
                .companyType("Big company")
                .foundedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void findAll() {
        when(airCompanyRepository.findAll()).thenReturn(airCompanies);
        final Set<AirCompany> airCompanies = airCompanyService.findAll();
        assertThat(airCompanies).isNotNull();
        assertThat(airCompanies.size()).isEqualTo(2);
        verify(airCompanyRepository, times(1)).findAll();
    }

    @Test
    void findAllThrowsException() {
        when(airCompanyRepository.findAll()).thenReturn(Collections.EMPTY_SET);
        assertThrows(AirCompanyNotFoundException.class, airCompanyService::findAll);
    }

    @Test
    void findById() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.of(airCompany));
        final AirCompany airCompany = airCompanyService.findById(ID_1L);
        assertThat(airCompany).isNotNull();
        assertThat(airCompany.getId()).isEqualTo(ID_1L);
        assertThat(airCompany.getName()).isEqualTo(FRANCE_AIR_LINES);
        verify(airCompanyRepository, times(1)).findById(anyLong());
    }

    @Test
    void findByIdThrowsException() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(AirCompanyNotFoundException.class, () -> airCompanyService.findById(1L));
    }

    @Test
    void saveOrUpdateSuccessfullySave() {
        when(airCompanyRepository.existsById(anyLong())).thenReturn(false);
        when(airCompanyRepository.save(any(AirCompany.class))).thenReturn(airCompany);
        final AirCompany resultAirCompany = airCompanyService
                .saveOrUpdate(AirCompany.builder().id(ID_1L).build());
        assertThat(resultAirCompany).isNotNull();
        assertThat(resultAirCompany.getId()).isEqualTo(1L);
        assertThat(resultAirCompany.getName()).isEqualTo(FRANCE_AIR_LINES);
        verify(airCompanyRepository, times(1)).existsById(anyLong());
        verify(airCompanyRepository, times(1)).save(any(AirCompany.class));
    }

    @Test
    void deleteById() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.of(airCompany));
        airCompanyService.deleteById(ID_1L);
        verify(airCompanyRepository, times(1)).delete(any(AirCompany.class));
    }

    @Test
    void deleteByIdThrowsException() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(AirCompanyNotFoundException.class, () -> airCompanyService.deleteById(1L));
    }
}