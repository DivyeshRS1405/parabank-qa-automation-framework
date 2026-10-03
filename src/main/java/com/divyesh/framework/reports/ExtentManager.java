package com.divyesh.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.divyesh.framework.config.ConfigReader;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ExtentManager {

    private static final Path REPORT_DIR = Path.of("reports", "html");
    private static ExtentReports extentReports;

    private ExtentManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extentReports == null) {
            extentReports = createInstance();
        }
        return extentReports;
    }

    /** Screenshots are linked relative to the report so the reports folder can be moved or downloaded as a whole. */
    public static String relativeToReport(Path file) {
        return REPORT_DIR.toAbsolutePath().relativize(file.toAbsolutePath()).toString().replace('\\', '/');
    }

    private static ExtentReports createInstance() {
        try {
            Files.createDirectories(REPORT_DIR);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not create report directory " + REPORT_DIR, e);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter(REPORT_DIR.resolve("ExtentReport_" + timestamp + ".html").toString());
        sparkReporter.config().setDocumentTitle("ParaBank QA Automation Report");
        sparkReporter.config().setReportName("ParaBank Test Execution Report");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application Under Test", "ParaBank");
        extent.setSystemInfo("Base URL", ConfigReader.get("baseUrl"));
        extent.setSystemInfo("Browser", ConfigReader.get("browser"));
        return extent;
    }
}
