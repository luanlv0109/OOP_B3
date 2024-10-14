package com.example.da.mapper;

import com.example.da.domain.Department;
import com.example.da.dto.DepartmentDTO;
import org.springframework.stereotype.Component;

@Component

public class DepartmentMapper {

    public DepartmentDTO toDTO(Department department) {
        return new DepartmentDTO(
                department.getId(),
                department.getCode(),
                department.getName()
        );
    }

    public Department toEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setId(departmentDTO.getId());
        department.setCode(departmentDTO.getCode());
        department.setName(departmentDTO.getName());
        return department;
    }
}
