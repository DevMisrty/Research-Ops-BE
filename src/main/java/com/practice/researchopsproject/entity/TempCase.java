package com.practice.researchopsproject.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor @Builder
@Document
public class TempCase {

    @Id
    private String id;

    @Indexed(unique = true)
    private String caseId;

    private boolean isComplete= false;

    @CreatedDate
    private LocalDateTime createdAt;

}
