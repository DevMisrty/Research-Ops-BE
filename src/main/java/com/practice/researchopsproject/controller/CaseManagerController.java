package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.InvitationDto;
import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.RegisterCaseManagerRequestDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.exception.customException.ResourceNotFoundException;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/cm")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = {"localhost:3000", "localhost:5173"})
public class CaseManagerController {

    private final CaseManagerService service;
    private final JwtUtilities jwtUtilities;
    private final CaseService caseService;


    // validate the token itself first, if token expired then return error, else return successful response.
    @PostMapping("/register/{token}")
    public ResponseEntity<?> createCaseManager(
            @PathVariable String token,
            @RequestBody RegisterCaseManagerRequestDto requestDto) throws BadRequestException, TokenExpireException {

        service.createCaseManager(token, requestDto);

        log.info("CaseManager profile has been created, Invitation token as {}.", token);
        return ApiResponse.getResponse(HttpStatus.CREATED, Messages.CASEMANAGER_CREATED, null);
    }

    // accepts the email for each Researcher assigned in the case. as a List.
    @PostMapping("/create/case")
    public ResponseEntity<?> createCase(
            @RequestBody CaseDto requestDto,
            HttpServletRequest request){

        String token = request.getHeader("Authorization");
        token = token.substring(7);
        String email = jwtUtilities.getEmailFromToken(token);
        log.info("Email from Token found is, {}", email);

        Case response = service.createCase(requestDto, email);

        log.info("New Case Has been created by CaseManager with email, {}", email);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASE_CREATED, Mappers.mapCaseToCaseDto(response));
    }


    @GetMapping("/get/rs")
    public ResponseEntity<?> getListOfActiveResearcher(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "name") String searchBy
    ){
        if(page< 0)page =0;
        if(limit<= 0)limit =10;

        Page<ResearcherResponseDto> researchers = service.getResearchers(page, limit);

        log.info("List of Active Researcher profile has been fetched successfully, with page {}, limit {}, searchBy {}. ", page , limit, searchBy);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.LIST_OF_RESEARCHER_FETCHED, researchers);
    }


    @PutMapping("/edit/case/{id}")
    public ResponseEntity<?> editCase(
            @RequestBody CaseDto caseDto,
            @PathVariable String id,
            HttpServletRequest request ) throws BadRequestException, ResourceNotFoundException {

        log.info("Edit case started");
        String token = request.getHeader("Authorization");
        token = token.substring(7);

        String email = jwtUtilities.getEmailFromToken(token);

        Case aCase = service.editCase(caseDto, email, id);

        log.info("Case with CaseId {}, has been updated successfully, with the CaseManager email as {}", id, email);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASE_UPDATED, Mappers.mapCaseToCaseDto(aCase) );

    }

    @GetMapping("/cases")
    public ResponseEntity<?> getCaseList(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "caseName") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String dir,
            @RequestParam(required = false) String searchBy,
            HttpServletRequest request
    ){

        if(page< 0)page =0;
        if(limit <= 0) limit = 10;

        String token = request.getHeader("Authorization");
        token = token.substring(7);

        String email = jwtUtilities.getEmailFromToken(token);

        Page<CaseDto> response
                = caseService.getListOfCasesByCreator(page, limit, sortBy, dir, searchBy, email);

        log.info("List of Cases Created By CaseManager {}, has been fetched with page {}, limit {}, sortBy {}, dir {}, searchBy {}.", email, page, limit, sortBy, dir, searchBy);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASES_FETCHED_SUCCESSFULLY, response);
    }

}
