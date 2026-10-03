package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AccountsOverviewPage extends BasePage {

    private final By accountsTable = By.id("accountTable");
    private final By accountRows = By.cssSelector("#accountTable tbody tr");
    private final By accountIdLinks = By.cssSelector("#accountTable tbody tr td:first-child a");

    public AccountsOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAccountsTableDisplayed() {
        return wait.isAnyVisible(accountsTable);
    }

    public List<String> getAccountIds() {
        return wait.waitForPresenceOfAll(accountIdLinks).stream().map(WebElement::getText).toList();
    }

    public double getBalance(String accountId) {
        wait.waitForPresenceOfAll(accountIdLinks);
        for (WebElement row : driver.findElements(accountRows)) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() > 1 && cells.get(0).getText().trim().equals(accountId)) {
                return parseAmount(cells.get(1).getText());
            }
        }
        throw new IllegalArgumentException("Account " + accountId + " not found on the accounts overview page");
    }

    public AccountActivityPage openAccount(String accountId) {
        wait.waitForPresenceOfAll(accountIdLinks);
        wait.waitForClickability(By.linkText(accountId)).click();
        return new AccountActivityPage(driver);
    }

    private static double parseAmount(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.\\-]", ""));
    }
}
