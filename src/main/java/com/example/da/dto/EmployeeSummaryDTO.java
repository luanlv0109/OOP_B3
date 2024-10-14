package com.example.da.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeSummaryDTO {
    private Long employeeId;
    private String employeeName;
    private String employeeImage;
    private String departmentName;
    private Integer totalAchievements;
    private Integer totalDisciplinary;
    private Integer finalScore;

    public EmployeeSummaryDTO(Long employeeId, String employeeName, String employeeImage, String departmentName, Long totalAchievements, Long totalDisciplinary, Long finalScore) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeImage = employeeImage;
        this.departmentName = departmentName;
        this.totalAchievements = Math.toIntExact(totalAchievements);
        this.totalDisciplinary = Math.toIntExact(totalDisciplinary);
        this.finalScore = Math.toIntExact(finalScore);
    }


}
