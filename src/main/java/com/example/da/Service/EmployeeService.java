package com.example.da.Service;

import com.example.da.domain.Department;
import com.example.da.domain.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    boolean saveEmployee(Employee employee);
    boolean updateEmployee(Employee employee , MultipartFile imageFile) throws IOException;

    void deleteEmployee(Long id);


}
