package com.example.da.Service;

import com.example.da.domain.Department;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    Department saveDepartment(Department department);
    void deleteDepartment(Long id);
}
