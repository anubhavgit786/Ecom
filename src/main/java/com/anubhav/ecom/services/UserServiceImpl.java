package com.anubhav.ecom.services;

import com.anubhav.ecom.config.JwtConfig;
import com.anubhav.ecom.dtos.*;
import com.anubhav.ecom.exceptions.ResourceConflictException;
import com.anubhav.ecom.exceptions.ResourceNotFoundException;
import com.anubhav.ecom.exceptions.UnauthorizedException;
import com.anubhav.ecom.models.Role;
import com.anubhav.ecom.models.User;
import com.anubhav.ecom.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    @Override
    public List<UserDTO> getAllUsers(String sort)
    {
        List<User> users = userRepository.findAll(Sort.by(sort));
        return users
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUser(Long id)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserRequestDTO inputUser)
    {
        boolean emailExists = userRepository.existsByEmail(inputUser.getEmail());

        if(emailExists)
        {
            throw new ResourceConflictException("User already exists with email: " + inputUser.getEmail());
        }

        User user = modelMapper.map(inputUser, User.class);
        String hash = passwordEncoder.encode(user.getPassword());
        user.setPassword(hash);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return this.modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public UserDTO updateUser(Long id, UserUpdateDTO inputUser)
    {
        boolean userExists = userRepository.existsById(id);

        if(!userExists)
        {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        boolean emailExists = userRepository.existsByEmail(inputUser.getEmail());

        if(emailExists)
        {
            throw new ResourceConflictException("User already exists with email: " + inputUser.getEmail());
        }

        User user = modelMapper.map(inputUser, User.class);
        user.setId(id);
        user.setRole(Role.USER);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public void deleteUser(Long id)
    {
        boolean userExists = userRepository.existsById(id);

        if(!userExists)
        {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }

        userRepository.deleteById(id);
    }

    @Override
    public void changePassword(Long id, ChangePasswordRequestDTO passwordDTO)
    {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if(!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword()))
        {
            throw new UnauthorizedException("Invalid Credentials");
        }

        String hash = passwordEncoder.encode(passwordDTO.getNewPassword());
        user.setPassword(hash);
        userRepository.save(user);
    }

    @Override
    public JwtResponseDTO login(LoginRequestDTO request, HttpServletResponse response)
    {
        var authToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        authenticationManager.authenticate(authToken);
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new ResourceNotFoundException("User does not exist with email : " + request.getEmail()));
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        var cookie = new Cookie("refreshToken", refreshToken);

        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration().intValue());
        cookie.setSecure(true);

        response.addCookie(cookie);

        var jwtResponse = new JwtResponseDTO(accessToken);
        return jwtResponse;

    }

    @Override
    public UserDTO me()
    {
        User user = getCurrentUser();
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public JwtResponseDTO refresh(String refreshToken)
    {
        if(!jwtService.validateToken(refreshToken))
        {
            throw new UnauthorizedException("Client must authenticate");
        }

        String email = jwtService.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User not found."));
        var accessToken = jwtService.generateAccessToken(user);
        return new JwtResponseDTO(accessToken);
    }

    @Override
    public User getCurrentUser()
    {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        return userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("User does not exist with email : " + email));
    }
}
