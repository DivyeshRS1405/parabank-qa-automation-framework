package com.divyesh.tests.api;

import com.divyesh.tests.api.ApiTestSupport.ApiTestCustomer;
import com.divyesh.tests.base.ApiBaseTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class CustomerApiTest extends ApiBaseTest {

    private ApiTestCustomer customer;

    @BeforeClass(alwaysRun = true)
    public void setUpCustomer() {
        customer = ApiTestSupport.registerAndResolveCustomer();
    }

    @Test(description = "Fetching a customer by id returns the matching first and last name")
    public void getCustomerById_returnsMatchingDetails() {
        given()
                .pathParam("customerId", customer.customerId())
                .when()
                .get("/customers/{customerId}")
                .then()
                .statusCode(200)
                .body("customer.firstName", equalTo(customer.data().firstName()))
                .body("customer.lastName", equalTo(customer.data().lastName()));
    }

    @Test(description = "Fetching a customer with an id that does not exist does not return 200")
    public void getCustomerById_withInvalidId_isNotFound() {
        given()
                .pathParam("customerId", 999999999)
                .when()
                .get("/customers/{customerId}")
                .then()
                .statusCode(not(200));
    }

    @Test(description = "Logging in through the API with valid credentials returns the matching customer")
    public void loginViaApi_withValidCredentials_returnsCustomer() {
        given()
                .pathParam("username", customer.data().username())
                .pathParam("password", customer.data().password())
                .when()
                .get("/login/{username}/{password}")
                .then()
                .statusCode(200)
                .body("customer.id", equalTo(String.valueOf(customer.customerId())));
    }

    @Test(description = "Logging in through the API with an incorrect password is rejected")
    public void loginViaApi_withInvalidPassword_isRejected() {
        given()
                .pathParam("username", customer.data().username())
                .pathParam("password", "WrongPassword!23")
                .when()
                .get("/login/{username}/{password}")
                .then()
                .statusCode(not(200));
    }
}
