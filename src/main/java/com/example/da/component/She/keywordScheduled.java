package com.example.da.component.She;

import com.example.da.Service.FilesStorageService;
import com.example.da.domain.SearchKeyword;
import com.example.da.domain.SearchResult;
import com.example.da.repository.SearchKeywordRepository;
import com.example.da.repository.SearchResultRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
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
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class keywordScheduled {
    @Autowired
    private FilesStorageService filesStorageService;
    @Autowired
    private SearchKeywordRepository searchKeywordRepository;
    @Autowired
    private SearchResultRepository searchResultRepository;

    private final Path root = Path.of("./uploads");

//    @Scheduled(cron = "0 */1 * * * ?")
    public void runScheduledSearches() {
        List<SearchKeyword> keywords = getAllKeywords();

        if (keywords.isEmpty()) {
            return;
        }

        for (SearchKeyword keyword : keywords) {
            performSearch(keyword);
        }
    }

    public void performSearch(SearchKeyword keyword) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://www.google.com");

            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys(keyword.getKeyword());
            Thread.sleep(2000);

            List<WebElement> suggestions = driver.findElements(By.xpath("//ul[@role='listbox']//li"));
            StringBuilder suggestionsHtml = new StringBuilder();
            boolean isMatched = false;  // Biến này sẽ theo dõi xem có gợi ý nào khớp không

            for (WebElement suggestion : suggestions) {
                String suggestionText = suggestion.getText();
                log.info("Gợi ý nhận được: {}", suggestionText);

                // So sánh với gợi ý mong muốn (desiredSuggestion)
                if (suggestionText.equalsIgnoreCase(keyword.getDesiredSuggestion())) {
                    log.info("Gợi ý '{}' đã khớp với gợi ý mong muốn: '{}'", suggestionText, keyword.getDesiredSuggestion());
                    // Bọc gợi ý khớp với thẻ <strong>
                    suggestionsHtml.append("<strong>").append(suggestionText).append("</strong><br>");
                    isMatched = true;  // Ghi nhận rằng đã tìm thấy gợi ý khớp
                } else {
                    log.info("Gợi ý '{}' không khớp với gợi ý mong muốn: '{}'", suggestionText, keyword.getDesiredSuggestion());
                    // Thêm gợi ý vào mà không cần bọc <strong>
                    suggestionsHtml.append(suggestionText).append("<br>");
                }
            }

// Lưu kết quả tìm kiếm với tất cả các gợi ý được bọc HTML
            saveSearchResult(keyword, suggestionsHtml.toString(), isMatched ,  takeScreenshot(driver, keyword));


            Thread.sleep(2000);
        } catch (InterruptedException | IOException e) {
            log.error("Lỗi khi thực hiện tìm kiếm cho từ khóa {}: {}", keyword.getKeyword(), e.getMessage(), e);
        } finally {
            driver.quit();
        }
    }

    private String takeScreenshot(WebDriver driver, SearchKeyword keyword) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Chuyển file ảnh chụp màn hình thành MultipartFile
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
                Files.copy(screenshot.toPath(), dest.toPath());
            }
        };

        // Gọi phương thức save của FilesStorageService để lưu file
        return filesStorageService.save(multipartFile);

    }

        private void saveSearchResult(SearchKeyword keyword, String suggestion, boolean isMatched , String screenshotPath) {
        SearchResult result = new SearchResult();
        result.setKeyword(keyword.getKeyword());
        result.setSuggestion(suggestion);
        result.setMatched(isMatched);
        result.setSearchKeyword(keyword);
        result.setScreenshotPath(screenshotPath);  // Lưu đường dẫn ảnh vào DB

            // Lấy thời gian hiện tại và lưu vào các cột ngày, tháng, năm
            LocalDateTime now = LocalDateTime.now();
            result.setDay(now.getDayOfMonth());
            result.setMonth(now.getMonthValue());
            result.setYear(now.getYear());
        searchResultRepository.save(result);
    }

    public List<SearchKeyword> getAllKeywords() {
        return searchKeywordRepository.findAll();
    }
}
