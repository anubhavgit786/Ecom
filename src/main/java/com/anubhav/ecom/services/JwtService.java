package com.anubhav.ecom.services;

import com.anubhav.ecom.models.Role;
import com.anubhav.ecom.models.User;

public interface JwtService
{
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String getEmailFromToken(String token);
    Role getRoleFromToken(String token);
}
