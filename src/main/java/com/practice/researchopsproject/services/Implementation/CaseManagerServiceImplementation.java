package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.RegisterCaseManagerRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.exception.customException.ResourceNotFoundException;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.repository.CaseManagerProfileRepository;
import com.practice.researchopsproject.repository.CaseRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaseManagerServiceImplementation implements CaseManagerService {

    private final CaseRepository caseRepository;
    private final CaseManagerProfileRepository repository;
    private final UsersService usersService;
    private final InvitationService invitationService;
    private final PasswordEncoder encoder;
    private final ResearcherProfileRepository resRepo;
    private final UsersRepository usersRepository;


    @Override
    public void createCaseManager(String token, RegisterCaseManagerRequestDto requestDto) throws BadRequestException, TokenExpireException {
        Invitation invitation = invitationService.getInvitationFromToken(token);

        log.info("Invitation fetched, {}", invitation);
        boolean isExpired = new Date().after(invitation.getExpiresIn());

        if (invitation.getStatus().equals(InvitationStatus.EXPIRE) || isExpired) {
            throw new TokenExpireException(Messages.TOKEN_EXPIRE);
        }

        Users users = Users.builder()
                .email(invitation.getEmail())
                .password(encoder.encode(requestDto.getPassword()))
                .name(invitation.getName())
                .role(invitation.getRole())
                .lastLogin(LocalDateTime.now())
                .isActive(true)
                .address(invitation.getAddress())
                .state(invitation.getState())
                .city(invitation.getCity())
                .zip(invitation.getZip()).build();

        CaseManagerProfile profile = CaseManagerProfile.builder()
                .user(users).assignCaseId(new ArrayList<>())
                .build();

        log.info("Before saving the CaseManager data");

        try {

            Users savedUser = usersService.saveUsers(users);

            profile.setUser(savedUser);
            CaseManagerProfile save = repository.save(profile);
            log.info("CaseManagerProfile Saved , {}", save);

            invitationService.changeStatus(invitation, InvitationStatus.EXPIRE);
            log.info("Invitation status has been changed");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public Page<ResearcherResponseDto> getResearchers(int page, int limit) {

        // 1. create the Pageable object.
        PageRequest pageRequest = PageRequest.of(page, limit);

        // 2. get the list of the Users which have isActive as true, and Role as Role.Researchers
        Page<Users> allUsers = usersRepository.findAllByIsActiveAndRole(true, Role.RESEARCHER, pageRequest);

        // 3. get the list of Researchers with the matching Users.
        Page<ResearcherProfile> response =
                allUsers.map(users -> resRepo.findByUser(users).orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND)));

        // 4. convert to response type.
        return response.map(profile -> Mappers.mapResearcherToResearcherResponseDto(profile));

    }

    @Override
    public Case createCase(CaseDto requestDto, String email) {

        String id = createId();
        log.info("Id created is ,{}", id);

        Users user = usersRepository.findByEmail(email).get();

        CaseManagerProfile creator = repository.findByUser(user).get();
        log.info("Case Manager found By email is, {}", creator);

        List<ResearcherProfile> researcherProfiles = requestDto.getResearchers().stream()
                .map(emailId -> {
                    // First, find the Users by email
                    Users researcherUser = usersRepository.findByEmail(emailId)
                            .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

                    // Then, find the ResearcherProfile by the Users reference
                    return resRepo.findByUser(researcherUser)
                            .orElseThrow(() -> new RuntimeException("Researcher profile not found for email: " + emailId));
                })
                .toList();

        log.info("Researcher list has been fetched and created, {}", researcherProfiles);


        Case caseBuilder = Case.builder()
                .caseId(id)
                .clientName(requestDto.getClientName())
                .practiceArea(PracticeArea.valueOf(requestDto.getPracticeArea()))
                .caseName(requestDto.getCaseName())
                .currentStage(CaseStage.valueOf(requestDto.getCurrentStage()))
                .plaintiffPronounce(Pronounces.valueOf(requestDto.getPlaintiffPronounce()))
                .plaintiffName(requestDto.getPlaintiffName())
                .work(requestDto.getWork())
                .titleOfWork(requestDto.getTitleOfWork())
                .descriptionOfWork(requestDto.getDescriptionOfWork())
                .hasRegisteredDocument(requestDto.isHasRegisteredDocument())
                .copyrightRegistrationNumber(requestDto.getCopyrightRegistrationNumber())
                .copyrightRegistration(requestDto.getCopyrightRegistration())
                .dateOfFistPublish(requestDto.getDateOfFistPublish())
                .dateOfViolation(requestDto.getDateOfViolation())
                .infringementInformation(requestDto.getInfringementInformation())
                .linkToInfringement(requestDto.getLinkToInfringement())
                .CMIRemoved(requestDto.isCMIRemoved())
                .creator(creator)
                .researchers(researcherProfiles).build();

        log.info("Case has been created successfully, {}", caseBuilder);

        Case savedCase = caseRepository.save(caseBuilder);
        log.info("Case has been persisted successfully, {}", savedCase);

        creator.getAssignCaseId().add(savedCase.getId());
        repository.save(creator);

        researcherProfiles.stream()
                .forEach(profile -> {
                    profile.getAssignCaseIds().add(savedCase.getId());
                    resRepo.save(profile);
                });


        return savedCase;

    }

    @Override
    public Case editCase(CaseDto caseDto, String email, String id) throws BadRequestException, ResourceNotFoundException {

        Case fetchedCase = caseRepository.findByCaseId(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.CASE_NOT_FOUND));

        if (!fetchedCase.getCreator().getUser().getEmail().equalsIgnoreCase(email)) {
            throw new BadRequestException(Messages.CASE_NOT_FOUND);
        }

        fetchedCase.setCaseName(caseDto.getCaseName());
        fetchedCase.setCurrentStage(CaseStage.valueOf(caseDto.getCurrentStage()));
        fetchedCase.setPlaintiffPronounce(Pronounces.valueOf(caseDto.getPlaintiffPronounce()));
        fetchedCase.setPlaintiffName(caseDto.getPlaintiffName());
        fetchedCase.setWork(caseDto.getWork());
        fetchedCase.setTitleOfWork(caseDto.getTitleOfWork());
        fetchedCase.setDescriptionOfWork(caseDto.getDescriptionOfWork());
        fetchedCase.setHasRegisteredDocument(caseDto.isHasRegisteredDocument());
        fetchedCase.setCopyrightRegistrationNumber(caseDto.getCopyrightRegistrationNumber());
        fetchedCase.setCopyrightRegistration(caseDto.getCopyrightRegistration());
        fetchedCase.setDateOfFistPublish(caseDto.getDateOfFistPublish());
        fetchedCase.setDateOfViolation(caseDto.getDateOfViolation());
        fetchedCase.setInfringementInformation(caseDto.getInfringementInformation());
        fetchedCase.setLinkToInfringement(caseDto.getLinkToInfringement());
        fetchedCase.setCMIRemoved(caseDto.isCMIRemoved());

        return caseRepository.save(fetchedCase);
    }

    @Override
    public CaseManagerResponseDto getCaseManagerById(String id) {
        CaseManagerProfile caseManager = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        return Mappers.mapCaseManagerToCaseManagerResponseDto(caseManager);
    }

    @Override
    public CaseDto editCaseByAdmin(CaseDto caseDto, String id) throws CaseNotFoundException {
        Case fetchedCase = caseRepository.findByCaseId(id)
                .orElseThrow(() -> new CaseNotFoundException(Messages.CASE_NOT_FOUND));

        fetchedCase.setCaseName(caseDto.getCaseName());
        fetchedCase.setCurrentStage(CaseStage.valueOf(caseDto.getCurrentStage()));
        fetchedCase.setPlaintiffPronounce(Pronounces.valueOf(caseDto.getPlaintiffPronounce()));
        fetchedCase.setPlaintiffName(caseDto.getPlaintiffName());
        fetchedCase.setWork(caseDto.getWork());
        fetchedCase.setTitleOfWork(caseDto.getTitleOfWork());
        fetchedCase.setDescriptionOfWork(caseDto.getDescriptionOfWork());
        fetchedCase.setHasRegisteredDocument(caseDto.isHasRegisteredDocument());
        fetchedCase.setCopyrightRegistrationNumber(caseDto.getCopyrightRegistrationNumber());
        fetchedCase.setCopyrightRegistration(caseDto.getCopyrightRegistration());
        fetchedCase.setDateOfFistPublish(caseDto.getDateOfFistPublish());
        fetchedCase.setDateOfViolation(caseDto.getDateOfViolation());
        fetchedCase.setInfringementInformation(caseDto.getInfringementInformation());
        fetchedCase.setLinkToInfringement(caseDto.getLinkToInfringement());
        fetchedCase.setCMIRemoved(caseDto.isCMIRemoved());

        return Mappers.mapCaseToCaseDto(caseRepository.save(fetchedCase));
    }

    private String createId() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonthValue());
        String day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        long count = caseRepository.count();

        String id = "CC" + year + month + day + count;
        count++;
        return id;
    }
}
