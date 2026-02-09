package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.request.EditResearcherDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.repository.CaseRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Mappers;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResearcherServiceImplementationTest {

    @Mock
    private InvitationService invitationService;

    @Mock
    private ResearcherProfileRepository researcherProfileRepository;

    @Mock
    private UsersService usersService;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private CaseRepository caseRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private JwtUtilities jwtUtilities;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private ResearcherServiceImplementation service;

    private Invitation invitation;
    private RegisterResearcherRequestDto registerRequestDto;
    private EditResearcherDto editResearcherDto;
    private Users researcherUser;
    private ResearcherProfile researcherProfile;

    @BeforeEach
    void setUp() {
        invitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test Researcher")
                .email("researcher@test.com")
                .role(Role.RESEARCHER)
                .address("123 Research St")
                .city("Research City")
                .state("RS")
                .zip(12345L)
                .experience(5)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000)) // 24 hours from now
                .build();

        registerRequestDto = RegisterResearcherRequestDto.builder()
                .password("password123")
                .confirmPassword("password123")
                .build();

        editResearcherDto = EditResearcherDto.builder()
                .name("Updated Researcher")
                .address("456 Updated St")
                .city("Updated City")
                .state("US")
                .zip("67890")
                .experience("7")
                .isActive(true)
                .build();

        researcherUser = Users.builder()
                .id("u001")
                .email("researcher@test.com")
                .name("Test Researcher")
                .role(Role.RESEARCHER)
                .isActive(true)
                .address("123 Research St")
                .city("Research City")
                .state("RS")
                .zip(12345L)
                .build();

        researcherProfile = ResearcherProfile.builder()
                .id("r001")
                .user(researcherUser)
                .experience(5)
                .assignCaseIds(new ArrayList<>())
                .build();
    }

    @Test
    void createResearchProfile_success() throws BadRequestException, TokenExpireException {
        when(invitationService.getInvitationFromToken(anyString())).thenReturn(invitation);
        when(usersService.saveUsers(any(Users.class))).thenReturn(researcherUser);
        when(researcherProfileRepository.save(any(ResearcherProfile.class))).thenReturn(researcherProfile);

        service.createResearchProfile("valid-token", registerRequestDto);

        verify(invitationService).getInvitationFromToken("valid-token");
        verify(usersService).saveUsers(any(Users.class));
        verify(researcherProfileRepository).save(any(ResearcherProfile.class));
        verify(invitationService).changeStatus(eq(invitation), eq(InvitationStatus.EXPIRE));
    }

    @Test
    void createResearchProfile_tokenExpired_throwsTokenExpireException() throws BadRequestException {
        Invitation expiredInvitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test Researcher")
                .email("researcher@test.com")
                .role(Role.RESEARCHER)
                .status(InvitationStatus.EXPIRE)
                .expiresIn(new Date(System.currentTimeMillis() - 86400000)) // 24 hours ago
                .build();

        when(invitationService.getInvitationFromToken(anyString())).thenReturn(expiredInvitation);

        assertThrows(TokenExpireException.class,
                () -> service.createResearchProfile("expired-token", registerRequestDto));

        verify(researcherProfileRepository, never()).save(any());
        verify(invitationService, never()).changeStatus(any(), any());
    }

    @Test
    void getListOfAssignedCases_success_withoutSearch() {
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.of(researcherProfile));

        Case aCase = Case.builder()
                .id("case001")
                .caseId("CASE-001")
                .caseName("Alpha")
                .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                .currentStage(CaseStage.WORKING)
                .CMIRemoved(false)
                .isValid(true)
                .build();

        when(caseRepository.findAllByResearchersAndIsValidTrue(eq(researcherProfile), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(aCase)));

        Page<CaseResponseDto> result = service.getListOfAssignedCases(1, 10, "createdOn", "ASC", null, "researcher@test.com");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseRepository).findAllByResearchersAndIsValidTrue(eq(researcherProfile), any(PageRequest.class));
    }

    @Test
    void getListOfAssignedCases_success_withSearch() {
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.of(researcherProfile));

        Case aCase = Case.builder()
                .id("case001")
                .caseId("CASE-001")
                .caseName("Alpha")
                .practiceArea(PracticeArea.COPYRIGHT_INFRINGEMENT)
                .currentStage(CaseStage.WORKING)
                .CMIRemoved(false)
                .isValid(true)
                .build();

        when(caseRepository.searchByResearcherAndKeyword(any(ObjectId.class), eq("alpha"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(aCase)));

        Page<CaseResponseDto> result = service.getListOfAssignedCases(1, 10, "createdOn", "DESC", "alpha", "researcher@test.com");

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseRepository).searchByResearcherAndKeyword(any(ObjectId.class), eq("alpha"), any(PageRequest.class));
    }

    @Test
    void getListOfAssignedCases_userNotFound_throwsUsernameNotFoundException() {
        when(usersRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.getListOfAssignedCases(1, 10, "createdOn", "ASC", null, "missing@test.com"));

        verify(researcherProfileRepository, never()).findByUser(any());
        verify(caseRepository, never()).findAllByResearchersAndIsValidTrue(any(), any());
    }

    @Test
    void getListOfAssignedCases_profileNotFound_throwsUsernameNotFoundException() {
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.getListOfAssignedCases(1, 10, "createdOn", "ASC", null, "researcher@test.com"));

        verify(caseRepository, never()).findAllByResearchersAndIsValidTrue(any(), any());
    }

    @Test
    void getResearcherById_success() {
        when(researcherProfileRepository.findById("r001")).thenReturn(Optional.of(researcherProfile));

        ResearcherResponseDto result = service.getResearcherById("r001");

        assertNotNull(result);
        verify(researcherProfileRepository).findById("r001");
    }

    @Test
    void getResearcherById_notFound_throwsUsernameNotFoundException() {
        when(researcherProfileRepository.findById("r999")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.getResearcherById("r999"));

        verify(researcherProfileRepository).findById("r999");
    }

    @Test
    void editResearcher_success() {
        when(researcherProfileRepository.findById("r001")).thenReturn(Optional.of(researcherProfile));
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(usersRepository.save(any(Users.class))).thenReturn(researcherUser);
        when(researcherProfileRepository.save(any(ResearcherProfile.class))).thenReturn(researcherProfile);

        service.editResearcher(editResearcherDto, "r001");

        verify(researcherProfileRepository).findById("r001");
        verify(usersRepository).findByEmail("researcher@test.com");
        verify(usersRepository).save(any(Users.class));
        verify(researcherProfileRepository).save(any(ResearcherProfile.class));
    }

    @Test
    void editResearcher_profileNotFound_throwsUsernameNotFoundException() {
        when(researcherProfileRepository.findById("r999")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.editResearcher(editResearcherDto, "r999"));

        verify(usersRepository, never()).findByEmail(any());
        verify(usersRepository, never()).save(any());
        verify(researcherProfileRepository, never()).save(any());
    }

    @Test
    void editMyProfile_success() throws BadRequestException {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer valid-jwt-token");
        when(jwtUtilities.getEmailFromToken("valid-jwt-token")).thenReturn("researcher@test.com");
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.of(researcherProfile));
        when(researcherProfileRepository.findById("r001")).thenReturn(Optional.of(researcherProfile));
        when(usersRepository.save(any(Users.class))).thenReturn(researcherUser);
        when(researcherProfileRepository.save(any(ResearcherProfile.class))).thenReturn(researcherProfile);

        service.editMyProfile(editResearcherDto, httpServletRequest);

        verify(jwtUtilities).getEmailFromToken("valid-jwt-token");
        verify(usersRepository).findByEmail("researcher@test.com");
        verify(researcherProfileRepository).findByUser(any(Users.class));
        verify(researcherProfileRepository).findById("r001");
        verify(usersRepository).save(any(Users.class));
        verify(researcherProfileRepository).save(any(ResearcherProfile.class));
    }

    @Test
    void editMyProfile_userNotFound_throwsUsernameNotFoundException() throws BadRequestException {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer valid-jwt-token");
        when(jwtUtilities.getEmailFromToken("valid-jwt-token")).thenReturn("missing@test.com");
        when(usersRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.editMyProfile(editResearcherDto, httpServletRequest));

        verify(researcherProfileRepository, never()).findByUser(any());
        verify(researcherProfileRepository, never()).findById(any());
        verify(usersRepository, never()).save(any());
        verify(researcherProfileRepository, never()).save(any());
    }

    @Test
    void getResearcherByEmail_success() {
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.of(researcherProfile));

        ResearcherResponseDto result = service.getResearcherByEmail("researcher@test.com");

        assertNotNull(result);
        verify(usersRepository).findByEmail("researcher@test.com");
        verify(researcherProfileRepository).findByUser(any(Users.class));
    }

    @Test
    void getResearcherByEmail_userNotFound_throwsUsernameNotFoundException() {
        when(usersRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.getResearcherByEmail("missing@test.com"));

        verify(researcherProfileRepository, never()).findByUser(any());
    }

    @Test
    void getResearcherByEmail_profileNotFound_throwsUsernameNotFoundException() {
        when(usersRepository.findByEmail("researcher@test.com")).thenReturn(Optional.of(researcherUser));
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.getResearcherByEmail("researcher@test.com"));

        verify(usersRepository).findByEmail("researcher@test.com");
        verify(researcherProfileRepository).findByUser(any(Users.class));
    }
}
