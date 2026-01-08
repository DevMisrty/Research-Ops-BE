package com.practice.researchopsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ResearcherProfile {

    @MongoId
    private String id;

    @DBRef(lazy = true)
    private Users user;

    private Integer experience;

    private List<String> assignCaseIds;

}
