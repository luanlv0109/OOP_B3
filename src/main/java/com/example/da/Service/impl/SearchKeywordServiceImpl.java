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
        // Kiểm tra xem từ khóa đã tồn tại hay chưa (có thể kiểm tra theo tên từ khóa hoặc gợi ý mong muốn)
        Optional<SearchKeyword> existingKeyword = searchKeywordRepository.findByKeywordAndDesiredSuggestion(
                searchKeywordDTO.getKeyword(), searchKeywordDTO.getDesiredSuggestion()
        );

        // Nếu từ khóa đã tồn tại, trả về false
        if (existingKeyword.isPresent()) {
            return false;
        }

        // Nếu chưa tồn tại, tạo mới một SearchKeyword và lưu vào cơ sở dữ liệu
        SearchKeyword searchKeyword = new SearchKeyword();
        searchKeyword.setKeyword(searchKeywordDTO.getKeyword());
        searchKeyword.setDesiredSuggestion(searchKeywordDTO.getDesiredSuggestion());
        searchKeyword.setPlatform(searchKeywordDTO.getPlatform());
        searchKeyword.setScheduleFrequency(searchKeywordDTO.getScheduleFrequency());
        searchKeyword.setTimesPerDay(searchKeywordDTO.getTimesPerDay());
        searchKeyword.setTimesPerWeek(searchKeywordDTO.getTimesPerWeek());
        searchKeyword.setTimesPerMonth(searchKeywordDTO.getTimesPerMonth());
        searchKeyword.setCreatedAt(LocalDate.now());  // Gán ngày tạo hiện tại

        // Lưu từ khóa vào cơ sở dữ liệu
        searchKeywordRepository.save(searchKeyword);

        return true;
    }
}
