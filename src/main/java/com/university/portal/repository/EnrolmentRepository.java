package com.university.portal.repository;

import com.university.portal.model.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrolmentRepository extends JpaRepository<Enrolment, Long> {
    List<Enrolment> findByStudentId(Long studentId);
    boolean existsByStudentIdAndCourseId(Long studentId, Long courseId);
}