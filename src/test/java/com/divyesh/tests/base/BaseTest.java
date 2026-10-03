package com.divyesh.tests.base;

import com.divyesh.framework.config.ConfigReader;
import com.divyesh.framework.driver.DriverFactory;
import com.divyesh.framework.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        DriverFactory.initDriver();
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected LoginPage openLoginPage() {
        driver.get(ConfigReader.get("baseUrl"));
        return new LoginPage(driver);
    }
}
