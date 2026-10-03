package com.divyesh.tests.api;

import com.divyesh.tests.api.ApiTestSupport.ApiTestCustomer;
import com.divyesh.tests.base.ApiBaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class AccountApiTest extends ApiBaseTest {

    private ApiTestCustomer customer;

    @BeforeClass(alwaysRun = true)
    public void setUpCustomer() {
        customer = ApiTestSupport.registerAndResolveCustomer();
    }

    @Test(description = "A newly registered customer has at least one account with a balance")
    public void getAccountsForCustomer_returnsAtLeastOneAccountWithBalance() {

        given()
                .pathParam("customerId", customer.customerId())
                .when()
                .get("/customers/{customerId}/accounts")
                .then()
                .statusCode(200)
                .body("accounts.account.id", notNullValue())
                .body("accounts.account.balance", notNullValue());
    }

    @Test(description = "Fetching an account by id returns the matching customer id")
    public void getAccountById_returnsMatchingAccountDetails() {

        given()
                .pathParam("accountId", customer.primaryAccountId())
                .when()
                .get("/accounts/{accountId}")
                .then()
                .statusCode(200)
                .body("account.customerId",
                        equalTo(String.valueOf(customer.customerId())));
    }

    @Test(description = "Fetching an account with an id that does not exist does not return 200")
    public void getAccountById_withInvalidId_isNotFound() {
        given()
                .pathParam("accountId", 999999999)
                .when()
                .get("/accounts/{accountId}")
                .then()
                .statusCode(not(200));
    }

    @Test(description = "Depositing into an account increases its balance by the deposited amount")
    public void depositToAccount_increasesBalance() {
        double balanceBefore = currentBalance();

        given()
                .queryParam("accountId", customer.primaryAccountId())
                .queryParam("amount", 100.00)
                .when()
                .post("/deposit")
                .then()
                .statusCode(200);

        Assert.assertEquals(currentBalance(), balanceBefore + 100.00, 0.001,
                "Balance should increase by the deposited amount");
    }

    @Test(description = "Withdrawing from an account decreases its balance by the withdrawn amount")
    public void withdrawFromAccount_decreasesBalance() {
        double balanceBefore = currentBalance();

        given()
                .queryParam("accountId", customer.primaryAccountId())
                .queryParam("amount", 40.00)
                .when()
                .post("/withdraw")
                .then()
                .statusCode(200);

        Assert.assertEquals(currentBalance(), balanceBefore - 40.00, 0.001,
                "Balance should decrease by the withdrawn amount");
    }

    private double currentBalance() {
        return given()
                .pathParam("accountId", customer.primaryAccountId())
                .when()
                .get("/accounts/{accountId}")
                .then()
                .statusCode(200)
                .extract().xmlPath()
                .getDouble("account.balance");
    }
}
