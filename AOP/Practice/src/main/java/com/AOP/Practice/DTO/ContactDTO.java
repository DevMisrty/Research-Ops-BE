package com.AOP.Practice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDTO {

    @NotNull(message = "Pls enter proper firstName")
    @Size(min = 3, max = 20)
    String firstName;

    @NotNull(message = "Pls enter the lastName")
    @Size(min=3,max=20)
    String lastName;

    @NotNull(message = "Pls enter valid email Address")
    @Email
    String email;

    @NotNull(message = "Pls enter Valid Phone Number")
    @Pattern(regexp = "[1-9]([0-9]{9})")
    String phone;

    @NotNull(message = "Subject cant be empty")
    @Size(min =3)
    String subject;

    @NotNull(message = "Message cant be Empty")
            @Size(min=10)
    String message;

}
