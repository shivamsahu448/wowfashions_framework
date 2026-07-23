package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "html:reports/cucumber-report.html",
                "json:reports/cucumber.json"
        },
        monochrome = true
        
)

public class TestRunner extends AbstractTestNGCucumberTests {

	
}