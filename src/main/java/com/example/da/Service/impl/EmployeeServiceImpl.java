package com.example.da.Service.impl;

import com.example.da.Service.EmployeeService;
import com.example.da.Service.FilesStorageService;
import com.example.da.domain.Employee;
import com.example.da.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FilesStorageService filesStorageService;
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public boolean saveEmployee(Employee employee) {

        if (Objects.isNull(employee.getId())) {
            // Kiểm tra trùng lặp khi tạo mới
            boolean codeExists = employeeRepository.existsByCode(employee.getCode());
            boolean emailExists = employeeRepository.existsByEmail(employee.getEmail());
            boolean phoneExists = employeeRepository.existsByPhone(employee.getPhone());

            if (codeExists || emailExists || phoneExists) {
                return false; // Trả về false nếu phát hiện trùng lặp
            }
        } else {
            // Kiểm tra trùng lặp khi cập nhật (bỏ qua nhân viên hiện tại với cùng id)
            boolean codeExists = employeeRepository.existsByCodeAndIdNot(employee.getCode(), employee.getId());
            boolean emailExists = employeeRepository.existsByEmailAndIdNot(employee.getEmail(), employee.getId());
            boolean phoneExists = employeeRepository.existsByPhoneAndIdNot(employee.getPhone(), employee.getId());

            if (codeExists || emailExists || phoneExists) {
                return false; // Trả về false nếu phát hiện trùng lặp
            }
        }

        employeeRepository.save(employee);
        return true;
    }

    @Override
    public boolean updateEmployee(Employee employee, MultipartFile imageFile) throws IOException {
        Employee employeeOld = employeeRepository.findById(employee.getId()).orElse(null);
        log.info("e:{}" , employee);
        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename().isEmpty()) {
            employee.setImage(employeeOld.getImage());
        }
        else{
            String fileName = filesStorageService.save(imageFile);
            employee.setImage(fileName);
        }
        boolean codeExists = employeeRepository.existsByCodeAndIdNot(employee.getCode(), employee.getId());
        boolean emailExists = employeeRepository.existsByEmailAndIdNot(employee.getEmail(), employee.getId());
        boolean phoneExists = employeeRepository.existsByPhoneAndIdNot(employee.getPhone(), employee.getId());

        if (codeExists || emailExists || phoneExists) {
            return false; // Trả về false nếu phát hiện trùng lặp
        }
        employeeRepository.save(employee);
        return true;
}

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
