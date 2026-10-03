package com.divyesh.tests.ui;

import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.pages.RegisterPage;
import com.divyesh.framework.utils.TestDataFactory;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RegistrationTest extends BaseTest {

    private RegisterPage openRegisterPage() {
        return openLoginPage().goToRegisterPage();
    }

    @Test(description = "Registering with complete, valid data creates the account and logs the customer in")
    public void validRegistration_logsCustomerIn() {

        RegisterPage registerPage = openRegisterPage();

        RegistrationData data = TestDataFactory.newRegistrationData();

        HomePage homePage = registerPage.registerNewCustomer(data);

        Assert.assertTrue(homePage.isLoggedIn(),
                "A successful registration should sign the customer in and show the account menu");
    }

    @DataProvider(name = "incompleteRegistrations")
    public Object[][] incompleteRegistrations() {
        RegistrationData valid = TestDataFactory.newRegistrationData();
        return new Object[][]{
                {withFirstName(valid, "")},
                {withLastName(valid, "")},
                {withUsername(valid, "")},
                {withPassword(valid, "")}
        };
    }

    @Test(dataProvider = "incompleteRegistrations",
            description = "Registration is rejected when a required field is left blank")
    public void requiredFieldValidation_showsErrorAndDoesNotRegister(RegistrationData incompleteData) {
        RegisterPage registerPage = openRegisterPage();

        registerPage.fillForm(incompleteData);
        registerPage.clickRegister();

        Assert.assertFalse(driver.getCurrentUrl().contains("overview.htm"),
                "Registration must not succeed when a required field is missing");
        Assert.assertTrue(registerPage.isValidationMessageDisplayed(),
                "A validation message should be shown for the missing field");
    }

    @Test(description = "Registration is rejected when password and confirm password do not match")
    public void passwordConfirmationMismatch_showsError() {
        RegisterPage registerPage = openRegisterPage();
        RegistrationData data = TestDataFactory.newRegistrationData();

        registerPage.fillForm(data, data.password() + "different");
        registerPage.clickRegister();

        Assert.assertFalse(driver.getCurrentUrl().contains("overview.htm"),
                "Registration must not succeed when passwords do not match");
        Assert.assertTrue(registerPage.isValidationMessageDisplayed(),
                "A validation message should be shown for the mismatched passwords");
    }

    private static RegistrationData withFirstName(RegistrationData data, String value) {
        return new RegistrationData(value, data.lastName(), data.street(), data.city(), data.state(),
                data.zipCode(), data.phoneNumber(), data.ssn(), data.username(), data.password());
    }

    private static RegistrationData withLastName(RegistrationData data, String value) {
        return new RegistrationData(data.firstName(), value, data.street(), data.city(), data.state(),
                data.zipCode(), data.phoneNumber(), data.ssn(), data.username(), data.password());
    }

    private static RegistrationData withUsername(RegistrationData data, String value) {
        return new RegistrationData(data.firstName(), data.lastName(), data.street(), data.city(), data.state(),
                data.zipCode(), data.phoneNumber(), data.ssn(), value, data.password());
    }

    private static RegistrationData withPassword(RegistrationData data, String value) {
        return new RegistrationData(data.firstName(), data.lastName(), data.street(), data.city(), data.state(),
                data.zipCode(), data.phoneNumber(), data.ssn(), data.username(), value);
    }
}
