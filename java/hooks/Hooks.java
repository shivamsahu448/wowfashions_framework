package hooks;

import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import utils.ConfigReader;
import utils.DriverFactory;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;

public class Hooks {

    private static final ConfigReader config = new ConfigReader();

    @BeforeAll
    public static void setupAll() {
        ExtentReportManager.getInstance();
        DriverFactory.getDriver();
        System.out.println("Browser opened.");
    }

    @Before
    public void setup(Scenario scenario) {
        ExtentReportManager.createTest(scenario.getName());
        ExtentReportManager.getTest().log(Status.INFO, "Scenario started: " + scenario.getName());
        DriverFactory.getDriver().manage().deleteAllCookies();
        DriverFactory.getDriver().get(config.getUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverFactory.getDriver();

        if (scenario.isFailed()) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failure Screenshot");
                String path = ScreenshotUtil.saveScreenshotBytes(screenshot, scenario.getName());
                ExtentReportManager.getTest().fail("Scenario FAILED");
                ExtentReportManager.getTest().addScreenCaptureFromPath(path, "Failure Screenshot");
                System.out.println("Screenshot saved at: " + path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ExtentReportManager.getTest().pass("Scenario PASSED");
        }
    }

    @AfterAll
    public static void tearDownAll() {
        ExtentReportManager.flush();
        DriverFactory.quitDriver();
        System.out.println("Extent report generated. Browser closed.");
    }
}
