package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.entity.InvitationStatus;
import com.practice.researchopsproject.entity.Role;
import com.practice.researchopsproject.repository.InvitationRepository;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.utilities.ImageUploadUtilities;
import com.practice.researchopsproject.utilities.MailUtilities;
import com.practice.researchopsproject.utilities.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvitationServiceImplementation implements InvitationService {

    private final InvitationRepository repo;
    private final MailUtilities mail;
    private final ImageUploadUtilities imageUploadUtilities;

    @Override
    public UUID createAndSaveInvitation(CreateUserRequestDto requestDto) {
         Invitation invite = Invitation.builder()
                .id(UUID.randomUUID())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .role(Role.CASE_MANAGER)
                .address(requestDto.getAddress())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .zip(Long.valueOf(requestDto.getZip()))
                .status(InvitationStatus.USE)
                .expiresIn( new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .build();

            Invitation savedData = repo.save(invite);
            log.info("Saved Data {}", savedData);

            mail.sendMail(savedData.getEmail(), "Create Credentials", savedData.getId().toString() );

            return savedData.getId();
    }

    @Override
    public UUID createAndSaveInvitationWithProgileImage(CreateUserRequestDto requestDto, MultipartFile file)
            throws IOException {
        String fileName = imageUploadUtilities.storeFile(file, requestDto.getEmail());

        Invitation invite = Invitation.builder()
                .id(UUID.randomUUID())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .role(Role.CASE_MANAGER)
                .address(requestDto.getAddress())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .zip(Long.valueOf(requestDto.getZip()))
                .status(InvitationStatus.USE)
                .expiresIn( new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .fileName(fileName)
                .build();

        Invitation savedData = repo.save(invite);
        log.info("Saved Data {}", savedData);

        mail.sendMail(savedData.getEmail(), "Create Credentials", savedData.getId().toString() );

        return savedData.getId();

    }

    @Override
    public UUID createAndSaveInvitationForResWithFile(CreateResearcherRequestDto requestDto, MultipartFile file) throws IOException {

        String fileName = imageUploadUtilities.storeFile(file, requestDto.getEmail());

        Invitation invite = Invitation.builder()
                .id(UUID.randomUUID())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .role(Role.RESEARCHER)
                .address(requestDto.getAddress())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .experience(requestDto.getExperience())
                .zip(Long.valueOf(requestDto.getZip()))
                .status(InvitationStatus.USE)
                .fileName(fileName)
                .expiresIn(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .build();

        Invitation savedData = repo.save(invite);
        mail.sendMail(savedData.getEmail(), "Create Credentials", savedData.getId().toString() );

        return savedData.getId();
    }

    @Override
    public UUID createAndSaveInvitationForRes(CreateResearcherRequestDto requestDto) {
        Invitation invite = Invitation.builder()
                .id(UUID.randomUUID())
                .name(requestDto.getName())
                .email(requestDto.getEmail())
                .role(Role.RESEARCHER)
                .address(requestDto.getAddress())
                .city(requestDto.getCity())
                .state(requestDto.getState())
                .experience(requestDto.getExperience())
                .zip(Long.valueOf(requestDto.getZip()))
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 ))
                .build();

        Invitation savedData = repo.save(invite);
        mail.sendMail(savedData.getEmail(), "Create Credentials", savedData.getId().toString() );

        return savedData.getId();
    }

    @Override
    public Invitation getInvitationFromToken(String token) throws BadRequestException {
        Invitation byId = repo.findById(UUID.fromString(token))
                .orElseThrow(() -> new BadRequestException(Messages.TOKEN_EXPIRE));

        return byId;
    }

    @Override
    public Invitation changeStatus(Invitation invitation, InvitationStatus invitationStatus) {
        invitation.setStatus(InvitationStatus.EXPIRE);
        return repo.save(invitation);
    }


}
