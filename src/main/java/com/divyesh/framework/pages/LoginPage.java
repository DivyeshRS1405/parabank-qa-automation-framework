package com.divyesh.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameInput = By.name("username");
    private final By passwordInput = By.name("password");
    private final By registerLink = By.linkText("Register");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        wait.waitForVisibility(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        driver.findElement(passwordInput).submit();
    }

    public RegisterPage goToRegisterPage() {
        wait.waitForClickability(registerLink).click();
        return new RegisterPage(driver);
    }

    public boolean isLoginFormDisplayed() {
        return wait.isAnyVisible(usernameInput);
    }
}
