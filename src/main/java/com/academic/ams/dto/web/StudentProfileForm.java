package com.academic.ams.dto.web;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentProfileForm {

    @NotBlank
    private String name;

    @Email @NotBlank
    private String email;

    private String phone;
}
