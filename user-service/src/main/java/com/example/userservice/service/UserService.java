package com.example.userservice.service;

import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.security.JwtService;

import org.springframework.stereotype.Service;
import com.example.userservice.dto.CreateUserRequest;
import com.example.userservice.entity.Role;
import com.example.userservice.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.LoginResponse;
import com.example.userservice.dto.UserProfileResponse;


import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                    RoleRepository roleRepository,
                    PasswordEncoder passwordEncoder,
                    JwtService jwtService) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public User createUser(CreateUserRequest request) {

        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() ->
                        new RuntimeException("Role not found"));

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(
        passwordEncoder.encode(request.getPassword())
        );

        user.setRole(role);

        return userRepository.save(user);
    }


    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId(),user.getEmail(),user.getRole().getRoleName());

        return new LoginResponse(token);
    }

    public UserProfileResponse getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole().getRoleName()
        );
    }
}

