package com.academic.ams.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentResponse {
    private Long id;
    private String studentId;
    private String name;
    private String email;
    private String phone;
    private String deptCode;
}
