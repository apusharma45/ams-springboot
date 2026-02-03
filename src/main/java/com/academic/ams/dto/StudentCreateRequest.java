package com.academic.ams.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentCreateRequest {

    @NotBlank
    private String studentId;

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String deptCode;

    @NotBlank
    private String password; // raw from request, we will hash it
}
