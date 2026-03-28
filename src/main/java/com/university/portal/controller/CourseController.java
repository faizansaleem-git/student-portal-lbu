package com.university.portal.controller;

import com.university.portal.JwtUtil;
import com.university.portal.dto.CourseResponse;
import com.university.portal.dto.EnrolmentRequest;
import com.university.portal.dto.EnrolmentResponse;
import com.university.portal.repository.UserRepository;
import com.university.portal.service.CourseService;
import com.university.portal.service.EnrolmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrolmentService enrolmentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // GET all courses
    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // POST enrol in a course
    @PostMapping("/enrolments")
    public ResponseEntity<?> enrol(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody EnrolmentRequest request) {
        try {
            String token = authHeader.substring(7);
            String username = jwtUtil.extractUsername(token);
            Long userId = userRepository.findByUsername(username).get().getId();
            return ResponseEntity.ok(enrolmentService.enrol(userId, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", e.getMessage()));
        }
    }

    // GET my enrolments
    @GetMapping("/enrolments")
    public ResponseEntity<List<EnrolmentResponse>> getMyEnrolments(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        Long userId = userRepository.findByUsername(username).get().getId();

        return ResponseEntity.ok(enrolmentService.getMyEnrolments(userId));
    }
}