package com.practice.researchopsproject.services;

import com.practice.researchopsproject.dto.PasswordResetTokenDto;
import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.request.ResetPasswordRequestDto;
import com.practice.researchopsproject.dto.request.UserRequestDto;
import com.practice.researchopsproject.dto.response.CaseManagerResponseDto;
import com.practice.researchopsproject.dto.response.ResearcherResponseDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.Users;
import com.practice.researchopsproject.exception.customException.TokenExpireException;
import com.practice.researchopsproject.exception.customException.TokenNotFoundException;
import com.practice.researchopsproject.exception.customException.UserNameAlreadyTaken;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

public interface UsersService {
    UserResponseDto saveUsers(UserRequestDto requestDto) throws UserNameAlreadyTaken;

    Users saveUsers(Users users);

    UserDto getUserByEmail(String email);

    Page<CaseManagerResponseDto>
    getListOfCaseManager(Integer page, Integer limit, String sort, String direction,String searchBy);

    Page<ResearcherResponseDto> getListofResearcher
            (Integer page, Integer limit, String sortBy, String direction, String searchBy);

    boolean checkProfileIsActive(String email);

    void changePassword(String email, String password);

    void forgotPasswordMail(String email);

    PasswordResetTokenDto fetchResetPasswordToken(String token) throws BadRequestException;

    void setResetPassword(ResetPasswordRequestDto requestDto)
            throws TokenNotFoundException, TokenExpireException, BadRequestException;
}
