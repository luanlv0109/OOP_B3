package com.example.da.Service;

import com.example.da.Service.impl.SearchResultServiceImpl;
import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import com.example.da.repository.SearchKeywordRepository;
import com.example.da.repository.SearchResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchResultServiceTest {

    @Mock
    private SearchKeywordRepository searchKeywordRepository;

    @Mock
    private SearchResultRepository searchResultRepository;

    @InjectMocks
    private SearchResultServiceImpl searchResultService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSearchResultsByDayNew() {
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword("example");
        searchKeyword.setDesiredSuggestion("example suggestion");
        searchKeyword.setPlatform("Google");

        SearchResult searchResult = new SearchResult();
        searchResult.setSuggestion("example suggestion");
        searchResult.setScreenshotPath("path/to/screenshot.png");
        searchResult.setIsMatched(2);
        searchResult.setDay(1);
        searchResult.setMonth(10);
        searchResult.setYear(2024);
        searchResult.setCreatedAt(LocalDateTime.now());

        when(searchKeywordRepository.findAll()).thenReturn(List.of(searchKeyword));

        when(searchResultRepository.findBySearchKeywordAndMonthAndYearAndDay(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(List.of(searchResult));

        List<Map<String, Object>> result = searchResultService.getSearchResultsByDayNew(10, 2024);

        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, Object> keywordData = result.get(0);
        assertEquals("example", keywordData.get("keyword"));
        assertEquals("example suggestion", keywordData.get("desiredSuggestion"));
        assertEquals("Google", keywordData.get("platform"));

        List<Map<String, Object>> data = (List<Map<String, Object>>) keywordData.get("data");
        assertNotNull(data);
        assertEquals(31, data.size());

        Map<String, Object> firstDayData = data.get(0);
        assertEquals(1, firstDayData.get("day"));
        assertEquals("example suggestion<br>", firstDayData.get("suggestion"));
        assertEquals("path/to/screenshot.png", ((List<String>) firstDayData.get("img")).get(0));
    }

    @Test
    void testGetSearchResultsWithNoResults() {
        when(searchKeywordRepository.findAll()).thenReturn(new ArrayList<>());

        List<Map<String, Object>> result = searchResultService.getSearchResultsByDayNew(10, 2024);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetSearchResultsWithNoSearchResults() {
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword("example");
        searchKeyword.setDesiredSuggestion("example suggestion");
        searchKeyword.setPlatform("Google");

        when(searchKeywordRepository.findAll()).thenReturn(List.of(searchKeyword));

        when(searchResultRepository.findBySearchKeywordAndMonthAndYearAndDay(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(new ArrayList<>());

        List<Map<String, Object>> result = searchResultService.getSearchResultsByDayNew(10, 2024);

        assertNotNull(result);
        assertEquals(1, result.size());

        Map<String, Object> keywordData = result.get(0);
        List<Map<String, Object>> data = (List<Map<String, Object>>) keywordData.get("data");
        assertNotNull(data);
        for (Map<String, Object> dayData : data) {
            assertNull(dayData.get("suggestion"));
        }
    }
}
