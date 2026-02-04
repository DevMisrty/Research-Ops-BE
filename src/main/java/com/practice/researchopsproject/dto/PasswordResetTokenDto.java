package com.practice.researchopsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PasswordResetTokenDto {

    private String token;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt ;
    private boolean isUsed;
}
