package com.practice.researchopsproject.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document
public class Case {

    //ToDo: make sure that the caseid follows rule- CC[current Year][current month][and an 4 digit counter]

    @Id
    private String id;

    private String caseId;

    private String clientName;

    private PracticeArea practiceArea;
    private String caseName;
    private CaseStage currentStage;

    private Pronounces plaintiffPronounce;
    private String plaintiffName;

    private String work;
    private String titleOfWork;
    private String descriptionOfWork;

    private boolean hasRegisteredDocument;

    private String copyrightRegistrationNumber;


    private LocalDate copyrightRegistration;

    @CreatedDate
    private LocalDate dateOfFistPublish;

    private LocalDate dateOfViolation;

    private String infringementInformation;
    private String linkToInfringement;

    private Boolean CMIRemoved;

    @DBRef
    private CaseManagerProfile creator;

    @DBRef
    private List<ResearcherProfile> researchers;

}
