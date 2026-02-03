package com.academic.ams.service;

import com.academic.ams.dto.DeptCreateRequest;
import com.academic.ams.entity.Dept;
import com.academic.ams.repository.DeptRepository;
import org.springframework.stereotype.Service;

@Service
public class DeptService {

    private final DeptRepository deptRepository;

    public DeptService(DeptRepository deptRepository) {
        this.deptRepository = deptRepository;
    }

    public Dept createDept(DeptCreateRequest request) {

        // business rule: dept code must be unique
        deptRepository.findByCode(request.getCode())
                .ifPresent(d -> {
                    throw new IllegalArgumentException("Department code already exists");
                });

        Dept dept = new Dept();
        dept.setCode(request.getCode());
        dept.setName(request.getName());

        return deptRepository.save(dept);
    }
}
