package base;

import org.openqa.selenium.WebDriver;

import utils.DriverFactory;

public class BaseTest {

    public WebDriver driver = DriverFactory.getDriver();
}