package com.spring.springsecuritypractice1.security;

import com.spring.springsecuritypractice1.model.Users;
import com.spring.springsecuritypractice1.repository.UsersRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepo usersRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = usersRepo.findByUsername(username).orElseThrow();
        return new User(
                users.getUsername(),
                users.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + users.getRole()))
        );
    }
}
