package com.practice.researchopsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdditionalCaseInformation{
    private String counselName;

    private Double numberedDemandAmount;
    private Double numberedSettlementAmount;

    private String opposingCounselName;
    private String opposingCounselEmail;
    private String opposingPhoneNumber;

    private Double wordedDemandAmount;
    private Double wordedSettlementAmount;
}
