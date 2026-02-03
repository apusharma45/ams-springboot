package com.academic.ams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseCreateRequest {

    @NotBlank
    private String code;     // e.g. CSE101

    @NotBlank
    private String title;    // e.g. Intro to Programming

    @NotBlank
    private String deptCode; // e.g. CSE
}
