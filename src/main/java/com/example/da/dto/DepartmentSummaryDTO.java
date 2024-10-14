package com.example.da.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DepartmentSummaryDTO {
    private Long departmentId;
    private String departmentName;
    private Long totalAchievements;
    private Long totalDisciplinary;
    private Long finalScore;
}
