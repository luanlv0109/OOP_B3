package com.example.da.Service.impl;

import com.example.da.Service.DepartmentService;
import com.example.da.domain.Department;
import com.example.da.dto.DepartmentDTO;
import com.example.da.mapper.DepartmentMapper;
import com.example.da.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl  implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        return departmentRepository.findById(id)
                .map(departmentMapper::toDTO)
                .orElse(null);
    }

    @Override
    public boolean addDepartment(DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByCodeOrName(departmentDTO.getCode(), departmentDTO.getName())) {
            return false;
        }
        Department department = departmentMapper.toEntity(departmentDTO);
        departmentRepository.save(department);
        return true;
    }

    @Override
    public boolean updateDepartment(Long id, DepartmentDTO departmentDTO) {
        if (departmentRepository.existsByCodeOrNameAndIdNot(departmentDTO.getCode(), departmentDTO.getName(), id)) {
            return false;
        }
        Department department = departmentMapper.toEntity(departmentDTO);
        department.setId(id);
        departmentRepository.save(department);
        return true;
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
