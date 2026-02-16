package com.academic.ams.service;

import com.academic.ams.dto.StudentCreateRequest;
import com.academic.ams.entity.Course;
import com.academic.ams.entity.Dept;
import com.academic.ams.entity.Student;
import com.academic.ams.repository.CourseRepository;
import com.academic.ams.repository.DeptRepository;
import com.academic.ams.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentRepository studentRepository;
    @Mock DeptRepository deptRepository;
    @Mock CourseRepository courseRepository;
    @Mock PasswordEncoder passwordEncoder;

    @InjectMocks StudentService studentService;

    private Dept cse;

    @BeforeEach
    void setup() {
        cse = new Dept();
        cse.setId(1L);
        cse.setCode("CSE");
        cse.setName("Computer Science");
    }

    @Test
    void createStudentByTeacher_success_encodesPassword_andSaves() {
        StudentCreateRequest req = new StudentCreateRequest();
        req.setStudentId("S-001");
        req.setName("Apu");
        req.setEmail("apu@example.com");
        req.setPhone("01700000000");
        req.setDeptCode("CSE");
        req.setPassword("plain");

        when(studentRepository.findByStudentId("S-001")).thenReturn(Optional.empty());
        when(studentRepository.findByEmail("apu@example.com")).thenReturn(Optional.empty());
        when(deptRepository.findByCode("CSE")).thenReturn(Optional.of(cse));
        when(passwordEncoder.encode("plain")).thenReturn("hashed");
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(10L);
            return s;
        });

        var resp = studentService.createStudentByTeacher(req);

        assertEquals(10L, resp.getId());
        assertEquals("S-001", resp.getStudentId());
        assertEquals("CSE", resp.getDeptCode());

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        Student saved = captor.getValue();

        assertEquals("hashed", saved.getPasswordHash());
        assertEquals(cse, saved.getDept());
    }

    @Test
    void createStudentByTeacher_studentIdAlreadyExists_throws() {
        StudentCreateRequest req = new StudentCreateRequest();
        req.setStudentId("S-001");
        req.setEmail("apu@example.com");
        req.setDeptCode("CSE");
        req.setPassword("plain");
        req.setName("Apu");

        when(studentRepository.findByStudentId("S-001")).thenReturn(Optional.of(new Student()));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.createStudentByTeacher(req)
        );
        assertEquals("studentId already exists", ex.getMessage());

        verify(studentRepository, never()).save(any());
    }

    @Test
    void enrollMyself_duplicateEnroll_throws() {
        Student s = new Student();
        s.setStudentId("S-001");
        s.setName("Apu");
        s.setEmail("apu@example.com");
        s.setDept(cse);
        s.setPasswordHash("hashed");

        Course c = new Course();
        c.setId(7L);
        c.setCode("CSE101");
        c.setTitle("Intro");
        c.setDept(cse);

        s.getCourses().add(c); // already enrolled

        when(studentRepository.findByStudentId("S-001")).thenReturn(Optional.of(s));
        when(courseRepository.findById(7L)).thenReturn(Optional.of(c));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.enrollMyself("S-001", 7L)
        );
        assertEquals("Already enrolled in this course", ex.getMessage());
    }

    @Test
    void deleteStudentByTeacher_clearsEnrollments_thenDeletes() {
        Student s = new Student();
        s.setId(99L);
        s.setStudentId("S-001");
        s.setName("Apu");
        s.setEmail("apu@example.com");
        s.setDept(cse);
        s.setPasswordHash("hashed");

        Course c = new Course();
        c.setId(7L);
        c.setCode("CSE101");
        c.setTitle("Intro");
        c.setDept(cse);

        s.getCourses().add(c);

        when(studentRepository.findById(99L)).thenReturn(Optional.of(s));

        studentService.deleteStudentByTeacher(99L);

        assertTrue(s.getCourses().isEmpty());
        verify(studentRepository).delete(s);
    }
}
