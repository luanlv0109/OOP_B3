package com.example.da.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    @Valid
    private Long id;

    private Long departmentId;

    private String departmentName;

    @NotBlank(message = "Code cannot be blank")
    @Size(max = 11, message = "Code must be less than or equal to 11 characters")
    private String code;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 50, message = "Name must be less than or equal to 50 characters")
    private String name;

    @NotBlank(message = "Gender cannot be blank")
    private String gender;

    private String image;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Date of birth is required")
    private LocalDate dob;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private Double salary;

    @Min(value = 1, message = "Level must be at least 1")
    @Max(value = 10, message = "Level must not exceed 10")
    private Integer level;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Size(max = 15, message = "Phone must be less than or equal to 15 characters")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number should be valid (only digits and optional '+')")
    private String phone;

    private String note;
}
