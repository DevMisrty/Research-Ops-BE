package com.practice.researchopsproject.dto;

import com.practice.researchopsproject.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UserProfileDto {

    private String id;
    private String email;
    private String password;
    private String name;
    private Role role;
    private LocalDateTime lastLogin;
    private boolean isActive;
    private String address;
    private String state;
    private String city;
    private Long zip;

}
