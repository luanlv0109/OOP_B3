package com.example.da.Service;

import com.example.da.domain.Department;
import com.example.da.dto.DepartmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
    DepartmentDTO getDepartmentById(Long id);
    boolean addDepartment(DepartmentDTO departmentDTO);
    boolean updateDepartment(Long id, DepartmentDTO departmentDTO);
    void deleteDepartment(Long id);
}
