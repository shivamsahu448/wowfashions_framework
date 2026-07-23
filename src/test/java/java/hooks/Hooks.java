package java.hooks;
import java.utils.ConfigReader;
import java.utils.DriverFactory;
import java.utils.ScreenshotUtil;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.gherkin.model.Scenario;


public class Hooks {

    WebDriver driver;
    ConfigReader config = new ConfigReader();

    @Before
    public void setup() {
        driver = DriverFactory.getDriver();
        DriverFactory.getDriver().get(config.getUrl());
    }

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {

            String path = ScreenshotUtil.takeScreenshot(driver, scenario.getName());

            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);

                scenario.attach(screenshot, "image/png", "Failure Screenshot");

                System.out.println("Screenshot saved at: " + path);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DriverFactory.quitDriver();
    }
}