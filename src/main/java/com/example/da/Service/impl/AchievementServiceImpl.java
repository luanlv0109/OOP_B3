package com.example.da.Service.impl;

import com.example.da.Service.AchievementService;
import com.example.da.domain.Achievement;
import com.example.da.dto.AchievementDTO;
import com.example.da.dto.EmployeeSummaryDTO;
import com.example.da.mapper.AchievementMapper;
import com.example.da.repository.AchievementRepository;
import com.example.da.repository.EmployeeRepository;
import com.example.da.utils.Constant; // Đảm bảo import lớp Constant
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AchievementMapper achievementMapper;

    @Override
    public List<AchievementDTO> getAllAchievements() {
        return achievementRepository.findAll()
                .stream()
                .map(achievementMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AchievementDTO getAchievementById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Constant.ACHIEVEMENT_NOT_FOUND_ERROR));
        return achievementMapper.toDTO(achievement);
    }

    @Override
    public boolean addAchievement(AchievementDTO achievementDTO) {
        Achievement achievement = achievementMapper.toEntity(achievementDTO);
        achievement.setEmployee(employeeRepository.findById(achievementDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(Constant.EMPLOYEE_NOT_FOUND_ERROR)));

        // Kiểm tra trùng lặp trước khi thêm
        boolean exists = achievementRepository.existsByEmployeeAndTypeAndDate(
                achievement.getEmployee(), achievement.getType(), achievement.getDate());
        if (exists) {
            throw new RuntimeException(Constant.ACHIEVEMENT_EXIST_ERROR);
        }

        achievementRepository.save(achievement);
        return true;
    }

    @Override
    public boolean updateAchievement(AchievementDTO achievementDTO) {
        Achievement achievement = achievementMapper.toEntity(achievementDTO);
        achievement.setEmployee(employeeRepository.findById(achievementDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(Constant.EMPLOYEE_NOT_FOUND_ERROR)));

        // Kiểm tra trùng lặp trong khi cập nhật, ngoại trừ thành tích hiện tại
        boolean exists = achievementRepository.existsByEmployeeAndTypeAndDateAndIdNot(
                achievement.getEmployee(), achievement.getType(), achievement.getDate(), achievement.getId());
        if (exists) {
            throw new RuntimeException(Constant.ACHIEVEMENT_EXIST_ERROR);
        }

        achievementRepository.save(achievement);
        return true;
    }

    @Override
    public void deleteAchievement(Long id) {
        achievementRepository.deleteById(id);
    }

    @Override
    public List<EmployeeSummaryDTO> getEmployeeAchievementSummary() {
        List<EmployeeSummaryDTO> result = achievementRepository.getEmployeeAchievementSummary();
        for (EmployeeSummaryDTO employeeSummaryDTO : result) {
            log.info("data:{}", employeeSummaryDTO.getEmployeeName());
        }
        return result;
    }
}
