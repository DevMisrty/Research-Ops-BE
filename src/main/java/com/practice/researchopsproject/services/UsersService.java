package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.PaginationResponseDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.CreateUserRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.Users;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;

public interface UsersService {
    UserResponseDto saveUsers(UserRequestDto requestDto);

    Users saveUsers(Users users);

    UserDto getUserByEmail(String email);

    Page<CaseManagerResponseDto> getListOfCaseManager(Integer page, Integer limit, String sort, String direction,String searchBy);

    Page<ResearcherResponseDto> getListofResearcher(Integer page, Integer limit, String sortBy, String direction, String searchBy);

    UserResponseDto activateUserProfile(String id);

    UserResponseDto deactivateUserProfile(String id);

    boolean checkProfileIsActive(String email);
}
