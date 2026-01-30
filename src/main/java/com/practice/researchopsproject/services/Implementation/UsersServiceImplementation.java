package com.practice.researchopsproject.services.Implementation;

import com.practice.researchopsproject.dto.PaginationResponseDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.CaseManagerProfile;
import com.practice.researchopsproject.entity.ResearcherProfile;
import com.practice.researchopsproject.entity.Role;
import com.practice.researchopsproject.entity.Users;
import com.practice.researchopsproject.repository.CaseManagerProfileRepository;
import com.practice.researchopsproject.repository.ResearcherProfileRepository;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.services.UsersService;
import com.practice.researchopsproject.utilities.Mappers;
import com.practice.researchopsproject.utilities.Messages;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImplementation implements UsersService {

    private final UsersRepository repo;
    private final CaseManagerProfileRepository caseRepo;
    private final ResearcherProfileRepository resRepo;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

    //role must be admin
    @Override
    public UserResponseDto saveUsers(UserRequestDto requestDto) {

        UserResponseDto response =null;

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
    public UserResponseDto activateUserProfile(String id) {

        Users users = repo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        if(users.getRole()==Role.ADMIN){
            throw new InvalidRequestStateException(Messages.CANT_PERFROM_ON_ADMIN);
        }
        if(users.isActive())return mapper.map(users, UserResponseDto.class);

        users.setActive(true);
        Users savedusers = repo.save(users);
        UserResponseDto response = mapper.map(users, UserResponseDto.class);
        return response;

    }

    @Override
    public UserResponseDto deactivateUserProfile(String id) {
        Users users = repo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        if(users.getRole()==Role.ADMIN){
            throw new InvalidRequestStateException(Messages.CANT_PERFROM_ON_ADMIN);
        }
        if(!users.isActive())return mapper.map(users, UserResponseDto.class);

        users.setActive(false);
        Users savedUser = repo.save(users);
        UserResponseDto response = mapper.map(savedUser, UserResponseDto.class);
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


}
