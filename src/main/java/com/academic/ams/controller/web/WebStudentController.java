package com.academic.ams.controller.web;

import com.academic.ams.dto.StudentCreateRequest;
import com.academic.ams.dto.StudentUpdateByTeacherRequest;
import com.academic.ams.dto.web.StudentForm;
import com.academic.ams.entity.Student;
import com.academic.ams.repository.DeptRepository;
import com.academic.ams.repository.StudentRepository;
import com.academic.ams.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/students")
@PreAuthorize("hasRole('TEACHER')")
public class WebStudentController {

    private final StudentService studentService;
    private final DeptRepository deptRepository;
    private final StudentRepository studentRepository;

    public WebStudentController(StudentService studentService, DeptRepository deptRepository, StudentRepository studentRepository) {
        this.studentService = studentService;
        this.deptRepository = deptRepository;
        this.studentRepository = studentRepository;
    }

    // List all students
    @GetMapping
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());
        return "web/student-list";
    }

    // View single student
    @GetMapping("/{id}")
    public String viewStudent(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        model.addAttribute("student", student);
        return "web/student-view";
    }

    // Show edit student form
    @GetMapping("/{id}/edit")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        StudentForm form = new StudentForm();
        form.setStudentId(student.getStudentId());
        form.setName(student.getName());
        form.setEmail(student.getEmail());
        form.setPhone(student.getPhone());
        form.setDeptCode(student.getDept().getCode());
        
        model.addAttribute("studentForm", form);
        model.addAttribute("student", student);
        model.addAttribute("depts", deptRepository.findAll());
        model.addAttribute("isEdit", true);
        return "web/student-edit";
    }

    // Handle update student
    @PostMapping("/{id}/update")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("studentForm") StudentForm form,
                                BindingResult br,
                                Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        if (br.hasErrors()) {
            model.addAttribute("student", student);
            model.addAttribute("depts", deptRepository.findAll());
            model.addAttribute("isEdit", true);
            return "web/student-edit";
        }

        StudentUpdateByTeacherRequest req = new StudentUpdateByTeacherRequest();
        req.setName(form.getName());
        req.setEmail(form.getEmail());
        req.setPhone(form.getPhone());
        req.setDeptCode(form.getDeptCode());

        try {
            studentService.updateStudentByTeacher(id, req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("student", student);
            model.addAttribute("depts", deptRepository.findAll());
            model.addAttribute("isEdit", true);
            return "web/student-edit";
        }

        return "redirect:/web/students";
    }

    // Delete student
    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        studentService.deleteStudentByTeacher(id);
        return "redirect:/web/students";
    }

    // 1) Show Create Student form
    @GetMapping("/new")
    public String newStudentForm(Model model) {
        model.addAttribute("studentForm", new StudentForm());
        model.addAttribute("depts", deptRepository.findAll()); // dropdown list
        return "web/student-new"; // must match templates/web/student-new.html
    }

    // 2) Handle Create Student submit
    @PostMapping
    public String createStudent(@Valid @ModelAttribute("studentForm") StudentForm form,
                                BindingResult br,
                                Model model) {

        if (br.hasErrors()) {
            // IMPORTANT: reload dropdown on error
            model.addAttribute("depts", deptRepository.findAll());
            return "web/student-new";
        }

        // Reuse your existing API DTO internally (totally fine)
        StudentCreateRequest req = new StudentCreateRequest();
        req.setStudentId(form.getStudentId());
        req.setName(form.getName());
        req.setEmail(form.getEmail());
        req.setPhone(form.getPhone());
        req.setDeptCode(form.getDeptCode());
        req.setPassword(form.getPassword());

        try {
            studentService.createStudentByTeacher(req);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("depts", deptRepository.findAll());
            return "web/student-new";
        }

        return "redirect:/web/home";
    }
}
