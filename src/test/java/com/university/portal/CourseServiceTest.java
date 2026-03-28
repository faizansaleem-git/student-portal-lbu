package com.university.portal;

import com.university.portal.dto.CourseResponse;
import com.university.portal.model.Course;
import com.university.portal.repository.CourseRepository;
import com.university.portal.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void testGetAllCoursesReturnsResults() {
        List<CourseResponse> courses = courseService.getAllCourses();
        assertNotNull(courses);
        assertFalse(courses.isEmpty());
    }

    @Test
    void testGetAllCoursesContainsExpectedFields() {
        List<CourseResponse> courses = courseService.getAllCourses();
        CourseResponse first = courses.get(0);

        assertNotNull(first.getName());
        assertNotNull(first.getCode());
        assertNotNull(first.getDescription());
        assertTrue(first.getCredits() > 0);
    }
}