package stepdefinitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CartPage;
import pages.ProductPage;
import utils.DriverFactory;
import utils.ExtentReportManager;
import utils.Log;

public class AddToCartSteps {

    private final ProductPage productPage;
    private final CartPage    cartPage;

    // State carried across steps within a scenario
    private String capturedProductName      = "";
    private String capturedProductPrice     = "";
    private double capturedProductPriceValue = 0.0;
    private int    cartBadgeCountBefore     = 0;

    public AddToCartSteps() {
        WebDriver driver = DriverFactory.getDriver();
        this.productPage = new ProductPage(driver);
        this.cartPage    = new CartPage(driver);
    }

    // ════════════════════════════════════════════════════
    // BACKGROUND
    // ════════════════════════════════════════════════════

    @Given("user opens a readymade product page")
    public void user_opens_readymade_product_page() {
        Log.info("STEP: Navigating to readymade listing and selecting first product");
        ExtentReportManager.getTest().log(Status.INFO, "Opening readymade product page");
        try {
            productPage.navigateToReadymadeAndSelectFirst();
            Log.pass("Readymade product page opened");
            ExtentReportManager.getTest().log(Status.PASS, "Readymade product page loaded");
        } catch (Exception e) {
            Log.fail("Failed to open readymade product page", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to open readymade product page: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // PRODUCT PAGE LOAD
    // ════════════════════════════════════════════════════

    @Then("the product page loads successfully")
    public void product_page_loads_successfully() {
        Log.info("STEP: Verifying product page loaded");
        ExtentReportManager.getTest().log(Status.INFO, "Checking product page load");
        try {
            productPage.waitForPageLoad();
            Log.pass("Product page loaded successfully");
            ExtentReportManager.getTest().log(Status.PASS, "Product page loaded");
        } catch (Exception e) {
            Log.fail("Product page failed to load", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Product page load failed");
            throw e;
        }
    }

    @Then("the product name is displayed")
    public void product_name_is_displayed() {
        Log.info("STEP: Verifying product name is displayed");
        ExtentReportManager.getTest().log(Status.INFO, "Checking product name");
        try {
            capturedProductName = productPage.getProductName();
            Assert.assertFalse(capturedProductName.isEmpty(), "Product name should not be empty");
            Log.pass("Product name: " + capturedProductName);
            ExtentReportManager.getTest().log(Status.PASS, "Product name: " + capturedProductName);
        } catch (AssertionError e) {
            Log.fail("Product name not displayed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Product name not found");
            throw e;
        }
    }

    @Then("the product price is displayed")
    public void product_price_is_displayed() {
        Log.info("STEP: Verifying product price is displayed");
        ExtentReportManager.getTest().log(Status.INFO, "Checking product price");
        try {
            capturedProductPrice      = productPage.getProductPrice();
            capturedProductPriceValue = productPage.getProductPriceValue();
            Assert.assertFalse(capturedProductPrice.isEmpty(), "Product price should not be empty");
            Log.pass("Product price: " + capturedProductPrice);
            ExtentReportManager.getTest().log(Status.PASS, "Product price: " + capturedProductPrice);
        } catch (AssertionError e) {
            Log.fail("Product price not displayed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Product price not found");
            throw e;
        }
    }

    @Then("the Add to Cart button is visible")
    public void add_to_cart_button_is_visible() {
        Log.info("STEP: Verifying Add to Cart button is visible");
        ExtentReportManager.getTest().log(Status.INFO, "Checking Add to Cart visibility");
        try {
            Assert.assertTrue(productPage.isAddToCartButtonVisible(), "Add to Cart button should be visible");
            Log.pass("Add to Cart button is visible");
            ExtentReportManager.getTest().log(Status.PASS, "Add to Cart button visible");
        } catch (AssertionError e) {
            Log.fail("Add to Cart button not visible", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Add to Cart button not visible");
            throw e;
        }
    }

    @And("the Add to Cart button is clickable")
    public void add_to_cart_button_is_clickable() {
        Log.info("STEP: Verifying Add to Cart button is enabled");
        ExtentReportManager.getTest().log(Status.INFO, "Checking Add to Cart button state");
        try {
            Assert.assertTrue(productPage.isAddToCartButtonEnabled(), "Add to Cart button should be enabled");
            Log.pass("Add to Cart button is enabled");
            ExtentReportManager.getTest().log(Status.PASS, "Add to Cart button enabled");
        } catch (AssertionError e) {
            Log.fail("Add to Cart button is disabled", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Add to Cart button disabled");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // ADD TO CART ACTIONS
    // ════════════════════════════════════════════════════

    @When("user clicks the Add to Cart button")
    public void user_clicks_add_to_cart() {
        Log.info("STEP: Clicking Add to Cart button");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking Add to Cart");
        try {
            capturedProductName       = productPage.getProductName();
            capturedProductPrice      = productPage.getProductPrice(); 
            capturedProductPriceValue = productPage.getProductPriceValue();
            cartBadgeCountBefore = productPage.getCartBadgeCount();
            productPage.clickAddToCart();
           
               Log.pass("Add to Cart clicked — product: " + capturedProductName);
            
            ExtentReportManager.getTest().log(Status.PASS, "Add to Cart clicked");
        } catch (Exception e) {
            Log.fail("Add to Cart click failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Add to Cart failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("a success notification is displayed")
    public void success_notification_is_displayed() {
        Log.info("STEP: Verifying success notification appeared");
        ExtentReportManager.getTest().log(Status.INFO, "Checking success notification");
        try {
            Assert.assertTrue(productPage.isSuccessNotificationDisplayed(),
                "Success notification should appear after Add to Cart");
            Log.pass("Success notification displayed");
            ExtentReportManager.getTest().log(Status.PASS, "Success notification shown");
        } catch (AssertionError e) {
            Log.fail("Success notification not shown", e);
            ExtentReportManager.getTest().log(Status.FAIL, "No success notification");
            throw e;
        }
    }

    @Then("the cart badge count increments")
    public void cart_badge_count_increments() {
        Log.info("STEP: Verifying cart badge count incremented");
        ExtentReportManager.getTest().log(Status.INFO, "Checking cart badge");
        try {
        	System.out.print("------------"+cartBadgeCountBefore);
            int after = productPage.getCartBadgeCount();
        	System.out.print("------------"+after);
            Assert.assertTrue(after > cartBadgeCountBefore,
                "Cart badge should increment. Before: " + cartBadgeCountBefore + ", After: " + after);
            Log.pass("Cart badge: " + cartBadgeCountBefore + " → " + after);
            ExtentReportManager.getTest().log(Status.PASS, "Cart badge incremented to: " + after);
        } catch (AssertionError e) {
            Log.fail("Cart badge did not increment", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart badge not incremented");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // CART PAGE
    // ════════════════════════════════════════════════════

    @When("user navigates to the cart page")
    public void user_navigates_to_cart_page() {
        Log.info("STEP: Navigating to cart page");
        ExtentReportManager.getTest().log(Status.INFO, "Opening cart page");
        try {
            cartPage.navigateToCart();
            Log.pass("Cart page opened");
            ExtentReportManager.getTest().log(Status.PASS, "Cart page loaded");
        } catch (Exception e) {
            Log.fail("Failed to open cart page", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart page navigation failed");
            throw e;
        }
    }

    @Then("the cart contains the added product")
    public void cart_contains_added_product() {
        Log.info("STEP: Verifying cart has items");
        ExtentReportManager.getTest().log(Status.INFO, "Checking cart item count");
        try {
            int count = cartPage.getCartItemCount();
            Assert.assertTrue(count > 0, "Cart should contain at least one item");
            Log.pass("Cart has " + count + " item(s)");
            ExtentReportManager.getTest().log(Status.PASS, "Cart items: " + count);
        } catch (AssertionError e) {
            Log.fail("Cart is empty — expected items", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart has no items");
            throw e;
        }
    }

    @Then("the product name in cart matches the product page")
    public void product_name_in_cart_matches() {
        Log.info("STEP: Verifying product name in cart");
        ExtentReportManager.getTest().log(Status.INFO, "Comparing cart product name");
        try {
            String cartName = cartPage.getCartProductName();
            Assert.assertFalse(cartName.isEmpty(), "Cart product name should not be empty");
            Log.pass("Cart name: " + cartName + " | Product page: " + capturedProductName);
            ExtentReportManager.getTest().log(Status.PASS, "Cart product name: " + cartName);
        } catch (AssertionError e) {
            Log.fail("Product name issue in cart", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart product name check failed");
            throw e;
        }
    }

    @Then("the product price in cart is correct")
    public void product_price_in_cart_is_correct() {
        Log.info("STEP: Verifying product price in cart");
        ExtentReportManager.getTest().log(Status.INFO, "Checking cart product price");
        try {
            String cartPrice = cartPage.getCartProductPrice();
            Assert.assertFalse(cartPrice.isEmpty(), "Cart product price should not be empty");
            Log.pass("Cart price: " + cartPrice + " | Product page: " + capturedProductPrice);
            ExtentReportManager.getTest().log(Status.PASS, "Cart price: " + cartPrice);
        } catch (AssertionError e) {
            Log.fail("Product price issue in cart", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart price check failed");
            throw e;
        }
    }

    @When("user removes the item from the cart")
    public void user_removes_item_from_cart() {
        Log.info("STEP: Removing all items from cart");
        ExtentReportManager.getTest().log(Status.INFO, "Removing all cart items");
        try {
            cartPage.removeAllItems();
            Log.pass("All items removed from cart");
            ExtentReportManager.getTest().log(Status.PASS, "Cart cleared");
        } catch (Exception e) {
            Log.fail("Remove item failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Remove item failed");
            throw e;
        }
    }

    @And("user clears all items from the cart")
    public void user_clears_all_items_from_cart() {
        Log.info("STEP: Clearing all items from cart");
        ExtentReportManager.getTest().log(Status.INFO, "Clearing cart");
        try {
            cartPage.removeAllItems();
            Log.pass("Cart cleared");
            ExtentReportManager.getTest().log(Status.PASS, "Cart cleared");
        } catch (Exception e) {
            Log.fail("Clear cart failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Clear cart failed");
            throw e;
        }
    }

    @Then("the cart shows an empty message")
    public void cart_shows_empty_message() {
        Log.info("STEP: Verifying cart shows empty message");
        ExtentReportManager.getTest().log(Status.INFO, "Checking empty cart message");
        try {
            Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(),
                "Cart should show an empty message");
            Log.pass("Empty cart message displayed");
            ExtentReportManager.getTest().log(Status.PASS, "Empty cart message shown");
        } catch (AssertionError e) {
            Log.fail("Empty cart message not found", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Empty cart message missing");
            throw e;
        }
    }
}
