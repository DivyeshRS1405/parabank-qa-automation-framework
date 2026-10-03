package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class UpdateProfilePage extends BasePage {

    private final By firstName = By.name("customer.firstName");
    private final By lastName = By.name("customer.lastName");
    private final By street = By.name("customer.address.street");
    private final By city = By.name("customer.address.city");
    private final By state = By.name("customer.address.state");
    private final By zipCode = By.name("customer.address.zipCode");
    private final By phoneNumber = By.name("customer.phoneNumber");
    private final By updateProfileButton = By.cssSelector("input[value='Update Profile']");

    public UpdateProfilePage(WebDriver driver) {
        super(driver);
    }

    public void fillForm(String firstNameValue, String lastNameValue, String streetValue, String cityValue,
                         String stateValue, String zipValue, String phoneValue) {
        clearAndFill(firstName, firstNameValue);
        clearAndFill(lastName, lastNameValue);
        clearAndFill(street, streetValue);
        clearAndFill(city, cityValue);
        clearAndFill(state, stateValue);
        clearAndFill(zipCode, zipValue);
        clearAndFill(phoneNumber, phoneValue);
    }

    public void clickUpdateProfile() {
        driver.findElement(updateProfileButton).click();
    }

    public boolean isProfileUpdated() {
        return isHeadingDisplayed("Profile Updated");
    }

    private void clearAndFill(By field, String value) {
        wait.waitForVisibility(field).clear();
        driver.findElement(field).sendKeys(value);
    }
}
