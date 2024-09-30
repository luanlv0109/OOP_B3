package com.example.da.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "FK_department_employee"))

    private Department department;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "gender")
    private String gender;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private LocalDate dob;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "level")
    private Integer level;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

}
