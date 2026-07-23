package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;
import utils.WaitUtils;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private final String cartUrl;
    private final WebDriver driver;
    private final WaitUtils wait;

    // ── Cart Items ────────────────────────────────────────────
    // One "Remove" link exists per cart item — count them to get item count
    private final By cartItems =
        By.xpath("//a[normalize-space(.)='Remove' and not(ancestor::header)] "
               + "| //button[normalize-space(.)='Remove' and not(ancestor::header)]");

    // ── Product Name in Cart ──────────────────────────────────
    // Product names follow the pattern "Name (SKU)" e.g. "Qiswa Jalabiya (QJ377)"
    private final By cartProductName =
        By.xpath("(//main//*[self::a or self::p or self::h4 or self::h3 or self::span]"
               + "[contains(normalize-space(.),'(') and string-length(normalize-space(.))>5 "
               + "and not(ancestor::header) and not(ancestor::nav) "
               + "and not(contains(normalize-space(.),'Remove')) and not(contains(normalize-space(.),'Wishlist')) "
               + "and not(contains(normalize-space(.),'Standard')) and not(contains(normalize-space(.),'Item total')) "
               + "and not(contains(normalize-space(.),'Expected delivery'))])[1]");
    private final By cartProductNameFallback =
        By.xpath("(//main//a[contains(normalize-space(.),'(') and not(ancestor::header)])[1]");

    // ── Product Price in Cart ─────────────────────────────────
    // First ₹/AED price element inside main (item price, before the order summary total)
    private final By cartProductPrice =
        By.xpath("(//main//*[starts-with(normalize-space(text()),'₹') or starts-with(normalize-space(text()),'AED')]"
               + "[not(ancestor::header) and not(ancestor::nav)])[1]");
    private final By cartProductPriceFallback =
        By.xpath("(//main//*[contains(text(),'₹') and not(ancestor::header)])[1]");

    // ── Quantity Controls in Cart ─────────────────────────────
    private final By cartQtyInput =
        By.xpath("(//input[@type='number' and ancestor::*[contains(@class,'cart')]])[1]");
    private final By cartQtyInputFallback =
        By.cssSelector("[class*='cart-item'] input[type='number'], [class*='cart-item'] input[class*='qty']");

    private final By cartQtyIncrementBtn =
        By.xpath("(//*[contains(@class,'cart-item') or contains(@class,'lineItem')]"
               + "//button[normalize-space(text())='+' or @aria-label='Increase quantity'])[1]");
    private final By cartQtyIncrementBtnFallback =
        By.cssSelector("[class*='cart-item'] [class*='increment'], [class*='cart-item'] [class*='plus']");

    private final By cartQtyDecrementBtn =
        By.xpath("(//*[contains(@class,'cart-item') or contains(@class,'lineItem')]"
               + "//button[normalize-space(text())='-' or @aria-label='Decrease quantity'])[1]");
    private final By cartQtyDecrementBtnFallback =
        By.cssSelector("[class*='cart-item'] [class*='decrement'], [class*='cart-item'] [class*='minus']");

    // ── Remove Button ─────────────────────────────────────────
    // "Remove" appears as a plain text link next to each cart item
    private final By removeItemBtn =
        By.xpath("(//a[normalize-space(.)='Remove' and not(ancestor::header)] "
               + "| //button[normalize-space(.)='Remove' and not(ancestor::header)])[1]");
    private final By removeItemBtnFallback =
        By.xpath("(//a[contains(normalize-space(.),'Remove') and not(ancestor::header) and string-length(normalize-space(.))<15] "
               + "| //button[contains(@aria-label,'remove') or contains(@aria-label,'Remove')])[1]");

    // ── Subtotal ──────────────────────────────────────────────
    private final By cartSubtotal =
        By.xpath("(//*[contains(normalize-space(.),'Subtotal') or contains(normalize-space(.),'Total')]"
               + "/following-sibling::*)[1]");
    private final By cartSubtotalFallback =
        By.cssSelector("[class*='subtotal'], [class*='cart-total'], [class*='cartTotal'], [data-testid='subtotal']");

    // ── Empty Cart Message ────────────────────────────────────
    private final By emptyCartMessage =
        By.xpath("//*[contains(normalize-space(.),'cart is empty') "
               + "or contains(normalize-space(.),'Your cart is empty') "
               + "or contains(normalize-space(.),'No items in cart')]");
    private final By emptyCartFallback =
        By.cssSelector("[class*='empty-cart'], [class*='emptyCart'], [class*='cart-empty']");

    // ── Loading Spinner ───────────────────────────────────────
    private final By loadingSpinner =
        By.cssSelector("[class*='loading'], [class*='spinner'], [class*='Loader']");

    public CartPage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WaitUtils(driver);
        this.cartUrl = new ConfigReader().getCartUrl();
    }

    // ── Navigation ────────────────────────────────────────────

    public void navigateToCart() {
        driver.get(cartUrl);
        wait.waitForDomStable();
        // Cart data loads asynchronously — wait for items or empty-cart text to appear
        WebDriverWait cartWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        try {
            cartWait.until(d -> {
                List<WebElement> removeLinks = d.findElements(By.xpath(
                    "//a[normalize-space(.)='Remove' and not(ancestor::header)] "
                    + "| //button[normalize-space(.)='Remove' and not(ancestor::header)]"));
                List<WebElement> emptyMsg = d.findElements(By.xpath(
                    "//*[contains(normalize-space(.),'Your cart is empty') "
                    + "or contains(normalize-space(.),'cart is empty') "
                    + "or contains(normalize-space(.),'No items')]"));
                return !removeLinks.isEmpty() || !emptyMsg.isEmpty();
            });
        } catch (Exception ignored) {}
    }

    public boolean isCartPageLoaded() {
        try {
            wait.waitForDomStable();
            return driver.getCurrentUrl().contains("cart");
        } catch (Exception e) {
            return false;
        }
    }

    // ── Cart Item Queries ─────────────────────────────────────

    public int getCartItemCount() {
        try {
            List<WebElement> items = driver.findElements(cartItems);
            return items.size();
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean hasItems() {
        return getCartItemCount() > 0;
    }

    public String getCartProductName() {
        try {
            return wait.waitForVisible(cartProductName).getText().trim();
        } catch (Exception e) {
            System.out.println("[CartPage] Product name primary failed, trying fallback: " + e.getMessage());
            return wait.waitForVisible(cartProductNameFallback).getText().trim();
        }
    }

    public String getCartProductPrice() {
        try {
            return wait.waitForVisible(cartProductPrice).getText().trim();
        } catch (Exception e) {
            System.out.println("[CartPage] Price primary failed, trying fallback: " + e.getMessage());
            return wait.waitForVisible(cartProductPriceFallback).getText().trim();
        }
    }

    public int getCartQuantity() {
        try {
            WebElement input = wait.waitForVisible(cartQtyInput);
            String val = input.getAttribute("value");
            if (val == null || val.isEmpty()) val = input.getText().trim();
            return Integer.parseInt(val.trim());
        } catch (Exception e) {
            try {
                String val = wait.waitForVisible(cartQtyInputFallback).getAttribute("value");
                return Integer.parseInt(val == null ? "0" : val.trim());
            } catch (Exception ex) {
                return 0;
            }
        }
    }

    // ── Quantity Controls ─────────────────────────────────────

    public void clickCartQtyIncrement() {
        try {
            WebElement btn = wait.waitForClickable(cartQtyIncrementBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            btn.click();
        } catch (Exception e) {
            System.out.println("[CartPage] Increment primary failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForClickable(cartQtyIncrementBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        waitForCartUpdate();
    }

    public void clickCartQtyDecrement() {
        try {
            WebElement btn = wait.waitForClickable(cartQtyDecrementBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            btn.click();
        } catch (Exception e) {
            System.out.println("[CartPage] Decrement primary failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForClickable(cartQtyDecrementBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        waitForCartUpdate();
    }

    public void clickRemoveItem() {
        try {
            WebElement btn = wait.waitForVisible(removeItemBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("[CartPage] Remove primary failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForVisible(removeItemBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        waitForCartUpdate();
    }

    public void removeAllItems() {
        By removeLinkLocator = By.xpath(
            "//a[normalize-space(.)='Remove' and not(ancestor::header)] "
            + "| //button[normalize-space(.)='Remove' and not(ancestor::header)]");
        for (int i = 0; i < 30; i++) {
            List<WebElement> links = driver.findElements(removeLinkLocator);
            if (links.isEmpty()) break;
            try {
                WebElement btn = links.get(0);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
                waitForCartUpdate();
            } catch (Exception e) {
                break;
            }
        }
    }

    // ── Subtotal & Empty State ────────────────────────────────

    public double getSubtotalValue() {
        try {
            String raw = wait.waitForVisible(cartSubtotal).getText().trim().replaceAll("[^0-9.]", "");
            return raw.isEmpty() ? 0.0 : Double.parseDouble(raw);
        } catch (Exception e) {
            try {
                String raw = wait.waitForVisible(cartSubtotalFallback).getText().trim().replaceAll("[^0-9.]", "");
                return raw.isEmpty() ? 0.0 : Double.parseDouble(raw);
            } catch (Exception ex) {
                return 0.0;
            }
        }
    }

    public boolean isEmptyCartMessageDisplayed() {
        try {
            return wait.waitForVisible(emptyCartMessage).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.waitForVisible(emptyCartFallback).isDisplayed();
            } catch (Exception ex) {
                return getCartItemCount() == 0;
            }
        }
    }

    public void waitForCartUpdate() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                .until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception ignored) {}
        wait.waitForDomStable();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }
}
