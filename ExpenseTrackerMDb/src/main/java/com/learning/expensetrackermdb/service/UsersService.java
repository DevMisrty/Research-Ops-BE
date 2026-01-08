package com.learning.expensetrackermdb.service;

import com.learning.expensetrackermdb.dto.request.UsersRequestDto;
import com.learning.expensetrackermdb.dto.request.UsersUpdateRequestDto;
import com.learning.expensetrackermdb.dto.response.UsersResponseDto;
import com.learning.expensetrackermdb.entity.Users;
import com.learning.expensetrackermdb.exception.customexception.UserAlreadyExistsException;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;
import org.apache.coyote.BadRequestException;

import java.util.Optional;

public interface UsersService {
    UsersResponseDto addNewUsers(UsersRequestDto usersRequestDto) throws UserAlreadyExistsException;

    void deleteUser(String email) throws BadRequestException;


    UsersResponseDto updateUser(UsersUpdateRequestDto usersRequestDto) throws UserNotFoundException;

    boolean checkUserExists(String email);

    Optional<Users> findUsersByEmail(String username);
}
