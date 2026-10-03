package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * The left-hand navigation menu shown on every page once a customer is logged in.
 */
public class HomePage extends BasePage {

    private final By accountsOverviewLink = By.linkText("Accounts Overview");
    private final By openNewAccountLink = By.linkText("Open New Account");
    private final By transferFundsLink = By.linkText("Transfer Funds");
    private final By billPayLink = By.linkText("Bill Pay");
    private final By findTransactionsLink = By.linkText("Find Transactions");
    private final By updateContactInfoLink = By.linkText("Update Contact Info");
    private final By requestLoanLink = By.linkText("Request Loan");
    private final By logOutLink = By.linkText("Log Out");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoggedIn() {
        return !driver.findElements(logOutLink).isEmpty();
    }

    public AccountsOverviewPage goToAccountsOverview() {
        wait.waitForClickability(accountsOverviewLink).click();
        return new AccountsOverviewPage(driver);
    }

    public OpenNewAccountPage goToOpenNewAccount() {
        wait.waitForClickability(openNewAccountLink).click();
        return new OpenNewAccountPage(driver);
    }

    public TransferFundsPage goToTransferFunds() {
        wait.waitForClickability(transferFundsLink).click();
        return new TransferFundsPage(driver);
    }

    public BillPayPage goToBillPay() {
        wait.waitForClickability(billPayLink).click();
        return new BillPayPage(driver);
    }

    public FindTransactionsPage goToFindTransactions() {
        wait.waitForClickability(findTransactionsLink).click();
        return new FindTransactionsPage(driver);
    }

    public UpdateProfilePage goToUpdateProfile() {
        wait.waitForClickability(updateContactInfoLink).click();
        return new UpdateProfilePage(driver);
    }

    public RequestLoanPage goToRequestLoan() {
        wait.waitForClickability(requestLoanLink).click();
        return new RequestLoanPage(driver);
    }

    public LoginPage logout() {
        wait.waitForClickability(logOutLink).click();
        return new LoginPage(driver);
    }
}
