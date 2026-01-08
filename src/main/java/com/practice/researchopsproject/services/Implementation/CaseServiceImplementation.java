package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.entity.Case;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.Users;
import com.practice.researchopsproject.exception.customException.CaseNotFoundException;
import com.practice.researchopsproject.repository.CaseManagerProfileRepository;
import com.practice.researchopsproject.repository.CaseRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.CaseService;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaseServiceImplementation implements CaseService {

    private final CaseRepository caseRepository;
    private final UsersRepository usersRepository;
    private final CaseManagerProfileRepository caseManagerProfileRepository;



    @Override
    public Page<CaseDto> getListOfCases(int page, int limit, String sortBy, String dir, String searchBy) {

        Sort sort = null;
        if(dir.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else {
            sort = Sort.by(sortBy).descending();
        }

        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        Page<Case> all = null;

        if(searchBy == null || searchBy.isEmpty()){
            all = caseRepository.findAll(pageRequest);
        }else {
            all = caseRepository
                    .findAllByCaseNameContainingOrClientNameContainingOrPracticeAreaContaining(searchBy, searchBy, searchBy, pageRequest);
        }

        Page<CaseDto> response = all.map(aCase -> Mappers.mapCaseToCaseDto(aCase));

        return response;
    }

    @Override
    public Page<CaseDto> getListOfCasesByCreator(int page, int limit, String sortBy, String dir, String searchBy, String email) {
        // 1. get Users according to email,
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        // 2. get Case Manager according to the User,
        CaseManagerProfile caseManager = caseManagerProfileRepository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND));

        // 3. create a Pageable object,
        Sort sort = null;
        if(dir.equals("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else {
            sort = Sort.by(sortBy).descending();
        }

        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        // 4. get the List of Cases with this Case Manager, and also apply the pagination
        Page<Case> allByCreator = null;

        if(searchBy == null || searchBy.isEmpty()){
            allByCreator = caseRepository.findAllByCreator(caseManager, pageRequest);
        }else {
            allByCreator = caseRepository
                    .findAllByCreatorAndCaseNameContainingIgnoreCaseOrCreatorAndClientNameContainingIgnoreCaseOrCreatorAndPracticeArea(
                            caseManager, searchBy, caseManager, searchBy, caseManager, searchBy, pageRequest);
        }

        // 5. Map the Page<Case> to Page<CaseDto> using model mapper.
        Page<CaseDto> response = allByCreator.map(aCase -> Mappers.mapCaseToCaseDto(aCase));

        return response;
    }

    @Override
    public CaseDto getCaseById(String id) throws CaseNotFoundException {
        Case aCase = caseRepository.findById(id)
                .orElseThrow(() -> new CaseNotFoundException(Messages.CASE_NOT_FOUND));
        return Mappers.mapCaseToCaseDto(aCase);
    }
}
