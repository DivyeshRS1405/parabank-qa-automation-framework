package com.divyesh.framework.pages;

import com.divyesh.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Every ParaBank content page renders inside the same #rightPanel container and reports
 * outcomes through headings and .error elements, so those accessors live here.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitUtils wait;

    private final By pageHeading = By.cssSelector("#rightPanel h1");
    private final By validationMessages = By.cssSelector(".error");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitUtils(driver);
    }

    public String getPageHeading() {
        return wait.waitForFirstVisible(pageHeading).getText();
    }

    public boolean isHeadingDisplayed(String expectedHeading) {
        return wait.isTextVisible(pageHeading, expectedHeading);
    }

    public boolean isValidationMessageDisplayed() {
        return wait.isAnyVisible(validationMessages);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    protected void selectByValue(By selectLocator, String value) {
        wait.waitForOptionValue(selectLocator, value);
        new Select(driver.findElement(selectLocator)).selectByValue(value);
    }
}
