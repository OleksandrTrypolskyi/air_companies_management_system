package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AirCompanyServiceImplIntegrationTest {

    public static final String VERY_BIG_COMPANY = "Very Big company";
    public static final String BRITISH_AIR_LINES = "British Air Lines";
    public static final long ID_1L = 1L;
    private AirCompanyService airCompanyService;
    @Autowired
    private AirCompanyRepository airCompanyRepository;

    @BeforeEach
    void setUp() {
        airCompanyRepository.deleteAll();
        airCompanyService = new AirCompanyServiceImpl(airCompanyRepository);
        airCompanyRepository.save(AirCompany
                .builder()
                .id(ID_1L)
                .name("France Air Lines")
                .companyType("Big company")
                .foundedAt(LocalDateTime.now())
                .build());
    }

    @Test
    void saveOrUpdateSuccessfullyUpdate() {
        final AirCompany resultAirCompany = airCompanyService.saveOrUpdate(AirCompany
                .builder()
                .name(BRITISH_AIR_LINES)
                .id(ID_1L)
                .companyType(VERY_BIG_COMPANY)
                .foundedAt(LocalDateTime.now())
                .build());

        assertThat(resultAirCompany.getName()).isEqualTo(BRITISH_AIR_LINES);
        assertThat(resultAirCompany.getCompanyType()).isEqualTo(VERY_BIG_COMPANY);
    }

}