package com.practice.researchopsproject.dto.request;

import com.practice.researchopsproject.utilities.Messages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotNull(message = "Name must be present. ")
    @NotBlank(message = "Name must be present. ")
    private String name;

    @Email(message = "Email must be present. ")
    @NotNull(message = "Email must be present. ")
    private String email;

    @NotNull(message = "Password cant be null")
    @Size( min = 4, max = 15)
    private String password;

}
