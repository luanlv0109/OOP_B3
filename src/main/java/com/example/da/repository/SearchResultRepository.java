package com.example.da.repository;

import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, Long> {
    // Bạn có thể thêm các phương thức tùy chỉnh nếu cần thiết ở đây
    List<SearchResult> findBySearchKeywordAndMonthAndYearAndDay(SearchKeyword searchKeyword, int month, int year ,int days );

}
