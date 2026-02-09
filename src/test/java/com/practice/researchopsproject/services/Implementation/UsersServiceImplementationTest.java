package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.PasswordResetTokenDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.ResetPasswordRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.exception.customException.TokenNotFoundException;
import com.practice.researchopsproject.exception.customException.UserNameAlreadyTaken;
import com.practice.researchopsproject.repository.CaseManagerProfileRepository;
import com.practice.researchopsproject.repository.PasswordResetTokenRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.utilities.MailUtilities;
import org.apache.coyote.BadRequestException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceImplementationTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private CaseManagerProfileRepository caseManagerProfileRepository;

    @Mock
    private ResearcherProfileRepository researcherProfileRepository;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailUtilities mailUtilities;

    @InjectMocks
    private UsersServiceImplementation usersService;

    private UserRequestDto userRequestDto;
    private Users adminUser;
    private Users caseManagerUser;
    private Users researcherUser;
    private CaseManagerProfile caseManagerProfile;
    private ResearcherProfile researcherProfile;
    private PasswordResetToken passwordResetToken;
    private ResetPasswordRequestDto resetPasswordRequestDto;

    private final String BASE_URL = "http://localhost:3000/";

    @BeforeEach
    void setUp() {
        // Set up the BASE_URL field using reflection
        ReflectionTestUtils.setField(usersService, "BASEURL", BASE_URL);

        // Setup test data
        userRequestDto = UserRequestDto.builder()
                .email("admin@test.com")
                .name("Admin User")
                .password("password123")
                .build();

        adminUser = Users.builder()
                .id("u001")
                .email("admin@test.com")
                .name("Admin User")
                .role(Role.ADMIN)
                .password("encodedPassword")
                .isActive(true)
                .address("123 Admin St")
                .city("Admin City")
                .state("AS")
                .zip(12345L)
                .build();

        caseManagerUser = Users.builder()
                .id("u002")
                .email("cm@test.com")
                .name("Case Manager")
                .role(Role.CASE_MANAGER)
                .password("encodedPassword")
                .isActive(true)
                .address("456 CM St")
                .city("CM City")
                .state("CS")
                .zip(67890L)
                .build();

        researcherUser = Users.builder()
                .id("u003")
                .email("researcher@test.com")
                .name("Researcher")
                .role(Role.RESEARCHER)
                .password("encodedPassword")
                .isActive(true)
                .address("789 Research St")
                .city("Research City")
                .state("RS")
                .zip(11111L)
                .build();

        caseManagerProfile = CaseManagerProfile.builder()
                .id("cm001")
                .user(caseManagerUser)
                .assignCaseId(new ArrayList<>())
                .build();

        researcherProfile = ResearcherProfile.builder()
                .id("r001")
                .user(researcherUser)
                .experience(5)
                .assignCaseIds(new ArrayList<>())
                .build();

        passwordResetToken = PasswordResetToken.builder()
                .id("token001")
                .token(UUID.randomUUID().toString())
                .email("test@example.com")
                .expiresAt(LocalDateTime.now().plusMinutes(60))
                .isUsed(false)
                .build();

        resetPasswordRequestDto = ResetPasswordRequestDto.builder()
                .token(passwordResetToken.getToken())
                .password("newPassword123")
                .confirmPassword("newPassword123")
                .build();
    }

    @Test
    void saveUsers_success() throws UserNameAlreadyTaken {
        // Arrange
        when(usersRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(false);
        when(modelMapper.map(userRequestDto, Users.class)).thenReturn(adminUser);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(adminUser);
        when(modelMapper.map(adminUser, UserResponseDto.class)).thenReturn(
                UserResponseDto.builder()
                        .email(adminUser.getEmail())
                        .name(adminUser.getName())
                        .build()
        );

        // Act
        UserResponseDto result = usersService.saveUsers(userRequestDto);

        // Assert
        assertNotNull(result);
        assertEquals(adminUser.getEmail(), result.getEmail());
        assertEquals(adminUser.getName(), result.getName());

        verify(usersRepository).existsByEmail(userRequestDto.getEmail());
        verify(modelMapper).map(userRequestDto, Users.class);
        verify(usersRepository).save(any(Users.class));
        verify(modelMapper).map(adminUser, UserResponseDto.class);
    }

    @Test
    void saveUsers_emailAlreadyExists_throwsUserNameAlreadyTaken() {
        // Arrange
        when(usersRepository.existsByEmail(userRequestDto.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(UserNameAlreadyTaken.class, () -> usersService.saveUsers(userRequestDto));

        verify(usersRepository).existsByEmail(userRequestDto.getEmail());
        verify(usersRepository, never()).save(any());
    }

    @Test
    void saveUsers_withUsersEntity_success() {
        // Arrange
        when(usersRepository.save(adminUser)).thenReturn(adminUser);

        // Act
        Users result = usersService.saveUsers(adminUser);

        // Assert
        assertNotNull(result);
        assertEquals(adminUser.getEmail(), result.getEmail());
        verify(usersRepository).save(adminUser);
    }

    @Test
    void getUserByEmail_success() {
        // Arrange
        when(usersRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(modelMapper.map(adminUser, UserDto.class)).thenReturn(
                UserDto.builder()
                        .email(adminUser.getEmail())
                        .role(adminUser.getRole())
                        .build()
        );

        // Act
        UserDto result = usersService.getUserByEmail(adminUser.getEmail());

        // Assert
        assertNotNull(result);
        assertEquals(adminUser.getEmail(), result.getEmail());

        verify(usersRepository).findByEmail(adminUser.getEmail());
        verify(modelMapper).map(adminUser, UserDto.class);
    }

    @Test
    void getUserByEmail_userNotFound_throwsUsernameNotFoundException() {
        // Arrange
        when(usersRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> usersService.getUserByEmail("nonexistent@test.com"));

        verify(usersRepository).findByEmail("nonexistent@test.com");
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getListOfCaseManager_success_withoutSearch() {
        // Arrange
        Page<CaseManagerProfile> caseManagerPage = new PageImpl<>(List.of(caseManagerProfile));
        when(caseManagerProfileRepository.findAll(any(PageRequest.class))).thenReturn(caseManagerPage);

        // Act
        Page<CaseManagerResponseDto> result = usersService.getListOfCaseManager(1, 10, "name", "ASC", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(caseManagerProfileRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getListOfCaseManager_success_withSearch() {
        // Arrange
        Page<Users> usersPage = new PageImpl<>(List.of(caseManagerUser));
        when(usersRepository.findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.CASE_MANAGER), eq("manager"), eq(Role.CASE_MANAGER), eq("manager"), any(PageRequest.class)))
                .thenReturn(usersPage);
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.of(caseManagerProfile));

        // Act
        Page<CaseManagerResponseDto> result = usersService.getListOfCaseManager(1, 10, "name", "ASC", "manager");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(usersRepository).findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.CASE_MANAGER), eq("manager"), eq(Role.CASE_MANAGER), eq("manager"), any(PageRequest.class));
        verify(caseManagerProfileRepository).findByUser(caseManagerUser);
    }

    @Test
    void getListOfCaseManager_searchWithNoProfile_throwsUsernameNotFoundException() {
        // Arrange
        Page<Users> usersPage = new PageImpl<>(List.of(caseManagerUser));
        when(usersRepository.findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.CASE_MANAGER), eq("manager"), eq(Role.CASE_MANAGER), eq("manager"), any(PageRequest.class)))
                .thenReturn(usersPage);
        when(caseManagerProfileRepository.findByUser(caseManagerUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
                () -> usersService.getListOfCaseManager(1, 10, "name", "ASC", "manager"));

        verify(usersRepository).findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.CASE_MANAGER), eq("manager"), eq(Role.CASE_MANAGER), eq("manager"), any(PageRequest.class));
        verify(caseManagerProfileRepository).findByUser(caseManagerUser);
    }

    @Test
    void getListofResearcher_success_withoutSearch() {
        // Arrange
        Page<ResearcherProfile> researcherPage = new PageImpl<>(List.of(researcherProfile));
        when(researcherProfileRepository.findAll(any(PageRequest.class))).thenReturn(researcherPage);

        // Act
        Page<ResearcherResponseDto> result = usersService.getListofResearcher(1, 10, "name", "DESC", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(researcherProfileRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getListofResearcher_success_withSearch() {
        // Arrange
        Page<Users> usersPage = new PageImpl<>(List.of(researcherUser));
        when(usersRepository.findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.RESEARCHER), eq("research"), eq(Role.RESEARCHER), eq("research"), any(PageRequest.class)))
                .thenReturn(usersPage);
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.of(researcherProfile));

        // Act
        Page<ResearcherResponseDto> result = usersService.getListofResearcher(1, 10, "name", "DESC", "research");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(usersRepository).findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.RESEARCHER), eq("research"), eq(Role.RESEARCHER), eq("research"), any(PageRequest.class));
        verify(researcherProfileRepository).findByUser(researcherUser);
    }

    @Test
    void getListofResearcher_searchWithNoProfile_throwsUsernameNotFoundException() {
        // Arrange
        Page<Users> usersPage = new PageImpl<>(List.of(researcherUser));
        when(usersRepository.findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.RESEARCHER), eq("research"), eq(Role.RESEARCHER), eq("research"), any(PageRequest.class)))
                .thenReturn(usersPage);
        when(researcherProfileRepository.findByUser(researcherUser)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
                () -> usersService.getListofResearcher(1, 10, "name", "DESC", "research"));

        verify(usersRepository).findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                eq(Role.RESEARCHER), eq("research"), eq(Role.RESEARCHER), eq("research"), any(PageRequest.class));
        verify(researcherProfileRepository).findByUser(researcherUser);
    }

    @Test
    void checkProfileIsActive_success() {
        // Arrange
        when(usersRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = usersService.checkProfileIsActive(adminUser.getEmail());

        // Assert
        assertTrue(result);
        verify(usersRepository).findByEmail(adminUser.getEmail());
    }

    @Test
    void checkProfileIsActive_inactiveUser() {
        // Arrange
        adminUser.setActive(false);
        when(usersRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));

        // Act
        boolean result = usersService.checkProfileIsActive(adminUser.getEmail());

        // Assert
        assertFalse(result);
        verify(usersRepository).findByEmail(adminUser.getEmail());
    }

    @Test
    void checkProfileIsActive_userNotFound_throwsUsernameNotFoundException() {
        // Arrange
        when(usersRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
                () -> usersService.checkProfileIsActive("nonexistent@test.com"));

        verify(usersRepository).findByEmail("nonexistent@test.com");
    }

    @Test
    void changePassword_success() {
        // Arrange
        when(usersRepository.findByEmail(adminUser.getEmail())).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(adminUser);

        // Act
        usersService.changePassword(adminUser.getEmail(), "newPassword123");

        // Assert
        verify(usersRepository).findByEmail(adminUser.getEmail());
        verify(passwordEncoder).encode("newPassword123");
        verify(usersRepository).save(adminUser);
        assertEquals("newEncodedPassword", adminUser.getPassword());
    }

    @Test
    void changePassword_userNotFound_throwsUsernameNotFoundException() {
        // Arrange
        when(usersRepository.findByEmail("nonexistent@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
                () -> usersService.changePassword("nonexistent@test.com", "newPassword123"));

        verify(usersRepository).findByEmail("nonexistent@test.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(usersRepository, never()).save(any());
    }

    @Test
    void forgotPasswordMail_success() {
        // Arrange
        when(usersRepository.existsByEmail(adminUser.getEmail())).thenReturn(true);
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        // Act
        usersService.forgotPasswordMail(adminUser.getEmail());

        // Assert
        verify(usersRepository).existsByEmail(adminUser.getEmail());
        verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));

    }

    @Test
    void forgotPasswordMail_userNotFound_throwsUsernameNotFoundException() {
        // Arrange
        when(usersRepository.existsByEmail("nonexistent@test.com")).thenReturn(false);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
                () -> usersService.forgotPasswordMail("nonexistent@test.com"));

        verify(usersRepository).existsByEmail("nonexistent@test.com");
        verify(passwordResetTokenRepository, never()).save(any());
        verify(mailUtilities, never()).sendMail(any(), any(), any());
    }

    @Test
    void fetchResetPasswordToken_success() throws BadRequestException {
        // Arrange
        when(passwordResetTokenRepository.findByToken(passwordResetToken.getToken())).thenReturn(Optional.of(passwordResetToken));
        when(modelMapper.map(passwordResetToken, PasswordResetTokenDto.class)).thenReturn(
                PasswordResetTokenDto.builder()
                        .token(passwordResetToken.getToken())
                        .email(passwordResetToken.getEmail())
                        .expiresAt(passwordResetToken.getExpiresAt())
                        .isUsed(passwordResetToken.isUsed())
                        .build()
        );

        // Act
        PasswordResetTokenDto result = usersService.fetchResetPasswordToken(passwordResetToken.getToken());

        // Assert
        assertNotNull(result);
        assertEquals(passwordResetToken.getToken(), result.getToken());
        assertEquals(passwordResetToken.getEmail(), result.getEmail());

        verify(passwordResetTokenRepository).findByToken(passwordResetToken.getToken());
        verify(modelMapper).map(passwordResetToken, PasswordResetTokenDto.class);
    }

    @Test
    void fetchResetPasswordToken_tokenNotFound_throwsBadRequestException() {
        // Arrange
        when(passwordResetTokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadRequestException.class, 
                () -> usersService.fetchResetPasswordToken("invalid-token"));

        verify(passwordResetTokenRepository).findByToken("invalid-token");
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void setResetPassword_success() throws TokenNotFoundException, TokenExpireException, BadRequestException {
        // Arrange
        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken())).thenReturn(Optional.of(passwordResetToken));
        when(usersRepository.findByEmail(passwordResetToken.getEmail())).thenReturn(Optional.of(adminUser));
        when(passwordEncoder.encode(resetPasswordRequestDto.getPassword())).thenReturn("newEncodedPassword");
        when(usersRepository.save(any(Users.class))).thenReturn(adminUser);
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenReturn(passwordResetToken);

        // Act
        usersService.setResetPassword(resetPasswordRequestDto);

        // Assert
        verify(passwordResetTokenRepository).findByToken(resetPasswordRequestDto.getToken());
        verify(usersRepository).findByEmail(passwordResetToken.getEmail());
        verify(passwordEncoder).encode(resetPasswordRequestDto.getPassword());
        verify(usersRepository).save(adminUser);
        verify(passwordResetTokenRepository).save(passwordResetToken);
        verify(mailUtilities).sendMail(eq(adminUser.getEmail()), eq("Account Password has been chnaged."), eq(adminUser.getEmail()));
        
        assertTrue(passwordResetToken.isUsed());
        assertEquals("newEncodedPassword", adminUser.getPassword());
    }

    @Test
    void setResetPassword_passwordMismatch_throwsBadRequestException() {
        // Arrange
        resetPasswordRequestDto.setConfirmPassword("differentPassword");

        // Act & Assert
        assertThrows(BadRequestException.class, 
                () -> usersService.setResetPassword(resetPasswordRequestDto));

        verify(passwordResetTokenRepository, never()).findByToken(any());
        verify(usersRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).encode(any());
        verify(usersRepository, never()).save(any());
    }

    @Test
    void setResetPassword_tokenNotFound_throwsTokenNotFoundException() {
        // Arrange
        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TokenNotFoundException.class, 
                () -> usersService.setResetPassword(resetPasswordRequestDto));

        verify(passwordResetTokenRepository).findByToken(resetPasswordRequestDto.getToken());
        verify(usersRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).encode(any());
        verify(usersRepository, never()).save(any());
    }

    @Test
    void setResetPassword_tokenExpired_throwsTokenExpireException() {
        // Arrange
        passwordResetToken.setExpiresAt(LocalDateTime.now().minusMinutes(1)); // Expired
        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken())).thenReturn(Optional.of(passwordResetToken));

        // Act & Assert
        assertThrows(TokenExpireException.class, 
                () -> usersService.setResetPassword(resetPasswordRequestDto));

        verify(passwordResetTokenRepository).findByToken(resetPasswordRequestDto.getToken());
        verify(usersRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).encode(any());
        verify(usersRepository, never()).save(any());
    }

    @Test
    void setResetPassword_tokenAlreadyUsed_throwsTokenExpireException() {
        // Arrange
        passwordResetToken.setUsed(true);
        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken())).thenReturn(Optional.of(passwordResetToken));

        // Act & Assert
        assertThrows(TokenExpireException.class, 
                () -> usersService.setResetPassword(resetPasswordRequestDto));

        verify(passwordResetTokenRepository).findByToken(resetPasswordRequestDto.getToken());
        verify(usersRepository, never()).findByEmail(any());
        verify(passwordEncoder, never()).encode(any());
        verify(usersRepository, never()).save(any());
    }

    @Test
    void setResetPassword_userNotFound_throwsUsernameNotFoundException() {
        // Arrange
        when(passwordResetTokenRepository.findByToken(resetPasswordRequestDto.getToken())).thenReturn(Optional.of(passwordResetToken));
        when(usersRepository.findByEmail(passwordResetToken.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, 
                () -> usersService.setResetPassword(resetPasswordRequestDto));

        verify(passwordResetTokenRepository).findByToken(resetPasswordRequestDto.getToken());
        verify(usersRepository).findByEmail(passwordResetToken.getEmail());
        verify(passwordEncoder, never()).encode(any());
        verify(usersRepository, never()).save(any());
    }
}
