package com.university.portal.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String message;
    private Long userId;
    private String username;
    private String token;

    public AuthResponse(String message, Long userId, String username, String token) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.token = token;
    }
}