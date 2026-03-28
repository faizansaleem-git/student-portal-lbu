package com.university.portal.dto;

import lombok.Data;

@Data
public class ProfileResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}