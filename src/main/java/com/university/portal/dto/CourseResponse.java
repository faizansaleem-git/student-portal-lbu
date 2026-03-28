package com.university.portal.dto;

import lombok.Data;

@Data
public class CourseResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private int credits;
}