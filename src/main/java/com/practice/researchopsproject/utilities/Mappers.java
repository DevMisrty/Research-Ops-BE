package com.practice.researchopsproject.utilities;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.ResearcherProfile;

import java.util.List;
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
                .build();
    }

    public static CaseDto mapCaseToCaseDto(Case aCase){

        List<String> profiles = aCase.getResearchers()
                .stream()
                .map(profile -> profile.getUser().getEmail())
                .toList();

        return CaseDto.builder()
                .id(aCase.getId())
                .caseId(aCase.getCaseId())
                .clientName(aCase.getClientName())
                .caseName(aCase.getCaseName())
                .practiceArea(aCase.getPracticeArea().toString())
                .currentStage(aCase.getCurrentStage().toString())
                .plaintiffPronounce(aCase.getPlaintiffPronounce().toString())
                .plaintiffName(aCase.getPlaintiffName())
                .work(aCase.getWork())
                .titleOfWork(aCase.getTitleOfWork())
                .descriptionOfWork(aCase.getDescriptionOfWork())
                .hasRegisteredDocument(aCase.isHasRegisteredDocument())
                .copyrightRegistrationNumber(aCase.getCopyrightRegistrationNumber())
                .copyrightRegistration(aCase.getCopyrightRegistration())
                .dateOfFistPublish(aCase.getDateOfFistPublish())
                .dateOfViolation(aCase.getDateOfViolation())
                .infringementInformation(aCase.getInfringementInformation())
                .linkToInfringement(aCase.getLinkToInfringement())
                .CMIRemoved(aCase.getCMIRemoved())
                .researchers(profiles)
                .build();
    }

}
