package com.practice.springsecuritycomplete.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document
public class Users {

    @MongoId
    private String id;

    private String name;
    private String email;
    private String password;

}
