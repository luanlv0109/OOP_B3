package com.example.da.Controller;

import com.example.da.Service.SearchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/search-result")
public class SearchResultController {
    @Autowired
    private SearchResultService searchResultService;


    @GetMapping("/list")
    public String showListPage(Model model, @RequestParam("month") int month, @RequestParam("year") int year) {
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        model.addAttribute("results", results);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        return "search-result/list"; // Hiển thị list.html
    }

    @PostMapping("/list")
    public String GetListPage(Model model, @RequestParam("month") int month, @RequestParam("year") int year) {
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        model.addAttribute("results", results);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        return "search-result/list"; // Hiển thị list.html
    }

    @GetMapping("/detail-list")
    public String showDetailListPage(Model model, @RequestParam("month") int month, @RequestParam("year") int year) {
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        model.addAttribute("results", results);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        return "search-result/detail-list";
    }

    @PostMapping("/detail-list")
    public String getDetailListPage(Model model, @RequestParam("month") int month, @RequestParam("year") int year) {
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        model.addAttribute("results", results);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        return "search-result/detail-list";
    }

    @GetMapping("/get-search-results-json")
    @ResponseBody
    public List<Map<String, Object>> getSearchResults(@RequestParam("month") int month, @RequestParam("year") int year) {
        List<Map<String, Object>> results = searchResultService.getSearchResultsByDayNew(month, year);
        return results;
    }
}
