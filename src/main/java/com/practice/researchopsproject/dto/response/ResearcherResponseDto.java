package com.practice.researchopsproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class ResearcherResponseDto {

    private String id;
    private String name;
    private String email;
    private int assignCases;
    private int experience;
    private boolean isActive;
    private String address;
    private String city;
    private String zip;
    private String file;

}
