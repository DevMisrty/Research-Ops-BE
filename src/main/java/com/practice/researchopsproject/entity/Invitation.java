package com.practice.researchopsproject.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document(collection = "invitations")
public class Invitation {

    @MongoId
    private UUID id;

    private String name;
    private String email;
    private Role role;
    private String address;
    private String city;
    private String state;
    private Long zip;
    private int experience;

    private InvitationStatus status;

    @CreatedDate
    private LocalDateTime createdAt;

    private Date expiresIn;

    private String fileName;

}
