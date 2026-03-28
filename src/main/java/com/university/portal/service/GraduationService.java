package com.university.portal.service;

import com.university.portal.model.Student;
import com.university.portal.repository.EnrolmentRepository;
import com.university.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class GraduationService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrolmentRepository enrolmentRepository;

    @Autowired
    private FinanceService financeService;

    public Map<String, Object> checkEligibility(Long userId) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) throw new RuntimeException("Student not found");

        long enrolmentCount = enrolmentRepository
                .findByStudentId(student.getId()).size();

        int totalCredits = (int) enrolmentCount * 20;
        int requiredCredits = 60;
        boolean eligible = totalCredits >= requiredCredits;

        boolean hasDebt = financeService.hasOutstandingBalance(student.getUser().getUsername());
        if (hasDebt) {
            eligible = false;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("studentName", student.getFirstName() + " " + student.getLastName());
        result.put("totalCredits", totalCredits);
        result.put("requiredCredits", requiredCredits);
        result.put("coursesEnrolled", enrolmentCount);
        result.put("eligible", eligible);
        result.put("financeStatus", hasDebt
                ? "Outstanding balance detected - please clear dues before graduation."
                : "No outstanding balance.");
        result.put("message", eligible
                ? "Congratulations! You are eligible for graduation."
                : "You need " + (requiredCredits - totalCredits) + " more credits to be eligible.");

        return result;
    }
}