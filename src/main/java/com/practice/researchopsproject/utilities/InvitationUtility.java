package com.practice.researchopsproject.utilities;

import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class InvitationUtility {

    private static final PasswordEncoder encoder  = new BCryptPasswordEncoder();

    public static Users createUserFromInvitation(Invitation invitation, String password){
        Users users = Users.builder()
                .email(invitation.getEmail())
                .password(encoder.encode(password))
                .name(invitation.getName())
                .role(invitation.getRole())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .address(invitation.getAddress())
                .state(invitation.getState())
                .city(invitation.getCity())
                .zip(invitation.getZip()).build();
        return users;
    }

}
