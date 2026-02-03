package com.academic.ams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CourseUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String deptCode; // allow teacher to move course to another dept
}
