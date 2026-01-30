package com.practice.researchopsproject.utilities;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.EditCaseManagerDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.ResearcherProfile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Mappers {

    public static CaseManagerResponseDto mapCaseManagerToCaseManagerResponseDto(CaseManagerProfile profile){
        return CaseManagerResponseDto.builder()
                .id(profile.getId())
                .name(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .isActive(profile.getUser().isActive())
                .assignCases(profile.getAssignCaseId().size())
                .lastLogin(profile.getUser().getLastLogin())
                .address(profile.getUser().getAddress())
                .city(profile.getUser().getCity())
                .zip(String.valueOf(profile.getUser().getZip()))
                .file(profile.getUser().getFileName())
                .build();
    }

    public static ResearcherResponseDto mapResearcherToResearcherResponseDto(ResearcherProfile profile){
        return ResearcherResponseDto.builder()
                .id(profile.getId())
                .name(profile.getUser().getName())
                .email(profile.getUser().getEmail())
                .assignCases(profile.getAssignCaseIds().size())
                .experience(profile.getExperience())
                .isActive(profile.getUser().isActive())
                .address(profile.getUser().getAddress())
                .city(profile.getUser().getCity())
                .zip(String.valueOf(profile.getUser().getZip()))
                .file(profile.getUser().getFileName())
                .build();
    }

    public static CaseDto mapCaseToCaseDto(Case aCase) {

        return CaseDto.builder()
                // ✅ Guaranteed fields
                .id(aCase.getId())
                .caseId(aCase.getCaseId())
                .caseName(aCase.getCaseName())
                .clientName(aCase.getClientName())
                .practiceArea(aCase.getPracticeArea().toString())
                .currentStage(aCase.getCurrentStage().toString())
                .dateOfFistPublish(aCase.getDateOfFistPublish())
                .descriptionOfWork(aCase.getDescriptionOfWork())
                .createdOn(aCase.getCreatedOn())
                .infringementInformation(aCase.getInfringementInformation())

                // ⚠️ Optional / nullable fields
                .plaintiffPronounce(
                        aCase.getPlaintiffPronounce() != null
                                ? aCase.getPlaintiffPronounce().toString()
                                : null)

                .plaintiffName(aCase.getPlaintiffName())
                .work(aCase.getWork())
                .titleOfWork(aCase.getTitleOfWork())
                .hasRegisteredDocument(aCase.isHasRegisteredDocument())
                .copyrightRegistrationNumber(aCase.getCopyrightRegistrationNumber())
                .copyrightRegistration(aCase.getCopyrightRegistration())
                .dateOfViolation(aCase.getDateOfViolation())
                .linkToInfringement(aCase.getLinkToInfringement())
                .CMIRemoved(aCase.getCMIRemoved())
                .additionalCaseInformation(aCase.getAdditionalCaseInformation())
                .defendantDetails(aCase.getDefendantDetails())
                .litigationInformation(aCase.getLitigationInformation())
                .billingInfo(aCase.getBillingInfo())

                .build();
    }

    public static CaseResponseDto mapCaseToCaseResponseDto(Case aCase) {

        if (aCase == null) {
            return null;
        }

        // ---- Optional: single researcher extraction ----
        ResearcherProfile researcher = null;
        if (aCase.getResearchers() != null && !aCase.getResearchers().isEmpty()) {
            researcher = aCase.getResearchers().iterator().next();
        }

        return CaseResponseDto.builder()

                // =====================
                // GUARANTEED FIELDS
                // =====================
                .id(aCase.getId())
                .caseId(aCase.getCaseId())
                .caseName(aCase.getCaseName())
                .clientName(aCase.getClientName())
                .practiceArea(aCase.getPracticeArea().toString())
                .currentStage(aCase.getCurrentStage().toString())
                .dateOfFistPublish(aCase.getDateOfFistPublish())
                .descriptionOfWork(aCase.getDescriptionOfWork())
                .createdOn(aCase.getCreatedOn())
                .infringementInformation(aCase.getInfringementInformation())

                // =====================
                // OPTIONAL FIELDS
                // =====================
                .plaintiffPronounce(
                        aCase.getPlaintiffPronounce() != null
                                ? aCase.getPlaintiffPronounce().toString()
                                : null
                )
                .plaintiffName(aCase.getPlaintiffName())

                .work(aCase.getWork())
                .titleOfWork(aCase.getTitleOfWork())

                .hasRegisteredDocument(aCase.isHasRegisteredDocument())

                .copyrightRegistrationNumber(aCase.getCopyrightRegistrationNumber())
                .copyrightRegistration(aCase.getCopyrightRegistration())
                .dateOfViolation(aCase.getDateOfViolation())

                .linkToInfringement(aCase.getLinkToInfringement())

                .CMIRemoved(Boolean.TRUE.equals(aCase.getCMIRemoved()))

                // =====================
                // RESEARCHER (OPTIONAL)
                // =====================
                .researcherId(
                        researcher != null ? researcher.getId() : null
                )
                .researcherName(
                        researcher != null ? researcher.getUser().getName() : null
                )

                .researchers(
                        aCase.getResearchers() != null
                                ? aCase.getResearchers()
                                .stream()
                                .map(ResearcherProfile::getId)
                                .toList()
                                : List.of()
                )
                .additionalCaseInformation(aCase.getAdditionalCaseInformation())
                .defendantDetails(aCase.getDefendantDetails())
                .litigationInformation(aCase.getLitigationInformation())
                .billingInfo(aCase.getBillingInfo())

                .build();
    }



}
