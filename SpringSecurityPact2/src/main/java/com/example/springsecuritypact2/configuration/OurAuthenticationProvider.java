package com.example.springsecuritypact2.configuration;

import com.example.springsecuritypact2.model.Users;
import com.example.springsecuritypact2.repository.UsersRepo;
import com.example.springsecuritypact2.service.UsersDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class  OurAuthenticationProvider implements AuthenticationProvider {

    private final UsersDetailsService usersDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepo usersRepo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        UserDetails user = usersDetailsService.loadUserByUsername(username);
        Users u = usersRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("Your Credentials are wrong pls try again"));
        if(passwordEncoder.matches(password, user.getPassword()) && u.getAge()>=18  ){
            return new UsernamePasswordAuthenticationToken(username,password,user.getAuthorities());
        }
        throw new UsernameNotFoundException("Your Credentials are wrong pls try again");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
