package com.practice.researchopsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class EditCaseManagerDto {

    private String name;
    private String address;
    private String state;
    private String city;
    private String zip;
    private boolean isActive;
}
