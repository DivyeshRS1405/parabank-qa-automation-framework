package com.divyesh.tests.ui;

import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.pages.LoginPage;
import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private RegistrationData registeredCustomer;

    @BeforeClass(alwaysRun = true)
    public void registerTestCustomer() {
        registeredCustomer = RegistrationHelper.registerInTemporaryBrowser();
    }

    @Test(description = "A registered customer can log in and reaches the accounts overview page")
    public void validLogin_navigatesToAccountsOverview() {
        LoginPage loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), registeredCustomer.password());
        
        
        
        System.out.println("CURRENT URL = " + driver.getCurrentUrl());
        System.out.println("PAGE TITLE = " + driver.getTitle());
        
        var errors = driver.findElements(org.openqa.selenium.By.cssSelector(".error"));

        if (!errors.isEmpty()) {
            System.out.println("LOGIN ERROR = " + errors.get(0).getText());
        }
        
        
        
        
        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(driver.getCurrentUrl().contains("overview.htm"),
                "Expected to land on the accounts overview page after a valid login");
        Assert.assertTrue(homePage.isLoggedIn(), "Logged-in navigation menu should be visible");
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][]{
                {registeredCustomer.username(), "WrongPassword123!"},
                {"no_such_user_" + System.currentTimeMillis(), "WhateverPassword1!"}
        };
    }

    @Test(dataProvider = "invalidCredentials",
            description = "Login is rejected for a wrong password and for an unknown username")
    public void invalidLogin_showsErrorAndStaysOnLoginPage(String username, String password) {
        LoginPage loginPage = openLoginPage();
        loginPage.login(username, password);

        Assert.assertFalse(driver.getCurrentUrl().contains("overview.htm"),
                "An invalid login must not reach the accounts overview page");
        Assert.assertTrue(loginPage.isValidationMessageDisplayed(),
                "An error message should be shown for invalid credentials");
    }

    @Test(description = "Submitting the login form without a username keeps the user on the login page")
    public void emptyUsername_staysOnLoginPage() {
        LoginPage loginPage = openLoginPage();
        loginPage.login("", registeredCustomer.password());

        Assert.assertFalse(driver.getCurrentUrl().contains("overview.htm"),
                "Login with an empty username must not succeed");
    }

    @Test(description = "Submitting the login form without a password keeps the user on the login page")
    public void emptyPassword_staysOnLoginPage() {
        LoginPage loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), "");

        Assert.assertFalse(driver.getCurrentUrl().contains("overview.htm"),
                "Login with an empty password must not succeed");
    }

    @Test(description = "Submitting the login form with no credentials keeps the user on the login page")
    public void emptyCredentials_staysOnLoginPage() {
        LoginPage loginPage = openLoginPage();
        loginPage.login("", "");

        Assert.assertFalse(driver.getCurrentUrl().contains("overview.htm"),
                "Login with no credentials must not succeed");
    }

    @Test(description = "A logged-in customer can log out and returns to the login page")
    public void logout_returnsToLoginPage() {
        LoginPage loginPage = openLoginPage();
        loginPage.login(registeredCustomer.username(), registeredCustomer.password());
        HomePage homePage = new HomePage(driver);

        homePage.logout();
        System.out.println("AFTER LOGOUT URL = " + driver.getCurrentUrl());
        System.out.println("AFTER LOGOUT TITLE = " + driver.getTitle());

        Assert.assertTrue(driver.getCurrentUrl().contains("index.htm"),
                "Logout should return the user to the login page");
    }
}
