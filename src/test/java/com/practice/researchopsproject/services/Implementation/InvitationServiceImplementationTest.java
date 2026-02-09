package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.request.CreateResearcherRequestDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.entity.Invitation;
import com.practice.researchopsproject.entity.InvitationStatus;
import com.practice.researchopsproject.entity.Role;
import com.practice.researchopsproject.repository.InvitationRepository;
import com.practice.researchopsproject.utilities.ImageUploadUtilities;
import com.practice.researchopsproject.utilities.MailUtilities;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceImplementationTest {

    @Mock
    private InvitationRepository repo;

    @Mock
    private MailUtilities mail;

    @Mock
    private ImageUploadUtilities imageUploadUtilities;

    @InjectMocks
    private InvitationServiceImplementation service;

    private CreateUserRequestDto createUserRequestDto;
    private CreateResearcherRequestDto createResearcherRequestDto;
    private Invitation invitation;

    @BeforeEach
    void setUp() {
        createUserRequestDto = CreateUserRequestDto.builder()
                .name("Test Manager")
                .email("test@example.com")
                .address("123 Test St")
                .city("Test City")
                .state("TS")
                .zip("12345")
                .build();

        createResearcherRequestDto = CreateResearcherRequestDto.builder()
                .name("Test Researcher")
                .email("researcher@example.com")
                .address("456 Research Ave")
                .city("Research City")
                .state("RS")
                .zip("67890")
                .experience(5)
                .build();

        invitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000)) // 24 hours from now
                .build();
    }

    @Test
    void createAndSaveInvitation_success() {
        Invitation savedInvitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test Manager")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .address("123 Test St")
                .city("Test City")
                .state("TS")
                .zip(12345L)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .build();

        when(repo.save(any(Invitation.class))).thenReturn(savedInvitation);

        UUID result = service.createAndSaveInvitation(createUserRequestDto);

        assertNotNull(result);
        verify(repo).save(any(Invitation.class));
        verify(mail).sendMail(eq("test@example.com"), eq("Create Credentials"), anyString());
    }

    @Test
    void createAndSaveInvitationWithProgileImage_success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        String fileName = "profile.jpg";

        Invitation savedInvitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test Manager")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .address("123 Test St")
                .city("Test City")
                .state("TS")
                .zip(12345L)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .fileName(fileName)
                .build();

        when(imageUploadUtilities.storeFile(file, "test@example.com")).thenReturn(fileName);
        when(repo.save(any(Invitation.class))).thenReturn(savedInvitation);

        UUID result = service.createAndSaveInvitationWithProgileImage(createUserRequestDto, file);

        assertNotNull(result);
        verify(imageUploadUtilities).storeFile(file, "test@example.com");
        verify(repo).save(any(Invitation.class));
        verify(mail).sendMail(eq("test@example.com"), eq("Create Credentials"), contains(result.toString()));
    }

    @Test
    void createAndSaveInvitationForResWithFile_success() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        String fileName = "researcher.pdf";

        Invitation savedInvitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test Researcher")
                .email("researcher@example.com")
                .role(Role.RESEARCHER)
                .address("456 Research Ave")
                .city("Research City")
                .state("RS")
                .zip(67890L)
                .experience(5)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .fileName(fileName)
                .build();

        when(imageUploadUtilities.storeFile(file, "researcher@example.com")).thenReturn(fileName);
        when(repo.save(any(Invitation.class))).thenReturn(savedInvitation);

        UUID result = service.createAndSaveInvitationForResWithFile(createResearcherRequestDto, file);

        assertNotNull(result);
        verify(imageUploadUtilities).storeFile(file, "researcher@example.com");
        verify(repo).save(any(Invitation.class));
        verify(mail).sendMail(eq("researcher@example.com"), eq("Create Credentials"), contains(result.toString()));
    }

    @Test
    void createAndSaveInvitationForRes_success() {
        Invitation savedInvitation = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test Researcher")
                .email("researcher@example.com")
                .role(Role.RESEARCHER)
                .address("456 Research Ave")
                .city("Research City")
                .state("RS")
                .zip(67890L)
                .experience(5)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .build();

        when(repo.save(any(Invitation.class))).thenReturn(savedInvitation);

        UUID result = service.createAndSaveInvitationForRes(createResearcherRequestDto);

        assertNotNull(result);
        verify(repo).save(any(Invitation.class));
        verify(mail).sendMail(eq("researcher@example.com"), eq("Create Credentials"), anyString());
    }

    @Test
    void getInvitationFromToken_success() throws BadRequestException {
        UUID tokenId = UUID.randomUUID();
        Invitation foundInvitation = Invitation.builder()
                .id(tokenId)
                .name("Test User")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .build();

        when(repo.findById(tokenId)).thenReturn(Optional.of(foundInvitation));

        Invitation result = service.getInvitationFromToken(tokenId.toString());

        assertNotNull(result);
        assertEquals(tokenId, result.getId());
        verify(repo).findById(tokenId);
    }

    @Test
    void getInvitationFromToken_notFound_throwsBadRequestException() {
        UUID tokenId = UUID.randomUUID();

        when(repo.findById(tokenId)).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> service.getInvitationFromToken(tokenId.toString()));

        verify(repo).findById(tokenId);
    }

    @Test
    void changeStatus_success() {
        Invitation invitationToChange = Invitation.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .status(InvitationStatus.USE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .build();

        Invitation updatedInvitation = Invitation.builder()
                .id(invitationToChange.getId())
                .name("Test User")
                .email("test@example.com")
                .role(Role.CASE_MANAGER)
                .status(InvitationStatus.EXPIRE)
                .expiresIn(new Date(System.currentTimeMillis() + 86400000))
                .build();

        when(repo.save(any(Invitation.class))).thenReturn(updatedInvitation);

        Invitation result = service.changeStatus(invitationToChange, InvitationStatus.EXPIRE);

        assertNotNull(result);
        assertEquals(InvitationStatus.EXPIRE, result.getStatus());
        verify(repo).save(invitationToChange);
    }
}
