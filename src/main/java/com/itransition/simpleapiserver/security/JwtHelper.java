package com.itransition.simpleapiserver.security;

import com.itransition.simpleapiserver.configuration.SecurityJwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
@Log
@RequiredArgsConstructor
public class JwtHelper {
    private final SecurityJwtProperties securityJwtProperties;

    public String generateToken(Long id) {
        Date date = Date.from(LocalDate.now().plusDays(securityJwtProperties.getTokenExpiration().toMillis())
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setSubject(id.toString())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, securityJwtProperties.getSecret())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(securityJwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.severe(String.format("Invalid token: %s", e.toString()));
        }
        return false;
    }

    public Long getIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(securityJwtProperties.getSecret()).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }
}
