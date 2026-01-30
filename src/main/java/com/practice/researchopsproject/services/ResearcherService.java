package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.EditResearcherDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface ResearcherService {
    void createResearchProfile
            (String token, RegisterResearcherRequestDto requestDto) throws BadRequestException, TokenExpireException;

    Page<CaseResponseDto> getListOfAssignedCases
            (int page, int limit, String sortBy, String dir,String searchBy, String email);

    ResearcherResponseDto getResearcherById(String id);

    void editResearcher(EditResearcherDto requestDto, String id);

    void editMyProfile
            (EditResearcherDto researcherDto, HttpServletRequest request) throws BadRequestException;

    ResearcherResponseDto getResearcherByEmail(String email);
}
