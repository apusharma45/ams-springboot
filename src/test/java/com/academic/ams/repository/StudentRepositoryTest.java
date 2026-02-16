package com.academic.ams.repository;

import com.academic.ams.entity.Dept;
import com.academic.ams.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentRepositoryTest {

    @Mock
    private StudentRepository studentRepository;

    private Student testStudent;
    private Dept testDept;

    @BeforeEach
    void setUp() {
        // Create a test department
        testDept = new Dept();
        testDept.setCode("CS");
        testDept.setName("Computer Science");

        // Create a test student
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setStudentId("S001");
        testStudent.setName("John Doe");
        testStudent.setEmail("john@example.com");
        testStudent.setPhone("123456789");
        testStudent.setPasswordHash("hashedpassword");
        testStudent.setDept(testDept);
    }

    @Test
    void testFindByStudentId_Success() {
        // Arrange
        when(studentRepository.findByStudentId("S001"))
                .thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> found = studentRepository.findByStudentId("S001");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
        assertEquals("john@example.com", found.get().getEmail());
    }

    @Test
    void testFindByStudentId_NotFound() {
        // Arrange
        when(studentRepository.findByStudentId("S999"))
                .thenReturn(Optional.empty());

        // Act
        Optional<Student> found = studentRepository.findByStudentId("S999");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByEmail_Success() {
        // Arrange
        when(studentRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> found = studentRepository.findByEmail("john@example.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("S001", found.get().getStudentId());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        when(studentRepository.findByEmail("nonexistent@example.com"))
                .thenReturn(Optional.empty());

        // Act
        Optional<Student> found = studentRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(found.isPresent());
    }

    @Test
    void testSaveStudent_Success() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setStudentId("S002");
        newStudent.setName("Jane Doe");
        newStudent.setEmail("jane@example.com");
        newStudent.setPhone("987654321");
        newStudent.setPasswordHash("hashedpassword");
        newStudent.setDept(testDept);

        Student savedStudent = new Student();
        savedStudent.setId(2L);
        savedStudent.setStudentId("S002");
        savedStudent.setName("Jane Doe");
        savedStudent.setEmail("jane@example.com");
        savedStudent.setPhone("987654321");
        savedStudent.setPasswordHash("hashedpassword");
        savedStudent.setDept(testDept);

        when(studentRepository.save(any(Student.class)))
                .thenReturn(savedStudent);

        // Act
        Student result = studentRepository.save(newStudent);

        // Assert
        assertNotNull(result.getId());
        assertEquals("S002", result.getStudentId());
        assertEquals("jane@example.com", result.getEmail());
    }
}
