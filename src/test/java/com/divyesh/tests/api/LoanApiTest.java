package com.divyesh.tests.api;

import com.divyesh.tests.api.ApiTestSupport.ApiTestCustomer;
import com.divyesh.tests.base.ApiBaseTest;
import io.restassured.path.xml.XmlPath;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class LoanApiTest extends ApiBaseTest {

    private ApiTestCustomer customer;

    @BeforeClass(alwaysRun = true)
    public void setUpCustomer() {
        customer = ApiTestSupport.registerAndResolveCustomer();
    }

    @Test(description = "A loan request with valid input returns an approval decision, "
            + "with a new account id only when approved")
    public void validLoanRequest_returnsConsistentApprovalDecision() {

        XmlPath response = given()
                .queryParam("customerId", customer.customerId())
                .queryParam("amount", 5000)
                .queryParam("downPayment", 500)
                .queryParam("fromAccountId", customer.primaryAccountId())
                .when()
                .post("/requestLoan")
                .then()
                .statusCode(200)
                .extract()
                .xmlPath();

        boolean approved = response.getBoolean("loanResponse.approved");
        String newAccountId = response.getString("loanResponse.accountId");

        if (approved) {
            Assert.assertNotNull(newAccountId,
                    "An approved loan should be assigned a new account id");

        } else {
            Assert.assertTrue(newAccountId == null || newAccountId.isBlank(),
                    "A denied loan should not be assigned a new account id");
        }
    }

    @Test(description = "Requesting a loan with an invalid from-account is handled by the API")
    public void loanRequest_withInvalidFromAccount_isRejected() {

        XmlPath response = given()
                .queryParam("customerId", customer.customerId())
                .queryParam("amount", 5000)
                .queryParam("downPayment", 500)
                .queryParam("fromAccountId", 999999999)
                .when()
                .post("/requestLoan")
                .then()
                .statusCode(200)
                .extract()
                .xmlPath();

        boolean approved = response.getBoolean("loanResponse.approved");

        Assert.assertFalse(approved,
                "A loan request using a non-existing fromAccountId should not be approved");
    }
}