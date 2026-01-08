package com.learning.expensetrackermdb.controller;

import com.learning.expensetrackermdb.dto.request.UsersRequestDto;
import com.learning.expensetrackermdb.dto.request.UsersUpdateRequestDto;
import com.learning.expensetrackermdb.dto.response.UsersResponseDto;
import com.learning.expensetrackermdb.exception.customexception.IncorrectInputValueException;
import com.learning.expensetrackermdb.exception.customexception.UserAlreadyExistsException;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;
import com.learning.expensetrackermdb.jwt.JWTUtility;
import com.learning.expensetrackermdb.service.UsersService;
import com.learning.expensetrackermdb.utility.ApiResponse;
import com.learning.expensetrackermdb.utility.MessageConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;
    private final JWTUtility utility;

    @PostMapping("/add-user")
    public ResponseEntity<?> addNewUser(@RequestBody UsersRequestDto usersRequestDto) throws IncorrectInputValueException, UserAlreadyExistsException {
        System.out.println("🔥 Controller reached");
        if( !usersRequestDto.getPassword().equals(usersRequestDto.getConfirmPassword()) ){
            throw new IncorrectInputValueException(MessageConstants.PASSWORD_NOT_MATCH);
        }

        UsersResponseDto response = usersService.addNewUsers(usersRequestDto);
        System.out.println("🔥 Controller EXITED ");
        return ApiResponse.generateApiResponse(HttpStatus.CREATED, MessageConstants.USER_CREATED, response);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<?>    deleteUser(HttpServletRequest request) throws IncorrectInputValueException, BadRequestException {

        String email = utility.getEmailFromToken(request.getHeader("Authorization").substring(7));

        if(email.isEmpty()){
            throw new IncorrectInputValueException(MessageConstants.INVALID_CREDENTIALS);
        }
        usersService.deleteUser(email);
        return ApiResponse.generateApiResponse(HttpStatus.ACCEPTED, MessageConstants.USER_DELETED, null);
    }

    @PutMapping("/update-user")
    public ResponseEntity<?> updateUser(@RequestBody UsersUpdateRequestDto usersRequestDto) throws UserNotFoundException {
        if( !usersService.checkUserExists(usersRequestDto.getEmail())){
            throw new UserNotFoundException(MessageConstants.USER_NOT_FOUND);
        }

        UsersResponseDto updatedUser = usersService.updateUser(usersRequestDto);
        return ApiResponse.generateApiResponse(HttpStatus.ACCEPTED, MessageConstants.User_UPDATED, updatedUser);

    }

}
