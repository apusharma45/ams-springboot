package com.academic.ams.controller;

import com.academic.ams.dto.StudentCreateRequest;
import com.academic.ams.dto.StudentResponse;
import com.academic.ams.dto.StudentSelfUpdateRequest;
import com.academic.ams.dto.StudentUpdateByTeacherRequest;
import com.academic.ams.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Teacher updates student by DB id
    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping("/{id}/teacher")
    public StudentResponse updateStudentByTeacher(@PathVariable Long id,
                                                  @Valid @RequestBody StudentUpdateByTeacherRequest req) {
        return studentService.updateStudentByTeacher(id, req);
    }

    //update own profile
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/me")
    public StudentResponse updateMyProfile(@Valid @RequestBody StudentSelfUpdateRequest req,
                                           Authentication authentication) {
        String studentId = authentication.getName();
        return studentService.updateOwnProfile(studentId, req);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/me")
    public StudentResponse getMyProfile(Authentication authentication) {
        String studentId = authentication.getName();  // from JWT subject
        return studentService.getMyProfile(studentId);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    public StudentResponse createStudent(@Valid @RequestBody StudentCreateRequest req) {
        return studentService.createStudentByTeacher(req);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/me/enroll/{courseId}")
    public StudentResponse enrollInCourse(@PathVariable Long courseId,
                                          Authentication authentication) {
        String studentId = authentication.getName(); // from JWT
        return studentService.enrollMyself(studentId, courseId);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentByTeacher(id);
    }


}
