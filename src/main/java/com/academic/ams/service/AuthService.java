package com.academic.ams.service;

import com.academic.ams.dto.LoginRequest;
import com.academic.ams.security.JwtService;
import com.academic.ams.security.Role;
import com.academic.ams.repository.StudentRepository;
import com.academic.ams.repository.TeacherRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final StudentRepository studentRepo;
    private final TeacherRepository teacherRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public AuthService(StudentRepository studentRepo, TeacherRepository teacherRepo,
                       PasswordEncoder encoder, JwtService jwt) {
        this.studentRepo = studentRepo;
        this.teacherRepo = teacherRepo;
        this.encoder = encoder;
        this.jwt = jwt;
    }

    public String loginStudent(LoginRequest req) {
        var s = studentRepo.findByStudentId(req.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(req.getPassword(), s.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwt.createToken(s.getStudentId(), Role.STUDENT);
    }

    public String loginTeacher(LoginRequest req) {
        var t = teacherRepo.findByTeacherId(req.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!encoder.matches(req.getPassword(), t.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        return jwt.createToken(t.getTeacherId(), Role.TEACHER);
    }
}
