package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
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
        final Optional<AirCompany> optionalAirCompany = airCompanyRepository.findById(airCompanyId);
        if (optionalAirCompany.isEmpty()) {
            log.error("AirCompany with id: " + airCompanyId + " does not exist in DB." +
                    "AirplaneServiceImpl.changeAirCompany() failed");
            throw new AirCompanyNotFoundException("Air company with id:" + airCompanyId + " does not exists.");
        } else {
            final Optional<Airplane> optionalAirPlane = airplaneRepository.findById(airPlaneId);
            if (optionalAirPlane.isPresent()) {
                log.info("Airplane with id: " + airPlaneId + " was retrieved from DB.");
                final Airplane airplane = optionalAirPlane.get();
                log.info("Airplane, id=" + airPlaneId + ". Change Air Company from id="
                        + airplane.getAirCompany().getId() + " to id=" + airCompanyId);
                airplane.setAirCompany(optionalAirCompany.get());
                return airplaneRepository.save(airplane);
            } else {
                log.error("Airplane with id: " + airPlaneId + " does not exist in DB." +
                        "AirplaneServiceImpl.changeAirCompany() failed");
                throw new AirplaneNotFoundException("Airplane with id:" + airCompanyId + " does not exists.");
            }
        }

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
