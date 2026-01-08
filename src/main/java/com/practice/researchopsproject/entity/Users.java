package com.practice.researchopsproject.entity;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document
public class Users {

    @MongoId
    private String id;

    // used Index to make email unique for email field
    @Indexed(unique = true)
    private String email;

    private String password;
    private String name;
    private Role role;

    @LastModifiedDate
    private LocalDateTime lastLogin;

    private boolean isActive;

    private String address;
    private String state;
    private String city;
    private Long zip;

    private String fileName;

}
