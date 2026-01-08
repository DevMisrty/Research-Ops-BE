package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.entity.InvitationStatus;
import com.practice.researchopsproject.entity.Role;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;

import java.util.UUID;

public interface InvitationService {
    UUID createAndSaveInvitation(@Valid CreateUserRequestDto requestDto);

    UUID createAndSaveInvitationForRes(@Valid CreateResearcherRequestDto requestDto);

    Invitation getInvitationFromToken(String token) throws BadRequestException;

    Invitation changeStatus(Invitation invitation, InvitationStatus invitationStatus);
}
