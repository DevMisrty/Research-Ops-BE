package com.practice.researchopsproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.ResearcherService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class GeneralControllerTest {

    @MockitoBean
    private CaseService caseService;

    @MockitoBean
    private ResearcherService researcherService;

    @MockitoBean
    private UsersService usersService;

    @MockitoBean
    private CaseManagerService caseManagerService;

    @MockitoBean
    private InvitationService invitationService;

    @MockitoBean
    private JwtUtilities jwtUtilities;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CaseDto caseDto;
    private ResearcherResponseDto researcherResponseDto;
    private CaseManagerResponseDto caseManagerResponseDto;
    private Invitation invitation;
    private RegisterResearcherRequestDto registerResearcherRequestDto;

    @BeforeEach
    void setUp() {

        caseDto = CaseDto.builder()
                .id("case001")
                .caseId("CASE-2024-001")
                .caseName("Patent Infringement Analysis")
                .currentStage("WORKING")
                .build();

        researcherResponseDto = ResearcherResponseDto.builder()
                .id("rs001")
                .name("Dr. Amit Patel")
                .email("researcher@test.com")
                .build();

        caseManagerResponseDto = CaseManagerResponseDto.builder()
                .id("cm001")
                .name("Rajesh Kumar")
                .email("cm@test.com")
                .build();

        invitation = Invitation.builder()
                .name("Invited User")
                .email("invite@test.com")
                .build();

        registerResearcherRequestDto = RegisterResearcherRequestDto.builder()
                .password("New@123")
                .confirmPassword("New@123")
                .build();
    }

    @Test
    void getCaseById() throws Exception {

        when(caseService.getCaseById("CASE-2024-001"))
                .thenReturn(caseDto);

        mockMvc.perform(get("/api/general/case/{id}", "CASE-2024-001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.caseId").value("CASE-2024-001"));
    }

    @Test
    void getResearcherById() throws Exception {

        when(researcherService.getResearcherById("rs001"))
                .thenReturn(researcherResponseDto);

        mockMvc.perform(get("/api/general/researcher/{id}", "rs001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Dr. Amit Patel"));
    }

    @Test
    void getCaseManagerById() throws Exception {

        when(caseManagerService.getCaseManagerById("cm001"))
                .thenReturn(caseManagerResponseDto);

        mockMvc.perform(get("/api/general/casemanager/{id}", "cm001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Rajesh Kumar"));
    }

    @Test
    void getStringDetails() throws Exception {

        mockMvc.perform(get("/api/general/getString")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("List of Admin User fetched."));
    }

    @Test
    void getToken() throws Exception {

        when(invitationService.getInvitationFromToken("token"))
                .thenReturn(invitation);

        mockMvc.perform(get("/api/general/token/{token}", "token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("invite@test.com"));
    }

    @Test
    void changePassword() throws Exception {

        when(jwtUtilities.getEmailFromToken("fake-jwt-token"))
                .thenReturn("user@test.com");
        doNothing().when(usersService)
                .changePassword(any(String.class), any(String.class));

        mockMvc.perform(post("/api/general/change/password")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerResearcherRequestDto)))
                .andExpect(status().isOk());
    }
}
