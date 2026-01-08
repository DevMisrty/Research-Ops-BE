package com.practice.researchopsproject.utilities;

import com.practice.researchopsproject.dto.UserDto;
import com.practice.researchopsproject.dto.response.UserResponseDto;
import com.practice.researchopsproject.entity.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtilities {


    private final String key = "ahslkjghakjdshbfakjhkjhdlndbvcljhagbdsjhglfjhadjncvljhavdsjhcvjlhadvscljhvalhj";

    private SecretKey getKey(){
        return Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String getAccessToken(UserDto dto){
        return Jwts.builder()
                .subject(dto.getEmail())
                .claim("role", dto.getRole().toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 *  60 * 24 ))
                .signWith(getKey())
                .compact();
    }

    public String getRefreshToken(String email){
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 + 60 * 60 * 24 * 7 ))
                .signWith(getKey())
                .compact();
    }

    public boolean isValidToken(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .after(new Date());
    }

    public String getEmailFromToken(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

    }

    public String getRoleFromToken(String token){
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }


}
