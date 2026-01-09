package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.LoginRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor()
@RequestMapping("/api/auth")
@Slf4j
@CrossOrigin(originPatterns = {"localhost:3000", "localhost:5173"})
public class AuthController {

    private final UsersService usersService;
    private final AuthenticationManager manager;
    private final JwtUtilities utilities;

    @PostMapping("/admin/register")
    private ResponseEntity<?> RegisterAdmin(@Valid @RequestBody UserRequestDto requestDto){

        var response = usersService.saveUsers(requestDto);
        log.info("Registered the User with email, {}", requestDto.getEmail());

        return ApiResponse.getResponse(HttpStatus.CREATED, Messages.USER_CREATED, response);
    }

    // returns Jwt access token, and refresh tokens
    @PostMapping("/login")
    private ResponseEntity<?> Login(@Valid @RequestBody LoginRequestDto requestDto)
            throws AuthenticationException {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );

        var tokens = getTokens(requestDto.getEmail());
        if( !usersService.checkProfileIsActive(requestDto.getEmail()))
            throw new UsernameNotFoundException(Messages.LOGIN_FAILED);

        log.info("Login credentials has been successfully verified, and Token has been generated, for email, {} ", requestDto.getEmail());
        return ApiResponse.getResponse(HttpStatus.ACCEPTED, Messages.LOGIN_SUCCESS, tokens);
    }

    @PostMapping("/refresh")
    private ResponseEntity<?> refreshToken(@RequestParam String token){

        String email = utilities.getEmailFromToken(token);

        var tokens = getTokens(email);

        log.info("RefreshToken has been used, and new access token has been created and send.");
        return ApiResponse.getResponse(HttpStatus.CREATED, Messages.TOKEN_GENERATED, tokens);

    }


    private Map<String, String> getTokens(String email){
        UserDto user = usersService.getUserByEmail(email);

        String accessToken = utilities.getAccessToken(user);
        String refreshToken = utilities.getRefreshToken(user.getEmail());

        HashMap<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        tokens.put("role", user.getRole().toString());
        tokens.put("fileName", user.getFileName());
        return tokens;
    }

}
