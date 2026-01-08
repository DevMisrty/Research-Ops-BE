package com.learning.expensetrackermdb.service.implementation;

import com.learning.expensetrackermdb.dto.request.UsersRequestDto;
import com.learning.expensetrackermdb.dto.request.UsersUpdateRequestDto;
import com.learning.expensetrackermdb.dto.response.UsersResponseDto;
import com.learning.expensetrackermdb.entity.Users;
import com.learning.expensetrackermdb.exception.customexception.UserAlreadyExistsException;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;
import com.learning.expensetrackermdb.repository.UsersRepo;
import com.learning.expensetrackermdb.service.UsersService;
import com.learning.expensetrackermdb.utility.MessageConstants;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepo usersRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsersResponseDto addNewUsers(UsersRequestDto usersRequestDto) throws UserAlreadyExistsException {

        Users users = modelMapper.map(usersRequestDto, Users.class);

        if(usersRepository.existsUsersByEmail(users.getEmail())){
            throw new UserAlreadyExistsException(MessageConstants.USER_ALREADY_EXISTS);
        }

        users.setPassword(passwordEncoder.encode(users.getPassword()));

        Users savedUsers = usersRepository.save(users);
        return modelMapper.map(savedUsers, UsersResponseDto.class);

    }

    @Override
    public void deleteUser(String email) throws BadRequestException {
        if( !usersRepository.existsUsersByEmail(email) ){
            throw new BadRequestException(MessageConstants.USER_NOT_FOUND);
        }
        usersRepository.deleteUsersByEmail(email);
    }

    @Override
    public UsersResponseDto updateUser(UsersUpdateRequestDto usersRequestDto) throws UserNotFoundException {

        Users users = usersRepository.findByEmail(usersRequestDto.getEmail())
                .orElseThrow(()-> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));
        if(usersRequestDto.getFirstName() != null) users.setFirstName(usersRequestDto.getFirstName());
        if(usersRequestDto.getLastName() != null) users.setLastName(usersRequestDto.getLastName());
        if(usersRequestDto.getPhoneNumber() != null) users.setPhoneNumber(usersRequestDto.getPhoneNumber());

        Users updatedUser = usersRepository.save(users);
        return modelMapper.map(updatedUser, UsersResponseDto.class);
    }

    @Override
    public boolean checkUserExists(String email) {
        return usersRepository.existsUsersByEmail(email);
    }

    @Override
    public Optional<Users> findUsersByEmail(String username) {
        return usersRepository.findByEmail(username);
    }
}
