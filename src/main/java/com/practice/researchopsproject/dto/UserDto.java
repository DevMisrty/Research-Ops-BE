package com.practice.researchopsproject.dto;

import com.practice.researchopsproject.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserDto {

    private String id;
    private String email;
    private Role role;
    private String fileName;

}
