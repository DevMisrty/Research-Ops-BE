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
public class CaseManagerProfile {

    @MongoId
    private String id;

    @DBRef(lazy = false)
    private Users user;

    // keeping this list, as if we go using fetching the whole Case table, and filter them based on creator id
    // may take longer time based, on data size
    private List<String> assignCaseId;

}
