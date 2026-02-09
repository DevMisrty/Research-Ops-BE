package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.repository.*;
import com.practice.researchopsproject.utilities.JwtUtilities;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CaseServiceImplementationTest {

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private CaseManagerProfileRepository caseManagerProfileRepository;

    @Mock
    private TempCaseRepository tempCaseRepository;

    @Mock
    private AvailCaseIdRepository availCaseIdRepository;

    @Mock
    private JwtUtilities jwtUtilities;

    @Mock
    private ResearcherProfileRepository researcherProfileRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private CaseServiceImplementation service;

    private Users caseManagerUser;
    private CaseManagerProfile caseManagerProfile;

    @BeforeEach
    void setUp() {
        caseManagerUser = Users.builder()
                .id("u001")
                .email("cm@test.com")
                .role(Role.CASE_MANAGER)
                .isActive(true)
                .build();

        caseManagerProfile = CaseManagerProfile.builder()
                .id("cm001")
                .user(caseManagerUser)
                .assignCaseId(new ArrayList<>())
                .build();
    }

    @Test
    void getListOfCases_success_withoutSearch_asc() {
        Case aCase = Case.builder()
                .id("case001")
                .caseId("CASE-001")
                .caseName("Alpha")
                .clientName("Client")
                .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                .currentStage(CaseStage.WORKING)
                .createdOn(LocalDateTime.now())
                .isValid(true)
                .build();

        when(caseRepository.findAllByIsValidTrue(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(aCase)));

        Page<CaseResponseDto> result = service.getListOfCases(1, 10, "createdOn", "ASC", null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseRepository).findAllByIsValidTrue(any(PageRequest.class));
    }

    @Test
    void getListOfCases_success_withSearch_desc() {
        when(caseRepository
                .findByIsValidTrueAndCaseNameContainingOrIsValidTrueAndClientNameContainingOrIsValidTrueAndPracticeAreaContaining(
                        eq("alpha"), eq("alpha"), eq("alpha"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(Case.builder()
                        .caseId("CASE-001")
                        .caseName("Alpha")
                        .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                        .currentStage(CaseStage.WORKING)
                        .CMIRemoved(false)
                        .isValid(true)
                        .build())));

        Page<CaseResponseDto> result = service.getListOfCases(1, 10, "createdOn", "DESC", "alpha");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseRepository)
                .findByIsValidTrueAndCaseNameContainingOrIsValidTrueAndClientNameContainingOrIsValidTrueAndPracticeAreaContaining(
                        eq("alpha"), eq("alpha"), eq("alpha"), any(PageRequest.class));
    }

    @Test
    void getListOfCasesByCreator_success_withoutSearch() {
        when(usersRepository.findByEmail("cm@test.com")).thenReturn(Optional.of(caseManagerUser));
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.of(caseManagerProfile));

        when(caseRepository.findAllByCreatorAndIsValidTrue(eq(caseManagerProfile), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(Case.builder()
                        .caseId("CASE-001")
                        .caseName("Alpha")
                        .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                        .currentStage(CaseStage.WORKING)
                        .CMIRemoved(false)
                        .isValid(true)
                        .build())));

        Page<CaseResponseDto> result =
                service.getListOfCasesByCreator(1, 10, "createdOn", "ASC", null, "cm@test.com");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseRepository).findAllByCreatorAndIsValidTrue(eq(caseManagerProfile), any(PageRequest.class));
    }

    @Test
    void getListOfCasesByCreator_success_withSearch() {
        when(usersRepository.findByEmail("cm@test.com")).thenReturn(Optional.of(caseManagerUser));
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.of(caseManagerProfile));

        when(caseRepository
                .findAllByCreatorAndCaseNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndClientNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndPracticeAreaAndIsValidTrue(
                        eq(caseManagerProfile), eq("alpha"),
                        eq(caseManagerProfile), eq("alpha"),
                        eq(caseManagerProfile), eq("alpha"),
                        any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(Case.builder()
                        .caseId("CASE-001")
                        .caseName("Alpha")
                        .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                        .currentStage(CaseStage.WORKING)
                        .CMIRemoved(false)
                        .isValid(true)
                        .build())));

        Page<CaseResponseDto> result =
                service.getListOfCasesByCreator(1, 10, "createdOn", "DESC", "alpha", "cm@test.com");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseRepository)
                .findAllByCreatorAndCaseNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndClientNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndPracticeAreaAndIsValidTrue(
                        eq(caseManagerProfile), eq("alpha"),
                        eq(caseManagerProfile), eq("alpha"),
                        eq(caseManagerProfile), eq("alpha"),
                        any(PageRequest.class));
    }

    @Test
    void getListOfCasesByCreator_userNotFound_throws() {
        when(usersRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.getListOfCasesByCreator(1, 10, "createdOn", "ASC", null, "missing@test.com"));

        verify(caseManagerProfileRepository, never()).findByUser(any());
    }

    @Test
    void getListOfCasesByCreator_caseManagerNotFound_throws() {
        when(usersRepository.findByEmail("cm@test.com")).thenReturn(Optional.of(caseManagerUser));
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.getListOfCasesByCreator(1, 10, "createdOn", "ASC", null, "cm@test.com"));
    }

    @Test
    void getCaseById_success() throws Exception {
        Case aCase = Case.builder()
                .id("case001")
                .caseId("CASE-001")
                .caseName("Alpha")
                .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                .currentStage(CaseStage.WORKING)
                .CMIRemoved(false)
                .isValid(true)
                .build();

        when(caseRepository.findByCaseId("CASE-001")).thenReturn(Optional.of(aCase));

        CaseDto result = service.getCaseById("CASE-001");

        assertNotNull(result);
        assertEquals("CASE-001", result.getCaseId());
    }

    @Test
    void getCaseById_notFound_throwsCaseNotFoundException() {
        when(caseRepository.findByCaseId("CASE-404")).thenReturn(Optional.empty());

        assertThrows(CaseNotFoundException.class, () -> service.getCaseById("CASE-404"));
    }

    @Test
    void createTempCase_whenNoExisting_createsNewAndSaves() throws Exception {
        when(tempCaseRepository.findFirstByIsCompleteFalseOrderByCreatedAtAsc()).thenReturn(Optional.empty());

        TempCase saved = TempCase.builder()
                .id("tmp001")
                .caseId("CC20260101")
                .isComplete(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(tempCaseRepository.save(any(TempCase.class))).thenReturn(saved);

        TempCase result = service.createTempCase(httpServletRequest);

        assertNotNull(result);
        assertFalse(result.isComplete());
        verify(tempCaseRepository).save(any(TempCase.class));
    }

    @Test
    void createTempCase_whenExisting_returnsExistingAndUpdatesTimestamp() throws Exception {
        TempCase existing = TempCase.builder()
                .id("tmp001")
                .caseId("CC20260101")
                .isComplete(false)
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .build();

        when(tempCaseRepository.findFirstByIsCompleteFalseOrderByCreatedAtAsc())
                .thenReturn(Optional.of(existing));

        TempCase result = service.createTempCase(httpServletRequest);

        assertNotNull(result);
        assertEquals("tmp001", result.getId());
        verify(tempCaseRepository, never()).save(any());
    }

    @Test
    void saveCase_success_persistsAndUpdatesRelations() {
        Users researcherUser = Users.builder()
                .id("ru001")
                .email("researcher@test.com")
                .role(Role.RESEARCHER)
                .isActive(true)
                .build();

        ResearcherProfile researcherProfile = ResearcherProfile.builder()
                .id("r001")
                .user(researcherUser)
                .experience(3)
                .assignCaseIds(new ArrayList<>())
                .build();

        CaseDto request = CaseDto.builder()
                .caseId("CC2026250")
                .clientName("Client")
                .caseName("Alpha")
                .practiceArea("COPYRIGHT_INFRINGEMENT")
                .currentStage("WORKING")
                .plaintiffPronounce("Mr")
                .plaintiffName("John")
                .descriptionOfWork("Desc")
                .researchers(List.of("researcher@test.com"))
                .build();

        when(usersRepository.findByEmail("cm@test.com")).thenReturn(Optional.of(caseManagerUser));
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.of(caseManagerProfile));

        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.of(researcherProfile));

        Case savedCase = Case.builder()
                .id("case001")
                .caseId("CC2026250")
                .clientName("Client")
                .caseName("Alpha")
                .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                .currentStage(CaseStage.WORKING)
                .plaintiffPronounce(Pronounces.Mr)
                .plaintiffName("John")
                .descriptionOfWork("Desc")
                .CMIRemoved(false)
                .creator(caseManagerProfile)
                .isValid(true)
                .build();

        when(caseRepository.save(any(Case.class))).thenReturn(savedCase);

        TempCase tempCase = TempCase.builder()
                .id("tmp001")
                .caseId("CC2026250")
                .isComplete(false)
                .createdAt(LocalDateTime.now())
                .build();

        when(tempCaseRepository.findByCaseId("CC2026250")).thenReturn(Optional.of(tempCase));
        when(tempCaseRepository.save(any(TempCase.class))).thenReturn(tempCase);

        when(caseManagerProfileRepository.save(any(CaseManagerProfile.class))).thenReturn(caseManagerProfile);
        when(researcherProfileRepository.save(any(ResearcherProfile.class))).thenReturn(researcherProfile);

        CaseDto result = service.saveCase(request, "cm@test.com");

        assertNotNull(result);
        assertEquals("CC2026250", result.getCaseId());
        verify(caseRepository).save(any(Case.class));
        verify(tempCaseRepository).findByCaseId("CC2026250");
        verify(tempCaseRepository).save(any(TempCase.class));
        verify(caseManagerProfileRepository).save(any(CaseManagerProfile.class));
        verify(researcherProfileRepository, atLeastOnce()).save(any(ResearcherProfile.class));
    }

    @Test
    void saveCase_researcherUserNotFound_throws() {
        CaseDto request = CaseDto.builder()
                .caseId("CC2026250")
                .caseName("Alpha")
                .practiceArea("COPYRIGHT_INFRINGEMENT")
                .currentStage("WORKING")
                .plaintiffPronounce("Mr")
                .descriptionOfWork("Desc")
                .researchers(List.of("researcher@test.com"))
                .build();

        when(usersRepository.findByEmail("cm@test.com")).thenReturn(Optional.of(caseManagerUser));
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.of(caseManagerProfile));

        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.saveCase(request, "cm@test.com"));

        verify(caseRepository, never()).save(any());
    }
}
