package com.example.da.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;

public class WebDriverFactory {

    // Tạo WebDriver tùy theo nền tảng
    public static WebDriver getDriver(String platform) {
        switch (platform.toLowerCase()) {
//            case "chrome":
//                System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
//                ChromeOptions chromeOptions = new ChromeOptions();
//                chromeOptions.addArguments("--headless"); // Chạy Chrome ở chế độ không hiển thị
//                return new ChromeDriver(chromeOptions);
//
//            case "firefox":
////                System.setProperty("webdriver.gecko.driver", "path/to/geckodriver");
////                FirefoxOptions firefoxOptions = new FirefoxOptions();
////                firefoxOptions.setHeadless(true); // Chạy Firefox ở chế độ không hiển thị
////                return new FirefoxDriver(firefoxOptions);
//
//            case "edge":
//                System.setProperty("webdriver.edge.driver", "path/to/edgedriver");
//                return new EdgeDriver();  // Edge không hỗ trợ headless mode tốt như Chrome và Firefox
//
//            case "safari":
//                return new SafariDriver();  // Safari hiện không hỗ trợ headless mode, chỉ chạy trên MacOS

            default:
                throw new IllegalArgumentException("Nền tảng không được hỗ trợ: " + platform);
        }
    }
}
