package com.example.air_companies_management_system.domain;

import lombok.*;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString(exclude = {"airplane"})
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "air_company_id")
    private AirCompany airCompany;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airplane_id")
    private Airplane airplane;
    private String departureCountry;
    private String destinationCountry;
    private Float distance;
    private Duration estimatedFlightTime;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime delayStartedAt;
    private LocalDateTime createdAt;

    @Builder
    public Flight(Long id, FlightStatus flightStatus, AirCompany airCompany, Airplane airplane,
                  String departureCountry, String destinationCountry, Float distance,
                  Duration estimatedFlightTime, LocalDateTime startedAt, LocalDateTime endedAt,
                  LocalDateTime delayStartedAt, LocalDateTime createdAt) {
        this.id = id;
        this.flightStatus = flightStatus;
        this.airCompany = airCompany;
        this.airplane = airplane;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
        this.distance = distance;
        this.estimatedFlightTime = estimatedFlightTime;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.delayStartedAt = delayStartedAt;
        this.createdAt = createdAt;
    }
}
