package stepdefinitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CountryCurrencyPage;
import utils.DriverFactory;
import utils.ExtentReportManager;
import utils.Log;

public class CountryCurrencySteps {

    private final CountryCurrencyPage page;

    public CountryCurrencySteps() {
        WebDriver driver = DriverFactory.getDriver();
        this.page = new CountryCurrencyPage(driver);
    }

    // ════════════════════════════════════════════════════
    // BACKGROUND
    // ════════════════════════════════════════════════════

    @Given("user is on the India product listing page")
    public void user_is_on_india_product_listing_page() {
        Log.info("STEP: Navigating to India readymade product listing");
        ExtentReportManager.getTest().log(Status.INFO, "Opening India product listing page");
        try {
            page.navigateToIndiaProductListing();
            Log.pass("India product listing page loaded");
            ExtentReportManager.getTest().log(Status.PASS, "India product listing page loaded");
        } catch (Exception e) {
            Log.fail("Failed to load India product listing page", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "India product listing page load failed: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // COUNTRY SELECTOR — VISIBILITY & INTERACTION
    // ════════════════════════════════════════════════════

    @Then("the country selector widget is visible in the header")
    public void country_selector_is_visible() {
        Log.info("STEP: Verifying country selector widget is visible in header");
        ExtentReportManager.getTest().log(Status.INFO, "Checking country selector visibility");
        try {
            Assert.assertTrue(page.isCountrySelectorVisible(),
                "Country selector widget should be visible in the header");
            Log.pass("Country selector widget is visible");
            ExtentReportManager.getTest().log(Status.PASS, "Country selector widget visible");
        } catch (AssertionError e) {
            Log.fail("Country selector widget not visible in header", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Country selector widget not visible");
            throw e;
        }
    }

    @When("user opens the country selector")
    public void user_opens_country_selector() {
        Log.info("STEP: Opening the country selector dropdown");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking country selector widget");
        try {
            page.openCountrySelector();
            Log.pass("Country selector dropdown opened");
            ExtentReportManager.getTest().log(Status.PASS, "Country selector opened");
        } catch (Exception e) {
            Log.fail("Failed to open country selector dropdown", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "Country selector open failed: " + e.getMessage());
            throw e;
        }
    }

    @And("user selects country {string}")
    public void user_selects_country(String country) {
        Log.info("STEP: Selecting country '" + country + "' from dropdown");
        ExtentReportManager.getTest().log(Status.INFO, "Selecting country: " + country);
        try {
            page.selectCountry(country);
            Log.pass("Country '" + country + "' selected");
            ExtentReportManager.getTest().log(Status.PASS, "Country selected: " + country);
            
        } catch (Exception e) {
            Log.fail("Failed to select country '" + country + "'", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "Country selection failed for '" + country + "': " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // POSITIVE — CURRENCY ASSERTIONS
    // ════════════════════════════════════════════════════

    @Then("all product prices show the {string} currency symbol")
    public void all_prices_show_symbol(String symbol) {
        Log.info("STEP: Verifying all product prices show currency symbol '" + symbol + "'");
        page.waitForPricesWithSymbol(symbol, 45);
        ExtentReportManager.getTest().log(Status.INFO, "Checking all prices contain: " + symbol);
        try {
            Assert.assertTrue(page.areAllPricesShowingSymbol(symbol),
                "All product prices should contain the symbol: " + symbol);
            Log.pass("All product prices show symbol: " + symbol);
            ExtentReportManager.getTest().log(Status.PASS, "All prices contain symbol: " + symbol);
        } catch (AssertionError e) {
            Log.fail("Not all prices show symbol '" + symbol + "'", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "Price symbol assertion failed for: " + symbol);
            throw e;
        }
    }

    @Then("all visible product prices contain the {string} currency symbol")
    public void all_visible_prices_contain_symbol(String symbol) {
        Log.info("STEP: Verifying all visible product prices contain '" + symbol + "'");
        page.waitForPricesWithSymbol(symbol, 45);
        ExtentReportManager.getTest().log(Status.INFO,
            "Checking all visible prices contain: " + symbol);
        try {
            Assert.assertTrue(page.areAllPricesShowingSymbol(symbol),
                "All visible product prices should contain: " + symbol);
            Log.pass("All visible product prices contain: " + symbol);
            ExtentReportManager.getTest().log(Status.PASS,
                "All visible prices contain: " + symbol);
        } catch (AssertionError e) {
            Log.fail("Not all visible prices contain '" + symbol + "'", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "Visible price symbol check failed for: " + symbol);
            throw e;
        }
    }

    @Then("the product listing shows at least one product")
    public void product_listing_shows_at_least_one_product() {
        Log.info("STEP: Verifying product listing has at least one product");
        ExtentReportManager.getTest().log(Status.INFO, "Checking product count > 0");
        try {
            int count = page.getProductCount();
            Assert.assertTrue(count > 0,
                "Product listing should show at least one product after country switch. Count: " + count);
            Log.pass("Product listing has " + count + " product(s)");
            ExtentReportManager.getTest().log(Status.PASS, "Product count: " + count);
        } catch (AssertionError e) {
            Log.fail("No products found on listing after country switch", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Product listing is empty");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // NEGATIVE — CURRENCY ABSENCE ASSERTIONS
    // ════════════════════════════════════════════════════

    @Then("no product price shows the {string} currency symbol")
    public void no_price_shows_symbol(String symbol) {
        Log.info("STEP: Verifying no product price shows symbol '" + symbol + "'");
        ExtentReportManager.getTest().log(Status.INFO, "Checking no prices contain: " + symbol);
        try {
            Assert.assertTrue(page.isNoPriceShowingSymbol(symbol),
                "No product price should contain the symbol: " + symbol);
            Log.pass("No product price shows symbol: " + symbol);
            ExtentReportManager.getTest().log(Status.PASS, "No prices contain symbol: " + symbol);
        } catch (AssertionError e) {
            Log.fail("At least one price still shows symbol '" + symbol + "'", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "Unexpected symbol found in prices: " + symbol);
            throw e;
        }
    }

    @Then("product prices are non-empty after the country switch")
    public void product_prices_are_non_empty() {
        Log.info("STEP: Verifying product prices are non-empty after country switch");
        ExtentReportManager.getTest().log(Status.INFO, "Checking product prices are non-empty");
        try {
            Assert.assertTrue(page.areProductPricesNonEmpty(),
                "Product prices should be non-empty after country switch");
            Log.pass("Product prices are non-empty");
            ExtentReportManager.getTest().log(Status.PASS, "Product prices non-empty confirmed");
        } catch (AssertionError e) {
            Log.fail("Product prices are empty after country switch", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Product prices are empty");
            throw e;
        }
    }

    @Then("the country selector dropdown is closed")
    public void country_selector_dropdown_is_closed() {
        Log.info("STEP: Verifying country selector dropdown is closed after selection");
        ExtentReportManager.getTest().log(Status.INFO, "Checking dropdown is closed");
        try {
            Assert.assertTrue(page.isCountrySelectorClosed(),
                "Country selector dropdown should be closed after a valid selection");
            Log.pass("Country selector dropdown is closed");
            ExtentReportManager.getTest().log(Status.PASS, "Country selector dropdown closed");
        } catch (AssertionError e) {
            Log.fail("Country selector dropdown is still open", e);
            ExtentReportManager.getTest().log(Status.FAIL,
                "Dropdown did not close after selection");
            throw e;
        }
    }
}
