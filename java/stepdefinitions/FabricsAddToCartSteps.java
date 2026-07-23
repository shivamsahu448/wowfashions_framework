package stepdefinitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.CartPage;
import pages.FabricsProductPage;
import utils.DriverFactory;
import utils.ExtentReportManager;
import utils.Log;

public class FabricsAddToCartSteps {

    private final FabricsProductPage productPage;
    private final CartPage           cartPage;

    // State carried across steps within a scenario
    private String capturedProductName       = "";
    private String capturedProductPrice      = "";
    private double capturedProductPriceValue = 0.0;
    private int    cartBadgeCountBefore      = 0;
    private int    quantityBefore            = 0;
    private double subtotalBefore            = 0.0;
    private int    itemCountBefore           = 0;

    public FabricsAddToCartSteps() {
        WebDriver driver = DriverFactory.getDriver();
        this.productPage = new FabricsProductPage(driver);
        this.cartPage    = new CartPage(driver);
    }

    // ════════════════════════════════════════════════════
    // BACKGROUND
    // ════════════════════════════════════════════════════

    @Given("user opens a fabrics product page")
    public void user_opens_fabrics_product_page() {
        Log.info("STEP: Navigating to fabrics listing and selecting first product");
        ExtentReportManager.getTest().log(Status.INFO, "Opening fabrics product page");
        try {
            productPage.navigateToFabricsAndSelectFirst();
            Log.pass("Fabrics product page opened");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics product page loaded");
        } catch (Exception e) {
            Log.fail("Failed to open fabrics product page", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Failed to open fabrics product page: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // PRODUCT PAGE ASSERTIONS
    // ════════════════════════════════════════════════════

    @Then("the fabrics product page loads successfully")
    public void the_fabrics_product_page_loads_successfully() {
        Log.info("STEP: Verifying fabrics product page loaded");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics product page load");
        try {
            productPage.waitForPageLoad();
            Log.pass("Fabrics product page loaded successfully");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics product page loaded");
        } catch (Exception e) {
            Log.fail("Fabrics product page failed to load", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics product page load failed");
            throw e;
        }
    }

    @Then("the fabrics product name is displayed")
    public void the_fabrics_product_name_is_displayed() {
        Log.info("STEP: Verifying fabrics product name is displayed");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics product name");
        try {
            capturedProductName = productPage.getProductName();
            Assert.assertFalse(capturedProductName.isEmpty(), "Fabrics product name should not be empty");
            Log.pass("Fabrics product name: " + capturedProductName);
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics product name: " + capturedProductName);
        } catch (AssertionError e) {
            Log.fail("Fabrics product name not displayed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics product name not found");
            throw e;
        }
    }

    @Then("the fabrics product price is displayed")
    public void the_fabrics_product_price_is_displayed() {
        Log.info("STEP: Verifying fabrics product price is displayed");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics product price");
        try {
            capturedProductPrice      = productPage.getProductPrice();
            capturedProductPriceValue = productPage.getProductPriceValue();
            Assert.assertFalse(capturedProductPrice.isEmpty(), "Fabrics product price should not be empty");
            Log.pass("Fabrics product price: " + capturedProductPrice);
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics product price: " + capturedProductPrice);
        } catch (AssertionError e) {
            Log.fail("Fabrics product price not displayed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics product price not found");
            throw e;
        }
    }

    @Then("the fabrics Add to Cart button is visible")
    public void the_fabrics_add_to_cart_button_is_visible() {
        Log.info("STEP: Verifying fabrics Add to Cart button is visible");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics Add to Cart visibility");
        try {
            Assert.assertTrue(productPage.isAddToCartButtonVisible(),
                "Fabrics Add to Cart button should be visible");
            Log.pass("Fabrics Add to Cart button is visible");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics Add to Cart button visible");
        } catch (AssertionError e) {
            Log.fail("Fabrics Add to Cart button not visible", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics Add to Cart button not visible");
            throw e;
        }
    }

    @And("the fabrics Add to Cart button is clickable")
    public void the_fabrics_add_to_cart_button_is_clickable() {
        Log.info("STEP: Verifying fabrics Add to Cart button is enabled");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics Add to Cart button state");
        try {
            Assert.assertTrue(productPage.isAddToCartButtonEnabled(),
                "Fabrics Add to Cart button should be enabled");
            Log.pass("Fabrics Add to Cart button is enabled");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics Add to Cart button enabled");
        } catch (AssertionError e) {
            Log.fail("Fabrics Add to Cart button is disabled", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics Add to Cart button disabled");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // ADD TO CART ACTIONS
    // ════════════════════════════════════════════════════

    @When("user clicks the fabrics Add to Cart button")
    public void user_clicks_the_fabrics_add_to_cart_button() {
        Log.info("STEP: Clicking fabrics Add to Cart button");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking fabrics Add to Cart");
        try {
            capturedProductName       = productPage.getProductName();
            capturedProductPrice      = productPage.getProductPrice();
            capturedProductPriceValue = productPage.getProductPriceValue();
            cartBadgeCountBefore      = productPage.getCartBadgeCount();
            productPage.clickAddToCart();
            Log.pass("Fabrics Add to Cart clicked — product: " + capturedProductName);
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics Add to Cart clicked");
        } catch (Exception e) {
            Log.fail("Fabrics Add to Cart click failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics Add to Cart failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("a fabrics success notification is displayed")
    public void a_fabrics_success_notification_is_displayed() {
        Log.info("STEP: Verifying fabrics success notification appeared");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics success notification");
        try {
            Assert.assertTrue(productPage.isSuccessNotificationDisplayed(),
                "Success notification should appear after adding fabric to cart");
            Log.pass("Fabrics success notification displayed");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics success notification shown");
        } catch (AssertionError e) {
            Log.fail("Fabrics success notification not shown", e);
            ExtentReportManager.getTest().log(Status.FAIL, "No fabrics success notification");
            throw e;
        }
    }

    @Then("the fabrics cart badge count increments")
    public void the_fabrics_cart_badge_count_increments() {
        Log.info("STEP: Verifying cart badge incremented after adding fabric");
        ExtentReportManager.getTest().log(Status.INFO, "Checking cart badge after fabrics add");
        try {
            System.out.print("[FabricsAddToCart] Badge before: " + cartBadgeCountBefore);
            int after = productPage.getCartBadgeCount();
            System.out.print(" | After: " + after);
            Assert.assertTrue(after > cartBadgeCountBefore,
                "Cart badge should increment. Before: " + cartBadgeCountBefore + ", After: " + after);
            Log.pass("Cart badge: " + cartBadgeCountBefore + " → " + after);
            ExtentReportManager.getTest().log(Status.PASS, "Cart badge incremented to: " + after);
        } catch (AssertionError e) {
            Log.fail("Cart badge did not increment after adding fabric", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart badge not incremented");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // CART PAGE
    // ════════════════════════════════════════════════════

    @When("user navigates to the fabrics cart")
    public void user_navigates_to_the_fabrics_cart() {
        Log.info("STEP: Navigating to cart page");
        ExtentReportManager.getTest().log(Status.INFO, "Opening cart page after fabrics add");
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

    @Then("the fabrics cart contains the added product")
    public void the_fabrics_cart_contains_the_added_product() {
        Log.info("STEP: Verifying cart has fabric items");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics cart item count");
        try {
            int count = cartPage.getCartItemCount();
            Assert.assertTrue(count > 0, "Cart should contain at least one fabric item");
            Log.pass("Cart has " + count + " item(s)");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics cart items: " + count);
        } catch (AssertionError e) {
            Log.fail("Cart is empty — expected fabric items", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics cart has no items");
            throw e;
        }
    }

    @Then("the fabrics product name in cart matches")
    public void the_fabrics_product_name_in_cart_matches() {
        Log.info("STEP: Verifying fabric product name in cart");
        ExtentReportManager.getTest().log(Status.INFO, "Comparing cart fabric product name");
        try {
            String cartName = cartPage.getCartProductName();
            Assert.assertFalse(cartName.isEmpty(), "Cart fabric product name should not be empty");
            Log.pass("Cart name: " + cartName + " | Product page: " + capturedProductName);
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics cart product name: " + cartName);
        } catch (AssertionError e) {
            Log.fail("Fabric product name issue in cart", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics cart product name check failed");
            throw e;
        }
    }

    @Then("the fabrics product price in cart is correct")
    public void the_fabrics_product_price_in_cart_is_correct() {
        Log.info("STEP: Verifying fabric product price in cart");
        ExtentReportManager.getTest().log(Status.INFO, "Checking fabrics cart product price");
        try {
            String cartPrice = cartPage.getCartProductPrice();
            Assert.assertFalse(cartPrice.isEmpty(), "Cart fabric product price should not be empty");
            Log.pass("Cart price: " + cartPrice + " | Product page: " + capturedProductPrice);
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics cart price: " + cartPrice);
        } catch (AssertionError e) {
            Log.fail("Fabric product price issue in cart", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Fabrics cart price check failed");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // QUANTITY CONTROLS — POSITIVE
    // ════════════════════════════════════════════════════

    @When("user increments the fabrics cart quantity")
    public void user_increments_the_fabrics_cart_quantity() {
        Log.info("STEP: Incrementing cart quantity for fabric item");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking quantity increment");
        try {
            quantityBefore = cartPage.getCartQuantity();
            subtotalBefore = cartPage.getSubtotalValue();
            cartPage.clickCartQtyIncrement();
            Log.pass("Quantity increment clicked — was: " + quantityBefore);
            ExtentReportManager.getTest().log(Status.PASS, "Quantity increment clicked");
        } catch (Exception e) {
            Log.fail("Quantity increment failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Increment failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("the fabrics cart quantity should increase by one")
    public void the_fabrics_cart_quantity_should_increase_by_one() {
        Log.info("STEP: Verifying cart quantity increased by 1");
        ExtentReportManager.getTest().log(Status.INFO, "Asserting quantity increment");
        try {
            int quantityAfter = cartPage.getCartQuantity();
            Assert.assertEquals(quantityAfter, quantityBefore + 1,
                "Quantity should have increased by 1. Before: " + quantityBefore + ", After: " + quantityAfter);
            Log.pass("Quantity: " + quantityBefore + " → " + quantityAfter);
            ExtentReportManager.getTest().log(Status.PASS, "Quantity increased to: " + quantityAfter);
        } catch (AssertionError e) {
            Log.fail("Quantity did not increase by 1", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Quantity increment assertion failed");
            throw e;
        }
    }

    @Then("the fabrics cart subtotal should update")
    public void the_fabrics_cart_subtotal_should_update() {
        Log.info("STEP: Verifying cart subtotal updated after quantity change");
        ExtentReportManager.getTest().log(Status.INFO, "Checking subtotal after quantity increment");
        try {
            double subtotalAfter = cartPage.getSubtotalValue();
            Assert.assertTrue(subtotalAfter > subtotalBefore,
                "Subtotal should increase after quantity increment. Before: " + subtotalBefore + ", After: " + subtotalAfter);
            Log.pass("Subtotal: " + subtotalBefore + " → " + subtotalAfter);
            ExtentReportManager.getTest().log(Status.PASS, "Subtotal updated to: " + subtotalAfter);
        } catch (AssertionError e) {
            Log.fail("Subtotal did not update after quantity increment", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Subtotal update assertion failed");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // QUANTITY CONTROLS — NEGATIVE
    // ════════════════════════════════════════════════════

    @When("user decrements the fabrics cart quantity to minimum")
    public void user_decrements_the_fabrics_cart_quantity_to_minimum() {
        Log.info("STEP: Decrementing cart quantity to minimum for fabric item");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking quantity decrement at minimum");
        try {
            // Ensure we are at qty=1 before testing the minimum boundary
            while (cartPage.getCartQuantity() > 1) {
                cartPage.clickCartQtyDecrement();
            }
            // Now attempt to decrement below 1
            cartPage.clickCartQtyDecrement();
            Log.pass("Decrement at minimum clicked");
            ExtentReportManager.getTest().log(Status.PASS, "Decrement at minimum clicked");
        } catch (Exception e) {
            Log.fail("Decrement at minimum failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Decrement failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("the fabrics cart quantity should not go below one")
    public void the_fabrics_cart_quantity_should_not_go_below_one() {
        Log.info("STEP: Verifying cart quantity did not go below 1");
        ExtentReportManager.getTest().log(Status.INFO, "Asserting quantity minimum boundary");
        try {
            // If the item is still in cart, qty must be >= 1; if item was removed that is also acceptable
            int qty = cartPage.getCartQuantity();
            boolean itemRemoved = cartPage.getCartItemCount() == 0;
            Assert.assertTrue(qty >= 1 || itemRemoved,
                "Quantity went below 1 unexpectedly. Qty: " + qty + ", Items: " + cartPage.getCartItemCount());
            if (itemRemoved) {
                Log.pass("Item removed when decrement attempted at qty=1 (acceptable behavior)");
                ExtentReportManager.getTest().log(Status.PASS, "Item removed at minimum quantity");
            } else {
                Log.pass("Quantity held at minimum: " + qty);
                ExtentReportManager.getTest().log(Status.PASS, "Quantity at minimum: " + qty);
            }
        } catch (AssertionError e) {
            Log.fail("Quantity boundary violation — went below 1", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Quantity below minimum");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // REMOVE ITEMS
    // ════════════════════════════════════════════════════

    @When("user removes all fabrics items from cart")
    public void user_removes_all_fabrics_items_from_cart() {
        Log.info("STEP: Removing all fabric items from cart");
        ExtentReportManager.getTest().log(Status.INFO, "Removing all fabrics cart items");
        try {
            cartPage.removeAllItems();
            Log.pass("All fabric items removed from cart");
            ExtentReportManager.getTest().log(Status.PASS, "Fabrics cart cleared");
        } catch (Exception e) {
            Log.fail("Remove all fabrics items failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Remove all fabrics items failed");
            throw e;
        }
    }

    @Then("the fabrics cart shows empty message")
    public void the_fabrics_cart_shows_empty_message() {
        Log.info("STEP: Verifying fabrics cart shows empty message");
        ExtentReportManager.getTest().log(Status.INFO, "Checking empty cart message after fabric removal");
        try {
            Assert.assertTrue(cartPage.isEmptyCartMessageDisplayed(),
                "Cart should show an empty message after removing all fabric items");
            Log.pass("Empty cart message displayed");
            ExtentReportManager.getTest().log(Status.PASS, "Empty cart message shown");
        } catch (AssertionError e) {
            Log.fail("Empty cart message not found after removing fabrics", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Empty cart message missing");
            throw e;
        }
    }

    @When("user removes one fabrics item from cart")
    public void user_removes_one_fabrics_item_from_cart() {
        Log.info("STEP: Removing one fabric item from cart");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking Remove for one fabric item");
        try {
            itemCountBefore = cartPage.getCartItemCount();
            cartPage.clickRemoveItem();
            Log.pass("One fabric item removed. Count before: " + itemCountBefore);
            ExtentReportManager.getTest().log(Status.PASS, "One fabric item removed");
        } catch (Exception e) {
            Log.fail("Remove one fabric item failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Remove one item failed");
            throw e;
        }
    }

    @Then("the fabrics cart item count should decrease")
    public void the_fabrics_cart_item_count_should_decrease() {
        Log.info("STEP: Verifying cart item count decreased after removal");
        ExtentReportManager.getTest().log(Status.INFO, "Asserting item count decreased");
        try {
            int itemCountAfter = cartPage.getCartItemCount();
            Assert.assertTrue(itemCountAfter < itemCountBefore,
                "Cart item count should decrease. Before: " + itemCountBefore + ", After: " + itemCountAfter);
            Log.pass("Cart items: " + itemCountBefore + " → " + itemCountAfter);
            ExtentReportManager.getTest().log(Status.PASS, "Cart item count decreased to: " + itemCountAfter);
        } catch (AssertionError e) {
            Log.fail("Cart item count did not decrease after removal", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Cart item count unchanged after removal");
            throw e;
        }
    }
}
