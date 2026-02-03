package com.academic.ams.repository;

import com.academic.ams.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DeptRepository extends JpaRepository<Dept, Long> {
    Optional<Dept> findByCode(String code);
}
