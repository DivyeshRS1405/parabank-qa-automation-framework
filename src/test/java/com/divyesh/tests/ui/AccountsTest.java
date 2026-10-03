package com.divyesh.tests.ui;

import com.divyesh.framework.pages.AccountActivityPage;
import com.divyesh.framework.pages.AccountsOverviewPage;
import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.pages.OpenNewAccountPage;
import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class AccountsTest extends BaseTest {

    private RegistrationData registeredCustomer;

    @BeforeClass(alwaysRun = true)
    public void registerTestCustomer() {
        registeredCustomer = RegistrationHelper.registerInTemporaryBrowser();
    }

    private HomePage loginAsRegisteredCustomer() {
        var loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), registeredCustomer.password());
        return new HomePage(driver);
    }

    @Test(description = "The accounts overview page lists the account created at registration with a balance")
    public void accountsOverview_displaysRegisteredAccountWithBalance() {
        HomePage homePage = loginAsRegisteredCustomer();
        AccountsOverviewPage overviewPage = homePage.goToAccountsOverview();

        List<String> accountIds = overviewPage.getAccountIds();

        Assert.assertFalse(accountIds.isEmpty(), "A newly registered customer should have at least one account");
        Assert.assertTrue(overviewPage.getBalance(accountIds.get(0)) >= 0,
                "The account balance should be displayed as a non-negative amount");
    }

    @Test(description = "Opening an account from the overview page shows its transaction history")
    public void accountActivity_opensForSelectedAccountAndShowsTransactions() {
        HomePage homePage = loginAsRegisteredCustomer();
        AccountsOverviewPage overviewPage = homePage.goToAccountsOverview();
        String accountId = overviewPage.getAccountIds().get(0);

        AccountActivityPage activityPage = overviewPage.openAccount(accountId);

        Assert.assertEquals(activityPage.getAccountIdFromUrl(), accountId,
                "The account activity page should open for the account that was selected");
        Assert.assertTrue(activityPage.isTransactionsTableDisplayed(),
                "The transaction history table should be displayed for the account");
    }

    @Test(description = "Opening a new savings account adds a second account to the overview page")
    public void openingNewAccount_addsAccountToOverview() {
        HomePage homePage = loginAsRegisteredCustomer();
        AccountsOverviewPage overviewPage = homePage.goToAccountsOverview();
        String existingAccountId = overviewPage.getAccountIds().get(0);

        OpenNewAccountPage openNewAccountPage = new HomePage(driver).goToOpenNewAccount();
        String newAccountId = openNewAccountPage.openAccount(OpenNewAccountPage.SAVINGS, existingAccountId);

        AccountsOverviewPage updatedOverviewPage = new HomePage(driver).goToAccountsOverview();
        Assert.assertTrue(updatedOverviewPage.getAccountIds().contains(newAccountId),
                "The newly opened account should appear on the accounts overview page");
    }
}
