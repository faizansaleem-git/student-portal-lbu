package com.university.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class FinanceService {

    private static final String FINANCE_URL = "http://localhost:8081";

    @Autowired
    private RestTemplate restTemplate;

    // Called when student registers
    public void createFinanceAccount(String studentId) {
        try {
            Map<String, String> request = new HashMap<>();
            request.put("studentId", studentId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
            restTemplate.postForEntity(FINANCE_URL + "/accounts", entity, Object.class);

        } catch (Exception e) {
            System.out.println("Finance service unavailable: " + e.getMessage());
        }
    }

    // Called when student enrols in a course
    public void createInvoice(String studentId, String courseName) {
        try {
            // First get the account
            ResponseEntity<Map> accountResponse = restTemplate.getForEntity(
                    FINANCE_URL + "/accounts/student/" + studentId, Map.class);

            if (accountResponse.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> invoice = new HashMap<>();
                invoice.put("account", accountResponse.getBody());
                invoice.put("type", "TUITION_FEE");

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> entity = new HttpEntity<>(invoice, headers);
                restTemplate.postForEntity(FINANCE_URL + "/invoices", entity, Object.class);
            }
        } catch (Exception e) {
            System.out.println("Finance service unavailable: " + e.getMessage());
        }
    }

    // Called when student checks graduation eligibility
    public boolean hasOutstandingBalance(String studentId) {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    FINANCE_URL + "/accounts/student/" + studentId, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map body = response.getBody();
                // Check outstanding balance field
                Object balance = body.get("outstanding");
                if (balance != null) {
                    double amount = Double.parseDouble(balance.toString());
                    return amount > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Finance service unavailable: " + e.getMessage());
        }
        return false;
    }
}