package com.divyesh.tests.base;

import com.divyesh.framework.config.ConfigReader;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;

public class ApiBaseTest {

    @BeforeClass(alwaysRun = true)
    public void setUpApi() {
        RestAssured.baseURI = ConfigReader.get("apiBaseUrl");
    }
}
