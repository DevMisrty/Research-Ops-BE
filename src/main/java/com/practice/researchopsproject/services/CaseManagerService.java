package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.EditCaseManagerDto;
import com.practice.researchopsproject.dto.request.RegisterCaseManagerRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.exception.customException.ResourceNotFoundException;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface CaseManagerService {
    void createCaseManager
            (String token, RegisterCaseManagerRequestDto requestDto) throws BadRequestException, TokenExpireException;

    Page<ResearcherResponseDto> getResearchers(int page, int limit);

    Case createCase(@Valid CaseDto requestDto, String email);

    Case editCase(CaseDto caseDto, String email, String id)
            throws BadRequestException, ResourceNotFoundException;

    CaseManagerResponseDto getCaseManagerById(String id);

    CaseDto editCaseByAdmin(CaseDto requestDto, String id) throws CaseNotFoundException;

    CaseManagerResponseDto editCaseManager(EditCaseManagerDto requestDto, String id);

    void editMyProfile
            (EditCaseManagerDto requestDto, HttpServletRequest request) throws BadRequestException;

    String createEmptyCase(String email);

    CaseManagerProfile getCasMangerProfileByEmail(String email);

    void editCaseManagerProfile(EditCaseManagerDto requestDto, String email);
}
