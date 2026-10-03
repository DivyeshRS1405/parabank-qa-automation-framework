package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RequestLoanPage extends BasePage {

    private final By amountInput = By.id("amount");
    private final By downPaymentInput = By.id("downPayment");
    private final By fromAccountSelect = By.id("fromAccountId");
    private final By applyNowButton = By.cssSelector("input[value='Apply Now']");
    private final By loanStatus = By.id("loanStatus");

    public RequestLoanPage(WebDriver driver) {
        super(driver);
    }

    public void submitLoanRequest(String amount, String downPayment, String fromAccountId) {
        wait.waitForVisibility(amountInput).sendKeys(amount);
        driver.findElement(downPaymentInput).sendKeys(downPayment);
        selectByValue(fromAccountSelect, fromAccountId);
        driver.findElement(applyNowButton).click();
    }

    public boolean isLoanProcessed() {
        return isHeadingDisplayed("Loan Request Processed");
    }

    public String getLoanStatus() {
        return wait.waitForVisibility(loanStatus).getText();
    }
}
