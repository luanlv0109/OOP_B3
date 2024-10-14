package com.example.da.Service;

import com.example.da.Service.FilesStorageService;
import com.example.da.Service.impl.EmployeeServiceImpl;
import com.example.da.domain.Department;
import com.example.da.domain.Employee;
import com.example.da.dto.EmployeeDTO;
import com.example.da.mapper.EmployeeMapper;
import com.example.da.repository.DepartmentRepository;
import com.example.da.repository.EmployeeRepository;
import com.example.da.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private FilesStorageService filesStorageService;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        // Thiết lập các cấu hình nếu cần
    }

    @Test
    void testGetAllEmployees() {
        Employee employee = new Employee();
        EmployeeDTO employeeDTO = new EmployeeDTO();

        when(employeeRepository.findAll()).thenReturn(Collections.singletonList(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        verify(employeeRepository).findAll();
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    void testGetEmployeeById_Exists() {
        Employee employee = new Employee();
        EmployeeDTO employeeDTO = new EmployeeDTO();

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeMapper.toDTO(employee)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toDTO(employee);
    }

    @Test
    void testGetEmployeeById_NotExists() {
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNull(result);
        verify(employeeRepository).findById(1L);
    }

    @Test
    void testSaveEmployee_Success() throws IOException {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartmentId(1L); // Thiết lập ID phòng ban
        employeeDTO.setCode("EMP001"); // Thiết lập mã nhân viên
        employeeDTO.setEmail("test@example.com"); // Thiết lập email
        employeeDTO.setPhone("0123456789"); // Thiết lập số điện thoại

        Employee employee = new Employee();
        MultipartFile imageFile = mock(MultipartFile.class);

        // Giả lập phòng ban hợp lệ
        when(departmentRepository.findById(employeeDTO.getDepartmentId()))
                .thenReturn(Optional.of(new Department()));

        // Giả lập mapper
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);

        // Giả lập kiểm tra mã, email và số điện thoại
        when(employeeRepository.existsByCode(any())).thenReturn(false);
        when(employeeRepository.existsByEmail(any())).thenReturn(false);
        when(employeeRepository.existsByPhone(any())).thenReturn(false);

        // Giả lập lưu file
        when(filesStorageService.save(imageFile)).thenReturn("path/to/image");

        // Gọi phương thức saveEmployee với DTO
        boolean result = employeeService.saveEmployee(employeeDTO, imageFile);

        assertTrue(result);
        verify(employeeMapper).toEntity(employeeDTO);
        verify(employeeRepository).save(employee);
    }



    @Test
    void testSaveEmployee_CodeExists() throws IOException {
        // Tạo đối tượng EmployeeDTO và đặt các giá trị cần thiết
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setCode("EMP001"); // Đặt mã nhân viên
        employeeDTO.setDepartmentId(1L); // Đặt ID phòng ban

        MultipartFile imageFile = mock(MultipartFile.class);
        Employee employee = new Employee();
        employee.setCode(employeeDTO.getCode()); // Đặt mã cho đối tượng Employee

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(new Department()));
        when(employeeMapper.toEntity(employeeDTO)).thenReturn(employee);
        when(employeeRepository.existsByCode(employee.getCode())).thenReturn(true); // Mô phỏng rằng mã đã tồn tại

        // Kiểm tra xem phương thức saveEmployee có ném ra ngoại lệ như mong đợi không
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeService.saveEmployee(employeeDTO, imageFile);
        });

        // So sánh thông điệp ngoại lệ
        assertEquals(Constant.EMPLOYEE_CODE_EXIST_ERROR, thrown.getMessage());
    }



    @Test
    void testUpdateEmployee_Success() throws IOException {
        Long employeeId = 1L;
        Long departmentId = 1L; // Đặt ID phòng ban
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setDepartmentId(departmentId); // Đặt ID cho employeeDTO

        Employee existingEmployee = new Employee();
        existingEmployee.setDepartment(new Department()); // Giả sử đã có một phòng ban

        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.getOriginalFilename()).thenReturn("image.jpg"); // Mô phỏng tên tệp

        // Mô phỏng hành vi của các phương thức
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        // Sử dụng lenient để mô phỏng tìm phòng ban
        lenient().when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(new Department()));
//        when(employeeMapper.toEntity(employeeDTO)).thenReturn(existingEmployee);
        when(filesStorageService.save(imageFile)).thenReturn("path/to/image");

        // Gọi phương thức cần kiểm tra
        boolean result = employeeService.updateEmployee(employeeId, employeeDTO, imageFile);

        // Kiểm tra kết quả
        assertTrue(result);
        verify(employeeRepository).findById(employeeId);
        verify(employeeRepository).save(existingEmployee);
        verify(departmentRepository).findById(departmentId); // Xác minh tìm phòng ban
    }



    @Test
    void testUpdateEmployee_NotFound() {
        Long employeeId = 1L;
        EmployeeDTO employeeDTO = new EmployeeDTO();
        MultipartFile imageFile = mock(MultipartFile.class);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            employeeService.updateEmployee(employeeId, employeeDTO, imageFile);
        });

        assertEquals(Constant.EMPLOYEE_NOT_FOUND_ERROR, thrown.getMessage());
    }

    @Test
    void testDeleteEmployee() {
        Long employeeId = 1L;

        employeeService.deleteEmployee(employeeId);

        verify(employeeRepository).deleteById(employeeId);
    }
}
