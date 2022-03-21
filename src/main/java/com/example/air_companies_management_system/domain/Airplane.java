package com.example.air_companies_management_system.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = {"airCompany"})
@Entity
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String factorySerialNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "air_company_id")
    private AirCompany airCompany;
    private Integer numberOfFlights;
    private Float flightDistance;
    private Float fuelCapacity;
    private String type;
    private LocalDateTime createdAt;

    @Builder
    public Airplane(Long id, String name, String factorySerialNumber, AirCompany airCompany,
                    Integer numberOfFlights, Float flightDistance, Float fuelCapacity, String type,
                    LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.factorySerialNumber = factorySerialNumber;
        this.airCompany = airCompany;
        this.numberOfFlights = numberOfFlights;
        this.flightDistance = flightDistance;
        this.fuelCapacity = fuelCapacity;
        this.type = type;
        this.createdAt = createdAt;
    }
}
