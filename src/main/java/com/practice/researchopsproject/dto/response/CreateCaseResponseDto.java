package com.practice.researchopsproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CreateCaseResponseDto {

    private String caseId;
    private LocalDateTime createdOn;

}
