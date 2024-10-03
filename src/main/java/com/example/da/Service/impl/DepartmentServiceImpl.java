package com.example.da.Service.impl;

import com.example.da.Service.DepartmentService;
import com.example.da.domain.Department;
import com.example.da.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
public class DepartmentServiceImpl  implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department getDepartmentById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    @Override
    public boolean saveDepartment(Department department) {
        if (Objects.isNull(department.getId())) {
            boolean exists = departmentRepository.existsByCodeOrName(department.getCode(), department.getName());
            if (exists) {
                return false;
            }
        } else {
            // Nếu đang cập nhật (có id), kiểm tra xem có bản ghi khác với cùng code hoặc name nhưng khác id
            boolean exists = departmentRepository.existsByCodeOrNameAndIdNot(department.getCode(), department.getName(), department.getId());
            if (exists) {
                return false;
            }
        }
        departmentRepository.save(department);
        return true;
    }

    @Override
    public boolean updateDepartment(Department department, MultipartFile imageFile) {
        return false;
    }

    @Override
    public void deleteDepartment(Long id) {
        departmentRepository.deleteById(id);
    }
}
