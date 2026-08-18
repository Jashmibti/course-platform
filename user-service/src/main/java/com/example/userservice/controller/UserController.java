package com.example.userservice.controller;

import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.LoginResponse;
import com.example.userservice.dto.UserProfileResponse;
import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public User createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return userService.login(request);
    }
    @GetMapping("/me")
    public UserProfileResponse getCurrentUser(
            Authentication authentication) {

        return userService.getCurrentUser(
                authentication.getName()
        );
    }
}