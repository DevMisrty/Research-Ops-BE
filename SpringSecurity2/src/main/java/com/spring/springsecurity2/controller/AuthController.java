package com.spring.springsecurity2.controller;

import com.spring.springsecurity2.dto.LoginInRequestDto;
import com.spring.springsecurity2.dto.SignInRequestDto;
import com.spring.springsecurity2.model.Customer;
import com.spring.springsecurity2.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5173")
public class AuthController {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public String getCustomerSignIn(@RequestBody SignInRequestDto signInRequestDto){
        Customer customer = modelMapper.map(signInRequestDto, Customer.class);
        customer.setPassword(passwordEncoder.encode(signInRequestDto.getPassword()));
        customerRepository.save(customer);
        return "Customer has Successfully sign in";
    }

    @PostMapping("/login")
    public ResponseEntity<?> getCustomerLogIn(@RequestBody LoginInRequestDto loginInRequestDto){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginInRequestDto.getUsername(),
                            loginInRequestDto.getPassword()
                    )
            );
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("signin");
    }
}
