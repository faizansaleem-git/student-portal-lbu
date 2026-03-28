package com.university.portal.controller;

import com.university.portal.JwtUtil;
import com.university.portal.dto.ProfileResponse;
import com.university.portal.dto.ProfileUpdateRequest;
import com.university.portal.repository.UserRepository;
import com.university.portal.service.GraduationService;
import com.university.portal.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private GraduationService graduationService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // GET profile
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    // PUT update profile
    @PutMapping("/profile")
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ProfileUpdateRequest request) {

        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(profileService.updateProfile(userId, request));
    }

    // GET graduation eligibility
    @GetMapping("/graduation")
    public ResponseEntity<Map<String, Object>> checkGraduation(
            @RequestHeader("Authorization") String authHeader) {

        Long userId = extractUserId(authHeader);
        return ResponseEntity.ok(graduationService.checkEligibility(userId));
    }

    // Helper method to extract userId from token
    private Long extractUserId(String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username).get().getId();
    }
}