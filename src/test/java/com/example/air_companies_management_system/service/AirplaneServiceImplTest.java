package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.domain.Airplane;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.AirplaneNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.AirplaneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirplaneServiceImplTest {

    public static final long ID_2L = 2L;
    private AirplaneService airplaneService;
    @Mock
    private AirplaneRepository airplaneRepository;
    @Mock
    private AirCompanyRepository airCompanyRepository;
    private AirCompany airCompanyIdOne;
    private AirCompany airCompanyIdTwo;
    private Airplane airplane;

    @BeforeEach
    void setUp() {
        airplaneService = new AirplaneServiceImpl(airplaneRepository, airCompanyRepository);
        airCompanyIdOne = AirCompany.builder().id(1L).build();
        airCompanyIdTwo = AirCompany.builder().id(ID_2L).build();
        airplane = Airplane.builder().id(3L).airCompany(airCompanyIdOne).build();
    }

    @Test
    void changeAirCompany() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.of(airCompanyIdTwo));
        //Initially airplane had airCompanyWithIdOne
        when(airplaneRepository.findById(anyLong())).thenReturn(Optional.of(airplane));
        when(airplaneRepository.save(any())).thenReturn(Airplane.builder().id(3L).airCompany(airCompanyIdTwo).build());

        final Airplane resultAirplane = airplaneService.changeAirCompany(3L, ID_2L);

        assertThat(resultAirplane.getAirCompany().getId()).isEqualTo(ID_2L);

        verify(airCompanyRepository, times(1)).findById(anyLong());
        verify(airplaneRepository, times(1)).findById(anyLong());
        verify(airplaneRepository, times(1)).save(any(Airplane.class));
    }

    @Test
    void changeAirCompanyThrowsAirCompanyNotFoundExc() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AirCompanyNotFoundException.class,
                () -> airplaneService.changeAirCompany(3L, ID_2L));

        verify(airCompanyRepository, times(1)).findById(anyLong());
        verifyNoInteractions(airplaneRepository);
    }

    @Test
    void changeAirCompanyThrowsAirplaneNotFoundExc() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.of(airCompanyIdTwo));
        when(airplaneRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AirplaneNotFoundException.class,
                () -> airplaneService.changeAirCompany(3L, ID_2L));

        verify(airCompanyRepository, times(1)).findById(anyLong());
        verify(airplaneRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(airplaneRepository);
    }

    @Test
    void addNewAndAssignAirCompany() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.of(airCompanyIdTwo));
        when(airplaneRepository.save(any(Airplane.class))).thenReturn(airplane);

        final Airplane resultAirplane = airplaneService.addNewAndAssignAirCompany(this.airplane, ID_2L);

        assertThat(resultAirplane).isNotNull();
        verify(airCompanyRepository, times(1)).findById(anyLong());
        verify(airplaneRepository, times(1)).save(any(Airplane.class));

    }

    @Test
    void addNewAndAssignAirCompanyThrowAirCompanyNotFoundExc() {
        when(airCompanyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(AirCompanyNotFoundException.class,
                () -> airplaneService.addNewAndAssignAirCompany(this.airplane, ID_2L));
        verifyNoMoreInteractions(airCompanyRepository);
        verifyNoInteractions(airplaneRepository);
    }

    @Test
    void addNew() {
        when(airplaneRepository.save(any(Airplane.class))).thenReturn(airplane);
        final Airplane resultAirplane = airplaneService.addNew(airplane);

        assertThat(resultAirplane).isNotNull();
        verify(airplaneRepository, times(1)).save(any(Airplane.class));
    }
}