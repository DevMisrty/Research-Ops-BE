package com.practice.researchopsproject.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RegisterCaseManagerRequestDto {

    private String password;
    private String confirmPassword;

}
