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

            int check = 2;
            boolean hasZero = false;
            boolean hasTwo = false;

            List<Map<String, Object>> data = new ArrayList<>();
            for (int day = 1; day <= 31; day++) {
                Map<String, Object> dataSubItem = new HashMap<>();
                dataSubItem.put("day", day);

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
                dataSubItem.put("img", imgPaths);

                if (suggestionsForDay.length() == 0) {
                    dataSubItem.put("suggestion", null);
                } else {
                    int isMatched = searchResults.get(0).getIsMatched();
                    if (isMatched == 0) {
                        hasZero = true;
                    } else if (isMatched == 2) {
                        hasTwo = true;
                    }

                    dataSubItem.put("date" , searchResults.get(0).getCreatedAt().toString());
                    dataSubItem.put("suggestion", suggestionsForDay.toString());
                }

                data.add(dataSubItem);
            }

            if (hasZero && hasTwo) {
                check = 1;
            } else if (hasZero) {
                check = 0;
            } else if (hasTwo) {
                check = 2;
            }

            keywordData.put("match" , check);
            keywordData.put("data", data);
            resultsList.add(keywordData);
        }
        return resultsList;
    }
}
