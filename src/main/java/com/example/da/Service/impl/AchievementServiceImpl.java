package com.example.da.Service.impl;

import com.example.da.Service.AchievementService;
import com.example.da.domain.Achievement;
import com.example.da.dto.EmployeeSummaryDTO;
import com.example.da.repository.AchievementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class AchievementServiceImpl implements AchievementService {

    @Autowired
    private AchievementRepository achievementRepository;

    @Override
    public List<Achievement> getAllAchievements() {
        return achievementRepository.findAll();
    }

    @Override
    public Achievement getAchievementById(Long id) {
        return achievementRepository.findById(id).orElse(null);
    }

    @Override
    public boolean  saveAchievement(Achievement achievement) {
        if (achievement.getId() == null) {
            // Case for adding new achievement
            boolean exists = achievementRepository.existsByEmployeeAndTypeAndDate(
                    achievement.getEmployee(), achievement.getType(), achievement.getDate());
            if (exists) {
                return false;
            }
        } else {
            // Case for updating an existing achievement, excluding the current one
            boolean exists = achievementRepository.existsByEmployeeAndTypeAndDateAndIdNot(
                    achievement.getEmployee(), achievement.getType(), achievement.getDate(), achievement.getId());
            if (exists) {
                return false;
            }
        }

        // If no duplicates, save the achievement
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
        for(EmployeeSummaryDTO employeeSummaryDTO : result){
            log.info("data:{}" , employeeSummaryDTO.getEmployeeName());
        }
        return achievementRepository.getEmployeeAchievementSummary();
    }

}

