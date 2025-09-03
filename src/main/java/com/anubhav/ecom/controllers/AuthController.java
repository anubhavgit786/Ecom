package com.anubhav.ecom.controllers;

import com.anubhav.ecom.dtos.JwtResponseDTO;
import com.anubhav.ecom.dtos.LoginRequestDTO;
import com.anubhav.ecom.dtos.UserDTO;
import com.anubhav.ecom.dtos.UserUpdateDTO;
import com.anubhav.ecom.services.JwtService;
import com.anubhav.ecom.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletResponse response)
    {
        JwtResponseDTO responseDTO =  userService.login(request, response);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponseDTO> refresh(@CookieValue(value = "refreshToken") String refreshToken)
    {
        JwtResponseDTO response = userService.refresh(refreshToken);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> me()
    {
        UserDTO user = userService.me();
        return ResponseEntity.ok(user);
    }
}
