package com.practice.researchopsproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseManagerResponseDto {

    private String id;
    private String name;
    private String email;
    private Boolean isActive;
    private Integer assignCases;
    private LocalDateTime lastLogin;

}
