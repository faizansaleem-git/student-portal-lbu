package com.university.portal;

import com.university.portal.dto.LoginRequest;
import com.university.portal.dto.RegisterRequest;
import com.university.portal.model.User;
import com.university.portal.repository.UserRepository;
import com.university.portal.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    // ─── REGISTER TESTS ───────────────────────────────

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser1");
        request.setPassword("Test@1234");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("testuser1@university.ac.uk");

        User user = authService.register(request);

        assertNotNull(user);
        assertEquals("testuser1", user.getUsername());
        assertEquals("PORTAL_USER", user.getRole());
    }

    @Test
    void testRegisterPasswordIsEncrypted() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser2");
        request.setPassword("Test@1234");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("testuser2@university.ac.uk");

        User user = authService.register(request);

        // Password should NOT be stored as plain text
        assertNotEquals("Test@1234", user.getPassword());
        assertTrue(user.getPassword().startsWith("$2a$"));
    }

    @Test
    void testRegisterDuplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser3");
        request.setPassword("Test@1234");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setEmail("testuser3@university.ac.uk");

        authService.register(request);

        // Registering same username again should throw exception
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        assertEquals("Username already exists", exception.getMessage());
    }

    // ─── LOGIN TESTS ──────────────────────────────────

    @Test
    void testLoginSuccess() {
        // First register
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser4");
        registerRequest.setPassword("Test@1234");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setEmail("testuser4@university.ac.uk");
        authService.register(registerRequest);

        // Then login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser4");
        loginRequest.setPassword("Test@1234");

        String token = authService.login(loginRequest);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.startsWith("eyJ")); // JWT tokens start with eyJ
    }

    @Test
    void testLoginWrongPassword() {
        // First register
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser5");
        registerRequest.setPassword("Test@1234");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setEmail("testuser5@university.ac.uk");
        authService.register(registerRequest);

        // Login with wrong password
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser5");
        loginRequest.setPassword("WrongPassword");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    void testLoginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("nonexistentuser");
        loginRequest.setPassword("Test@1234");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("User not found", exception.getMessage());
    }
}