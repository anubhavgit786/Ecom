package com.anubhav.ecom.controllers;

import com.anubhav.ecom.dtos.ChangePasswordRequestDTO;
import com.anubhav.ecom.dtos.UserDTO;
import com.anubhav.ecom.dtos.UserRequestDTO;
import com.anubhav.ecom.dtos.UserUpdateDTO;
import com.anubhav.ecom.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController
{
    private final UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestParam(required = false, defaultValue = "", name = "sort" ) String sort)
    {
        if(!Set.of("name", "email").contains(sort))
        {
            sort = "name";
        }

        List<UserDTO> users = userService.getAllUsers(sort);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id)
    {
        UserDTO user = userService.getUser(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO inputUser)
    {
        UserDTO user = userService.createUser(inputUser);

        return new ResponseEntity<>(user, HttpStatus.CREATED); // 201 Created with Location header
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO inputUser)
    {
        UserDTO user = userService.updateUser(id, inputUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id)
    {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public  ResponseEntity<Void> changePassword(@PathVariable Long id, @RequestBody ChangePasswordRequestDTO passwordDTO)
    {
        userService.changePassword(id, passwordDTO);
        return ResponseEntity.noContent().build();
    }


}
