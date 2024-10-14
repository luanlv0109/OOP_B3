package com.example.da.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AchievementDTO {
    @Valid
    private Long id;

    @NotNull(message = "Employee ID cannot be null")
    private Long employeeId;

    @NotBlank(message = "Employee name cannot be blank")
    private String employeeName;

    @NotNull(message = "Type cannot be null")
    @Min(value = 0, message = "Type must be at least 0")
    @Max(value = 1, message = "Type must be at most 1")
    private Integer type; // Assuming type can only be 0 or 1 for different types of achievements

    @NotBlank(message = "Reason cannot be blank")
    private String reason;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date cannot be null")
    private LocalDate date;
}
