package com.university.portal.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String firstName;
    private String lastName;
    private String email;
}