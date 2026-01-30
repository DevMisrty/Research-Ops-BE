package com.practice.researchopsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DefendantDetails{
    private String name;
    private String lastName;
    private Pronounces pronounces;
    private String email;
    private String contactNumber;
    private String addressBox;
    private LocalDate businessIncorporateDate;
    private String insuranceCarrier;
}