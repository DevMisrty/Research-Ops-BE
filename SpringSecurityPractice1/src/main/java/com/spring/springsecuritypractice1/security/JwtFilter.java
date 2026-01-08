package com.spring.springsecuritypractice1.security;

import com.spring.springsecuritypractice1.model.Users;
import com.spring.springsecuritypractice1.repository.UsersRepo;
import com.spring.springsecuritypractice1.utility.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;


@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;
    private final UsersRepo usersRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token ;
        String username;
        String auth = request.getHeader("Authorization");
        if(auth==null || !auth.startsWith("Bearer")){
            filterChain.doFilter(request,response);
            return;
        }
        token = auth.substring(7);
        if(jwtUtility.isTokenValid(token)){
            username = jwtUtility.getUsernamefromToken(token);
            Users users = usersRepo.findByUsername(username).orElseThrow();
            UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(
                    users.getUsername(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + users.getRole()))
            );
            SecurityContextHolder.getContext().setAuthentication(authtoken);
        }
        Authentication aut = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("User: " + aut.getPrincipal());
        System.out.println("Authorities: " + aut.getAuthorities());
        filterChain.doFilter(request,response);
    }
}
