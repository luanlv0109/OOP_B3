package com.example.da.mapper;

import com.example.da.domain.Achievement;
import com.example.da.dto.AchievementDTO;
import org.springframework.stereotype.Component;

@Component
public class AchievementMapper {

    public AchievementDTO toDTO(Achievement achievement) {
        return new AchievementDTO(
                achievement.getId(),
                achievement.getEmployee().getId(),
                achievement.getEmployee().getName(),
                achievement.getType(),
                achievement.getReason(),
                achievement.getDate()
        );
    }

    public Achievement toEntity(AchievementDTO achievementDTO) {
        Achievement achievement = new Achievement();
        achievement.setId(achievementDTO.getId());
        achievement.setType(achievementDTO.getType());
        achievement.setReason(achievementDTO.getReason());
        achievement.setDate(achievementDTO.getDate());
        return achievement;
    }
}
