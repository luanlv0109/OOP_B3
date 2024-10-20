package com.example.da.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String keyword;
    private String suggestion;
    private String screenshotPath;
    private int isMatched;

    private int day;
    private int month;
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_keyword_id")
    private SearchKeyword searchKeyword;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
}
