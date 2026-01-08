package com.learning.expensetrackermdb.utility;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {

    private LocalDateTime date;
    private String message;
    private T data;

    public ApiResponse(String message, T data) {
        this.message = message;
        this.data= data;
        this.date = LocalDateTime.now();
    }

    public static <T> ResponseEntity<ApiResponse<T>> generateApiResponse(HttpStatus status, String message, T data){
        return ResponseEntity
                .status(status)
                .body(new ApiResponse<>(message,data));
    }
}
