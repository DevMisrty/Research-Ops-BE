package com.practice.researchopsproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ResetPasswordRequestDto {

    private String password;
    private String confirmPassword;

    private String token;

}
