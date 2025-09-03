package com.anubhav.ecom.services;

import com.anubhav.ecom.dtos.*;
import com.anubhav.ecom.models.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService
{
    List<UserDTO> getAllUsers(String sort);
    UserDTO getUser(Long id);
    UserDTO createUser(UserRequestDTO user);
    UserDTO updateUser(Long id, UserUpdateDTO inputUser);
    void deleteUser(Long id);
    void changePassword(Long id, ChangePasswordRequestDTO passwordDTO);
    JwtResponseDTO login(@Valid LoginRequestDTO request, HttpServletResponse response);
    UserDTO me();
    JwtResponseDTO refresh(String refreshToken);
    User getCurrentUser();
}
