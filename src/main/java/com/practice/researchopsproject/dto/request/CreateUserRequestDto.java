package com.practice.researchopsproject.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequestDto {

    @NotNull(message = "name cant be null")
    @NotBlank(message = "name cant be empty")
    private String name;

    @Email(message = "enter proper email. ")
    private String email;

    @NotBlank(message = "address cant be null")
    private String address;

    @NotBlank(message = "state cant be null")
    private String state;

    @NotBlank(message = "city cant be null")
    private String city;

    @NotNull(message = "zip cant be null")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Enter proper zip code"
    )
    private String zip;

}
