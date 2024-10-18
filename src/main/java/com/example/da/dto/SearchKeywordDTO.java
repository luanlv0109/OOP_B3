package com.example.da.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@   Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchKeywordDTO {
    private String keyword;  // Từ khóa tìm kiếm
    private String desiredSuggestion;  // Gợi ý mong muốn
    private String platform;  // Nền tảng tìm kiếm (Google, Yahoo, v.v.)
    private String scheduleFrequency;  // Tần suất tìm kiếm (daily, weekly, monthly)
    private int timesPerDay;  // Số lần tìm kiếm trong ngày
    private int timesPerWeek;  // Số lần tìm kiếm trong tuần
    private int timesPerMonth;  // Số lần tìm kiếm trong tháng
}