package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Airplane;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.exception.AirplaneNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import com.example.air_companies_management_system.repository.AirplaneRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class AirplaneServiceImpl implements AirplaneService {

    private final AirplaneRepository airplaneRepository;
    private final AirCompanyRepository airCompanyRepository;

    public AirplaneServiceImpl(AirplaneRepository airplaneRepository, AirCompanyRepository airCompanyRepository) {
        this.airplaneRepository = airplaneRepository;
        this.airCompanyRepository = airCompanyRepository;
    }

    @Transactional
    @Override
    public Airplane changeAirCompany(Long airPlaneId, Long airCompanyId) {
       return null;

    }

    @Override
    public AirplaneService addNewAndAssignAirCompany(Airplane airplane, Long AirCompanyId) {
        return null;
    }

    @Override
    public Airplane addNew(Airplane airPlane) {
        return null;
    }
}
