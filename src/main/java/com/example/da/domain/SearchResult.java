package com.example.da.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Tự động tạo ID
    private Long id;

    private String keyword;  // Từ khóa đã được tìm kiếm
    private String suggestion;  // Gợi ý trả về từ tìm kiếm
    private String screenshotPath;  // Đường dẫn đến ảnh chụp màn hình
    private boolean isMatched;  // Đúng nếu gợi ý khớp với gợi ý mong muốn

    // Thêm các cột riêng cho ngày, tháng và năm
    private int day;    // Ngày
    private int month;  // Tháng
    private int year;   // Năm

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "search_keyword_id")  // Tạo cột lưu tham chiếu tới SearchKeyword
    private SearchKeyword searchKeyword;  // Tham chiếu tới từ khóa đã sinh ra kết quả này

    // Các getter và setter cho mỗi thuộc tính
}
