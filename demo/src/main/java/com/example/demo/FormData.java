package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class FormData {

        @Id
        @Email @NotNull @NotBlank(message = "Invalid Email")
        String email;

        @NotBlank @NotNull
        @Size(min = 3, message = "Invalid Username, PLS enter again")
        String name;

        @NotBlank @NotNull
        String password;

        @NotNull @Min(value = 1) @Max(value =100)
        Integer age;

        @NotNull
        LocalDate dob;

        @NotNull
        String gender;

        @NotEmpty
        List<@NotNull String> hobbies;

        @NotNull
        String country;
        @NotNull
        String about;

        @AssertTrue
        boolean termsAccepted;
}
