package com.divyesh.tests.ui;

import com.divyesh.framework.pages.HomePage;
import com.divyesh.framework.pages.UpdateProfilePage;
import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;
import com.divyesh.tests.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class UpdateProfileTest extends BaseTest {

    private RegistrationData registeredCustomer;

    @BeforeClass(alwaysRun = true)
    public void registerTestCustomer() {
        registeredCustomer = RegistrationHelper.registerInTemporaryBrowser();
    }

    private UpdateProfilePage loginAndOpenUpdateProfile() {
        var loginPage = openLoginPage();

        loginPage.login(registeredCustomer.username(), registeredCustomer.password());

        HomePage homePage = new HomePage(driver);

        Assert.assertTrue(homePage.isLoggedIn(),
                "Customer should be logged in before opening Update Profile");

        return homePage.goToUpdateProfile();
    }

    @Test(description = "Updating the profile with complete, valid data shows a success message")
    public void validProfileUpdate_showsSuccessMessage() {
        UpdateProfilePage updateProfilePage = loginAndOpenUpdateProfile();

        updateProfilePage.fillForm(registeredCustomer.firstName(), registeredCustomer.lastName(),
                "456 Updated Street", "Shelbyville", "IL", "62565", "555-9876543");
        updateProfilePage.clickUpdateProfile();
        
        
        
        
        

        Assert.assertTrue(updateProfilePage.isProfileUpdated(),
                "A valid profile update should show the success confirmation page");
    }

    @DataProvider(name = "blankRequiredField")
    public Object[][] blankRequiredField() {
        return new Object[][]{
                {"", "Reyes", "742 Evergreen Terrace", "Springfield", "IL", "62704", "555-1234567"},
                {"Jordan", "", "742 Evergreen Terrace", "Springfield", "IL", "62704", "555-1234567"},
                {"Jordan", "Reyes", "", "Springfield", "IL", "62704", "555-1234567"}
        };
    }

    @Test(dataProvider = "blankRequiredField",
            description = "Updating the profile with a required field left blank is rejected")
    public void profileUpdate_missingRequiredField_showsValidationError(String firstName, String lastName,
                                                                         String street, String city, String state,
                                                                         String zip, String phone) {
        UpdateProfilePage updateProfilePage = loginAndOpenUpdateProfile();

        updateProfilePage.fillForm(firstName, lastName, street, city, state, zip, phone);
        updateProfilePage.clickUpdateProfile();

      
        
        Assert.assertTrue(driver.getCurrentUrl().contains("updateprofile.htm"),
                "Profile update must not succeed when a required field is missing");
    }
}
