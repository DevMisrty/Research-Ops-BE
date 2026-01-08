package com.practice.researchopsproject.dto;

import com.practice.researchopsproject.entity.InvitationStatus;
import com.practice.researchopsproject.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class InvitationDto {

    private String id;
    private String name;
    private String email;
    private Role role;
    private String address;
    private String city;
    private String state;
    private Long zip;
    private int experience;

    private InvitationStatus status;
    private Date expiresIn;

}
