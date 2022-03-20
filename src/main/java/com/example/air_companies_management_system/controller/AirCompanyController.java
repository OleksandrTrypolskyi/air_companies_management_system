package com.example.air_companies_management_system.controller;

import com.example.air_companies_management_system.domain.AirCompany;
import com.example.air_companies_management_system.service.AirCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/airCompanies")
public class AirCompanyController {
    private final AirCompanyService airCompanyService;

    public AirCompanyController(AirCompanyService airCompanyService) {
        this.airCompanyService = airCompanyService;
    }

    @GetMapping
    public Set<AirCompany> findAll() {
        log.info("GET request to AirCompanyController.findAll() endpoint.");
        return airCompanyService.findAll();
    }

    @GetMapping("/{id}")
    public AirCompany getById(@PathVariable String id) {
        log.info("GET request to AirCompanyController.getById() endpoint." +
                " With parameter id=" + id);
        return airCompanyService.findById(Long.parseLong(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AirCompany save(@RequestBody AirCompany airCompany) {
        log.info("POST request to AirCompanyController.save() endpoint." +
                " With body parameter airCompany=" + airCompany);
        return airCompanyService.saveOrUpdate(airCompany);
    }

    @PutMapping
    public AirCompany update(@RequestBody AirCompany airCompany) {
        log.info("PUT request to AirCompanyController.put() endpoint." +
                " With body parameter airCompany=" + airCompany);
        return airCompanyService.saveOrUpdate(airCompany);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        log.info("DELETE request to AirCompanyController.delete() endpoint." +
                " With parameter id=" + id);
        airCompanyService.deleteById(Long.parseLong(id));
    }


}
