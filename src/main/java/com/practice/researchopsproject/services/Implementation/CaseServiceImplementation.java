package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.repository.*;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaseServiceImplementation implements CaseService {

    private final CaseRepository caseRepository;
    private final UsersRepository usersRepository;
    private final CaseManagerProfileRepository caseManagerProfileRepository;
    private final TempCaseRepository tempCaseRepository;
    private final AvailCaseIdRepository availCaseIdRepository;
    private final JwtUtilities jwtUtilities;
    private final ResearcherProfileRepository researcherProfileRepository;


    @Override
    public Page<CaseResponseDto> getListOfCases(int page, int limit, String sortBy, String dir, String searchBy) {

        Sort sort = null;
        if(dir.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else {
            sort = Sort.by(sortBy).descending();
        }

        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);

        Page<Case> all = null;

        if(searchBy == null || searchBy.isEmpty()){
            all = caseRepository.findAllByIsValidTrue(pageRequest);
        }else {
            all = caseRepository
                    .findByIsValidTrueAndCaseNameContainingOrIsValidTrueAndClientNameContainingOrIsValidTrueAndPracticeAreaContaining
                            (searchBy, searchBy, searchBy, pageRequest);
        }

        Page<CaseResponseDto> response = all.map(aCase -> Mappers.mapCaseToCaseResponseDto(aCase));

        return response;
    }

    @Override
    public Page<CaseResponseDto> getListOfCasesByCreator(int page, int limit, String sortBy, String dir, String searchBy, String email) {
        // 1. get Users according to email,
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        // 2. get Case.java Manager according to the User,
        CaseManagerProfile caseManager = caseManagerProfileRepository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        // 3. create a Pageable object,
        Sort sort = null;
        if(dir.equals("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else {
            sort = Sort.by(sortBy).descending();
        }

        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);

        // 4. get the List of Cases with this Case.java Manager, and also apply the pagination
        Page<Case> allByCreator = null;

        if(searchBy == null || searchBy.isEmpty()){
            allByCreator = caseRepository.findAllByCreatorAndIsValidTrue(caseManager, pageRequest);
        }else {
            allByCreator = caseRepository
                    .findAllByCreatorAndCaseNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndClientNameContainingIgnoreCaseAndIsValidTrueOrCreatorAndPracticeAreaAndIsValidTrue(
                            caseManager, searchBy, caseManager, searchBy, caseManager, searchBy, pageRequest);
        }

        // 5. Map the Page<Case.java> to Page<CaseDto> using model mapper.
        Page<CaseResponseDto> response = allByCreator.map(aCase -> Mappers.mapCaseToCaseResponseDto(aCase));

        return response;
    }

    @Override
    public CaseDto getCaseById(String id) throws CaseNotFoundException {
        Case aCase = caseRepository.findByCaseId(id)
                .orElseThrow(() -> new CaseNotFoundException(Messages.CASE_NOT_FOUND));
        return Mappers.mapCaseToCaseDto(aCase);
    }

    @Override
    public TempCase createTempCase(HttpServletRequest request) throws CaseNotFoundException {

        Optional<TempCase> aCase = tempCaseRepository.findFirstByIsCompleteFalseOrderByCreatedAtAsc();
        TempCase res = null;
        if(aCase.isEmpty()){
            TempCase tempCase = TempCase.builder()
                    .caseId(createId()).isComplete(false).build();
            res = tempCaseRepository.save(tempCase);
        }else{
            res = aCase.get();
            res.setCreatedAt(LocalDateTime.now());
        }
        return res;
    }

    @Override
    public CaseDto saveCase(CaseDto requestDto, String email) {
        String id = requestDto.getCaseId();
        log.info("Id created is ,{}", id);

        Users user = usersRepository.findByEmail(email).get();

        CaseManagerProfile creator = caseManagerProfileRepository.findByUser(user).get();
        log.info("Case.java Manager found By email is, {}", creator);

        List<ResearcherProfile> researcherProfiles = requestDto.getResearchers().stream()
                .map(emailId -> {
                    // First, find the Users by email
                    Users researcherUser = usersRepository.findByEmail(emailId)
                            .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

                    // Then, find the ResearcherProfile by the Users reference
                    return researcherProfileRepository.findByUser(researcherUser)
                            .orElseThrow(() -> new RuntimeException("Researcher profile not found for email: " + emailId));
                })
                .toList();

        log.info("Researcher list has been fetched and created, {}", researcherProfiles);

        Case.CaseBuilder caseBuilder = Case.builder();
        caseBuilder.caseId(id);
        caseBuilder.creator(creator);
        if (requestDto.getClientName() != null && !requestDto.getClientName().isEmpty()) {
            caseBuilder.clientName(requestDto.getClientName());
        }
        if (requestDto.getCaseName() != null && !requestDto.getCaseName().isEmpty()) {
            caseBuilder.caseName(requestDto.getCaseName());
        }

        if (requestDto.getPlaintiffName() != null && !requestDto.getPlaintiffName().isEmpty()) {
            caseBuilder.plaintiffName(requestDto.getPlaintiffName());
        }

        if (requestDto.getWork() != null && !requestDto.getWork().isEmpty()) {
            caseBuilder.work(requestDto.getWork());
        }

        if (requestDto.getTitleOfWork() != null && !requestDto.getTitleOfWork().isEmpty()) {
            caseBuilder.titleOfWork(requestDto.getTitleOfWork());
        }

        if (requestDto.getDescriptionOfWork() != null && !requestDto.getDescriptionOfWork().isEmpty()) {
            caseBuilder.descriptionOfWork(requestDto.getDescriptionOfWork());
        }

        if (requestDto.getCopyrightRegistrationNumber() != null &&
                !requestDto.getCopyrightRegistrationNumber().isEmpty()) {
            caseBuilder.copyrightRegistrationNumber(
                    requestDto.getCopyrightRegistrationNumber()
            );
        }

        if (requestDto.getInfringementInformation() != null &&
                !requestDto.getInfringementInformation().isEmpty()) {
            caseBuilder.infringementInformation(
                    requestDto.getInfringementInformation()
            );
        }

        if (requestDto.getLinkToInfringement() != null &&
                !requestDto.getLinkToInfringement().isEmpty()) {
            caseBuilder.linkToInfringement(
                    requestDto.getLinkToInfringement()
            );
        }

        if (requestDto.getPracticeArea() != null &&
                !requestDto.getPracticeArea().isEmpty()) {
            try {
                caseBuilder.practiceArea(
                        PracticeArea.valueOf(requestDto.getPracticeArea())
                );
            } catch (IllegalArgumentException ignored) {}
        }

        if (requestDto.getCurrentStage() != null &&
                !requestDto.getCurrentStage().isEmpty()) {
            try {
                caseBuilder.currentStage(
                        CaseStage.valueOf(requestDto.getCurrentStage())
                );
            } catch (IllegalArgumentException ignored) {}
        }

        if (requestDto.getPlaintiffPronounce() != null &&
                !requestDto.getPlaintiffPronounce().isEmpty()) {
            try {
                caseBuilder.plaintiffPronounce(
                        Pronounces.valueOf(requestDto.getPlaintiffPronounce())
                );
            } catch (IllegalArgumentException ignored) {}
        }

        if (requestDto.getDateOfFistPublish() != null) {
            caseBuilder.dateOfFistPublish(requestDto.getDateOfFistPublish());
        }

        if (requestDto.getDateOfViolation() != null) {
            caseBuilder.dateOfViolation(requestDto.getDateOfViolation());
        }

        if (requestDto.getCopyrightRegistration() != null) {
            caseBuilder.copyrightRegistration(
                    requestDto.getCopyrightRegistration()
            );
        }

        caseBuilder.hasRegisteredDocument(requestDto.isHasRegisteredDocument());
        caseBuilder.CMIRemoved(requestDto.isCMIRemoved());
        caseBuilder.isValid(true);

        if (researcherProfiles != null && !researcherProfiles.isEmpty()) {
            caseBuilder.researchers(new HashSet<>(researcherProfiles));
        }

        log.info("Case.java has been created successfully, {}", caseBuilder);

        Case savedCase = caseRepository.save(caseBuilder.build());
        log.info("Case.java has been persisted successfully, {}", savedCase);

        TempCase tempCase = tempCaseRepository.findByCaseId(savedCase.getCaseId()).get();
        tempCase.setComplete(true);
        tempCaseRepository.save(tempCase);

        creator.getAssignCaseId().add(savedCase.getId());
        caseManagerProfileRepository.save(creator);

        researcherProfiles.stream()
                .forEach(profile -> {
                    profile.getAssignCaseIds().add(savedCase.getId());
                    researcherProfileRepository.save(profile);
                });
        return Mappers.mapCaseToCaseDto(savedCase);
    }

    private String createId() {
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.valueOf(LocalDateTime.now().getMonthValue());
        String day = String.valueOf(LocalDateTime.now().getDayOfMonth());
        long count = tempCaseRepository.count();

        String id = "CC" + year + month + day + count;
        return id;
    }
}
