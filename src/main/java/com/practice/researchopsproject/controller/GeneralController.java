package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.services.*;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/general")
@CrossOrigin(originPatterns = {"localhost:3000", "localhost:5173"})
public class GeneralController {

    private final CaseService caseService;
    private final ResearcherService researcherService;
    private final UsersService usersService;
    private final CaseManagerService caseManagerService;
    private final JwtUtilities jwtUtilities;
    private final InvitationService invitationService;

    @GetMapping("/case/{id}")
    public ResponseEntity<?> getCaseById(@PathVariable String id) throws CaseNotFoundException {
        CaseDto aCase = caseService.getCaseById(id);

        log.info("Case.java with ID as {}, details has been fetched.", aCase.getCaseId());
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

    @GetMapping("/getString")
    public ResponseEntity<?> getStringDetails(){
        log.info("GetString called. ");
        return ApiResponse.getResponse(HttpStatus.OK, Messages.LIST_OF_RESEARCHER_FETCHED, "List of Admin User fetched.");
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<?> getToken( @PathVariable String token ) throws BadRequestException {
        log.info("Invitation token is Fetched for id {}", token);
        Invitation invitation = invitationService.getInvitationFromToken(token);
        return ApiResponse.getResponse(HttpStatus.OK, Messages.INVITATION_TOKEN_FETCHED, invitation);
    }

    @PostMapping("/change/password")
    public ResponseEntity<?> changePassword(
            HttpServletRequest request,
            @RequestBody RegisterResearcherRequestDto requestDto) throws BadRequestException {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtilities.getEmailFromToken(token);

        if(!requestDto.getPassword().equals(requestDto.getConfirmPassword())){
            throw new BadRequestException(Messages.PASSWORD_DOESNT_MATCH);
        }

        usersService.changePassword(email, requestDto.getPassword());

        return ApiResponse.getResponse(HttpStatus.OK, Messages.PASSWORD_UPDATED, null);

    }

}
