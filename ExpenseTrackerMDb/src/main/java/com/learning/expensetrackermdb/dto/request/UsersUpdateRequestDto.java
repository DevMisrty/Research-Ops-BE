package com.learning.expensetrackermdb.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class UsersUpdateRequestDto {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;

}
