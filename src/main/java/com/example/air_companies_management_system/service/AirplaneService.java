package com.example.air_companies_management_system.service;

import com.example.air_companies_management_system.domain.Airplane;

public interface AirplaneService {
    Airplane changeAirCompany(Long airPlaneId, Long airCompanyId);
    Airplane addNewAndAssignAirCompany(Airplane airplane, Long airCompanyId);
    Airplane addNew(Airplane airPlane);
}
