package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AirCompanyServiceImplIntegrationTest {

    public static final String VERY_BIG_COMPANY = "Very Big company";
    public static final String BRITISH_AIR_LINES = "British Air Lines";
    private AirCompanyService airCompanyService;
    @Autowired
    private AirCompanyRepository airCompanyRepository;
    private Long airCompanyId;
    private AirCompany airCompany;

    @BeforeEach
    void setUp() {
        airCompanyRepository.deleteAll();
        airCompanyService = new AirCompanyServiceImpl(airCompanyRepository);
        airCompany = airCompanyRepository.save(AirCompany
                .builder()
                .name("France Air Lines")
                .companyType("Big company")
                .foundedAt(LocalDateTime.now())
                .build());
        airCompanyId = airCompany.getId();
    }

    @Test
    void saveOrUpdateSuccessfullyUpdate() {
        final AirCompany resultAirCompany = airCompanyService.saveOrUpdate(AirCompany
                .builder()
                .name(BRITISH_AIR_LINES)
                .id(airCompanyId)
                .companyType(VERY_BIG_COMPANY)
                .foundedAt(LocalDateTime.now())
                .build());

        assertThat(resultAirCompany.getName()).isEqualTo(BRITISH_AIR_LINES);
        assertThat(resultAirCompany.getCompanyType()).isEqualTo(VERY_BIG_COMPANY);
    }

    @Test
    void saveOrUpdateSuccessfullyUpdateThrowsAirCompanyNotFoundExc() {
        assertThrows(AirCompanyNotFoundException.class,
                () -> airCompanyService.saveOrUpdate(AirCompany.builder().id(2342432342L).build()));
    }

}