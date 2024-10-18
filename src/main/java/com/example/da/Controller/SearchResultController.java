package com.example.da.Controller;

import com.example.da.Service.SearchResultService;
import com.example.da.domain.SearchKeyword;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/admin/search-result")
public class SearchResultController {
    @Autowired
    private SearchResultService searchResultService;

    @GetMapping("/get-search-results")
    public String getSearchResults(Model model, @RequestParam("month") int month, @RequestParam("year") int year) {
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        log.info("gia tri dau vao thang: {}"  , month);
        log.info("gia tri dau năm       : {}"  , year);
        model.addAttribute("results", results);
        model.addAttribute("selectedMonth", month); // This sets the selected month
        model.addAttribute("selectedYear", year);   // This sets the selected year
        return "search-result/líst";
    }

    @PostMapping("/get-search-results")
    public String getSearchResultsPost(Model model, @RequestParam("month") int month, @RequestParam("year") int year) {
        log.info("Filter button pressed with month: {} and year: {}", month, year);
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        model.addAttribute("results", results);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        return "search-result/líst";
    }


    @GetMapping("/get-search-results-json")
    @ResponseBody
    public List<Map<String, Object>> getSearchResults(@RequestParam("month") int month, @RequestParam("year") int year) {
        // Sử dụng month và year từ tham số thay vì hardcoded
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);

        return results;  // Trả về kết quả dưới dạng JSON
    }
}
