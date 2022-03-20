package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;

import java.util.Optional;
import java.util.Set;

public interface AirCompanyService {
    Set<AirCompany> findAll();

    AirCompany findById(Long id);

    AirCompany saveOrUpdate(AirCompany airCompany);

    void deleteById(Long id);
}
