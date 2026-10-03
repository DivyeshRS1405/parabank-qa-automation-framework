package com.divyesh.tests.ui;

import com.divyesh.framework.driver.DriverFactory;
import com.divyesh.framework.pages.AccountsOverviewPage;
import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.pages.OpenNewAccountPage;
import com.divyesh.framework.pages.TransferFundsPage;
import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TransferFundsTest extends BaseTest {

    private RegistrationData registeredCustomer;
    private String fromAccountId;
    private String toAccountId;

    @BeforeClass(alwaysRun = true)
    public void registerCustomerWithTwoAccounts() {
        DriverFactory.initDriver();
        var setupDriver = DriverFactory.getDriver();

        registeredCustomer = RegistrationHelper.registerNewCustomer(setupDriver);
        AccountsOverviewPage overviewPage = new HomePage(setupDriver).goToAccountsOverview();
        fromAccountId = overviewPage.getAccountIds().get(0);

        OpenNewAccountPage openNewAccountPage = new HomePage(setupDriver).goToOpenNewAccount();
        toAccountId = openNewAccountPage.openAccount(OpenNewAccountPage.SAVINGS, fromAccountId);

        DriverFactory.quitDriver();
    }

    private AccountsOverviewPage loginAndOpenOverview() {
        var loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), registeredCustomer.password());
        return new HomePage(driver).goToAccountsOverview();
    }

    @Test(description = "Transferring between two of the customer's own accounts updates both balances")
    public void validTransfer_updatesSourceAndDestinationBalances() {
        AccountsOverviewPage overviewPage = loginAndOpenOverview();
        double balanceBefore = overviewPage.getBalance(fromAccountId);
        double destinationBefore = overviewPage.getBalance(toAccountId);
        double transferAmount = 25.00;

        TransferFundsPage transferPage = new HomePage(driver).goToTransferFunds();
        transferPage.transfer(String.valueOf(transferAmount), fromAccountId, toAccountId);

        Assert.assertTrue(transferPage.isTransferComplete(),
                "A valid transfer should show the transfer confirmation page");

        AccountsOverviewPage updatedOverview = new HomePage(driver).goToAccountsOverview();
        double balanceAfter = updatedOverview.getBalance(fromAccountId);
        double destinationAfter = updatedOverview.getBalance(toAccountId);

        Assert.assertEquals(balanceAfter, balanceBefore - transferAmount, 0.001,
                "The source account balance should decrease by the transferred amount");
        Assert.assertEquals(destinationAfter, destinationBefore + transferAmount, 0.001,
                "The destination account balance should increase by the transferred amount");
    }

    @Test(description = "Entering a non-numeric transfer amount is rejected")
    public void transferWithNonNumericAmount_showsValidationError() {
        loginAndOpenOverview();
        TransferFundsPage transferPage = new HomePage(driver).goToTransferFunds();

        transferPage.transfer("abc", fromAccountId, toAccountId);

        Assert.assertFalse(transferPage.isTransferComplete(),
                "A non-numeric amount must not be accepted as a valid transfer");
    }

    @Test(description = "Submitting the transfer form with no amount is rejected")
    public void transferWithEmptyAmount_showsValidationError() {
        loginAndOpenOverview();
        TransferFundsPage transferPage = new HomePage(driver).goToTransferFunds();

        transferPage.transfer("", fromAccountId, toAccountId);

        Assert.assertFalse(transferPage.isTransferComplete(),
                "An empty amount must not be accepted as a valid transfer");
    }
}
