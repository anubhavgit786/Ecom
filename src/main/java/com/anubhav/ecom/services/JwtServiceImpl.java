package com.anubhav.ecom.services;


import com.anubhav.ecom.config.JwtConfig;
import com.anubhav.ecom.models.Role;
import com.anubhav.ecom.models.User;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService
{
    private final JwtConfig jwtConfig;

    @Override
    public String generateAccessToken(User user)
    {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    @Override
    public String generateRefreshToken(User user)
    {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    @Override
    public boolean validateToken(String token)
    {
        try
        {
            Claims claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        }
        catch (JwtException ex)
        {
            return false;
        }
    }

    @Override
    public String getEmailFromToken(String token)
    {
        Claims claims = getClaims(token);
        return claims.get("email", String.class);
    }

    @Override
    public Role getRoleFromToken(String token)
    {
        Claims claims = getClaims(token);
        var role = claims.get("role", String.class);
        return Role.valueOf(role);
    }

    private Claims getClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(jwtConfig.getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private String generateToken(User user, long tokenExpiration)
    {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + (1000 * tokenExpiration)))
                .signWith(jwtConfig.getSecretKey())
                .compact();
    }
}
