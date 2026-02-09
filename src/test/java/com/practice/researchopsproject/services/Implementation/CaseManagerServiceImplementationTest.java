package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.EditCaseManagerDto;
import com.practice.researchopsproject.dto.request.RegisterCaseManagerRequestDto;
import com.practice.researchopsproject.dto.response.CreateCaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.repository.*;
import com.practice.researchopsproject.services.Implementation.CaseManagerServiceImplementation;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.UsersService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaseManagerServiceImplementationTest {

    @Mock
    private CaseManagerProfileRepository repository;

    @Mock
    private ResearcherProfileRepository researcherRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private TempCaseRepository tempCaseRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private InvitationService invitationService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private AvailCaseIdRepository availCaseIdRepository;

    @InjectMocks
    private CaseManagerServiceImplementation service;

    private RegisterCaseManagerRequestDto registerDto;
    private CaseDto caseDto;
    private EditCaseManagerDto editDto;
    private CaseManagerProfile cmProfile;
    private Case caseEntity;
    private Invitation invitation;
    private Users user;

    @BeforeEach
    void setUp() {

        registerDto = RegisterCaseManagerRequestDto.builder()
                .password("Password@123")
                .confirmPassword("Password@123")
                .build();

        caseDto = CaseDto.builder()
                .caseId("CASE-2024-001")
                .caseName("Test Case")
                .practiceArea("COPYRIGHT_INFRINGEMENT")
                .currentStage("WORKING")
                .descriptionOfWork("Test description")
                .researchers(List.of("researcher@test.com"))
                .plaintiffPronounce("Mr")
                .build();

        editDto = EditCaseManagerDto.builder()
                .name("Updated Name")
                .address("Updated Address")
                .city("Updated City")
                .state("Updated State")
                .zip("12345")
                .build();

        user = Users.builder()
                .id("u001")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .isActive(true)
                .build();

        cmProfile = CaseManagerProfile.builder()
                .id("cm001")
                .user(user)
                .assignCaseId(new ArrayList<>())
                .build();

        caseEntity = Case.builder()
                .id("case001")
                .caseId("CASE-2024-001")
                .currentStage(CaseStage.WORKING)
                .creator(cmProfile)
                .build();

        invitation = Invitation.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .build();
    }

    // ---------------- CREATE CASE MANAGER ----------------

    @Test
    void createCaseManager_success() throws Exception {

        when(invitationService.getInvitationFromToken("valid-token")).thenReturn(invitation);
        when(encoder.encode(anyString())).thenReturn("encoded");
        when(usersService.saveUsers((Users) any())).thenReturn(user);
        when(repository.save(any())).thenReturn(cmProfile);

        service.createCaseManager("valid-token", registerDto);

        verify(invitationService).changeStatus(invitation, InvitationStatus.EXPIRE);
    }

    @Test
    void createCaseManager_expiredToken() throws BadRequestException {
        invitation.setStatus(InvitationStatus.EXPIRE);
        when(invitationService.getInvitationFromToken("token")).thenReturn(invitation);

        assertThrows(TokenExpireException.class,
                () -> service.createCaseManager("token", registerDto));
    }

    // ---------------- GET RESEARCHERS ----------------

    @Test
    void getResearchers_success() {

        Users rsUser = Users.builder()
                .email("researcher@test.com")
                .role(Role.RESEARCHER)
                .isActive(true)
                .build();

        ResearcherProfile profile = ResearcherProfile.builder()
                .id("r001")
                .user(rsUser)
                .experience(5)
                .assignCaseIds(new ArrayList<>())
                .build();

        when(usersRepository.findAllByIsActiveAndRole(eq(true), eq(Role.RESEARCHER), any()))
                .thenReturn(new PageImpl<>(List.of(rsUser)));

        when(researcherRepository.findByUser(rsUser))
                .thenReturn(Optional.of(ResearcherProfile.builder()
                        .id("r001")
                        .user(rsUser)
                        .experience(5)
                        .assignCaseIds(new ArrayList<>())
                        .build()));

        Page<ResearcherResponseDto> page = service.getResearchers(0, 10);

        assertEquals(1, page.getTotalElements());
    }

    // ---------------- CREATE CASE ----------------

    @Test
    void createCase_success() {

        Users researcherUser = Users.builder()
                .email("researcher@test.com")
                .role(Role.RESEARCHER)
                .isActive(true)
                .build();

        ResearcherProfile researcherProfile = ResearcherProfile.builder()
                .id("r001")
                .user(researcherUser)
                .experience(5)
                .assignCaseIds(new ArrayList<>())
                .build();

        when(usersRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        when(repository.findByUser(user))
                .thenReturn(Optional.of(cmProfile));

        when(usersRepository.findByEmail("researcher@test.com"))
                .thenReturn(Optional.of(researcherUser));

        when(researcherRepository.findByUser(researcherUser))
                .thenReturn(Optional.of(researcherProfile));

        when(caseRepository.save(any()))
                .thenReturn(caseEntity);

        Case result = service.createCase(caseDto, "test@example.com");

        assertNotNull(result);
        verify(caseRepository).save(any());
    }

    // ---------------- TEMP CASE ----------------

    @Test
    void createEmptyCase_success() {

        TempCase tempCase = TempCase.builder()
                .caseId("CASE-001")
                .isComplete(false)
                .build();

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(repository.findByUser(user)).thenReturn(Optional.of(cmProfile));
        when(caseRepository.findFirstByIsValidFalseAndCreatedOnBeforeOrderByCaseIdAsc(any())).thenReturn(Optional.empty());
        when(caseRepository.save(any(Case.class))).thenReturn(caseEntity);

        CreateCaseResponseDto result = service.createEmptyCase("test@example.com");

        assertNotNull(result);
    }


    // ---------------- PROFILE ----------------

    @Test
    void getCaseManagerByEmail_success() {

        when(usersRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(repository.findByUser(user)).thenReturn(Optional.of(cmProfile));

        CaseManagerProfile result =
                service.getCasMangerProfileByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("cm001", result.getId());
    }
}

