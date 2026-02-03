package com.academic.ams.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter; import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    @NotBlank private String id;       // studentId or teacherId
    @NotBlank private String password;
}
