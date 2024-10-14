package com.example.da.dto;


import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentDTO {
    @Valid
    private Long id;

    @NotBlank(message = "Code cannot be blank")
    @Size(max = 11, message = "Code must be less than or equal to 11 characters")
    private String code;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be less than or equal to 100 characters")
    private String name;
}
