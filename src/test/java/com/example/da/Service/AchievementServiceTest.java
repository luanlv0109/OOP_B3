package com.example.da.Service;

import com.example.da.domain.Achievement;
import com.example.da.domain.Employee;
import com.example.da.dto.AchievementDTO;
import com.example.da.dto.EmployeeSummaryDTO;
import com.example.da.mapper.AchievementMapper;
import com.example.da.repository.AchievementRepository;
import com.example.da.repository.EmployeeRepository;
import com.example.da.Service.impl.AchievementServiceImpl;
import com.example.da.utils.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AchievementServiceTest {

    @InjectMocks
    private AchievementServiceImpl achievementService;

    @Mock
    private AchievementRepository achievementRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private AchievementMapper achievementMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAchievements() {
        // Giả lập danh sách thành tích
        List<Achievement> achievements = new ArrayList<>();
        Achievement achievement1 = new Achievement();
        achievement1.setId(1L);
        achievements.add(achievement1);

        when(achievementRepository.findAll()).thenReturn(achievements);
        when(achievementMapper.toDTO(any(Achievement.class))).thenReturn(new AchievementDTO());

        List<AchievementDTO> result = achievementService.getAllAchievements();

        assertEquals(1, result.size());
        verify(achievementRepository).findAll();
    }

    @Test
    void testGetAchievementById_Success() {
        Long id = 1L;
        Achievement achievement = new Achievement();
        achievement.setId(id);
        when(achievementRepository.findById(id)).thenReturn(Optional.of(achievement));
        when(achievementMapper.toDTO(achievement)).thenReturn(new AchievementDTO());

        AchievementDTO result = achievementService.getAchievementById(id);

        assertNotNull(result);
        verify(achievementRepository).findById(id);
    }

    @Test
    void testGetAchievementById_NotFound() {
        Long id = 1L;
        when(achievementRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            achievementService.getAchievementById(id);
        });
        assertEquals(Constant.ACHIEVEMENT_NOT_FOUND_ERROR, thrown.getMessage());
    }
    @Test
    void testAddAchievement_Success() {
        // Thiết lập AchievementDTO với ID nhân viên
        AchievementDTO achievementDTO = new AchievementDTO();
        achievementDTO.setEmployeeId(1L); // Thêm ID nhân viên
        achievementDTO.setType(0); // Gán một giá trị cho type (nếu cần thiết)
        achievementDTO.setReason("Hoàn thành dự án"); // Gán lý do
        achievementDTO.setDate(LocalDate.now()); // Gán ngày

        // Khởi tạo đối tượng Achievement
        Achievement achievement = new Achievement();
        achievement.setEmployee(new Employee()); // Giả lập nhân viên

        when(achievementMapper.toEntity(achievementDTO)).thenReturn(achievement);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(new Employee())); // Giả lập trả về nhân viên
        when(achievementRepository.existsByEmployeeAndTypeAndDate(any(), any(), any())).thenReturn(false);

        // Gọi phương thức addAchievement
        boolean result = achievementService.addAchievement(achievementDTO);

        assertTrue(result);
        verify(achievementRepository).save(achievement);
    }


    @Test
    void testAddAchievement_EmployeeNotFound() {
        AchievementDTO achievementDTO = new AchievementDTO();
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            achievementService.addAchievement(achievementDTO);
        });
        assertEquals(Constant.EMPLOYEE_NOT_FOUND_ERROR, thrown.getMessage());
    }

    @Test
    void testAddAchievement_AlreadyExists() {
        AchievementDTO achievementDTO = new AchievementDTO();
        achievementDTO.setEmployeeId(1L); // Thiết lập ID nhân viên
        achievementDTO.setType(0); // Thiết lập loại thành tích (nếu cần)
        achievementDTO.setDate(LocalDate.now()); // Gán ngày

        Achievement achievement = new Achievement();
        achievement.setEmployee(new Employee()); // Giả lập nhân viên

        when(achievementMapper.toEntity(achievementDTO)).thenReturn(achievement);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee())); // Giả lập trả về nhân viên
        when(achievementRepository.existsByEmployeeAndTypeAndDate(any(), any(), any())).thenReturn(true); // Giả lập thành tích đã tồn tại

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            achievementService.addAchievement(achievementDTO);
        });
        assertEquals(Constant.ACHIEVEMENT_EXIST_ERROR, thrown.getMessage());
    }


    @Test
    void testUpdateAchievement_Success() {
        AchievementDTO achievementDTO = new AchievementDTO();
        achievementDTO.setEmployeeId(1L); // Thiết lập ID nhân viên
        achievementDTO.setType(0); // Thiết lập loại thành tích (nếu cần)
        achievementDTO.setDate(LocalDate.now()); // Gán ngày

        Achievement achievement = new Achievement();
        achievement.setEmployee(new Employee()); // Giả lập nhân viên

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(new Employee())); // Giả lập trả về nhân viên
        when(achievementRepository.findById(achievementDTO.getId())).thenReturn(Optional.of(achievement)); // Giả lập thành tích tồn tại
        when(achievementMapper.toEntity(achievementDTO)).thenReturn(achievement); // Giả lập chuyển đổi DTO thành entity

        boolean result = achievementService.updateAchievement(achievementDTO);

        assertTrue(result);
        verify(achievementRepository).save(achievement);
    }


    @Test
    void testUpdateAchievement_EmployeeNotFound() {
        AchievementDTO achievementDTO = new AchievementDTO();
        achievementDTO.setId(1L);
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            achievementService.updateAchievement(achievementDTO);
        });
        assertEquals(Constant.EMPLOYEE_NOT_FOUND_ERROR, thrown.getMessage());
    }

    @Test
    void testUpdateAchievement_AlreadyExists() {
        // Giả lập thông tin thành tích
        AchievementDTO achievementDTO = new AchievementDTO();
        achievementDTO.setEmployeeId(1L); // Thiết lập ID nhân viên
        achievementDTO.setType(1);
        achievementDTO.setDate(LocalDate.now());
        achievementDTO.setId(1L); // ID thành tích hiện tại

        // Giả lập nhân viên tồn tại
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L); // ID nhân viên

        // Giả lập việc tìm thấy nhân viên
        when(employeeRepository.findById(achievementDTO.getEmployeeId()))
                .thenReturn(Optional.of(existingEmployee)); // Giả lập nhân viên tìm thấy

        // Giả lập thành tích đã tồn tại
        when(achievementRepository.existsByEmployeeAndTypeAndDateAndIdNot(
                existingEmployee, achievementDTO.getType(), achievementDTO.getDate(), achievementDTO.getId()))
                .thenReturn(true); // Giả lập thành tích đã tồn tại

        // Giả lập chuyển đổi AchievementDTO thành Achievement
        Achievement existingAchievement = new Achievement();
        existingAchievement.setId(1L);
        existingAchievement.setEmployee(existingEmployee);
        when(achievementMapper.toEntity(achievementDTO)).thenReturn(existingAchievement); // Thêm giả lập này

        // Kiểm tra ngoại lệ
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            achievementService.updateAchievement(achievementDTO);
        });

        assertEquals(Constant.ACHIEVEMENT_EXIST_ERROR, exception.getMessage()); // Kiểm tra thông báo lỗi
    }



    @Test
    void testDeleteAchievement() {
        Long id = 1L;

        achievementService.deleteAchievement(id);

        verify(achievementRepository).deleteById(id);
    }

    @Test
    void testGetEmployeeAchievementSummary() {
        // Giả lập kết quả trả về từ phương thức getEmployeeAchievementSummary
        List<EmployeeSummaryDTO> summaries = new ArrayList<>();
        summaries.add(new EmployeeSummaryDTO());
        when(achievementRepository.getEmployeeAchievementSummary()).thenReturn(summaries);

        List<EmployeeSummaryDTO> result = achievementService.getEmployeeAchievementSummary();

        assertEquals(1, result.size());
        verify(achievementRepository).getEmployeeAchievementSummary();
    }
}
