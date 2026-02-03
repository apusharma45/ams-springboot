package com.academic.ams.dto.web;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeptForm {
    @NotBlank private String code;
    @NotBlank private String name;
}
