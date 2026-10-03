package com.divyesh.framework.utils;

import com.divyesh.framework.config.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class WaitUtils {

    private final WebDriverWait wait;

    public WaitUtils(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicitWait")));
        wait.ignoring(StaleElementReferenceException.class);
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitForPresenceOfAll(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    public boolean waitForUrlContains(String fragment) {
        return wait.until(ExpectedConditions.urlContains(fragment));
    }

    public boolean waitForTitleContains(String fragment) {
        return wait.until(ExpectedConditions.titleContains(fragment));
    }

    // Several ParaBank pages keep hidden and visible headings/messages in the DOM together,
    // so the first displayed match is needed rather than the first match.
    public WebElement waitForFirstVisible(By locator) {
        return wait.until((ExpectedCondition<WebElement>) driver -> driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElse(null));
    }

    public boolean isAnyVisible(By locator) {
        try {
            waitForFirstVisible(locator);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isTextVisible(By locator, String expectedText) {
        try {
            return wait.until((ExpectedCondition<Boolean>) driver -> driver.findElements(locator).stream()
                    .anyMatch(element -> element.isDisplayed() && element.getText().contains(expectedText)));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitForOptionValue(By selectLocator, String value) {
        wait.until((ExpectedCondition<Boolean>) driver -> new Select(driver.findElement(selectLocator)).getOptions()
                .stream()
                .anyMatch(option -> value.equals(option.getAttribute("value"))));
    }
}
