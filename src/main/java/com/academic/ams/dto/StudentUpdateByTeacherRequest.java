package com.academic.ams.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentUpdateByTeacherRequest {

    @NotBlank
    private String name;

    @Email
    private String email;

    private String phone;

    @NotBlank
    private String deptCode; // teacher can move student to another dept
}
