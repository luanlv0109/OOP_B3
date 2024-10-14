package com.example.da.Service;

import com.example.da.domain.Achievement;
import com.example.da.dto.AchievementDTO;
import com.example.da.dto.EmployeeSummaryDTO;

import java.util.List;

public interface AchievementService {

    List<AchievementDTO> getAllAchievements();

    AchievementDTO getAchievementById(Long id);

    boolean addAchievement(AchievementDTO achievementDTO);

    boolean updateAchievement(AchievementDTO achievementDTO);

    void deleteAchievement(Long id);

    List<EmployeeSummaryDTO> getEmployeeAchievementSummary();
}
