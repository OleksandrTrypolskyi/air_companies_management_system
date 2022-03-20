package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Airplane;

public interface AirplaneService {
    Airplane changeAirCompany(Long airPlaneId, Long airCompanyId);
    AirplaneService addNewAndAssignAirCompany(Airplane airplane, Long AirCompanyId);
    Airplane addNew(Airplane airPlane);
}
