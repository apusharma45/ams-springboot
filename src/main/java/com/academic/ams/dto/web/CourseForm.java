package com.academic.ams.dto.web;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseForm {

    @NotBlank
    private String code;

    @NotBlank
    private String title;

    @NotBlank
    private String deptCode; // selected from dropdown
}
