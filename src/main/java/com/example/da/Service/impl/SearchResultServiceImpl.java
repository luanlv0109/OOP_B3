package com.example.da.Service.impl;

import com.example.da.Service.SearchResultService;
import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import com.example.da.repository.SearchKeywordRepository;
import com.example.da.repository.SearchResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SearchResultServiceImpl implements SearchResultService {

    @Autowired
    private SearchKeywordRepository searchKeywordRepository;

    @Autowired
    private SearchResultRepository searchResultRepository;

    public Map<SearchKeyword, Map<Integer, List<String>>> getSearchResultsByDay(int month, int year) {
        return null;
    }

    public List<Map<String, Object>> getSearchResultsByDayNew(int month, int year) {
        List<SearchKeyword> keywords = searchKeywordRepository.findAll();
        List<Map<String, Object>> resultsList = new ArrayList<>();

        for (SearchKeyword keyword : keywords) {
            Map<String, Object> keywordData = new HashMap<>();
            keywordData.put("keyword", keyword.getKeyword());
            keywordData.put("desiredSuggestion", keyword.getDesiredSuggestion());
            keywordData.put("platform", keyword.getPlatform());

            // Danh sách chứa dữ liệu cho mỗi ngày
            List<Map<String, Object>> dailySuggestions = new ArrayList<>();

            // Khởi tạo danh sách cho mỗi ngày từ 1 đến 31
            for (int day = 1; day <= 31; day++) {
                Map<String, Object> suggestionData = new HashMap<>();
                suggestionData.put("day", day); // Thêm ngày vào cặp key-value

                // Lấy dữ liệu cho ngày hiện tại
                StringBuilder suggestionsForDay = new StringBuilder(); // Sử dụng StringBuilder để xây dựng chuỗi
                List<String> imgPaths = new ArrayList<>(); // Danh sách để lưu trữ đường dẫn hình ảnh

                List<SearchResult> searchResults = searchResultRepository.findBySearchKeywordAndMonthAndYearAndDay(keyword, month, year , day);

                for (SearchResult result : searchResults) {
                    if (result.getDay() == day) {
                        suggestionsForDay.append(result.getSuggestion()).append("<br>"); // Thêm suggestion vào chuỗi
                    }
                    if (result.getScreenshotPath() != null) { // Kiểm tra nếu đường dẫn hình ảnh không null
                        imgPaths.add(result.getScreenshotPath()); // Thêm vào danh sách hình ảnh
                    }
                }
                suggestionData.put("img", imgPaths); // Thêm đường dẫn hình ảnh vào suggestionData

                if (suggestionsForDay.length() == 0) {
                    suggestionData.put("suggestion", null); // Nếu không có dữ liệu, thêm {}
                } else {
                    suggestionData.put("suggestion", suggestionsForDay.toString()); // Gán chuỗi vào suggestion
                }

                dailySuggestions.add(suggestionData); // Thêm vào danh sách dailySuggestions
            }

            keywordData.put("data", dailySuggestions); // Gán danh sách vào keywordData
            resultsList.add(keywordData);
        }

        log.info("data: {}", resultsList);
        return resultsList;
    }


}
