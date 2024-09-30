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
@Table(name = "achievements")
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thiết lập quan hệ Many-to-One với Employee
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;  // Chỉ cần ánh xạ qua quan hệ này

    @Column(name = "type")
    private Integer type;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "date")
    private LocalDate date;
}