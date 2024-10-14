package com.example.da.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.example.da.Service.impl.DepartmentServiceImpl;
import com.example.da.domain.Department;
import com.example.da.dto.DepartmentDTO;
import com.example.da.mapper.DepartmentMapper;
import com.example.da.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class) // Đảm bảo Mockito xử lý các @Mock và @InjectMocks
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository; // Khởi tạo đối tượng mock cho repository

    @Mock
    private DepartmentMapper departmentMapper; // Khởi tạo đối tượng mock cho mapper

    @InjectMocks // Khởi tạo đối tượng service và inject các mock vào
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void setUp() {
        // Optional: Khởi tạo bất kỳ cấu hình nào nếu cần
    }

    @Test
    void testGetAllDepartments() {
        Department department = new Department();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        when(departmentRepository.findAll()).thenReturn(Collections.singletonList(department));
        when(departmentMapper.toDTO(department)).thenReturn(departmentDTO);

        List<DepartmentDTO> result = departmentService.getAllDepartments();

        assertEquals(1, result.size());
        verify(departmentRepository).findAll();
        verify(departmentMapper).toDTO(department);
    }

    @Test
    void testGetDepartmentById_Exists() {
        Department department = new Department();
        DepartmentDTO departmentDTO = new DepartmentDTO();

        when(departmentRepository.findById(anyLong())).thenReturn(Optional.of(department));
        when(departmentMapper.toDTO(department)).thenReturn(departmentDTO);

        DepartmentDTO result = departmentService.getDepartmentById(1L);

        assertNotNull(result);
        verify(departmentRepository).findById(1L);
        verify(departmentMapper).toDTO(department);
    }

    @Test
    void testGetDepartmentById_NotExists() {
        when(departmentRepository.findById(anyLong())).thenReturn(Optional.empty());

        DepartmentDTO result = departmentService.getDepartmentById(1L);

        assertNull(result);
        verify(departmentRepository).findById(1L);
    }

    @Test
    void testAddDepartment_Success() {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        Department department = new Department();

        when(departmentRepository.existsByCodeOrName(any(), any())).thenReturn(false);
        when(departmentMapper.toEntity(departmentDTO)).thenReturn(department);

        boolean result = departmentService.addDepartment(departmentDTO);

        assertTrue(result);
        verify(departmentRepository).existsByCodeOrName(departmentDTO.getCode(), departmentDTO.getName());
        verify(departmentMapper).toEntity(departmentDTO);
        verify(departmentRepository).save(department);
    }

    @Test
    void testAddDepartment_AlreadyExists() {
        DepartmentDTO departmentDTO = new DepartmentDTO();

        when(departmentRepository.existsByCodeOrName(any(), any())).thenReturn(true);

        boolean result = departmentService.addDepartment(departmentDTO);

        assertFalse(result);
        verify(departmentRepository).existsByCodeOrName(departmentDTO.getCode(), departmentDTO.getName());
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testUpdateDepartment_Success() {
        Long departmentId = 1L;
        DepartmentDTO departmentDTO = new DepartmentDTO();
        Department department = new Department();

        when(departmentRepository.existsByCodeOrNameAndIdNot(any(), any(), anyLong())).thenReturn(false);
        when(departmentMapper.toEntity(departmentDTO)).thenReturn(department);

        boolean result = departmentService.updateDepartment(departmentId, departmentDTO);

        assertTrue(result);
        verify(departmentRepository).existsByCodeOrNameAndIdNot(departmentDTO.getCode(), departmentDTO.getName(), departmentId);
        verify(departmentMapper).toEntity(departmentDTO);
        verify(departmentRepository).save(department);
    }

    @Test
    void testUpdateDepartment_AlreadyExists() {
        Long departmentId = 1L;
        DepartmentDTO departmentDTO = new DepartmentDTO();

        when(departmentRepository.existsByCodeOrNameAndIdNot(any(), any(), anyLong())).thenReturn(true);

        boolean result = departmentService.updateDepartment(departmentId, departmentDTO);

        assertFalse(result);
        verify(departmentRepository).existsByCodeOrNameAndIdNot(departmentDTO.getCode(), departmentDTO.getName(), departmentId);
        verify(departmentRepository, never()).save(any());
    }

    @Test
    void testDeleteDepartment() {
        Long departmentId = 1L;

        departmentService.deleteDepartment(departmentId);

        verify(departmentRepository).deleteById(departmentId);
    }
}
