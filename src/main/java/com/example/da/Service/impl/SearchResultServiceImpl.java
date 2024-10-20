package com.example.da.Service.impl;

import com.example.da.Service.SearchResultService;
import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import com.example.da.repository.SearchKeywordRepository;
import com.example.da.repository.SearchResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
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

            List<Map<String, Object>> dailySuggestions = new ArrayList<>();
            for (int day = 1; day <= 31; day++) {
                Map<String, Object> suggestionData = new HashMap<>();
                suggestionData.put("day", day);

                StringBuilder suggestionsForDay = new StringBuilder();
                List<String> imgPaths = new ArrayList<>();

                List<SearchResult> searchResults = searchResultRepository.findBySearchKeywordAndMonthAndYearAndDay(keyword, month, year, day);

                for (SearchResult result : searchResults) {
                    if (result.getDay() == day) {
                        suggestionsForDay.append(result.getSuggestion()).append("<br>");
                    }
                    if (result.getScreenshotPath() != null) {
                        imgPaths.add(result.getScreenshotPath());
                    }
                }
                suggestionData.put("img", imgPaths);

                if (suggestionsForDay.length() == 0) {
                    suggestionData.put("suggestion", null);
                } else {
                    suggestionData.put("suggestion", suggestionsForDay.toString());
                }
                if (searchResults.size() > 0) {
                    keywordData.put("match", searchResults.get(0).getIsMatched());
                    suggestionData.put("date" ,searchResults.get(0).getCreatedAt() );
                } else {
                    keywordData.put("match", 0);
                }
                dailySuggestions.add(suggestionData);
            }
            keywordData.put("data", dailySuggestions);
            resultsList.add(keywordData);
        }
        return resultsList;
    }
}
