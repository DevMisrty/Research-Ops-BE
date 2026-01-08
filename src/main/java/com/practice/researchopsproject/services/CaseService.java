package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import org.springframework.data.domain.Page;

public interface CaseService {
    Page<CaseDto> getListOfCases(int page, int limit, String sort, String dir, String searchBy);

    Page<CaseDto> getListOfCasesByCreator(int page, int limit, String sortBy, String dir,String searchBy, String email);

    CaseDto getCaseById(String id) throws CaseNotFoundException;
}
