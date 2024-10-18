package com.example.da.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tạo ID
    private Long id;

    private String keyword;  // Từ khóa tìm kiếm
    private String desiredSuggestion;  // Gợi ý mong muốn xuất hiện
    private String platform;  // Nền tảng để tìm kiếm (Google, Yahoo, v.v.)
    private String scheduleFrequency;  // Tần suất tìm kiếm: "daily", "weekly", "monthly"
    private int timesPerDay;  // Số lần tìm kiếm trong ngày
    private int timesPerWeek;  // Số lần tìm kiếm trong tuần
    private int timesPerMonth;  // Số lần tìm kiếm trong tháng
    private LocalDate createdAt;  // Ngày tạo từ khóa
}
