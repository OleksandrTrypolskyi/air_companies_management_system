package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.Airplane;
import com.example.air_companies_management_system.service.AirplaneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/airplanes")
public class AirplaneController {

    private final AirplaneService airplaneService;

    public AirplaneController(AirplaneService airplaneService) {
        this.airplaneService = airplaneService;
    }

    @PatchMapping
    public Airplane changeAirCompany(@RequestParam Long airPlaneId,
                                     @RequestParam Long airCompanyId) {
        log.info("PATCH request to AirplaneController.changeAirCompany() endpoint. " +
                "With parameters airPlaneId=" + airPlaneId + " airCompanyId=" + airCompanyId);
        return airplaneService.changeAirCompany(airPlaneId, airCompanyId);
    }
}
