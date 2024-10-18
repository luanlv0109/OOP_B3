package com.example.da.Service;

import com.example.da.domain.SearchKeyword;

import java.util.List;
import java.util.Map;

public interface SearchResultService {
    Map<SearchKeyword, Map<Integer, List<String>>> getSearchResultsByDay(int month, int year);
    List<Map<String, Object>> getSearchResultsByDayNew(int month, int year);
}
