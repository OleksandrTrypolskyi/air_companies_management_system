package com.example.air_companies_management_system.repository;

import com.example.air_companies_management_system.domain.Airplane;
import org.springframework.data.repository.CrudRepository;

public interface AirplaneRepository extends CrudRepository<Airplane, Long> {
}
