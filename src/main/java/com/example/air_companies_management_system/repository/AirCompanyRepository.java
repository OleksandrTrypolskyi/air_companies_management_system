package com.example.air_companies_management_system.repository;

import com.example.air_companies_management_system.domain.AirCompany;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AirCompanyRepository extends CrudRepository<AirCompany, Long> {
    boolean existsAirCompanyByName(String companyName);
}
