package com.practice.researchopsproject.exception;

import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.exception.customException.InvaliTokenException;
import com.practice.researchopsproject.exception.customException.ResourceNotFoundException;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.utilities.ExceptionResponse;
import com.practice.researchopsproject.utilities.Messages;
import com.sun.jdi.request.InvalidRequestStateException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class AuthControllerExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(HttpServletRequest request){
        String url = request.getRequestURI();
        return ExceptionResponse.exceptionResponse(url, Messages.INVALID_EXCEPTION, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, Exception e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(TokenExpireException.class)
    public ResponseEntity<?> handleTokenExpireException(HttpServletRequest request, TokenExpireException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourcesNotFoundException(HttpServletRequest request, ResourceNotFoundException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleException(HttpServletRequest request, BadRequestException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InvalidRequestStateException.class)
    public ResponseEntity<?> handleInvalidRequestStateException(HttpServletRequest request, InvalidRequestStateException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(CaseNotFoundException.class)
    public ResponseEntity<?> handleCaseNotFoundException(HttpServletRequest request, CaseNotFoundException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleUsernameNotFoundException(HttpServletRequest request, UsernameNotFoundException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvaliTokenException.class)
    public ResponseEntity<?> handleInvalidException(HttpServletRequest request, InvaliTokenException e){
        return ExceptionResponse.exceptionResponse(
                request.getRequestURI(),
                e.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }
}
