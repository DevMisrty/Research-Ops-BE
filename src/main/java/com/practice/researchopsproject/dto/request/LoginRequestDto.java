package com.practice.researchopsproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor @Builder
@Data
public class LoginRequestDto {

    @NotNull(message = "Email must not be null")
    @Email
    private String email;

    @NotNull(message = "Password must not be null")
    @Size( min = 4, max = 15)
    private String password;

}
