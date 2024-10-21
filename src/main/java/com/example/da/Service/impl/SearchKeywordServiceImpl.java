package com.example.da.Service.impl;

import com.example.da.Service.SearchKeywordService;
import com.example.da.domain.SearchKeyword;
import com.example.da.dto.SearchKeywordDTO;
import com.example.da.repository.SearchKeywordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SearchKeywordServiceImpl implements SearchKeywordService {
    @Autowired
    private SearchKeywordRepository searchKeywordRepository;

    @Override
    public boolean addKeyword(SearchKeywordDTO searchKeywordDTO) {
        Optional<SearchKeyword> existingKeyword = searchKeywordRepository.findByKeywordAndDesiredSuggestion(
                searchKeywordDTO.getKeyword(), searchKeywordDTO.getDesiredSuggestion()
        );

        if (existingKeyword.isPresent()) {
            return false;
        }

        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword(searchKeywordDTO.getKeyword());
        searchKeyword.setDesiredSuggestion(searchKeywordDTO.getDesiredSuggestion());
        searchKeyword.setPlatform(searchKeywordDTO.getPlatform());
        searchKeyword.setScheduleFrequency(searchKeywordDTO.getScheduleFrequency());
        searchKeyword.setTimesPerDay(searchKeywordDTO.getTimesPerDay());
        searchKeyword.setTimesPerWeek(searchKeywordDTO.getTimesPerWeek());
        searchKeyword.setTimesPerMonth(searchKeywordDTO.getTimesPerMonth());
        searchKeyword.setCreatedAt(LocalDate.now());

        searchKeywordRepository.save(searchKeyword);

        return true;
    }
}
