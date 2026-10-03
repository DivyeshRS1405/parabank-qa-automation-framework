package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OpenNewAccountPage extends BasePage {

    public static final String CHECKING = "0";
    public static final String SAVINGS = "1";

    private final By accountTypeSelect = By.id("type");
    private final By fromAccountSelect = By.id("fromAccountId");
    private final By openAccountButton = By.cssSelector("input[value='Open New Account']");
    private final By newAccountId = By.id("newAccountId");

    public OpenNewAccountPage(WebDriver driver) {
        super(driver);
    }

    public String openAccount(String accountType, String fromAccountId) {
        selectByValue(accountTypeSelect, accountType);
        selectByValue(fromAccountSelect, fromAccountId);
        driver.findElement(openAccountButton).click();
        return wait.waitForVisibility(newAccountId).getText();
    }
}
