package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.exception.AirCompanyNotFoundException;
import com.example.air_companies_management_system.repository.AirCompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Transactional
@Service
public class AirCompanyServiceImpl implements AirCompanyService {
    private final AirCompanyRepository airCompanyRepository;

    public AirCompanyServiceImpl(AirCompanyRepository airCompanyRepository) {
        this.airCompanyRepository = airCompanyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Set<AirCompany> findAll() {
        final Set<AirCompany> airCompanies = StreamSupport
                .stream(airCompanyRepository.findAll().spliterator(), false)
                .collect(Collectors.toSet());

        if (airCompanies.isEmpty()) {
            log.error("AirCompany entities do not exist in DB. AriCompanyServiceImpl.findAll() failed.");
            throw new AirCompanyNotFoundException("Air companies do not exists.");
        } else {
            log.info("Retrieved from DB Set<AirCompany>, size= " + airCompanies.size() +
                    " AriCompanyServiceImpl.findAll() successful.");
            return airCompanies;
        }
    }

    @Override
    public AirCompany findById(Long id) {
        final Optional<AirCompany> optionalAirCompany = airCompanyRepository.findById(id);
        if (optionalAirCompany.isPresent()) {
            log.info("Retrieved from DB AirCompany with Id= " + id +
                    " AirCompanyServiceImpl.findById() successful.");
        }
        return optionalAirCompany
                .orElseThrow(getAirCompanyNotFoundExceptionSupplier(id));
    }

    @Override
    public AirCompany saveOrUpdate(AirCompany airCompany) {
        if (airCompanyRepository.existsById(airCompany.getId())) {
            log.info("Retrieved from DB AirCompany with Id= " + airCompany.getId() +
                    " AriCompanyServiceImpl.saveOrUpdate() will update AirCompany.");
            return update(airCompany);
        } else {
            log.info("AirCompany with Id= " + airCompany.getId() + " does not exist id DB." +
                    " AriCompanyServiceImpl.saveOrUpdate() will save new AirCompany.");
            return airCompanyRepository.save(airCompany);
        }
    }

    private AirCompany update(AirCompany airCompany) {
        final AirCompany airCompanyFromDB = findById(airCompany.getId());
        airCompanyFromDB.setName(airCompany.getName());
        airCompanyFromDB.setCompanyType(airCompany.getCompanyType());
        airCompanyFromDB.setFoundedAt(airCompany.getFoundedAt());
        return airCompanyRepository.save(airCompanyFromDB);
    }

    @Override
    public void deleteById(Long id) {
        airCompanyRepository.delete(findById(id));
        log.info("AirCompany with Id= " + id +
                " AirCompanyServiceImpl.deleteById() successful.");
    }

    private Supplier<AirCompanyNotFoundException> getAirCompanyNotFoundExceptionSupplier(Long id) {
        return () -> {
            log.error("AirCompany with id: " + id + " does not exist in DB. AriCompanyServiceImpl.findById() failed.");
            throw new AirCompanyNotFoundException("Air company with id:" + id + " does not exists.");
        };
    }
}
