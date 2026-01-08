package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.repository.CaseRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.ResearcherService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResearcherServiceImplementation implements ResearcherService {

    private final InvitationService invitationService;
    private final ResearcherProfileRepository repo;
    private final UsersService usersService;
    private final PasswordEncoder encoder;
    private final UsersRepository usersRepository;
    private final ResearcherProfileRepository researcherProfileRepository;
    private final CaseRepository caseRepository;
    private final ModelMapper mapper;


    @Override
    public void createResearchProfile(String token, RegisterResearcherRequestDto requestDto) throws BadRequestException, TokenExpireException {

        Invitation invitation = invitationService.getInvitationFromToken(token);
        boolean isExpire = invitation.getExpiresIn().before(new Date());

        if (invitation.getStatus().equals(InvitationStatus.EXPIRE) || isExpire) {
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

        ResearcherProfile profile = ResearcherProfile.builder()
                .experience(invitation.getExperience()).assignCaseIds(new ArrayList<>())
                .build();

        log.info("Before saving the Researcher data");
        Users savedUser = usersService.saveUsers(users);

        log.info("Users Saved, {}", savedUser);

        profile.setUser(savedUser);
        ResearcherProfile save = repo.save(profile);

        log.info("Researcher Profile saved, {}", save);

        invitationService.changeStatus(invitation, InvitationStatus.EXPIRE);

    }

    @Override
    public Page<CaseDto> getListOfAssignedCases(int page, int limit, String sortBy, String dir, String searchBy, String email) {
        // 1. get Users from email,
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        // 2. get ResearcherProfile based on Users
        ResearcherProfile profile = researcherProfileRepository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        // 3. create Pageable object
        Sort sort = null;
        if (dir == "ASC") {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        PageRequest pageRequest = PageRequest.of(page, limit, sort);

        // 4. get list of cases, where ResearcherProfile is present
        Page<Case> all = null;

        if (searchBy == null || searchBy.isEmpty()) {
            all = caseRepository.findAllByResearchers(profile, pageRequest);
        } else {
            all = caseRepository.findAllByResearchersAndCaseNameContainingIgnoreCaseOrResearchersAndClientNameContainsIgnoreCaseOrResearchersOrPracticeAreaContaining(
                    profile, searchBy, profile, searchBy, profile, searchBy, pageRequest);
        }

        // 5.  convert the Page<Case> to Page<CaseDto>
        Page<CaseDto> response = all.map(Mappers::mapCaseToCaseDto);
        return response;
    }

    @Override
    public ResearcherResponseDto getResearcherById(String id) {
        ResearcherProfile profile = researcherProfileRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        return Mappers.mapResearcherToResearcherResponseDto(profile);
    }
}
