package com.divyesh.tests.ui;

import com.divyesh.framework.driver.DriverFactory;
import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.pages.RequestLoanPage;
import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class RequestLoanTest extends BaseTest {

    private static final List<String> KNOWN_LOAN_STATUSES = List.of("Approved", "Denied");

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

    private RequestLoanPage loginAndOpenRequestLoan() {
        var loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), registeredCustomer.password());
        return new HomePage(driver).goToRequestLoan();
    }

    @Test(description = "A loan request with valid input is processed to an approved or denied result")
    public void validLoanRequest_showsApprovalOrDenialResult() {
        RequestLoanPage requestLoanPage = loginAndOpenRequestLoan();

        requestLoanPage.submitLoanRequest("5000", "500", accountId);

        Assert.assertTrue(requestLoanPage.isLoanProcessed(), "The loan request should be processed to a result");
        String status = requestLoanPage.getLoanStatus();
        Assert.assertTrue(KNOWN_LOAN_STATUSES.contains(status),
                "Loan status should be either Approved or Denied but was: " + status);
    }

    @Test(description = "Submitting a loan request with no amount does not produce a loan decision")
    public void loanRequest_missingAmount_showsValidationError() {
        RequestLoanPage requestLoanPage = loginAndOpenRequestLoan();

        requestLoanPage.submitLoanRequest("", "500", accountId);

        Assert.assertFalse(requestLoanPage.isLoanProcessed(),
                "A loan request with no amount must not be processed to a decision");
    }

    @Test(description = "Submitting a loan request with a non-numeric down payment does not produce a loan decision")
    public void loanRequest_nonNumericDownPayment_showsValidationError() {
        RequestLoanPage requestLoanPage = loginAndOpenRequestLoan();

        requestLoanPage.submitLoanRequest("5000", "not-a-number", accountId);

        Assert.assertFalse(requestLoanPage.isLoanProcessed(),
                "A loan request with an invalid down payment must not be processed to a decision");
    }
}
