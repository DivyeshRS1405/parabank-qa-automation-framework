package com.divyesh.tests.api;

import com.divyesh.framework.utils.RegistrationHelper;
import com.divyesh.framework.utils.TestDataFactory.RegistrationData;

import static io.restassured.RestAssured.given;

/**
 * ParaBank's REST API has no endpoint to create a customer directly (confirmed against
 * ParaBankService.java in the parasoft/parabank source), so every API test class registers a
 * fresh customer through the UI and then resolves that customer's id through the REST login
 * endpoint, rather than depending on CustomerApiTest or on a shared fixed account.
 */
final class ApiTestSupport {

    private ApiTestSupport() {
    }

    static ApiTestCustomer registerAndResolveCustomer() {
        RegistrationData data = RegistrationHelper.registerInTemporaryBrowser();

        var loginResponse = given()
                .pathParam("username", data.username())
                .pathParam("password", data.password())
                .when()
                .get("/login/{username}/{password}");

        System.out.println("API LOGIN STATUS = " + loginResponse.statusCode());
        System.out.println("API LOGIN CONTENT-TYPE = " + loginResponse.getContentType());
        System.out.println("API LOGIN RESPONSE = " + loginResponse.asString());

        int customerId = loginResponse.then()
                .statusCode(200)
                .extract()
                .xmlPath()
                .getInt("customer.id");
        
        

        var accountsResponse = given()
                .pathParam("customerId", customerId)
                .when()
                .get("/customers/{customerId}/accounts");

        System.out.println("ACCOUNTS STATUS = " + accountsResponse.statusCode());
        System.out.println("ACCOUNTS CONTENT-TYPE = " + accountsResponse.getContentType());
        System.out.println("ACCOUNTS RESPONSE = " + accountsResponse.asString());

        int primaryAccountId = accountsResponse.then()
                .statusCode(200)
                .extract()
                .xmlPath()
                .getInt("accounts.account.id");

        return new ApiTestCustomer(data, customerId, primaryAccountId);
    }

    record ApiTestCustomer(RegistrationData data, int customerId, int primaryAccountId) {
    }
}
