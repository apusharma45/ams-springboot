package com.academic.ams.controller;

import com.academic.ams.dto.LoginRequest;
import com.academic.ams.dto.LoginResponse;
import com.academic.ams.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setId("S001");
        loginRequest.setPassword("password123");
    }

    @Test
    void testStudentLogin_Success() {
        // Arrange
        String expectedToken = "jwt_token_xyz123";
        when(authService.loginStudent(any(LoginRequest.class)))
                .thenReturn(expectedToken);

        // Act
        LoginResponse response = authController.studentLogin(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
    }

    @Test
    void testTeacherLogin_Success() {
        // Arrange
        String expectedToken = "jwt_token_teacher456";
        when(authService.loginTeacher(any(LoginRequest.class)))
                .thenReturn(expectedToken);

        // Act
        LoginResponse response = authController.teacherLogin(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());
    }
}
