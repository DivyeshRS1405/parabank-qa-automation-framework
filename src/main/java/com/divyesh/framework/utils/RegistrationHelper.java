package com.divyesh.framework.utils;

import com.divyesh.framework.config.ConfigReader;
import com.divyesh.framework.driver.DriverFactory;
import com.divyesh.framework.pages.LoginPage;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import org.openqa.selenium.WebDriver;

/**
 * ParaBank's public demo instance resets its data periodically and its REST API has no
 * customer-creation endpoint, so every test class registers its own customer through the UI.
 */
public final class RegistrationHelper {

    private RegistrationHelper() {
    }

    public static RegistrationData registerNewCustomer(WebDriver driver) {
        driver.get(ConfigReader.get("baseUrl"));
        RegistrationData data = TestDataFactory.newRegistrationData();
        new LoginPage(driver).goToRegisterPage().registerNewCustomer(data);
        
        System.out.println("REGISTRATION URL = " + driver.getCurrentUrl());
        System.out.println("REGISTRATION TITLE = " + driver.getTitle());
        
        
        
        return data;
    }

    public static RegistrationData registerInTemporaryBrowser() {
        return DriverFactory.callWithTemporaryDriver(RegistrationHelper::registerNewCustomer);
    }
}
