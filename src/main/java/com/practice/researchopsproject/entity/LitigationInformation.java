package com.practice.researchopsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LitigationInformation{
    private String governingLaw;

    private District jurisdictionDistrict;

    private County jurisdictionCounty;
}
