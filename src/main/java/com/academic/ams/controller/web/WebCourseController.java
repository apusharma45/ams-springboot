package com.academic.ams.controller.web;

import com.academic.ams.dto.CourseCreateRequest;
import com.academic.ams.dto.CourseUpdateRequest;
import com.academic.ams.dto.web.CourseForm;
import com.academic.ams.entity.Course;
import com.academic.ams.repository.CourseRepository;
import com.academic.ams.repository.DeptRepository;
import com.academic.ams.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/courses")
@PreAuthorize("hasRole('TEACHER')")
public class WebCourseController {

    private final CourseService courseService;
    private final DeptRepository deptRepository;
    private final CourseRepository courseRepository;

    public WebCourseController(CourseService courseService, DeptRepository deptRepository, CourseRepository courseRepository) {
        this.courseService = courseService;
        this.deptRepository = deptRepository;
        this.courseRepository = courseRepository;
    }

    // List all courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        return "web/course-list";
    }

    // View single course
    @GetMapping("/{id}")
    public String viewCourse(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        model.addAttribute("course", course);
        return "web/course-view";
    }

    // Show edit course form
    @GetMapping("/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        CourseForm form = new CourseForm();
        form.setCode(course.getCode());
        form.setTitle(course.getTitle());
        form.setDeptCode(course.getDept().getCode());
        
        model.addAttribute("courseForm", form);
        model.addAttribute("course", course);
        model.addAttribute("depts", deptRepository.findAll());
        model.addAttribute("isEdit", true);
        return "web/course-edit";
    }

    // Handle update course
    @PostMapping("/{id}/update")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("courseForm") CourseForm form,
                               BindingResult br,
                               Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        if (br.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("depts", deptRepository.findAll());
            model.addAttribute("isEdit", true);
            return "web/course-edit";
        }

        CourseUpdateRequest req = new CourseUpdateRequest();
        req.setTitle(form.getTitle());
        req.setDeptCode(form.getDeptCode());

        try {
            courseService.updateCourse(id, req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("course", course);
            model.addAttribute("depts", deptRepository.findAll());
            model.addAttribute("isEdit", true);
            return "web/course-edit";
        }

        return "redirect:/web/courses";
    }

    // Delete course
    @PostMapping("/{id}/delete")
    public String deleteCourse(@PathVariable Long id) {
        courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        courseService.deleteCourse(id);
        return "redirect:/web/courses";
    }

    // Show form
    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("courseForm", new CourseForm());
        model.addAttribute("depts", deptRepository.findAll()); // for dropdown
        return "web/course-new";
    }

    // Handle submit
    @PostMapping
    public String createCourse(@Valid @ModelAttribute("courseForm") CourseForm form,
                               BindingResult br,
                               Model model) {

        if (br.hasErrors()) {
            model.addAttribute("depts", deptRepository.findAll());
            return "web/course-new";
        }

        CourseCreateRequest req = new CourseCreateRequest();
        req.setCode(form.getCode());
        req.setTitle(form.getTitle());
        req.setDeptCode(form.getDeptCode());

        try {
            courseService.createCourse(req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("depts", deptRepository.findAll());
            return "web/course-new";
        }

        return "redirect:/web/home";
    }
}
