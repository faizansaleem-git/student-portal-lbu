package com.university.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class LibraryService {

    private static final String LIBRARY_URL = "http://localhost:8082";

    @Autowired
    private RestTemplate restTemplate;

    // Called when student registers
    public void createLibraryAccount(String studentId) {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("studentId", studentId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            restTemplate.postForEntity(LIBRARY_URL + "/api/register", entity, Object.class);

        } catch (Exception e) {
            System.out.println("Library service unavailable: " + e.getMessage());
        }
    }
}