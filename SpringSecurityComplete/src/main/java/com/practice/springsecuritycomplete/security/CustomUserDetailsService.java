package com.practice.springsecuritycomplete.security;

import com.practice.springsecuritycomplete.entity.Users;
import com.practice.springsecuritycomplete.exception.customException.NoSuchUserFound;
import com.practice.springsecuritycomplete.repository.UsersRepository;
import com.practice.springsecuritycomplete.utitlies.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = repo.findUsersByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        return new User( users.getEmail(), users.getPassword(), List.of(new SimpleGrantedAuthority(null)) );
    }
}
