package com.example.da.repository;

import com.example.da.domain.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends  JpaRepository<Department, Long> {
    Department findByCode(String code);
    boolean existsByCodeOrName(String code, String name);
    boolean existsByCodeOrNameAndIdNot(String code, String name, Long id);


}