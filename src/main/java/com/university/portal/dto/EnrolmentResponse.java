package com.university.portal.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EnrolmentResponse {
    private Long enrolmentId;
    private String courseName;
    private String courseCode;
    private int credits;
    private LocalDateTime enrolmentDate;
}