package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.entity.TempCase;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface CaseService {
    Page<CaseResponseDto> getListOfCases
            (int page, int limit, String sort, String dir, String searchBy);

    Page<CaseResponseDto> getListOfCasesByCreator
            (int page, int limit, String sortBy, String dir, String searchBy, String email);

    CaseDto getCaseById(String id) throws CaseNotFoundException;

    TempCase createTempCase(HttpServletRequest request) throws CaseNotFoundException;

    CaseDto saveCase(CaseDto caseDto, String email);
}