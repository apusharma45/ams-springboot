package com.academic.ams.service;

import com.academic.ams.dto.CourseCreateRequest;
import com.academic.ams.dto.CourseUpdateRequest;
import com.academic.ams.entity.Course;
import com.academic.ams.entity.Dept;
import com.academic.ams.repository.CourseRepository;
import com.academic.ams.repository.DeptRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final DeptRepository deptRepository;

    public CourseService(CourseRepository courseRepository, DeptRepository deptRepository) {
        this.courseRepository = courseRepository;
        this.deptRepository = deptRepository;
    }

    @Transactional
    public Course createCourse(CourseCreateRequest req) {

        courseRepository.findByCode(req.getCode()).ifPresent(c -> {
            throw new IllegalArgumentException("Course code already exists");
        });

        Dept dept = deptRepository.findByCode(req.getDeptCode())
                .orElseThrow(() -> new IllegalArgumentException("Dept not found"));

        Course c = new Course();
        c.setCode(req.getCode());
        c.setTitle(req.getTitle());
        c.setDept(dept);

        return courseRepository.save(c);
    }

    @Transactional
    public Course updateCourse(Long courseId, CourseUpdateRequest req) {

        Course c = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Dept dept = deptRepository.findByCode(req.getDeptCode())
                .orElseThrow(() -> new IllegalArgumentException("Dept not found"));

        c.setTitle(req.getTitle());
        c.setDept(dept);

        return c;
    }

    @Transactional
    public void deleteCourse(Long courseId) {
        Course c = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        courseRepository.delete(c);
    }

}
