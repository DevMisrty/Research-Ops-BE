package com.practice.researchopsproject.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Data
public class ExceptionResponse {

    private String path;
    private HttpStatus status;
    private String message;

    public static ResponseEntity<ExceptionResponse> exceptionResponse(String path, String message, HttpStatus status){
        return ResponseEntity
                .status(status)
                .body(new ExceptionResponse(path,status, message));

    }

}
