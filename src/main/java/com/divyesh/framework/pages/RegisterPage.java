package com.divyesh.framework.pages;

import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    private final By firstName = By.name("customer.firstName");
    private final By lastName = By.name("customer.lastName");
    private final By street = By.name("customer.address.street");
    private final By city = By.name("customer.address.city");
    private final By state = By.name("customer.address.state");
    private final By zipCode = By.name("customer.address.zipCode");
    private final By phoneNumber = By.name("customer.phoneNumber");
    private final By ssn = By.name("customer.ssn");
    private final By username = By.name("customer.username");
    private final By password = By.name("customer.password");
    private final By confirmPassword = By.name("repeatedPassword");
    private final By registerButton = By.cssSelector("input[value='Register']");
    private final By successMessage = By.cssSelector("#rightPanel p");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void fillForm(RegistrationData data) {
        fillForm(data, data.password());
    }

    public void fillForm(RegistrationData data, String confirmPasswordValue) {
        wait.waitForVisibility(firstName).sendKeys(data.firstName());
        driver.findElement(lastName).sendKeys(data.lastName());
        driver.findElement(street).sendKeys(data.street());
        driver.findElement(city).sendKeys(data.city());
        driver.findElement(state).sendKeys(data.state());
        driver.findElement(zipCode).sendKeys(data.zipCode());
        driver.findElement(phoneNumber).sendKeys(data.phoneNumber());
        driver.findElement(ssn).sendKeys(data.ssn());
        driver.findElement(username).sendKeys(data.username());
        driver.findElement(password).sendKeys(data.password());
        driver.findElement(confirmPassword).sendKeys(confirmPasswordValue);
    }

    public void clickRegister() {
        driver.findElement(registerButton).click();

        System.out.println("AFTER REGISTER URL = " + driver.getCurrentUrl());
        System.out.println("AFTER REGISTER TITLE = " + driver.getTitle());

        if (!driver.findElements(By.cssSelector(".error")).isEmpty()) {
            System.out.println("ERROR MESSAGE = " +
                    driver.findElement(By.cssSelector(".error")).getText());
        }

        if (!driver.findElements(By.cssSelector("#rightPanel p")).isEmpty()) {
            System.out.println("RIGHT PANEL MESSAGE = " +
                    driver.findElement(By.cssSelector("#rightPanel p")).getText());
        }
    }

    public HomePage registerNewCustomer(RegistrationData data) {
        fillForm(data);
        clickRegister();

        if (!isRegistrationSuccessful()) {
            throw new IllegalStateException(
                    "Registration failed for username: " + data.username()
            );
        }

        return new HomePage(driver);
    }

    public boolean isRegistrationSuccessful() {
        return wait.isTextVisible(successMessage, "Your account was created successfully");
    }
}
