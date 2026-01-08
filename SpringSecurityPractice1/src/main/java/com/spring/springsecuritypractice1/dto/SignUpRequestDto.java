package com.spring.springsecuritypractice1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SignUpRequestDto {

    private String username;
    private String password;
    private Integer age;
}
