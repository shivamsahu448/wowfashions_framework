package stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.FilterPage;
import utils.ConfigReader;
import utils.DriverFactory;

public class FilterSteps {

    private FilterPage filterPage;

    public FilterSteps() {
        WebDriver driver = DriverFactory.getDriver();
        this.filterPage = new FilterPage(driver);
    }

    // ════════════════════════════════════════════════════
    // BACKGROUND STEP
    // ════════════════════════════════════════════════════

    @Given("user opens the Darziya brand page")
    public void user_opens_darziya_brand_page() {
        filterPage.navigateTo(new ConfigReader().getDarziyaUrl());
        System.out.println("[STEP] Darziya brand page khuli");
    }

    // ════════════════════════════════════════════════════
    // MIN PRICE INPUT STEPS
    // ════════════════════════════════════════════════════

    @When("user clears the Min Price input and types {string}")
    public void user_clears_min_price_and_types(String value) {
        filterPage.setMinPrice(value);
        System.out.println("[STEP] Min Price set kiya: " + value);
    }

    @Then("the Min Price input should show {string}")
    public void min_price_input_should_show(String expected) {
        String actual = filterPage.getMinPriceValue();
        System.out.println("[STEP] Min Price actual value: " + actual);
        Assert.assertEquals(actual, expected, "Min Price input mein galat value hai!");
    }

    // ════════════════════════════════════════════════════
    // MAX PRICE INPUT STEPS
    // ════════════════════════════════════════════════════

    @When("user clears the Max Price input and types {string}")
    public void user_clears_max_price_and_types(String value) {
        filterPage.setMaxPrice(value);
        System.out.println("[STEP] Max Price set kiya: " + value);
    }

    @Then("the Max Price input should show {string}")
    public void max_price_input_should_show(String expected) {
        String actual = filterPage.getMaxPriceValue();
        System.out.println("[STEP] Max Price actual value: " + actual);
        Assert.assertEquals(actual, expected, "Max Price input mein galat value hai!");
    }

    // ════════════════════════════════════════════════════
    // CONFIRM / TAB STEP
    // ════════════════════════════════════════════════════

    @When("user presses Tab to confirm")
    public void user_presses_tab_to_confirm() {
        filterPage.pressTabToConfirm();
        System.out.println("[STEP] Tab press kiya — grid refresh ho raha hai");
    }

    // ════════════════════════════════════════════════════
    // PRODUCT GRID STEPS
    // ════════════════════════════════════════════════════

    @Then("the product grid should refresh")
    public void the_product_grid_should_refresh() {
        int count = filterPage.getProductCount();
        System.out.println("[STEP] Grid refreshed — products visible: " + count);
        Assert.assertTrue(count >= 0, "Product grid render nahi hua (count returned -1)");
    }

    @Then("products displayed should be between {string} and {string} in price")
    public void products_should_be_in_price_range(String min, String max) {
        int minVal = Integer.parseInt(min);
        int maxVal = Integer.parseInt(max);
        boolean inRange = filterPage.areAllProductPricesInRange(minVal, maxVal);
        System.out.println("[STEP] Price range check: " + min + "–" + max
            + " | Result: " + (inRange ? "PASS" : "FAIL"));
        Assert.assertTrue(inRange, "Kuch products price range " + min + "–" + max + " ke bahar hain!");
    }

    // ════════════════════════════════════════════════════
    // NO RESULTS / ERROR STEPS
    // ════════════════════════════════════════════════════

    @Then("either no products are shown or an error message is displayed")
    public void no_products_or_error_shown() {
        int count = filterPage.getProductCount();
        boolean noResults = filterPage.isNoResultsMessageDisplayed();
        System.out.println("[STEP] Product count: " + count + " | No-results msg: " + noResults);
        Assert.assertTrue(count == 0 || noResults,
            "Impossible filter ke baad bhi products dikh rahe hain — expected 0 or error msg");
    }

    // ════════════════════════════════════════════════════
    // SLIDER STEP
    // ════════════════════════════════════════════════════

    @Then("the left slider thumb position should reflect value {string}")
    public void slider_thumb_should_reflect_value(String expectedValue) {
        int expected = Integer.parseInt(expectedValue);
        int actual = filterPage.getSliderMinValue();
        System.out.println("[STEP] Slider Min value — expected: " + expected + " | actual: " + actual);
        int tolerance = 200;
        Assert.assertTrue(Math.abs(actual - expected) <= tolerance,
            "Slider min value expected ~" + expected + " but got " + actual);
    }

    // ════════════════════════════════════════════════════
    // RESET STEP
    // ════════════════════════════════════════════════════

    @When("user clicks the Reset or Clear Filters button")
    public void user_clicks_clear_filters() {
        filterPage.clickClearFilters();
        System.out.println("[STEP] Clear/Reset button click kiya");
    }
}
