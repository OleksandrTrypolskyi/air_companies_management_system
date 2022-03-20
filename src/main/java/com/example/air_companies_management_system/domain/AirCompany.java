package com.example.air_companies_management_system.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class AirCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String companyType;
    private LocalDateTime foundedAt;

    @Builder
    public AirCompany(Long id, String name, String companyType, LocalDateTime foundedAt) {
        this.id = id;
        this.name = name;
        this.companyType = companyType;
        this.foundedAt = foundedAt;
    }
}
