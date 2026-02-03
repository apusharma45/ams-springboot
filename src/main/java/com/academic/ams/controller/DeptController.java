package com.academic.ams.controller;

import com.academic.ams.dto.DeptCreateRequest;
import com.academic.ams.entity.Dept;
import com.academic.ams.service.DeptService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/depts")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Dept createDept(@Valid @RequestBody DeptCreateRequest request) {
        return deptService.createDept(request);
    }
}
