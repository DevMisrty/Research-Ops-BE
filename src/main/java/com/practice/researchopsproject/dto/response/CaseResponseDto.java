package com.practice.researchopsproject.dto.response;

import com.practice.researchopsproject.entity.AdditionalCaseInformation;
import com.practice.researchopsproject.entity.BillingInfo;
import com.practice.researchopsproject.entity.DefendantDetails;
import com.practice.researchopsproject.entity.LitigationInformation;
import jakarta.annotation.security.DenyAll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Data
public class CaseResponseDto {
    private String id;
    private String caseId;
    private String clientName;
    private String caseName;
    private String practiceArea;
    private String currentStage;

    private String plaintiffPronounce;
    private String plaintiffName;

    private String work;
    private String titleOfWork;
    private String descriptionOfWork;

    private LocalDateTime createdOn;

    private String researcherId;
    private String researcherName;

    private boolean hasRegisteredDocument;

    private String copyrightRegistrationNumber;

    private LocalDate copyrightRegistration;
    private LocalDate dateOfFistPublish;
    private LocalDate dateOfViolation;

    private String infringementInformation;
    private String linkToInfringement;

    private boolean CMIRemoved;

    private List<String> researchers;

    private AdditionalCaseInformation additionalCaseInformation;
    private DefendantDetails defendantDetails;
    private LitigationInformation litigationInformation;
    private BillingInfo billingInfo;
}
