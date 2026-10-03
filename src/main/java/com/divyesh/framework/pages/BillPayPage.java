package com.divyesh.framework.pages;

import com.divyesh.framework.utils.TestDataFactory.PayeeData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class BillPayPage extends BasePage {

    private final By payeeName = By.name("payee.name");
    private final By payeeStreet = By.name("payee.address.street");
    private final By payeeCity = By.name("payee.address.city");
    private final By payeeState = By.name("payee.address.state");
    private final By payeeZip = By.name("payee.address.zipCode");
    private final By payeePhone = By.name("payee.phoneNumber");
    private final By payeeAccountNumber = By.name("payee.accountNumber");
    private final By verifyAccountNumber = By.name("verifyAccount");
    private final By amount = By.name("amount");
    private final By fromAccountSelect = By.cssSelector("select[name='fromAccountId'], select#fromAccountId");
    private final By sendPaymentButton = By.cssSelector("input[value='Send Payment']");

    public BillPayPage(WebDriver driver) {
        super(driver);
    }

    public void fillForm(PayeeData payee, String paymentAmount, String fromAccountId) {
        wait.waitForVisibility(payeeName).sendKeys(payee.name());
        driver.findElement(payeeStreet).sendKeys(payee.street());
        driver.findElement(payeeCity).sendKeys(payee.city());
        driver.findElement(payeeState).sendKeys(payee.state());
        driver.findElement(payeeZip).sendKeys(payee.zipCode());
        driver.findElement(payeePhone).sendKeys(payee.phoneNumber());
        driver.findElement(payeeAccountNumber).sendKeys(payee.accountNumber());
        driver.findElement(verifyAccountNumber).sendKeys(payee.accountNumber());
        driver.findElement(amount).sendKeys(paymentAmount);
        selectByValue(fromAccountSelect, fromAccountId);
    }

    public void clickSendPayment() {
        driver.findElement(sendPaymentButton).click();
    }

    public void payBill(PayeeData payee, String paymentAmount, String fromAccountId) {
        fillForm(payee, paymentAmount, fromAccountId);
        clickSendPayment();
    }

    public boolean isPaymentComplete() {
        return isHeadingDisplayed("Bill Payment Complete");
    }
}
