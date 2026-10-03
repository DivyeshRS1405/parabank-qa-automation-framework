package com.divyesh.tests.api;

import com.divyesh.tests.api.ApiTestSupport.ApiTestCustomer;
import com.divyesh.tests.base.ApiBaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.not;

public class TransferApiTest extends ApiBaseTest {

    private static final int SAVINGS_ACCOUNT_TYPE = 1;

    private ApiTestCustomer customer;
    private int secondAccountId;

    @BeforeClass(alwaysRun = true)
    public void setUpCustomerWithTwoAccounts() {
        customer = ApiTestSupport.registerAndResolveCustomer();

        var createAccountResponse = given()
                .queryParam("customerId", customer.customerId())
                .queryParam("newAccountType", SAVINGS_ACCOUNT_TYPE)
                .queryParam("fromAccountId", customer.primaryAccountId())
                .when()
                .post("/createAccount");

        System.out.println("CREATE ACCOUNT STATUS = " + createAccountResponse.statusCode());
        System.out.println("CREATE ACCOUNT CONTENT-TYPE = " + createAccountResponse.getContentType());
        System.out.println("CREATE ACCOUNT RESPONSE = " + createAccountResponse.asString());

        secondAccountId = createAccountResponse.then()
                .statusCode(200)
                .extract()
                .xmlPath()
                .getInt("account.id");
    }

    @Test(description = "Transferring between the customer's own accounts updates both balances")
    public void transferBetweenOwnAccounts_updatesBothBalances() {
        double fromBalanceBefore = balanceOf(customer.primaryAccountId());
        double toBalanceBefore = balanceOf(secondAccountId);

        given()
                .queryParam("fromAccountId", customer.primaryAccountId())
                .queryParam("toAccountId", secondAccountId)
                .queryParam("amount", 30.00)
                .when()
                .post("/transfer")
                .then()
                .statusCode(200);

        Assert.assertEquals(balanceOf(customer.primaryAccountId()), fromBalanceBefore - 30.00, 0.001,
                "The source account balance should decrease by the transferred amount");
        Assert.assertEquals(balanceOf(secondAccountId), toBalanceBefore + 30.00, 0.001,
                "The destination account balance should increase by the transferred amount");
    }

    @Test(description = "Transferring from an account id that does not exist is rejected")
    public void transferWithInvalidFromAccount_isRejected() {
        given()
                .queryParam("fromAccountId", 999999999)
                .queryParam("toAccountId", secondAccountId)
                .queryParam("amount", 10.00)
                .when()
                .post("/transfer")
                .then()
                .statusCode(not(200));
    }

    @Test(description = "Transferring to an account id that does not exist is rejected")
    public void transferWithInvalidToAccount_isRejected() {
        given()
                .queryParam("fromAccountId", customer.primaryAccountId())
                .queryParam("toAccountId", 999999999)
                .queryParam("amount", 10.00)
                .when()
                .post("/transfer")
                .then()
                .statusCode(not(200));
    }

    private double balanceOf(int accountId) {
        return given()
                .pathParam("accountId", accountId)
                .when()
                .get("/accounts/{accountId}")
                .then()
                .statusCode(200)
                .extract().xmlPath().getDouble("account.balance");
    }
}
