package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.PasswordResetTokenDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.ResetPasswordRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.*;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.exception.customException.TokenNotFoundException;
import com.practice.researchopsproject.exception.customException.UserNameAlreadyTaken;
import com.practice.researchopsproject.repository.CaseManagerProfileRepository;
import com.practice.researchopsproject.repository.PasswordResetTokenRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.MailUtilities;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImplementation implements UsersService {

    private final UsersRepository repo;
    private final CaseManagerProfileRepository caseRepo;
    private final ResearcherProfileRepository resRepo;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;
    private final MailUtilities mailUtilities;

    @Value("${frontend.baseurl}")
    private String BASEURL;

    //role must be admin
    @Override
    public UserResponseDto saveUsers(UserRequestDto requestDto) throws UserNameAlreadyTaken {

        UserResponseDto response =null;

        if(repo.existsByEmail(requestDto.getEmail())){
            throw new UserNameAlreadyTaken(Messages.USER_ALREADY_EXIST);
        }

        try{
            Users users = mapper.map(requestDto, Users.class);
            users.setRole(Role.ADMIN);
            users.setPassword(encoder.encode(users.getPassword()));
            log.info("Users info, {} ", users);
            users.setActive(true);

            Users savedUser = repo.save(users);

            log.info("Saved User is, {}", savedUser);
            response = mapper.map(savedUser, UserResponseDto.class);
            log.info("Map changed , {}", response);
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return response;
    }

    @Override
    public Users saveUsers(Users users) {
         return repo.save(users);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Users users = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));
        return mapper.map(users, UserDto.class);
    }

    // for case manager
    @Override
    public Page<CaseManagerResponseDto> getListOfCaseManager(Integer page, Integer limit, String sortBy, String direction, String searchBy) {

        // 1. create the pageable object,
        Sort sort = null;
        if(direction.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else {
            sort = Sort.by(sortBy).descending();
        }
        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);

        // 2. get the list of CaseManager based on searchBy keyword.
        Page<CaseManagerProfile> all = null;

        if(searchBy == null || searchBy.isEmpty()){
            all = caseRepo.findAll(pageRequest);
        }else {
            Page<Users> allUsers = repo.findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                    Role.CASE_MANAGER, searchBy, Role.CASE_MANAGER, searchBy, pageRequest);

            all = allUsers.map( users->  caseRepo.findByUser(users).orElseThrow(() -> new UsernameNotFoundException(Messages.CASEMANAGER_NOT_FOUND)));
        }

        // 3. convert Page<CaseManager> to Page<CaseManagerResponseDto>.
        Page<CaseManagerResponseDto> response =
                all.map( profile -> Mappers.mapCaseManagerToCaseManagerResponseDto(profile));

        return response;
    }

    @Override
    public Page<ResearcherResponseDto> getListofResearcher(Integer page, Integer limit, String sortBy, String direction, String searchBy) {

        // 1. create the Pageable object
        Sort sort = null;
        if(direction.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else {
            sort = Sort.by(sortBy).descending();
        }
        PageRequest pageRequest = PageRequest.of(page-1, limit, sort);

        // 2. get the list of Researcher, based on the Search keyword.
        Page<ResearcherProfile> all = null;
        if(searchBy == null || searchBy.isEmpty()){
            all = resRepo.findAll(pageRequest);
        }else{
            Page<Users> allUsers = repo.findAllByRoleAndNameContainingIgnoreCaseOrRoleAndEmailContainingIgnoreCase(
                    Role.RESEARCHER, searchBy, Role.RESEARCHER, searchBy, pageRequest);

            all = allUsers.map( users -> resRepo.findByUser(users).orElseThrow(()-> new UsernameNotFoundException(Messages.RESEARCHER_NOT_FOUND)));
        }

        // 3. convert the response.
        Page<ResearcherResponseDto> response = all
                .map( profile -> Mappers.mapResearcherToResearcherResponseDto(profile) );
        return response;
    }

    @Override
    public boolean checkProfileIsActive(String email) {

        Users users = repo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        return users.isActive();

    }

    @Override
    public void changePassword(String email, String password) {
        Users users = repo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        String encodedPassword = encoder.encode(password);

        users.setPassword(encodedPassword);
        repo.save(users);
    }

    @Override
    public void forgotPasswordMail(String email) throws UsernameNotFoundException {
        if (!repo.existsByEmail(email)) {
            throw new UsernameNotFoundException(Messages.USER_NOT_FOUND);
        }

        PasswordResetToken token = PasswordResetToken.builder()
                .token(UUID.randomUUID().toString())
                .email(email)
                .expiresAt(LocalDateTime.now().plusMinutes(60))
                .isUsed(false)
                .build();

        passwordResetTokenRepository.save(token);
        String url = BASEURL+"reset-password/"+token.getToken();

        mailUtilities.sendMail(email,"Reset Password Link, expires in 60 mintues.", url);
    }

    @Override
    public PasswordResetTokenDto fetchResetPasswordToken(String token) throws BadRequestException {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(()-> new BadRequestException(Messages.INVALID_TOKEN));

        return mapper.map(passwordResetToken, PasswordResetTokenDto.class);
    }

    @Override
    public void setResetPassword(ResetPasswordRequestDto requestDto)
            throws TokenNotFoundException, TokenExpireException, BadRequestException {
        if(!requestDto.getPassword().equals(requestDto.getConfirmPassword())){
            throw new BadRequestException(Messages.PASSWORD_NOT_MATCH);
        }

        PasswordResetToken token = passwordResetTokenRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new TokenNotFoundException(Messages.INVALID_TOKEN));

        if(token.isUsed() || token.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new TokenExpireException(Messages.TOKEN_EXPIRE);
        }

        Users profile = repo.findByEmail(token.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        profile.setPassword(encoder.encode(requestDto.getPassword()));
        Users savedProfile = repo.save(profile);

        token.setUsed(true);
        passwordResetTokenRepository.save(token);

        mailUtilities.sendMail(savedProfile.getEmail(), "Account Password has been chnaged.", savedProfile.getEmail());
    }


}
