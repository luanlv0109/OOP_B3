package com.example.da.Service;

import com.example.da.dto.EmployeeDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> getAllEmployees();

    EmployeeDTO getEmployeeById(Long id);

    boolean saveEmployee(EmployeeDTO employeeDTO, MultipartFile imageFile) throws IOException;


    boolean updateEmployee(Long id, EmployeeDTO employeeDTO , MultipartFile imageFile) throws IOException;

    void deleteEmployee(Long id);
}
