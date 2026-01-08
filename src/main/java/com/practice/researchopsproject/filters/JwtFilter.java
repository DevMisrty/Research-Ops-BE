package com.practice.researchopsproject.filters;

import com.practice.researchopsproject.repository.UsersRepository;
import com.practice.researchopsproject.utilities.JwtUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final UsersRepository repo;
    private final JwtUtilities utilities;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");


        if(token == null ||  !token.startsWith("Bearer ") ) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7);

        if(utilities.isValidToken(token)){

            String email = utilities.getEmailFromToken(token);
            String role = utilities.getRoleFromToken(token);

            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(
                                    email, null, List.of(new SimpleGrantedAuthority("ROLE_" + role))
                            )
                    );

        }
        filterChain.doFilter(request, response);
    }
}
