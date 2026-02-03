package com.academic.ams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DeptCreateRequest {

    @NotBlank
    private String code;

    @NotBlank
    private String name;
}
