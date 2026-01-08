package com.learning.expensetrackermdb.jwt;

import com.learning.expensetrackermdb.entity.Users;
import com.learning.expensetrackermdb.exception.customexception.UserNotFoundException;
import com.learning.expensetrackermdb.repository.UsersRepo;
import com.learning.expensetrackermdb.utility.MessageConstants;
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

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JWTUtility utility;
    private final UsersRepo repo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if( token == null || !token.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        token = token.substring(7).trim();
        if(utility.isTokenValid(token)){
            String username = utility.getEmailFromToken(token);
            try {
                Users user = repo.findByEmail(username)
                        .orElseThrow( ()-> new UserNotFoundException(MessageConstants.USER_NOT_FOUND));

                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                user.getEmail(),
                                user.getPassword(),
                                List.of(new SimpleGrantedAuthority("ROLE_USER"))
                        )
                );

            } catch (UserNotFoundException e) {
                SecurityContextHolder.clearContext();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return;
            }
        }
        filterChain.doFilter(request,response);
    }
}
