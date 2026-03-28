package com.university.portal.service;

import com.university.portal.dto.EnrolmentRequest;
import com.university.portal.dto.EnrolmentResponse;
import com.university.portal.model.Course;
import com.university.portal.model.Enrolment;
import com.university.portal.model.Student;
import com.university.portal.repository.CourseRepository;
import com.university.portal.repository.EnrolmentRepository;
import com.university.portal.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrolmentService {

    @Autowired
    private EnrolmentRepository enrolmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FinanceService financeService;

    public EnrolmentResponse enrol(Long userId, EnrolmentRequest request) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) throw new RuntimeException("Student not found");

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (enrolmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new RuntimeException("Already enrolled in this course");
        }

        Enrolment enrolment = new Enrolment();
        enrolment.setStudent(student);
        enrolment.setCourse(course);
        enrolment.setEnrolmentDate(LocalDateTime.now());
        enrolmentRepository.save(enrolment);

        // Call Finance microservice to create invoice
        financeService.createInvoice(student.getUser().getUsername(), course.getName());

        return mapToResponse(enrolment);
    }

    public List<EnrolmentResponse> getMyEnrolments(Long userId) {
        Student student = studentRepository.findByUserId(userId);
        if (student == null) throw new RuntimeException("Student not found");

        return enrolmentRepository.findByStudentId(student.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private EnrolmentResponse mapToResponse(Enrolment enrolment) {
        EnrolmentResponse response = new EnrolmentResponse();
        response.setEnrolmentId(enrolment.getId());
        response.setCourseName(enrolment.getCourse().getName());
        response.setCourseCode(enrolment.getCourse().getCode());
        response.setCredits(enrolment.getCourse().getCredits());
        response.setEnrolmentDate(enrolment.getEnrolmentDate());
        return response;
    }
}