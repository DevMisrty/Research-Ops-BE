package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.EditCaseManagerDto;
import com.practice.researchopsproject.dto.request.RegisterCaseManagerRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.exception.customException.ResourceNotFoundException;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.repository.*;
import com.practice.researchopsproject.services.CaseManagerService;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
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
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaseManagerServiceImplementation implements CaseManagerService {

    private final CaseRepository caseRepository;
    private final PasswordEncoder encoder;
    private final CaseManagerProfileRepository repository;
    private final UsersService usersService;
    private final InvitationService invitationService;
    private final ResearcherProfileRepository resRepo;
    private final UsersRepository usersRepository;
    private final JwtUtilities jwtUtilities;
    private final AvailCaseIdRepository availCaseIdRepository;


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
                .zip(invitation.getZip())
                .fileName(invitation.getFileName())
                .build();

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
        log.info("Case.java Manager found By email is, {}", creator);

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
                .researchers(new HashSet<>(researcherProfiles)).build();

        log.info("Case.java has been created successfully, {}", caseBuilder);

        Case savedCase = caseRepository.save(caseBuilder);
        log.info("Case.java has been persisted successfully, {}", savedCase);

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
    public String createEmptyCase(String email) {
        // 1. get the Users based on email,
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        // 2. get the Casemanager profile,
        CaseManagerProfile profile = repository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        // 3. set the caseId, and creator based on the CaseManager,
        Optional<AvailCaseId> availCaseId = availCaseIdRepository.findFirstByOrderByCaseIdAsc();
        String id = "";
        if(!availCaseId.isEmpty()){
            id = availCaseId.get().getCaseId();
            availCaseIdRepository.deleteAvailCaseIdByCaseId(id);
        }else{
            id = createId();
        }

        Case aCase = Case.builder()
                .caseId(id)
                .creator(profile)
                .build();

        // 4. save the case,
        Case savedCase = caseRepository.save(aCase);

        // 5. Add the id inside the AvailCaseId
        availCaseIdRepository.save(AvailCaseId.builder().caseId(id).build());

        // 6. return caseId
        return savedCase.getCaseId();
    }

    @Override
    public CaseManagerProfile getCasMangerProfileByEmail(String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        CaseManagerProfile caseManagerProfile = repository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        return caseManagerProfile;
    }

    @Override
    public void editCaseManagerProfile(EditCaseManagerDto requestDto, String email) {
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        CaseManagerProfile caseManagerProfile = repository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        editCaseManager(requestDto, caseManagerProfile.getId());
    }


    @Override
    public Case editCase(CaseDto caseDto, String email, String id) throws BadRequestException, ResourceNotFoundException {

        Case fetchedCase = caseRepository.findByCaseId(id)
                .orElseThrow(() -> new ResourceNotFoundException(Messages.CASE_NOT_FOUND));

        if (!fetchedCase.getCreator().getUser().getEmail().equalsIgnoreCase(email)) {
            throw new BadRequestException(Messages.CASE_NOT_FOUND);
        }

        boolean isResearcher = false;
        List<ResearcherProfile> researcherProfiles = null;

        if(caseDto.getResearchers() != null) {
            isResearcher = true;
             researcherProfiles = caseDto.getResearchers().stream()
                    .map(emailId -> {
                        // First, find the Users by email
                        Users researcherUser = usersRepository.findByEmail(emailId)
                                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

                        // Then, find the ResearcherProfile by the Users reference
                        return resRepo.findByUser(researcherUser)
                                .orElseThrow(() -> new RuntimeException("Researcher profile not found for email: " + emailId));
                    })
                    .toList();
        }

        if(fetchedCase.getResearchers()== null || fetchedCase.getResearchers().isEmpty()){
            fetchedCase.setResearchers(new HashSet<>());
        }

        if (caseDto.getCaseName() != null && !caseDto.getCaseName().isEmpty())
            fetchedCase.setCaseName(caseDto.getCaseName());

        if (caseDto.getClientName() != null && !caseDto.getClientName().isEmpty())
            fetchedCase.setClientName(caseDto.getClientName());

        if (caseDto.getPracticeArea() != null && !caseDto.getPracticeArea().isEmpty())
            fetchedCase.setPracticeArea(PracticeArea.valueOf(caseDto.getPracticeArea()));

        if (caseDto.getCurrentStage() != null && !caseDto.getCurrentStage().isEmpty())
            fetchedCase.setCurrentStage(CaseStage.valueOf(caseDto.getCurrentStage()));

        if (caseDto.getPlaintiffPronounce() != null && !caseDto.getPlaintiffPronounce().isEmpty())
            fetchedCase.setPlaintiffPronounce(Pronounces.valueOf(caseDto.getPlaintiffPronounce()));

        if (caseDto.getPlaintiffName() != null && !caseDto.getPlaintiffName().isEmpty()) {
            fetchedCase.setPlaintiffName(caseDto.getPlaintiffName());
        }

        if (caseDto.getWork() != null && !caseDto.getWork().isEmpty()) {
            fetchedCase.setWork(caseDto.getWork());
        }

        if (caseDto.getTitleOfWork() != null && !caseDto.getTitleOfWork().isEmpty()) {
            fetchedCase.setTitleOfWork(caseDto.getTitleOfWork());
        }

        if (caseDto.getDescriptionOfWork() != null && !caseDto.getDescriptionOfWork().isEmpty()) {
            fetchedCase.setDescriptionOfWork(caseDto.getDescriptionOfWork());
        }
        if (caseDto.getDateOfFistPublish() != null) {
            fetchedCase.setDateOfFistPublish(caseDto.getDateOfFistPublish());
        }
        fetchedCase.setHasRegisteredDocument(caseDto.isHasRegisteredDocument());
        if(caseDto.getCopyrightRegistrationNumber() != null && !caseDto.getCopyrightRegistrationNumber().isEmpty())
            fetchedCase.setCopyrightRegistrationNumber(caseDto.getCopyrightRegistrationNumber());

        if(caseDto.getCopyrightRegistration() != null)
            fetchedCase.setCopyrightRegistration(caseDto.getCopyrightRegistration());

        if(caseDto.getLinkToInfringement() != null && !caseDto.getLinkToInfringement().isEmpty())
            fetchedCase.setLinkToInfringement(caseDto.getLinkToInfringement());

        if(caseDto.getInfringementInformation() != null && !caseDto.getInfringementInformation().isEmpty())
            fetchedCase.setInfringementInformation(caseDto.getInfringementInformation());

        fetchedCase.setCMIRemoved(caseDto.isCMIRemoved());

        if (caseDto.getDateOfViolation() != null) {
            fetchedCase.setDateOfViolation(caseDto.getDateOfViolation());
        }

        if (caseDto.getAdditionalCaseInformation() != null) {
            fetchedCase.setAdditionalCaseInformation(
                    caseDto.getAdditionalCaseInformation()
            );
        }

        if (caseDto.getDefendantDetails() != null) {
            fetchedCase.setDefendantDetails(
                    caseDto.getDefendantDetails()
            );
        }

        if (caseDto.getLitigationInformation() != null) {
            fetchedCase.setLitigationInformation(
                    caseDto.getLitigationInformation()
            );
        }

        if (caseDto.getBillingInfo() != null) {
            fetchedCase.setBillingInfo(
                    caseDto.getBillingInfo()
            );
        }
        if(isResearcher){
            for(ResearcherProfile profile : researcherProfiles){
                fetchedCase.getResearchers().add(profile);
            }
        }

        fetchedCase.setValid(true);
        Case savedCase = caseRepository.save(fetchedCase);
        availCaseIdRepository.deleteAvailCaseIdByCaseId(savedCase.getCaseId());

        if(isResearcher){
            researcherProfiles.stream()
                    .forEach(profile -> {
                        profile.getAssignCaseIds().add(savedCase.getId());
                        resRepo.save(profile);
                    });
        }

        return savedCase;
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

    @Override
    public CaseManagerResponseDto editCaseManager(EditCaseManagerDto requestDto, String id) {
        CaseManagerProfile profile = repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        Users users = usersRepository.findByEmail(profile.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        users.setActive(requestDto.isActive());
        users.setName(requestDto.getName());
        users.setCity(requestDto.getCity());
        users.setState(requestDto.getState());
        users.setAddress(requestDto.getAddress());
        users.setZip(Long.valueOf(requestDto.getZip()));

        Users savedUser = usersRepository.save(users);
        profile.setUser(savedUser);

        CaseManagerProfile savedProfile = repository.save(profile);

        return Mappers.mapCaseManagerToCaseManagerResponseDto(savedProfile);

    }

    @Override
    public void editMyProfile(EditCaseManagerDto requestDto, HttpServletRequest request) throws BadRequestException {
        String token = request.getHeader("Authorization").substring(7);

        String email = jwtUtilities.getEmailFromToken(token);

        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        CaseManagerProfile profile = repository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        users.setActive(requestDto.isActive());
        users.setName(requestDto.getName());
        users.setCity(requestDto.getCity());
        users.setState(requestDto.getState());
        users.setAddress(requestDto.getAddress());
        users.setZip(Long.valueOf(requestDto.getZip()));

        Users savedUser = usersRepository.save(users);
        profile.setUser(savedUser);

        CaseManagerProfile savedProfile = repository.save(profile);
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
