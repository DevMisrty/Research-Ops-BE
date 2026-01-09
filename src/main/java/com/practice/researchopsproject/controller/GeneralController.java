package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.services.ResearcherService;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
public class GeneralController {

    private final CaseService caseService;
    private final ResearcherService researcherService;
    private final CaseManagerService caseManagerService;

    @GetMapping("/case/{id}")
    public ResponseEntity<?> getCaseById(@PathVariable String id) throws CaseNotFoundException {
        CaseDto aCase = caseService.getCaseById(id);

        log.info("Case with ID as {}, details has been fetched.", aCase.getCaseId());
        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASE_FETCHED_SUCCESSFULLY, aCase);
    }

    @GetMapping("/researcher/{id}")
    public ResponseEntity<?> getResearcherById(@PathVariable String id){
        ResearcherResponseDto profile = researcherService.getResearcherById(id);

        log.info("Researcher profile with Id as {}, details has been fetched.", id);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.RESEARCHERPROFILE_FETCHED, profile );
    }

    @GetMapping("/casemanager/{id}")
    public ResponseEntity<?> getCaseManagerById(@PathVariable String id){
        CaseManagerResponseDto profile = caseManagerService.getCaseManagerById(id);

        log.info("CaseManager profile with id {}, details has been fetched.", id);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASEMANAGER_FETCHED, profile);
    }

}
