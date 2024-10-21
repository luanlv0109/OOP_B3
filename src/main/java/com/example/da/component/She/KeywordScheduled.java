package com.example.da.component.She;

import com.example.da.Service.FilesStorageService;
import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import com.example.da.repository.SearchKeywordRepository;
import com.example.da.repository.SearchResultRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class KeywordScheduled {
    @Autowired
    private FilesStorageService filesStorageService;
    @Autowired
    private SearchKeywordRepository searchKeywordRepository;
    @Autowired
    private SearchResultRepository searchResultRepository;

    @Scheduled(cron = "30 20 08 * * ?")
    public void processEndOfThereDayData() {
        LocalDate startDate = LocalDate.of(2024, 10, 1);
        LocalDate endDate = LocalDate.of(2024, 10, 16);
        while (!startDate.isAfter(endDate)) {
            runScheduledSearches(startDate);
            startDate = startDate.plusDays(1);
        }
    }
    @Scheduled(cron = "0 0 08 * * ?")
    public void processForDay(){
            runScheduledSearches(LocalDate.now());
    }
    public void runScheduledSearches(LocalDate date) {
        List<SearchKeyword> keywords = getAllKeywords();
        if (keywords.isEmpty()) {
            return;
        }
        for (SearchKeyword keyword : keywords) {
            performSearch(keyword, date);
        }
    }

    public void performSearch(SearchKeyword keyword, LocalDate date) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);

        try {
            if (keyword.getPlatform().equalsIgnoreCase("Google")) {
                driver.get("https://www.google.com");
                WebElement searchBox = driver.findElement(By.name("q"));
                searchBox.sendKeys(keyword.getKeyword());
            } else if (keyword.getPlatform().equalsIgnoreCase("Yahoo")) {
                driver.get("https://search.yahoo.com");
                WebElement searchBox = driver.findElement(By.name("p"));
                searchBox.sendKeys(keyword.getKeyword());
            } else {
                return;
            }

            Thread.sleep(2000);
            List<WebElement> suggestions = driver.findElements(By.xpath("//ul[@role='listbox']//li"));

            StringBuilder suggestionsHtml = new StringBuilder();
            int isMatched = 0;

            for (WebElement suggestion : suggestions) {
                String suggestionText = suggestion.getText();

                if (suggestionText.equalsIgnoreCase(keyword.getDesiredSuggestion())) {
                    suggestionsHtml.append("<strong>").append(suggestionText).append("</strong><br>");
                    isMatched = 2;
                } else if (suggestionText.toLowerCase().contains(keyword.getDesiredSuggestion().toLowerCase())) {
                    suggestionsHtml.append(suggestionText).append("<br>");
                    isMatched = Math.max(isMatched, 1);
                } else {
                    suggestionsHtml.append(suggestionText).append("<br>");
                }
            }

            saveSearchResult(keyword, suggestionsHtml.toString(), isMatched, takeScreenshot(driver, keyword), date);

        } catch (InterruptedException | IOException e) {
        } finally {
            driver.quit();
        }
    }

    private String takeScreenshot(WebDriver driver, SearchKeyword keyword) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        MultipartFile multipartFile = new MultipartFile() {
            @Override
            public String getName() {
                return screenshot.getName();
            }

            @Override
            public String getOriginalFilename() {
                return keyword.getKeyword() + ".png";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return screenshot.length() == 0;
            }

            @Override
            public long getSize() {
                return screenshot.length();
            }

            @Override
            public byte[] getBytes() throws IOException {
                return Files.readAllBytes(screenshot.toPath());
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new FileInputStream(screenshot);
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.copy(screenshot.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        };

        return filesStorageService.save(multipartFile);
    }

    public void saveSearchResult(SearchKeyword keyword, String suggestion, int isMatched, String screenshotPath, LocalDate date) {
        SearchResult result = new SearchResult();
        result.setKeyword(keyword.getKeyword());
        result.setSuggestion(suggestion);
        result.setIsMatched(isMatched);
        result.setSearchKeyword(keyword);
        result.setScreenshotPath(screenshotPath);
        result.setDay(date.getDayOfMonth());
        result.setMonth(date.getMonthValue());
        result.setYear(date.getYear());
        result.setCreatedAt(LocalDateTime.now());
        searchResultRepository.save(result);

    }

    public List<SearchKeyword> getAllKeywords() {
        return searchKeywordRepository.findAll();
    }
}
