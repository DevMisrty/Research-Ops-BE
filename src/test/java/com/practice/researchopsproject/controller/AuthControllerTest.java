package com.practice.researchopsproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.ForgotPasswordEmail;
import com.practice.researchopsproject.dto.request.LoginRequestDto;
import com.practice.researchopsproject.dto.request.ResetPasswordRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.Role;
import com.practice.researchopsproject.exception.customException.UserNameAlreadyTaken;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    }

    @Test
    public void RegisterAdmin_Success() throws Exception {

        when(usersService.saveUsers(userRequestDto))
                .thenReturn(adminResponseDto);

        mockMvc.perform(post("/api/auth/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userRequestDto)))
                .andExpect(status().isCreated());




    }

}