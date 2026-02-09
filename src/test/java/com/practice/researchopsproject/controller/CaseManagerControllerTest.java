package com.practice.researchopsproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.EditCaseManagerDto;
import com.practice.researchopsproject.dto.request.RegisterCaseManagerRequestDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.TempCase;
import com.practice.researchopsproject.entity.Users;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.exception.customException.ResourceNotFoundException;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Messages;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
class CaseManagerControllerTest {

    @MockitoBean
    private CaseManagerService caseManagerService;

    @MockitoBean
    private CaseService caseService;

    @MockitoBean
    private JwtUtilities jwtUtilities;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Page<ResearcherResponseDto> researcherPage;
    private Page<CaseResponseDto> casePage;
    private CaseDto caseDto;
    private EditCaseManagerDto editCaseManagerDto;
    private RegisterCaseManagerRequestDto registerCaseManagerRequestDto;
    private TempCase tempCase;
    private CaseManagerProfile caseManagerProfile;
    private Case savedCase;

    @BeforeEach
    void setUp() {

        tempCase = TempCase.builder()
                .id("temp001")
                .caseId("CASE-2024-001")
                .isComplete(false)
                .createdAt(LocalDateTime.now())
                .build();

        registerCaseManagerRequestDto = RegisterCaseManagerRequestDto.builder()
                .password("SecurePass@123")
                .confirmPassword("SecurePass@123")
                .build();

        researcherPage = new PageImpl<>(List.of(
                ResearcherResponseDto.builder()
                        .id("rs001")
                        .name("Dr. Priya Sharma")
                        .email("priya.sharma@research.com")
                        .isActive(true)
                        .build(),
                ResearcherResponseDto.builder()
                        .id("rs002")
                        .name("Dr. Amit Patel")
                        .email("amit.patel@research.com")
                        .isActive(true)
                        .build()
        ));

        editCaseManagerDto = EditCaseManagerDto.builder()
                .name("Rajesh Kumar")
                .address("301, Sterling Heights, SG Highway")
                .state("Gujarat")
                .city("Ahmedabad")
                .isActive(true)
                .build();

        casePage = new PageImpl<>(List.of(
                CaseResponseDto.builder()
                        .id("case001")
                        .caseId("CASE-2024-001")
                        .caseName("Patent Infringement Analysis")
                        .currentStage("WORKING")
                        .createdOn(LocalDateTime.now().minusDays(5))
                        .build(),
                CaseResponseDto.builder()
                        .id("case002")
                        .caseId("CASE-2024-002")
                        .caseName("Trademark Registration Review")
                        .currentStage("REVIEW")
                        .createdOn(LocalDateTime.now().minusDays(2))
                        .build()
        ));

        caseDto = CaseDto.builder()
                .caseId("CASE-2024-001")
                .caseName("Patent Infringement Analysis")
                .practiceArea("Intellectual Property Rights")
                .currentStage("WORKING")
                .descriptionOfWork("Test description")
                .researchers(Arrays.asList("a@test.com", "b@test.com"))
                .build();

        caseManagerProfile = CaseManagerProfile.builder()
                .id("cm001")
                .user(Users.builder()
                        .email("dev@test.com")
                        .name("Dev Mistry")
                        .isActive(true)
                        .build())
                .assignCaseId(List.of())
                .build();

        savedCase = Case.builder()
                .caseId("CASE-2024-001")
                .caseName("Patent Infringement Analysis")
                .build();
    }

    // ---------------- REGISTER CM ----------------

    @Test
    void createCaseManager() throws Exception {

        registerCaseManagerRequestDto.setConfirmPassword("does-not-match");

        mockMvc.perform(post("/api/cm/register/{token}", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerCaseManagerRequestDto)))
                .andExpect(status().isInternalServerError());

        registerCaseManagerRequestDto.setConfirmPassword(registerCaseManagerRequestDto.getPassword());

        mockMvc.perform(post("/api/cm/register/{token}", "token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerCaseManagerRequestDto)))
                .andExpect(status().isCreated());
    }

    // ---------------- EDIT PROFILE ----------------

    @Test
    void editProfile_success() throws Exception {

        doNothing().when(caseManagerService)
                .editMyProfile(any(EditCaseManagerDto.class), any());

        mockMvc.perform(put("/api/cm/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCaseManagerDto)))
                .andExpect(status().isOk());
    }

    @Test
    void editProfile_badRequest() throws Exception {

        doThrow(new BadRequestException("Invalid data"))
                .when(caseManagerService)
                .editMyProfile(any(EditCaseManagerDto.class), any());

        mockMvc.perform(put("/api/cm/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCaseManagerDto)))
                .andExpect(status().isBadRequest());
    }

    // ---------------- CREATE TEMP CASE ----------------

    @Test
    void createCase() throws Exception {

        when(caseService.createTempCase(any()))
                .thenReturn(tempCase);

        mockMvc.perform(post("/api/cm/create/case"))
                .andExpect(status().isOk());
    }

    // ---------------- GET RESEARCHERS ----------------

    @Test
    void getListOfActiveResearcher() throws Exception {

        when(caseManagerService.getResearchers(anyInt(), anyInt()))
                .thenReturn(researcherPage);

        mockMvc.perform(get("/api/cm/get/rs")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk());
    }

    // ---------------- GET PROFILE ----------------

    @Test
    void getCasMangerProfileByToken() throws Exception {

        when(jwtUtilities.getEmailFromToken("fake-jwt-token"))
                .thenReturn("dev@test.com");

        when(caseManagerService.getCasMangerProfileByEmail("dev@test.com"))
                .thenReturn(caseManagerProfile);

        mockMvc.perform(get("/api/cm/")
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Dev Mistry"));

        when(jwtUtilities.getEmailFromToken("fake-jwt-token"))
                .thenReturn("missing@test.com");

        when(caseManagerService.getCasMangerProfileByEmail("missing@test.com"))
            .thenThrow(new ResourceNotFoundException("Profile not found"));

        mockMvc.perform(get("/api/cm/")
                        .header("Authorization", "Bearer fake-jwt-token"))
                .andExpect(status().isNotFound());
    }

    // ---------------- EDIT CASE ----------------

    @Test
    void editCase() throws Exception {

        when(jwtUtilities.getEmailFromToken("fake-jwt-token"))
                .thenReturn("dev@test.com");

        when(caseManagerService.editCase(any(CaseDto.class), eq("dev@test.com"), eq("CASE-2024-001")))
                .thenReturn(savedCase);

        mockMvc.perform(post("/api/cm/edit/case/{id}", "CASE-2024-001")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseDto)))
                .andExpect(status().isOk());

        when(caseManagerService.editCase(any(CaseDto.class), eq("dev@test.com"), eq("CASE-404")))
                .thenThrow(new CaseNotFoundException(Messages.CASE_NOT_FOUND));

        mockMvc.perform(post("/api/cm/edit/case/{id}", "CASE-404")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseDto)))
                .andExpect(status().isNotFound());
    }

    // ---------------- SAVE CASE ----------------

    @Test
    void saveCase() throws Exception {

        when(jwtUtilities.getEmailFromToken("fake-jwt-token"))
                .thenReturn("dev@test.com");

        when(caseService.saveCase(any(CaseDto.class), eq("dev@test.com")))
                .thenReturn(caseDto);

        mockMvc.perform(post("/api/cm/case/save")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseDto)))
                .andExpect(status().isOk());
    }

    // ---------------- GET CASE LIST ----------------

    @Test
    void getCaseList() throws Exception {

        when(jwtUtilities.getEmailFromToken("fake-jwt-token"))
                .thenReturn("dev@test.com");

        when(caseService.getListOfCasesByCreator(anyInt(), anyInt(), anyString(), anyString(), any(), eq("dev@test.com")))
                .thenReturn(casePage);

        mockMvc.perform(get("/api/cm/cases")
                        .header("Authorization", "Bearer fake-jwt-token")
                        .param("page", "1")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(2));
    }
}
