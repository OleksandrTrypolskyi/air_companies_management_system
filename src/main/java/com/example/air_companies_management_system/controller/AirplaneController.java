package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.Airplane;
import com.example.air_companies_management_system.service.AirplaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @PatchMapping
    public Airplane changeAirCompany(@RequestParam Long airplaneId,
                                     @RequestParam Long airCompanyId) {
        log.info("PATCH request to AirplaneController.changeAirCompany() endpoint. " +
                "With parameters airPlaneId=" + airplaneId + " airCompanyId=" + airCompanyId);
        return airplaneService.changeAirCompany(airplaneId, airCompanyId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Airplane addNew(@RequestBody Airplane airplane) {
        Optional<Long> airCompanyId = Optional.empty();

        if(airplane.getAirCompany() != null) {
            airCompanyId = Optional.of(airplane.getAirCompany().getId());
        }
        log.info("POST request to AirplaneController.addNew() endpoint. " +
                "With parameters airPlane" + airplane + " airCompanyId=" + airCompanyId.orElse(null));

        if(airCompanyId.isPresent()) {
            return airplaneService.addNewAndAssignAirCompany(airplane, airCompanyId.get());
        } else {
            return airplaneService.addNew(airplane);
        }
    }
}
