package com.spring.springsecuritypractice1.model;


import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private Integer age;

    public Users() {
    }

    public Users(String username, String password, String role, Integer age) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.age = age;
    }
}
