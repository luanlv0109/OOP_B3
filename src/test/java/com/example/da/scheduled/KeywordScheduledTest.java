package com.example.da.scheduled;

import com.example.da.Service.FilesStorageService;
import com.example.da.component.She.KeywordScheduled;
import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import com.example.da.repository.SearchKeywordRepository;
import com.example.da.repository.SearchResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class KeywordScheduledTest {

    @Mock
    private FilesStorageService filesStorageService;

    @Mock
    private SearchKeywordRepository searchKeywordRepository;

    @Mock
    private SearchResultRepository searchResultRepository;

    @InjectMocks
    private KeywordScheduled keywordScheduled;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testProcessEndOfThereDayData() {
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword("example");
        searchKeyword.setPlatform("Google");
        searchKeyword.setDesiredSuggestion("example suggestion");

        when(searchKeywordRepository.findAll()).thenReturn(List.of(searchKeyword));

        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 1); // Mock dữ liệu cho 1 ngày

        keywordScheduled.processEndOfThereDayData();

        verify(searchKeywordRepository, times(1)).findAll();
    }

    @Test
    public void testRunScheduledSearchesWithNoKeywords() {
        when(searchKeywordRepository.findAll()).thenReturn(Collections.emptyList());

        keywordScheduled.runScheduledSearches(LocalDate.now());

        verify(searchKeywordRepository, times(1)).findAll();
        verifyNoInteractions(searchResultRepository);
    }

    @Test
    public void testPerformSearchWithGoogleKeyword() throws Exception {
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword("example");
        searchKeyword.setPlatform("Google");
        searchKeyword.setDesiredSuggestion("example suggestion");

        WebDriver mockDriver = mock(ChromeDriver.class);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        when(searchKeywordRepository.findAll()).thenReturn(List.of(searchKeyword));

        keywordScheduled.performSearch(searchKeyword, LocalDate.now());

        verify(searchKeywordRepository, times(1)).findAll();
    }

    @Test
    public void testSaveSearchResult() {
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword("test keyword");

        LocalDate date = LocalDate.of(2024, 10, 1);
        String suggestionHtml = "sample suggestion";
        int isMatched = 1;
        String screenshotPath = "sample/path.png";

        keywordScheduled.saveSearchResult(searchKeyword, suggestionHtml, isMatched, screenshotPath, date);

        verify(searchResultRepository, times(1)).save(any(SearchResult.class));
    }
}
