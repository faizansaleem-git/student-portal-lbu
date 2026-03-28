package com.university.portal.service;

import com.university.portal.JwtUtil;
import com.university.portal.dto.LoginRequest;
import com.university.portal.dto.RegisterRequest;
import com.university.portal.model.Student;
import com.university.portal.model.User;
import com.university.portal.repository.StudentRepository;
import com.university.portal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private FinanceService financeService;

    @Autowired
    private LibraryService libraryService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("PORTAL_USER");
        userRepository.save(user);

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setUser(user);
        studentRepository.save(student);

        // Call Finance + Library microservices
        financeService.createFinanceAccount(user.getUsername());
        libraryService.createLibraryAccount(user.getUsername());

        return user;
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername());
    }
}