package com.practice.researchopsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document
public class AvailCaseId {

    private String id;

    @Indexed(unique = true)
    private String caseId;
}
