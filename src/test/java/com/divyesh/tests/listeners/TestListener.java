package com.divyesh.tests.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.divyesh.framework.driver.DriverFactory;
import com.divyesh.framework.reports.ExtentManager;
import com.divyesh.framework.utils.ScreenshotUtil;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.nio.file.Path;

public class TestListener implements ITestListener {

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private ExtentReports extent;

    @Override
    public void onStart(ITestContext context) {
        extent = ExtentManager.getInstance();
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentTest test = extentTest.get();
        test.log(Status.FAIL, result.getThrowable());

        WebDriver driver = DriverFactory.getDriver();
        if (driver != null) {
            Path screenshotPath = ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
            if (screenshotPath != null) {
                try {
                    test.addScreenCaptureFromPath(ExtentManager.relativeToReport(screenshotPath));
                } catch (Exception e) {
                    test.log(Status.WARNING, "Could not attach screenshot: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test skipped: " +
                (result.getThrowable() != null ? result.getThrowable().getMessage() : "no reason given"));
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
