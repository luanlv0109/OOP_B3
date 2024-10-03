package com.example.da.Service;

import com.example.da.domain.Achievement;
import com.example.da.dto.EmployeeSummaryDTO;

import java.util.List;

public interface AchievementService {
    List<Achievement> getAllAchievements();
    Achievement getAchievementById(Long id);
    boolean  saveAchievement(Achievement achievement);
    void deleteAchievement(Long id);

    List<EmployeeSummaryDTO> getEmployeeAchievementSummary();
}
