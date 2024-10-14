package com.example.da.mapper;

import com.example.da.domain.Employee;
import com.example.da.dto.EmployeeDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getDepartment() != null ? employee.getDepartment().getId() : null,
                employee.getDepartment() != null ? employee.getDepartment().getName() : null,
                employee.getCode(),
                employee.getName(),
                employee.getGender(),
                employee.getImage(),
                employee.getDob(),
                employee.getSalary(),
                employee.getLevel(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getNote()
        );
    }

    public Employee toEntity(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setCode(employeeDTO.getCode());
        employee.setName(employeeDTO.getName());
        employee.setGender(employeeDTO.getGender());
        employee.setImage(employeeDTO.getImage());
        employee.setDob(employeeDTO.getDob());
        employee.setSalary(employeeDTO.getSalary());
        employee.setLevel(employeeDTO.getLevel());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());
        employee.setNote(employeeDTO.getNote());
        return employee;
    }
}
