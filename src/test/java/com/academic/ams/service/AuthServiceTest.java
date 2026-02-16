package com.academic.ams.service;

import com.academic.ams.dto.LoginRequest;
import com.academic.ams.entity.Dept;
import com.academic.ams.entity.Student;
import com.academic.ams.entity.Teacher;
import com.academic.ams.repository.StudentRepository;
import com.academic.ams.repository.TeacherRepository;
import com.academic.ams.security.JwtService;
import com.academic.ams.security.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock StudentRepository studentRepo;
    @Mock TeacherRepository teacherRepo;
    @Mock PasswordEncoder encoder;
    @Mock JwtService jwt;

    @InjectMocks AuthService authService;

    @Test
    void loginStudent_success_returnsJwt() {
        LoginRequest req = new LoginRequest();
        req.setId("S-001");
        req.setPassword("plain");

        Student s = new Student();
        s.setStudentId("S-001");
        s.setPasswordHash("hashed");
        s.setDept(new Dept());

        when(studentRepo.findByStudentId("S-001")).thenReturn(Optional.of(s));
        when(encoder.matches("plain", "hashed")).thenReturn(true);
        when(jwt.createToken("S-001", Role.STUDENT)).thenReturn("jwt-token");

        String token = authService.loginStudent(req);

        assertEquals("jwt-token", token);
        verify(jwt).createToken("S-001", Role.STUDENT);
    }

    @Test
    void loginStudent_invalidId_throws() {
        LoginRequest req = new LoginRequest();
        req.setId("S-404");
        req.setPassword("plain");

        when(studentRepo.findByStudentId("S-404")).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.loginStudent(req)
        );
        assertEquals("Invalid credentials", ex.getMessage());

        verifyNoInteractions(jwt);
        verify(encoder, never()).matches(anyString(), anyString());
    }

    @Test
    void loginStudent_wrongPassword_throws() {
        LoginRequest req = new LoginRequest();
        req.setId("S-001");
        req.setPassword("wrong");

        Student s = new Student();
        s.setStudentId("S-001");
        s.setPasswordHash("hashed");
        s.setDept(new Dept());

        when(studentRepo.findByStudentId("S-001")).thenReturn(Optional.of(s));
        when(encoder.matches("wrong", "hashed")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.loginStudent(req)
        );
        assertEquals("Invalid credentials", ex.getMessage());

        verifyNoInteractions(jwt);
        verify(jwt, never()).createToken(anyString(), any());
    }

    @Test
    void loginTeacher_success_returnsJwt() {
        LoginRequest req = new LoginRequest();
        req.setId("T-1001");
        req.setPassword("plain");

        Teacher t = new Teacher();
        t.setTeacherId("T-1001");
        t.setPasswordHash("hashed");
        t.setDept(new Dept());

        when(teacherRepo.findByTeacherId("T-1001")).thenReturn(Optional.of(t));
        when(encoder.matches("plain", "hashed")).thenReturn(true);
        when(jwt.createToken("T-1001", Role.TEACHER)).thenReturn("jwt-token");

        String token = authService.loginTeacher(req);

        assertEquals("jwt-token", token);
        verify(jwt).createToken("T-1001", Role.TEACHER);
    }

    @Test
    void loginTeacher_wrongPassword_throws() {
        LoginRequest req = new LoginRequest();
        req.setId("T-1001");
        req.setPassword("wrong");

        Teacher t = new Teacher();
        t.setTeacherId("T-1001");
        t.setPasswordHash("hashed");
        t.setDept(new Dept());

        when(teacherRepo.findByTeacherId("T-1001")).thenReturn(Optional.of(t));
        when(encoder.matches("wrong", "hashed")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> authService.loginTeacher(req)
        );
        assertEquals("Invalid credentials", ex.getMessage());

        verifyNoInteractions(jwt);
    }
}
