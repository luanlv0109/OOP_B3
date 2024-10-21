package com.example.da.Service;

import com.example.da.Service.impl.SearchKeywordServiceImpl;
import com.example.da.domain.SearchKeyword;
import com.example.da.dto.SearchKeywordDTO;
import com.example.da.repository.SearchKeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SearchKeywordServiceTest {
    @Mock
    private SearchKeywordRepository searchKeywordRepository;

    @InjectMocks
    private SearchKeywordServiceImpl searchKeywordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddKeyword_Successful() {
        SearchKeywordDTO searchKeywordDTO = new SearchKeywordDTO();
        searchKeywordDTO.setKeyword("example");
        searchKeywordDTO.setDesiredSuggestion("desired suggestion");
        searchKeywordDTO.setPlatform("Google");
        searchKeywordDTO.setScheduleFrequency("daily");
        searchKeywordDTO.setTimesPerDay(2);
        searchKeywordDTO.setTimesPerWeek(3);
        searchKeywordDTO.setTimesPerMonth(4);

        when(searchKeywordRepository.findByKeywordAndDesiredSuggestion(
                searchKeywordDTO.getKeyword(), searchKeywordDTO.getDesiredSuggestion()))
                .thenReturn(Optional.empty());

        boolean result = searchKeywordService.addKeyword(searchKeywordDTO);

        assertTrue(result);

        verify(searchKeywordRepository, times(1)).save(any(SearchKeyword.class));
    }

    @Test
    void testAddKeyword_KeywordAlreadyExists() {
        SearchKeywordDTO searchKeywordDTO = new SearchKeywordDTO();
        searchKeywordDTO.setKeyword("example");
        searchKeywordDTO.setDesiredSuggestion("desired suggestion");

        SearchKeyword existingKeyword = new SearchKeyword();
        existingKeyword.setKeyword("example");
        existingKeyword.setDesiredSuggestion("desired suggestion");

        when(searchKeywordRepository.findByKeywordAndDesiredSuggestion(
                searchKeywordDTO.getKeyword(), searchKeywordDTO.getDesiredSuggestion()))
                .thenReturn(Optional.of(existingKeyword));

        boolean result = searchKeywordService.addKeyword(searchKeywordDTO);

        assertFalse(result);
        verify(searchKeywordRepository, never()).save(any(SearchKeyword.class));
    }
}
