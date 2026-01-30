package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.entity.InvitationStatus;
import com.practice.researchopsproject.entity.Role;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface InvitationService {
    UUID createAndSaveInvitation(@Valid CreateUserRequestDto requestDto);

    UUID createAndSaveInvitationForRes(@Valid CreateResearcherRequestDto requestDto);

    Invitation getInvitationFromToken(String token) throws BadRequestException;

    Invitation changeStatus(Invitation invitation, InvitationStatus invitationStatus);

    UUID createAndSaveInvitationWithProgileImage
            (CreateUserRequestDto requestDto, MultipartFile file) throws IOException;

    UUID createAndSaveInvitationForResWithFile
            (@Valid CreateResearcherRequestDto requestDto, MultipartFile file) throws IOException;
}
