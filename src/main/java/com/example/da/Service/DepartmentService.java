package com.example.da.Service;

import com.example.da.domain.Department;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DepartmentService {
    List<Department> getAllDepartments();
    Department getDepartmentById(Long id);
    boolean saveDepartment(Department department);
    boolean updateDepartment(Department department , MultipartFile imageFile);
    void deleteDepartment(Long id);
}
