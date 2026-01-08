package com.spring.springsecurity2.service;

import com.spring.springsecurity2.model.Customer;
import com.spring.springsecurity2.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {
    
    private final ModelMapper modelMapper;
    private final CustomerRepository repo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = repo.findByUsername(username);
        if (customer == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        List<SimpleGrantedAuthority> authorities = customer.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getName()))
                .toList();
        return new User(
                customer.getUsername(),
                customer.getPassword(),
                authorities
        );
    }
}
