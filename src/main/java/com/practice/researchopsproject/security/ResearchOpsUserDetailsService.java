package com.practice.researchopsproject.security;

import com.practice.researchopsproject.entity.Users;
import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.utilities.Messages;
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
public class ResearchOpsUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = usersRepository.findByEmail(username)
                .orElseThrow( ()-> new UsernameNotFoundException(Messages.USER_NOT_FOUND));

        return new User(
                users.getEmail(),
                users.getPassword(),
                List.of(new SimpleGrantedAuthority(users.getRole().toString())));

    }
}
