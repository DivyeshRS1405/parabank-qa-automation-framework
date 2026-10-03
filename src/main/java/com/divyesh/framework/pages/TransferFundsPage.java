package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TransferFundsPage extends BasePage {

    private final By amountInput = By.id("amount");
    private final By fromAccountSelect = By.id("fromAccountId");
    private final By toAccountSelect = By.id("toAccountId");
    private final By transferButton = By.cssSelector("input[value='Transfer']");

    public TransferFundsPage(WebDriver driver) {
        super(driver);
    }

    public void transfer(String amount, String fromAccountId, String toAccountId) {
        selectByValue(fromAccountSelect, fromAccountId);
        selectByValue(toAccountSelect, toAccountId);
        WebElement amountField = wait.waitForVisibility(amountInput);
        amountField.clear();
        amountField.sendKeys(amount);
        driver.findElement(transferButton).click();
    }

    public boolean isTransferComplete() {
        return isHeadingDisplayed("Transfer Complete");
    }
}
