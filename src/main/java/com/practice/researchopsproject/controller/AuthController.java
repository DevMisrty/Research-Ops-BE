package com.practice.researchopsproject.controller;

import com.practice.researchopsproject.dto.PasswordResetTokenDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.ForgotPasswordEmail;
import com.practice.researchopsproject.dto.request.LoginRequestDto;
import com.practice.researchopsproject.dto.request.ResetPasswordRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.entity.PasswordResetToken;
import com.practice.researchopsproject.exception.customException.InvaliTokenException;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.exception.customException.TokenNotFoundException;
import com.practice.researchopsproject.exception.customException.UserNameAlreadyTaken;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.ApiResponse;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    private ResponseEntity<?> RegisterAdmin(@Valid @RequestBody UserRequestDto requestDto)
            throws UserNameAlreadyTaken {

        var response = usersService.saveUsers(requestDto);
        log.info("Registered the User with email, {}", requestDto.getEmail());

        return ApiResponse.getResponse(HttpStatus.CREATED, Messages.USER_CREATED, response);
    }

    // returns Jwt access token, and refresh tokens
    @PostMapping("/login")
    private ResponseEntity<?> Login(@Valid @RequestBody LoginRequestDto requestDto)
            throws AuthenticationException, UsernameNotFoundException {
        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );


        if( !usersService.checkProfileIsActive(requestDto.getEmail()))
            throw new UsernameNotFoundException(Messages.LOGIN_FAILED);

        var tokens = getTokens(requestDto.getEmail());

        log.info("Login credentials has been successfully verified, and Token has been generated, for email, {} ", requestDto.getEmail());
        return ApiResponse.getResponse(HttpStatus.ACCEPTED, Messages.LOGIN_SUCCESS, tokens);
    }

    @PostMapping("/refresh")
    private ResponseEntity<?> refreshToken(@RequestBody String token) throws InvaliTokenException {

        String email = utilities.getEmailFromToken(token);
        if (!utilities.isValidToken(token)) {
            throw new InvaliTokenException(Messages.INVALID_TOKEN);
        }

        var tokens = getTokens(email);

        log.info("RefreshToken has been used, and new access token has been created and send.");
        return ApiResponse.getResponse(HttpStatus.CREATED, Messages.TOKEN_GENERATED, tokens);

    }

    @PostMapping("/forgotpassword")
    private ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordEmail request){
        usersService.forgotPasswordMail(request.getEmail());
        log.info("Forgot password mail has been send.");
        return ApiResponse.getResponse(HttpStatus.OK, Messages.MAIL_SENT, Messages.MAIL_SENT);
    }

    @GetMapping("/resetpassword/{token}")
    public ResponseEntity<?> getResetPasswordToken(@PathVariable String token) throws BadRequestException {
        PasswordResetTokenDto response = usersService.fetchResetPasswordToken(token);
        log.info("Reset Password Details has been send.");
        return ApiResponse.getResponse(HttpStatus.OK, Messages.INVITATION_TOKEN_FETCHED, response);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> setPassword(@RequestBody ResetPasswordRequestDto requestDto)
            throws TokenExpireException, BadRequestException, TokenNotFoundException {
        usersService.setResetPassword(requestDto);
        log.info("Password has been updated.");
        return ApiResponse.getResponse(HttpStatus.OK, Messages.PASSWORD_UPDATED, Messages.PASSWORD_UPDATED);
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
