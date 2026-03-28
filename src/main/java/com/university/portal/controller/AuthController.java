package com.university.portal.controller;

import com.university.portal.dto.AuthResponse;
import com.university.portal.dto.LoginRequest;
import com.university.portal.dto.RegisterRequest;
import com.university.portal.model.User;
import com.university.portal.repository.UserRepository;
import com.university.portal.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        User user = authService.register(request);
        return ResponseEntity.ok(
                new AuthResponse("Registration successful", user.getId(), user.getUsername(), null)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        String token = authService.login(request);
        User user = userRepository.findByUsername(request.getUsername()).get();
        return ResponseEntity.ok(
                new AuthResponse("Login successful", user.getId(), user.getUsername(), token)
        );
    }
}