package java.runners;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html"
        },
        monochrome = true
)

public class TestRunner extends AbstractTestNGCucumberTests {

}