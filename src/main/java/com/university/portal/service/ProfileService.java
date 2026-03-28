package com.university.portal.service;

import com.university.portal.dto.ProfileResponse;
import com.university.portal.dto.ProfileUpdateRequest;
import com.university.portal.model.Student;
import com.university.portal.repository.StudentRepository;
import com.university.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    public ProfileResponse getProfile(Long userId) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) throw new RuntimeException("Student not found");

        ProfileResponse response = new ProfileResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setUsername(student.getUser().getUsername());
        return response;
    }

    public ProfileResponse updateProfile(Long userId, ProfileUpdateRequest request) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) throw new RuntimeException("Student not found");

        if (request.getFirstName() != null) student.setFirstName(request.getFirstName());
        if (request.getLastName() != null) student.setLastName(request.getLastName());
        if (request.getEmail() != null) student.setEmail(request.getEmail());

        studentRepository.save(student);

        ProfileResponse response = new ProfileResponse();
        response.setId(student.getId());
        response.setFirstName(student.getFirstName());
        response.setLastName(student.getLastName());
        response.setEmail(student.getEmail());
        response.setUsername(student.getUser().getUsername());
        return response;
    }
}