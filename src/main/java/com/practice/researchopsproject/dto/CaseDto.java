package com.practice.researchopsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseDto {

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

    private boolean hasRegisteredDocument;

    private String copyrightRegistrationNumber;

    private LocalDate copyrightRegistration;
    private LocalDate dateOfFistPublish;
    private LocalDate dateOfViolation;

    private String infringementInformation;
    private String linkToInfringement;

    private boolean CMIRemoved;

    private List<String> researchers;


}
