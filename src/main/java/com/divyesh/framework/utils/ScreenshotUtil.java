package com.divyesh.framework.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private static final Path SCREENSHOT_DIR = Path.of("reports", "screenshots");
    private static final DateTimeFormatter TIMESTAMP = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");

    private ScreenshotUtil() {
    }

    public static Path capture(WebDriver driver, String testName) {
        try {
            Files.createDirectories(SCREENSHOT_DIR);
            String fileName = testName.replaceAll("[^A-Za-z0-9_-]", "_") + "_"
                    + LocalDateTime.now().format(TIMESTAMP) + ".png";
            Path target = SCREENSHOT_DIR.resolve(fileName);
            Files.write(target, ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES));
            return target;
        } catch (IOException e) {
            System.err.println("Could not save screenshot for " + testName + ": " + e.getMessage());
            return null;
        }
    }
}
