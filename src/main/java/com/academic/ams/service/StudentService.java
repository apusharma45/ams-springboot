package com.academic.ams.service;

import com.academic.ams.dto.StudentCreateRequest;
import com.academic.ams.dto.StudentResponse;
import com.academic.ams.dto.StudentSelfUpdateRequest;
import com.academic.ams.dto.StudentUpdateByTeacherRequest;
import com.academic.ams.entity.Course;
import com.academic.ams.entity.Dept;
import com.academic.ams.entity.Student;
import com.academic.ams.repository.CourseRepository;
import com.academic.ams.repository.DeptRepository;
import com.academic.ams.repository.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final DeptRepository deptRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;

    public StudentService(StudentRepository studentRepository,
                          DeptRepository deptRepository,
                          CourseRepository courseRepository,
                          PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.deptRepository = deptRepository;
        this.courseRepository = courseRepository;
        this.passwordEncoder = passwordEncoder;
    }


    // Teacher updates any student
    @Transactional
    public StudentResponse updateStudentByTeacher(Long studentDbId, StudentUpdateByTeacherRequest req) {

        Student s = studentRepository.findById(studentDbId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Dept dept = deptRepository.findByCode(req.getDeptCode())
                .orElseThrow(() -> new IllegalArgumentException("Dept not found"));

        s.setName(req.getName());
        s.setEmail(req.getEmail());
        s.setPhone(req.getPhone());
        s.setDept(dept);

        return toResponse(s);
    }


    // Student updates ONLY own profile
    @Transactional
    public StudentResponse updateOwnProfile(String callerStudentId, StudentSelfUpdateRequest req) {

        Student s = studentRepository.findByStudentId(callerStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        s.setName(req.getName());
        s.setEmail(req.getEmail());
        s.setPhone(req.getPhone());

        return toResponse(s);
    }


    private StudentResponse toResponse(com.academic.ams.entity.Student s) {
        return new StudentResponse(
                s.getId(),
                s.getStudentId(),
                s.getName(),
                s.getEmail(),
                s.getPhone(),
                s.getDept().getCode()
        );
    }

    @Transactional(readOnly = true)
    public StudentResponse getMyProfile(String callerStudentId) {
        Student s = studentRepository.findByStudentId(callerStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        return toResponse(s);
    }

    @Transactional
    public StudentResponse createStudentByTeacher(StudentCreateRequest req) {

        if (studentRepository.findByStudentId(req.getStudentId()).isPresent()) {
            throw new IllegalArgumentException("studentId already exists");
        }
        if (studentRepository.findByEmail(req.getEmail()).isPresent()) {
            throw new IllegalArgumentException("email already exists");
        }

        Dept dept = deptRepository.findByCode(req.getDeptCode())
                .orElseThrow(() -> new IllegalArgumentException("Dept not found"));

        Student s = new Student();
        s.setStudentId(req.getStudentId());
        s.setName(req.getName());
        s.setEmail(req.getEmail());
        s.setPhone(req.getPhone());
        s.setDept(dept);

        s.setPasswordHash(passwordEncoder.encode(req.getPassword()));

        Student saved = studentRepository.save(s);
        return toResponse(saved);
    }
    @Transactional
    public StudentResponse enrollMyself(String callerStudentId, Long courseId) {

        Student s = studentRepository.findByStudentId(callerStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course c = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // prevent duplicate enroll
        if (s.getCourses().contains(c)) {
            throw new IllegalArgumentException("Already enrolled in this course");
        }

        s.getCourses().add(c);

        return toResponse(s);
    }

    @Transactional
    public void deleteStudentByTeacher(Long studentDbId) {
        Student s = studentRepository.findById(studentDbId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Important: remove enrollments first to avoid join-table FK issues
        s.getCourses().clear();

        studentRepository.delete(s);
    }



}
