package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.AirPlane;

public interface AirPlaneService {
    AirPlane changeAirCompany(Long airPlaneId, Long airCompanyId);
    AirPlaneService addNewAndAssignAirCompany(AirPlane airplane, Long AirCompanyId);
    AirPlane addNew(AirPlane airPlane);
}
