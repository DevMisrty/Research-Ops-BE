package com.practice.researchopsproject.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.researchopsproject.dto.PasswordResetTokenDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.ForgotPasswordEmail;
import com.practice.researchopsproject.dto.request.LoginRequestDto;
import com.practice.researchopsproject.dto.request.ResetPasswordRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.Role;
import com.practice.researchopsproject.exception.AuthControllerExceptionHandler;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.exception.customException.TokenNotFoundException;
import com.practice.researchopsproject.exception.customException.UserNameAlreadyTaken;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Import(AuthControllerExceptionHandler.class)
class AuthControllerTest {

    @MockitoBean
    private UsersService usersService;

    @MockitoBean
    private  AuthenticationManager manager;

    @MockitoBean
    private JwtUtilities utilities;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDto userRequestDto;
    private UserResponseDto adminResponseDto;
    private LoginRequestDto loginRequestDto;
    private UserDto userDto;
    private ForgotPasswordEmail forgotPasswordEmail;
    private ResetPasswordRequestDto resetPasswordRequestDto;
    private PasswordResetTokenDto passwordResetTokenDto;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        userRequestDto = UserRequestDto.builder()
                .name("Dev")
                .email("dev@test.com")
                .password("1234")
                .build();

        adminResponseDto = UserResponseDto.builder()
                .id("1234")
                .name("Dev").email("dev@test.com").build();

        loginRequestDto = LoginRequestDto.builder()
                .email("dev@test.com")
                .password("1234")
                .build();

        userDto = UserDto.builder()
                .email("dev@test.com")
                .role(Role.ADMIN)   // use your enum
                .fileName("profile.png")
                .build();

        forgotPasswordEmail = ForgotPasswordEmail.builder()
                .email("dev@test.com")
                .build();

        resetPasswordRequestDto = ResetPasswordRequestDto.builder()
                .password("newPass")
                .confirmPassword("newPass")
                .token("reset-token")
                .build();

        passwordResetTokenDto = PasswordResetTokenDto.builder()
                .token("reset-token-123")
                .email("dev@test.com")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .isUsed(false)
                .build();
    }

    @Test
    public void RegisterAdmin() throws Exception {

        when(usersService.saveUsers(userRequestDto))
                .thenReturn(adminResponseDto);

        mockMvc.perform(post("/api/auth/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated());

        when(usersService.saveUsers(userRequestDto))
                .thenThrow(new UserNameAlreadyTaken("User already exists"));
        mockMvc.perform(post("/api/auth/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void Login() throws Exception {

        String endpoint = "/api/auth/login";

        Authentication authentication =
                new UsernamePasswordAuthenticationToken("dev@test.com", "1234");

        when(manager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(usersService.checkProfileIsActive(any()))
                .thenReturn(false);
        mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isNotFound());

        when(usersService.checkProfileIsActive(any()))
                .thenReturn(true);
        when(usersService.getUserByEmail(any())).thenReturn(userDto);
        when(utilities.getAccessToken(any())).thenReturn("access-token");
        when(utilities.getRefreshToken(any())).thenReturn("refresh-token");

        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void Refresh() throws Exception {

        String endpoint = "/api/auth/refresh";

        when(utilities.getEmailFromToken(anyString()))
                .thenReturn("dev@test.com");
        when(utilities.isValidToken(any()))
                .thenReturn(false);

        mockMvc.perform(post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"refreshToken\""))
                .andExpect(status().isUnauthorized());

         when(utilities.isValidToken(anyString()))
                 .thenReturn(true);
        when(usersService.checkProfileIsActive(any()))
                .thenReturn(true);
        when(usersService.getUserByEmail(any())).thenReturn(userDto);
        when(utilities.getAccessToken(any())).thenReturn("access-token");
        when(utilities.getRefreshToken(any())).thenReturn("refresh-token");

         mockMvc.perform(post(endpoint)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content("\"refreshToken\""))
                 .andExpect(status().isCreated());


    }

    @Test
    public void forgotPassword() throws Exception {

        doNothing().when(usersService).forgotPasswordMail(anyString());
        mockMvc.perform(post("/api/auth/forgotpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(forgotPasswordEmail)))
                .andExpect(status().isOk());
    }

    @Test
    public void getResetPasswordToken() throws Exception {

        when(usersService.fetchResetPasswordToken(any()))
                .thenReturn(passwordResetTokenDto);

        mockMvc.perform(get("/api/auth/resetpassword/{token}", "reset-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void setPassword() throws Exception {

        doNothing().when(usersService).setResetPassword(any());
        mockMvc.perform(post("/api/auth/resetpassword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resetPasswordRequestDto)))
                .andExpect(status().isOk());
    }

}