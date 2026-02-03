package com.academic.ams.controller;

import com.academic.ams.dto.CourseCreateRequest;
import com.academic.ams.dto.CourseUpdateRequest;
import com.academic.ams.entity.Course;
import com.academic.ams.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public Course createCourse(@Valid @RequestBody CourseCreateRequest req) {
        return courseService.createCourse(req);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}")
    public Course updateCourse(@PathVariable Long id,
                               @Valid @RequestBody CourseUpdateRequest req) {
        return courseService.updateCourse(id, req);
    }
}
