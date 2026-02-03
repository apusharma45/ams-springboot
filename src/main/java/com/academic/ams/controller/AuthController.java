package com.academic.ams.controller;

import com.academic.ams.dto.LoginRequest;
import com.academic.ams.dto.LoginResponse;
import com.academic.ams.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/student/login")
    public LoginResponse studentLogin(@Valid @RequestBody LoginRequest req) {
        return new LoginResponse(auth.loginStudent(req));
    }

    @PostMapping("/teacher/login")
    public LoginResponse teacherLogin(@Valid @RequestBody LoginRequest req) {
        return new LoginResponse(auth.loginTeacher(req));
    }
}
