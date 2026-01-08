package com.learning.expensetrackermdb.controller;

import com.learning.expensetrackermdb.dto.request.LoginRequestDto;
import com.learning.expensetrackermdb.exception.customexception.IncorrectInputValueException;
import com.learning.expensetrackermdb.jwt.JWTUtility;
import com.learning.expensetrackermdb.utility.ApiResponse;
import com.learning.expensetrackermdb.utility.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserAuthController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtility utility;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) throws IncorrectInputValueException {
        if(loginRequestDto.getEmail() == null || loginRequestDto.getPassword() == null){
            throw  new IncorrectInputValueException(MessageConstants.INVALID_CREDENTIALS);
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );
        String token = utility.generateAccessToken(loginRequestDto.getEmail());
        System.out.println(token);
        return ApiResponse.generateApiResponse(HttpStatus.ACCEPTED, MessageConstants.LOGIN_SUCCESS, token);
    }

}
