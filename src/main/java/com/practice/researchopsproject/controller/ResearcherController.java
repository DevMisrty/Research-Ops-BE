package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.InvitationDto;
import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.ResearcherService;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rs")
@RequiredArgsConstructor
@CrossOrigin(originPatterns = {"localhost:3000", "localhost:5173"})
public class ResearcherController {

    private final ResearcherService service;
    private final JwtUtilities jwtUtilities;

    @PostMapping("/register/{token}")
    public ResponseEntity<?> createResearcher(
            @PathVariable String token,
            @RequestBody RegisterResearcherRequestDto requestDto) throws BadRequestException, TokenExpireException {

        service.createResearchProfile(token, requestDto);

        return ApiResponse.getResponse(HttpStatus.CREATED, Messages.RESEARCHER_CREATED, "researcher profile created");
    }

    @GetMapping("/cases")
    public ResponseEntity<?> getListOfCases(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int limit,
            @RequestParam(required = false, defaultValue = "caseName") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String dir,
            @RequestParam(required = false) String searchBy,
            HttpServletRequest request
    ){
        if(page< 0)page =0;
        if(limit <= 0) limit =10;

        String token = request.getHeader("Authorization");
        token = token.substring(7);

        String email = jwtUtilities.getEmailFromToken(token);

        Page<CaseDto> response =
                service.getListOfAssignedCases(page, limit, sortBy, dir, searchBy, email);

        return ApiResponse.getResponse(HttpStatus.OK, Messages.CASES_FETCHED_SUCCESSFULLY, response);
    }


}
