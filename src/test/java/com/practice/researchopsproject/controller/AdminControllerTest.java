package com.practice.researchopsproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.EditCaseManagerDto;
import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.dto.request.EditResearcherDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @MockitoBean
    private UsersService usersService;

    @MockitoBean
    private InvitationService invitationService;

    @MockitoBean
    private CaseManagerService caseManagerService;

    @MockitoBean
    private CaseService caseService;

    @MockitoBean
    private ResearcherService researcherService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequestDto createUserRequestDto;
    private CreateResearcherRequestDto createResearcherRequestDto;
    private EditCaseManagerDto editCaseManagerDto;
    private EditResearcherDto editResearcherDto;
    private CaseDto caseDto;

    private CaseManagerResponseDto caseManagerResponseDto;
    private ResearcherResponseDto researcherResponseDto;
    private CaseResponseDto caseResponseDto;

    private Page<CaseManagerResponseDto> caseManagerPage;
    private Page<ResearcherResponseDto> researcherPage;
    private Page<CaseResponseDto> casePage;

    private MockMultipartFile file;

    @Autowired
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        createUserRequestDto = CreateUserRequestDto.builder()
                .name("Dev Mistry")
                .email("cm@test.com")
                .address("Street 1")
                .state("Gujarat")
                .city("Ahmedabad")
                .zip("380001")
                .build();

        createResearcherRequestDto = CreateResearcherRequestDto.builder()
                .name("Researcher One")
                .email("rs@test.com")
                .address("Street 2")
                .state("Maharashtra")
                .city("Mumbai")
                .zip("40000001")
                .experience(5)
                .build();

        editCaseManagerDto = EditCaseManagerDto.builder()
                .name("Updated CM")
                .address("New Address")
                .state("Delhi")
                .city("Delhi")
                .zip("110001")
                .isActive(true)
                .build();

        editResearcherDto = EditResearcherDto.builder()
                .name("Updated RS")
                .address("New Address")
                .state("Karnataka")
                .city("Bangalore")
                .zip("560001")
                .experience("6")
                .isActive(true)
                .build();

        caseDto = CaseDto.builder()
                .id("1")
                .caseId("CASE-001")
                .clientName("Client A")
                .caseName("Copyright Case")
                .practiceArea("IPR")
                .currentStage("WORKING")
                .createdOn(LocalDateTime.now())
                .hasRegisteredDocument(true)
                .copyrightRegistration(LocalDate.now())
                .researchers(List.of("RS1", "RS2"))
                .build();

        caseManagerResponseDto = CaseManagerResponseDto.builder()
                .id("1")
                .name("CM One")
                .email("cm@test.com")
                .isActive(true)
                .assignCases(3)
                .lastLogin(LocalDateTime.now())
                .address("Street 1")
                .city("Ahmedabad")
                .zip("380001")
                .build();

        researcherResponseDto = ResearcherResponseDto.builder()
                .id("1")
                .name("RS One")
                .email("rs@test.com")
                .assignCases(2)
                .experience(5)
                .isActive(true)
                .address("Street 2")
                .city("Mumbai")
                .zip("40000001")
                .build();

        caseResponseDto = CaseResponseDto.builder()
                .id("1")
                .caseId("CASE-001")
                .clientName("Client A")
                .caseName("Copyright Case")
                .practiceArea("IPR")
                .currentStage("WORKING")
                .createdOn(LocalDateTime.now())
                .researchers(List.of("RS1"))
                .build();

        caseManagerPage = new PageImpl<>(List.of(
                CaseManagerResponseDto.builder()
                        .id("1")
                        .name("CM One")
                        .email("cm@test.com")
                        .isActive(true)
                        .assignCases(2)
                        .build()
        ));

        researcherPage = new PageImpl<>(List.of(
                ResearcherResponseDto.builder()
                        .id("1")
                        .name("RS One")
                        .email("rs@test.com")
                        .assignCases(1)
                        .experience(5)
                        .isActive(true)
                        .build()
        ));

        casePage = new PageImpl<>(List.of(
                CaseResponseDto.builder()
                        .id("1")
                        .caseId("CASE-001")
                        .caseName("Test Case")
                        .build()
        ));
    }


    @Test
    void getListOfCaseManagers() throws Exception {
        when(usersService.getListOfCaseManager(eq(1), eq(10), anyString(), anyString(), any()))
                .thenReturn(caseManagerPage);

        mockMvc.perform(get("/api/admin/casemanager" )
                        .param("page", "1")
                        .param("limit", "10")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.content[0].name").value("CM One"));

    }

    @Test
    void editCaseManager() throws Exception {

        when(caseManagerService.editCaseManager(eq(editCaseManagerDto), any()))
                .thenReturn(caseManagerResponseDto);

        mockMvc.perform(put("/api/admin/edit/casemanager/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(editCaseManagerDto)))
                .andExpect(status().isAccepted());
    }

    @Test
    void createResearcher() throws Exception {

        UUID random = UUID.randomUUID();

        when(invitationService.createAndSaveInvitationForResWithFile(eq(createResearcherRequestDto), any()))
                .thenReturn(random);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "dummy-image".getBytes()
        );

        MockMultipartFile requestDtoPart = new MockMultipartFile(
                "requestDto",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createResearcherRequestDto)
        );

        mockMvc.perform(multipart("/api/admin/create/researcher")
                        .file(file)
                        .file(requestDtoPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(random.toString()));
    }

    @Test
    void getListOfCases() throws Exception {

        when(caseService.getListOfCases(eq(1), eq(10), anyString(), anyString(), any()))
                .thenReturn(casePage);

        mockMvc.perform(get("/api/admin/cases" )
                        .param("page", "1")
                        .param("limit", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(1));

        when(caseService.getListOfCases(eq(2), eq(10), anyString(), anyString(), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/admin/cases" )
                        .param("page", "2")
                        .param("limit", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.content.length()").value(0));

    }

    @Test
    void editCaseDetails() throws Exception {

        when(caseManagerService.editCaseByAdmin(eq(caseDto), any()))
                .thenReturn(caseDto);

        mockMvc.perform(put("/api/admin/case/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(caseDto)))
                .andExpect(status().isOk());

        when(caseManagerService.editCaseByAdmin(eq(caseDto), any()))
                .thenThrow(new CaseNotFoundException("Case not found"));

        mockMvc.perform(put("/api/admin/case/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(caseDto)))
                .andExpect(status().isNotFound());
    }
}
