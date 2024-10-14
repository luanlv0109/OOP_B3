package com.example.da.Service.impl;

import com.example.da.Service.EmployeeService;
import com.example.da.Service.FilesStorageService;
import com.example.da.domain.Employee;
import com.example.da.dto.EmployeeDTO;
import com.example.da.mapper.EmployeeMapper;
import com.example.da.repository.DepartmentRepository;
import com.example.da.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.da.utils.Constant;  // Thêm import cho Constant
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private FilesStorageService filesStorageService;

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        // Convert list of employees to EmployeeDTOs using the manual mapper
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        return employee != null ? employeeMapper.toDTO(employee) : null;
    }

    @Override
    public boolean saveEmployee(EmployeeDTO employeeDTO, MultipartFile imageFile) throws IOException {
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee.setDepartment(departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException(Constant.DEPARTMENT_NOT_FOUND_ERROR)));  // Sử dụng thông báo từ Constant

        // Kiểm tra trùng lặp mã
        if (employeeRepository.existsByCode(employee.getCode())) {
            throw new RuntimeException(Constant.EMPLOYEE_CODE_EXIST_ERROR);  // Thông báo lỗi cho mã
        }

        // Kiểm tra trùng lặp email
        if (employeeRepository.existsByEmail(employee.getEmail())) {
            throw new RuntimeException(Constant.EMPLOYEE_EMAIL_EXIST_ERROR);  // Thông báo lỗi cho email
        }

        // Kiểm tra trùng lặp số điện thoại
        if (employeeRepository.existsByPhone(employee.getPhone())) {
            throw new RuntimeException(Constant.EMPLOYEE_PHONE_EXIST_ERROR);  // Thông báo lỗi cho số điện thoại
        }
        String path = filesStorageService.save(imageFile);
        employee.setImage(path);
        employeeRepository.save(employee);
        return true;
    }


    @Override
    public boolean updateEmployee(Long id, EmployeeDTO employeeDTO , MultipartFile imageFile) throws IOException {
        // Tìm kiếm nhân viên theo ID, nếu không tìm thấy sẽ ném lỗi với thông báo rõ ràng
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constant.EMPLOYEE_NOT_FOUND_ERROR));

        // Cập nhật thông tin nhân viên
        existingEmployee.setCode(employeeDTO.getCode());
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setGender(employeeDTO.getGender());
        existingEmployee.setDob(employeeDTO.getDob());
        existingEmployee.setSalary(employeeDTO.getSalary());
        existingEmployee.setLevel(employeeDTO.getLevel());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setPhone(employeeDTO.getPhone());
        existingEmployee.setNote(employeeDTO.getNote());

        // Cập nhật phòng ban, nếu không tìm thấy sẽ ném lỗi với thông báo rõ ràng
        existingEmployee.setDepartment(departmentRepository.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException(Constant.DEPARTMENT_NOT_FOUND_ERROR)));
        if (imageFile == null || imageFile.isEmpty() || imageFile.getOriginalFilename().isEmpty()) {
            existingEmployee.setImage(existingEmployee.getImage());
        }
        else{
            String fileName = filesStorageService.save(imageFile);
            existingEmployee.setImage(fileName);
        }
        employeeMapper.toDTO(employeeRepository.save(existingEmployee));
        return true;
    }


    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
