package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.PaginationResponseDto;
import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = {"localhost:3000", "localhost:5173"})
public class AdminController {

    private final UsersService usersService;
    private final InvitationService invitationService;
    private final CaseManagerService caseManagerService;
    private final CaseService caseService;


    @GetMapping("/casemanager")
    public ResponseEntity<ApiResponse<Page<CaseManagerResponseDto>>> getListOfCaseManagers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "name") String sortBy ,
            @RequestParam(required = false, defaultValue = "ASC") String direction,
            @RequestParam(required = false) String searchBy
            ){

        if(page < 0 )page =0;
        if(limit <= 0)limit =10;

        Page<CaseManagerResponseDto> response =
                usersService.getListOfCaseManager(page, limit, sortBy, direction, searchBy);

        return ApiResponse.getResponse(HttpStatus.OK, Messages.LIST_OF_CASEMANAGER_FETCHED, response);
    }

    //access by admin, as well as Case Manager.
    @GetMapping("/researcher")
    public ResponseEntity<?> getListOfResearchers(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer limit,
            @RequestParam(required = false, defaultValue = "name") String sortBy ,
            @RequestParam(required = false, defaultValue = "ASC") String direction,
            @RequestParam(required = false) String searchBy ){
        if(page < 0)page =0;
        if(limit <= 0)limit =0;

        Page<ResearcherResponseDto> response =
                usersService.getListofResearcher(page, limit, sortBy, direction, searchBy);

        return ApiResponse.getResponse(HttpStatus.OK, Messages.LIST_OF_RESEARCHER_FETCHED, response);

    }

    //  currently sending the response, and mail, which contains only the TokenId, once the
    // react app is build change the data, to the correct route according to the react router.
    //
    @PostMapping("/create/casemanager")
    public ResponseEntity<?> createCaseManager( @RequestBody CreateUserRequestDto requestDto){

        log.info("CREATING THE INVITATION ENTITY FOR, {}",requestDto );

        UUID token = invitationService.createAndSaveInvitation(requestDto);
        log.info("Token generated for request, {}", token);

        return ApiResponse.getResponse(HttpStatus.OK, Messages.MAIL_SEND, token);
    }

    @PostMapping("/create/researcher")
    public ResponseEntity<?> createResearcher(@Valid @RequestBody CreateResearcherRequestDto requestDto){

        log.info("CREATING THE INVITATION ENTITY FOR, {}",requestDto );
        UUID token = invitationService.createAndSaveInvitationForRes(requestDto);
        log.info("Token generated for request, {}", token   );

        return ApiResponse.getResponse(HttpStatus.OK, Messages.MAIL_SEND, token);
    }


    @PostMapping("/active/{id}")
    public ResponseEntity<?> activateUserProfile(@PathVariable String id){

        UserResponseDto response = usersService.activateUserProfile(id);
        return ApiResponse.getResponse(HttpStatus.ACCEPTED, Messages.PROFILE_SET_ACTIVE, response);
    }

    @PostMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateUserProfile(@PathVariable String id){

        UserResponseDto response = usersService.deactivateUserProfile(id);
        return ApiResponse.getResponse(HttpStatus.ACCEPTED, Messages.PROFILE_SET_NOACTIVE, response);
    }


    @GetMapping("/cases")
    public ResponseEntity<?> getListOfCases(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "caseName") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String dir,
            @RequestParam(required = false) String searchBy
    ){
        if(page <0)page =0;
        if(limit <= 0) limit=10;

        Page<CaseDto> response = caseService.getListOfCases(page, limit, sortBy, dir, searchBy);

        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASES_FETCHED_SUCCESSFULLY, response);
    }

    @PutMapping("/case/{id}")
    public ResponseEntity<?> editCaseDetails(@RequestBody CaseDto requestDto, @PathVariable String id) throws CaseNotFoundException {

        CaseDto caseDto = caseManagerService.editCaseByAdmin(requestDto, id);

        return ApiResponse.getResponse(HttpStatus.OK , Messages.CASE_UPDATED, caseDto );
    }

}
