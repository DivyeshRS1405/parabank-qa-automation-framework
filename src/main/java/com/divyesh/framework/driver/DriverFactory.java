package com.divyesh.framework.driver;

import com.divyesh.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.function.Function;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        driverThreadLocal.set(createDriver(ConfigReader.get("browser"), ConfigReader.getBoolean("headless")));
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }

    public static <T> T callWithTemporaryDriver(Function<WebDriver, T> action) {
        initDriver();
        try {
            return action.apply(getDriver());
        } finally {
            quitDriver();
        }
    }

    private static WebDriver createDriver(String browser, boolean headless) {
        return switch (browser.trim().toLowerCase()) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--window-size=1920,1080");
                }
                yield new ChromeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) {
                    options.addArguments("-headless", "--width=1920", "--height=1080");
                }
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) {
                    options.addArguments("--headless=new", "--window-size=1920,1080");
                }
                yield new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }
}
