package com.divyesh.tests.ui;

import com.divyesh.framework.driver.DriverFactory;
import com.divyesh.framework.pages.AccountsOverviewPage;
import com.divyesh.framework.pages.BillPayPage;
import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory;
import com.divyesh.framework.utils.TestDataFactory.PayeeData;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class BillPayTest extends BaseTest {

    private RegistrationData registeredCustomer;
    private String accountId;

    @BeforeClass(alwaysRun = true)
    public void registerTestCustomer() {
        DriverFactory.initDriver();
        var setupDriver = DriverFactory.getDriver();

        registeredCustomer = RegistrationHelper.registerNewCustomer(setupDriver);
        accountId = new HomePage(setupDriver).goToAccountsOverview().getAccountIds().get(0);

        DriverFactory.quitDriver();
    }

    private HomePage loginAsRegisteredCustomer() {
        var loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), registeredCustomer.password());
        return new HomePage(driver);
    }

    @Test(description = "Paying a bill with complete, valid data debits the paying account")
    public void validBillPayment_showsConfirmationAndDebitsAccount() {
        HomePage homePage = loginAsRegisteredCustomer();
        AccountsOverviewPage overviewPage = homePage.goToAccountsOverview();
        double balanceBefore = overviewPage.getBalance(accountId);

        BillPayPage billPayPage = new HomePage(driver).goToBillPay();
        billPayPage.payBill(TestDataFactory.newPayeeData(), "20.00", accountId);

        Assert.assertTrue(billPayPage.isPaymentComplete(),
                "A valid bill payment should show the confirmation page");

        AccountsOverviewPage updatedOverview = new HomePage(driver).goToAccountsOverview();
        double balanceAfter = updatedOverview.getBalance(accountId);
        Assert.assertEquals(balanceAfter, balanceBefore - 20.00, 0.001,
                "The paying account balance should decrease by the payment amount");
    }

    @DataProvider(name = "incompletePayees")
    public Object[][] incompletePayees() {
        PayeeData valid = TestDataFactory.newPayeeData();
        return new Object[][]{
                {withName(valid, "")},
                {withAccountNumber(valid, "")}
        };
    }

    @Test(dataProvider = "incompletePayees",
            description = "Bill payment is rejected when a required payee field is left blank")
    public void billPayment_missingRequiredField_showsValidationError(PayeeData incompletePayee) {
        loginAsRegisteredCustomer();
        BillPayPage billPayPage = new HomePage(driver).goToBillPay();

        billPayPage.fillForm(incompletePayee, "20.00", accountId);
        billPayPage.clickSendPayment();

        Assert.assertFalse(billPayPage.isPaymentComplete(),
                "Bill payment must not succeed when a required field is missing");
    }

    @Test(description = "Bill payment is rejected when the amount is not numeric")
    public void billPayment_invalidAmount_showsValidationError() {
        loginAsRegisteredCustomer();
        BillPayPage billPayPage = new HomePage(driver).goToBillPay();

        billPayPage.fillForm(TestDataFactory.newPayeeData(), "not-an-amount", accountId);
        billPayPage.clickSendPayment();

        Assert.assertFalse(billPayPage.isPaymentComplete(),
                "Bill payment must not succeed with a non-numeric amount");
    }

    private static PayeeData withName(PayeeData data, String value) {
        return new PayeeData(value, data.street(), data.city(), data.state(), data.zipCode(),
                data.phoneNumber(), data.accountNumber());
    }

    private static PayeeData withAccountNumber(PayeeData data, String value) {
        return new PayeeData(data.name(), data.street(), data.city(), data.state(), data.zipCode(),
                data.phoneNumber(), value);
    }
}
