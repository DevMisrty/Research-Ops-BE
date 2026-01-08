package com.practice.researchopsproject.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Data
public class UserResponseDto {

    private String id;
    private String name;
    private String email;

}
