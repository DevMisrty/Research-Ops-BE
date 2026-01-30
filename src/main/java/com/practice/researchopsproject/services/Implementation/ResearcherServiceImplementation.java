package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.CaseDto;
import com.practice.researchopsproject.dto.request.EditResearcherDto;
import com.practice.researchopsproject.dto.request.RegisterResearcherRequestDto;
import com.practice.researchopsproject.dto.response.CaseResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.repository.CaseRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.InvitationService;
import com.practice.researchopsproject.services.ResearcherService;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.JwtUtilities;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.bson.types.ObjectId;
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
    private final JwtUtilities jwtUtilities;


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
                .experience(invitation.getExperience())
                .assignCaseIds(new ArrayList<>())
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
    public Page<CaseResponseDto> getListOfAssignedCases(int page, int limit, String sortBy, String dir, String searchBy, String email) {
        // 1. get Users from email,
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        // 2. get ResearcherProfile based on Users
        ResearcherProfile profile = researcherProfileRepository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        // 3. create Pageable object
        Sort sort = null;
        if (dir.equals("ASC")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }
        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);

        // 4. get list of cases, where ResearcherProfile is present
        Page<Case> all = null;

        if (searchBy == null || searchBy.isEmpty()) {
            all = caseRepository
                    .findAllByResearchersAndIsValidTrue(profile, pageRequest);
        } else {
            all = caseRepository
                    .searchByResearcherAndKeyword(
                    new ObjectId(profile.getId()), searchBy,pageRequest
            );
        }


        // 5.  convert the Page<Case.java> to Page<CaseDto>
        Page<CaseResponseDto> response = all.map(Mappers::mapCaseToCaseResponseDto);
        return response;
    }

    @Override
    public ResearcherResponseDto getResearcherById(String id) {
        ResearcherProfile profile = researcherProfileRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        return Mappers.mapResearcherToResearcherResponseDto(profile);
    }

    @Override
    public void editResearcher(EditResearcherDto requestDto, String id) {
        // 1. Get the researcher based on the id,
        ResearcherProfile profile = researcherProfileRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        // 2. get the User based on the researcher profile fetched,
        Users users = usersRepository.findByEmail(profile.getUser().getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        // 3. edit the user, and save it,
        users.setName(requestDto.getName());
        users.setAddress(requestDto.getAddress());
        users.setState(requestDto.getState());
        users.setCity(requestDto.getCity());
        users.setZip(Long.valueOf(requestDto.getZip()));
        users.setActive(requestDto.isActive());

        Users savedUsers = usersRepository.save(users);

        // 4. edit the researcher, and set the saved user to researcher, and save it.
        profile.setUser(savedUsers);
        profile.setExperience(Integer.valueOf(requestDto.getExperience()));

        ResearcherProfile savedProfile = researcherProfileRepository.save(profile);
    }

    @Override
    public void editMyProfile(EditResearcherDto researcherDto, HttpServletRequest request) throws BadRequestException {
        String token = request.getHeader("Authorization").substring(7);
        String email = jwtUtilities.getEmailFromToken(token);

        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        ResearcherProfile profile = researcherProfileRepository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        editResearcher(researcherDto, profile.getId());
    }

    @Override
    public ResearcherResponseDto getResearcherByEmail(String email) {
        // 1. get the user based on email.
        Users users = usersRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        // 2. get the researcher based on user
        ResearcherProfile profile = researcherProfileRepository.findByUser(users)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND));

        // 3. return the response
        return Mappers.mapResearcherToResearcherResponseDto(profile);
    }
}
