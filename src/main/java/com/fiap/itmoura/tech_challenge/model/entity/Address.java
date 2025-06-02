package com.fiap.itmoura.tech_challenge.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address{
    @Id
    @GeneratedValue
    private UUID id;

    private String street;
    private Integer number;
    private String city;
    private String complement;
    private String neighborhood;
    private String state;

    @Column(nullable = false)
    private String zipCode;

    private String country;
}
