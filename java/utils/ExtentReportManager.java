package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.DARK);
            spark.config().setDocumentTitle("Wow Fashions — Test Automation Report");
            spark.config().setReportName("Search Functionality");
            spark.config().setTimeStampFormat("dd-MMM-yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Application", "Wow Fashions");
            extent.setSystemInfo("Browser", "Chrome");
            extent.setSystemInfo("Environment", "Staging");
            extent.setSystemInfo("URL", new ConfigReader().getUrl());
        }
        return extent;
    }

    public static void createTest(String name) {
        extentTest.set(getInstance().createTest(name));
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
