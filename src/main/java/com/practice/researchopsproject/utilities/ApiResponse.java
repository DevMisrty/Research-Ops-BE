package com.practice.researchopsproject.utilities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Builder
@Data
public class ApiResponse<T> {

    private HttpStatus status;
    private String message;
    private LocalDateTime time;
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> getResponse(HttpStatus status ,String message, T data){
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .status(status)
                        .time(LocalDateTime.now())
                        .message(message)
                        .data(data)
                        .build());
    }
}
