package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccountActivityPage extends BasePage {

    private static final Pattern ACCOUNT_ID_PARAM = Pattern.compile("id=(\\d+)");

    private final By transactionsTable = By.id("transactionTable");

    public AccountActivityPage(WebDriver driver) {
        super(driver);
    }

    public boolean isTransactionsTableDisplayed() {
        return wait.isAnyVisible(transactionsTable);
    }

    public boolean isTransactionListed(String textFragment) {
        return wait.isTextVisible(transactionsTable, textFragment);
    }

    public String getAccountIdFromUrl() {
        Matcher matcher = ACCOUNT_ID_PARAM.matcher(currentUrl());
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalStateException("No account id query parameter found in URL: " + currentUrl());
    }
}
