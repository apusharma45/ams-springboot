package com.academic.ams.controller.web;

import com.academic.ams.repository.CourseRepository;
import com.academic.ams.service.StudentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/courses")
@PreAuthorize("hasRole('STUDENT')")
public class WebEnrollController {

    private final CourseRepository courseRepository;
    private final StudentService studentService;

    public WebEnrollController(CourseRepository courseRepository, StudentService studentService) {
        this.courseRepository = courseRepository;
        this.studentService = studentService;
    }

    // List courses to enroll
    @GetMapping("/enroll")
    public String enrollPage(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "web/enroll";
    }

    // Enroll in a specific course
    @PostMapping("/{courseId}/enroll")
    public String enroll(@PathVariable Long courseId,
                         Authentication auth,
                         Model model) {

        String studentId = auth.getName();

        try {
            studentService.enrollMyself(studentId, courseId);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("courses", courseRepository.findAll());
            return "web/enroll";
        }

        return "redirect:/web/courses/enroll?success=true";
    }
}
