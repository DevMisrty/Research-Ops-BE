package com.spring.springsecuritypractice1.contoller;

import com.spring.springsecuritypractice1.dto.SignUpRequestDto;
import com.spring.springsecuritypractice1.model.Users;
import com.spring.springsecuritypractice1.repository.UsersRepo;
import com.spring.springsecuritypractice1.utility.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequiredArgsConstructor
public class AuthController {

    private final UsersRepo usersRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;

    @PostMapping("/auth/signup")
    public ResponseEntity<?> getUserSignUp(@RequestBody SignUpRequestDto user){
        if(usersRepo.findByUsername(user.getUsername()).isPresent()){
            return new ResponseEntity<>("username is already taken", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(usersRepo.save(new Users(user.getUsername()
                ,passwordEncoder.encode(user.getPassword())
                , "user"
                ,user.getAge())));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> getJwtToken(@RequestBody SignUpRequestDto user){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword())
        );
        return ResponseEntity.ok(jwtUtility.getJWTToken(usersRepo.findByUsername(user.getUsername()).orElseThrow()));
    }
}
