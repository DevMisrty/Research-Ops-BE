package com.practice.researchopsproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
public class EditResearcherDto {

    private String name;
    private String address;
    private String state;
    private String city;
    private String zip;
    private String experience;
    private boolean isActive;

}
