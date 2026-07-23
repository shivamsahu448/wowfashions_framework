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

public class ProductPage {

    private final String readymadeUrl;
    private final WebDriver driver;
    private final WaitUtils wait;
    private final WebDriverWait shortWait;

    // ── Listing Page — First Product Card ────────────────────
    private final By firstProductCard =
        By.xpath("//*[@id=\"main-content\"]/div/div/div[2]/div[3]/div/div[2]/div[2]/div[1]/div/div[1]/div[1]");
    private final By firstProductCardFallback =
        By.xpath("(//a[.//img][not(ancestor::header)][not(ancestor::footer)][not(ancestor::nav)])[1]");

    // ── Product Name ──────────────────────────────────────────
    private final By productName =
        By.xpath("//h1");
    private final By productNameFallback =
        By.cssSelector("[class*='product-name'], [class*='productName'], [class*='product-title'], [class*='ProductTitle']");

    // ── Product Price ─────────────────────────────────────────
    private final By productPrice =
        By.xpath("(//*[contains(@class,'price') and not(ancestor::*[contains(@class,'cart')])])[1]");
    private final By productPriceFallback =
        By.cssSelector("[class*='product-price']:not([class*='cart']), [class*='ProductPrice'], [itemprop='price']");

    // ── Add to Cart Button ────────────────────────────────────
    private final By addToCartBtn =
        By.xpath("//button[text()='Add to Cart']");
    private final By addToCartBtnFallback =
        By.cssSelector("[class*='add-to-cart'], [class*='addToCart'], [data-testid='add-to-cart']");

    // ── Success Notification / Toast ──────────────────────────
    private final By successNotification =
        By.xpath("//*[contains(normalize-space(.),'Items Added Successfully') "
               + "or contains(normalize-space(.),'Added Successfully') "
               + "or contains(normalize-space(.),'Added to Cart') "
               + "or contains(normalize-space(.),'added to cart')]");
    private final By successNotificationFallback =
        By.xpath("(//*[@role='dialog'] | //*[@role='alert'] | //*[@role='status'])"
               + "[contains(normalize-space(.),'Added') or contains(normalize-space(.),'cart') or contains(normalize-space(.),'success')]");

    // ── Cart Badge ────────────────────────────────────────────
    // Any numeric-only leaf element in the header is the cart count badge
    private final By cartBadge =
        By.xpath("(//header//*[not(*) "
               + "and translate(normalize-space(text()), '0123456789', '')='' "
               + "and string-length(normalize-space(text()))>0 "
               + "and string-length(normalize-space(text()))<=3])[1]");
    private final By cartBadgeFallback =
        By.cssSelector("header a[href*='cart'] span, [data-testid='cart-count'], header [aria-label*='cart'] span");

    // ── Loading Spinner ───────────────────────────────────────
    private final By loadingSpinner =
        By.cssSelector("[class*='loading'], [class*='spinner'], [class*='Loader']");

    public ProductPage(WebDriver driver) {
        this.driver       = driver;
        this.wait         = new WaitUtils(driver);
        this.shortWait    = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.readymadeUrl = new ConfigReader().getReadymadeUrl();
    }

    // ── Navigation ────────────────────────────────────────────

    public void navigateToReadymadeAndSelectFirst() {
        driver.get(readymadeUrl);
        wait.waitForDomStable();
        // Products load asynchronously via API; 30 s is needed — default 10 s is not enough.
        WebDriverWait productWait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement first;
        try {
            first = productWait.until(ExpectedConditions.elementToBeClickable(firstProductCard));
        } catch (Exception e) {
            System.out.println("[ProductPage] Primary product card locator failed, trying fallback: " + e.getMessage());
            first = productWait.until(ExpectedConditions.elementToBeClickable(firstProductCardFallback));
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", first);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", first);
        wait.waitForDomStable();
        waitForPageLoad();
    }

    public void waitForPageLoad() {
        try {
            shortWait.until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception ignored) {}
        wait.waitForDomStable();
    }

    // ── Product Info ──────────────────────────────────────────

    public String getProductName() {
        try {
            return wait.waitForVisible(productName).getText().trim();
        } catch (Exception e) {
            System.out.println("[ProductPage] h1 locator failed, trying fallback: " + e.getMessage());
            return wait.waitForVisible(productNameFallback).getText().trim();
        }
    }

    public String getProductPrice() {
        try {
            return wait.waitForVisible(productPrice).getText().trim();
        } catch (Exception e) {
            System.out.println("[ProductPage] Price primary locator failed, trying fallback: " + e.getMessage());
            return wait.waitForVisible(productPriceFallback).getText().trim();
        }
    }

    public double getProductPriceValue() {
        String raw = getProductPrice().replaceAll("[^0-9.]", "");
        try {
            return Double.parseDouble(raw);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // ── Add to Cart Button ────────────────────────────────────

    public boolean isAddToCartButtonVisible() {
        try {
            return wait.waitForVisible(addToCartBtn).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.waitForVisible(addToCartBtnFallback).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public boolean isAddToCartButtonEnabled() {
        try {
            WebElement btn = wait.waitForVisible(addToCartBtn);
            return btn.isEnabled() && !"true".equals(btn.getAttribute("disabled"));
        } catch (Exception e) {
            try {
                WebElement btn = wait.waitForVisible(addToCartBtnFallback);
                return btn.isEnabled();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public void clickAddToCart() {
        try {
            WebElement btn = wait.waitForVisible(addToCartBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("[ProductPage] Add to Cart primary failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForVisible(addToCartBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    // ── Notifications & Badge ─────────────────────────────────

    public boolean isSuccessNotificationDisplayed() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(successNotification));
            return true;
        } catch (Exception e) {
            try {
                shortWait.until(ExpectedConditions.visibilityOfElementLocated(successNotificationFallback));
                return true;
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public int getCartBadgeCount() {
        try {
            String text = driver.findElement(cartBadge).getText().trim().replaceAll("[^0-9]", "");
            return text.isEmpty() ? 0 : Integer.parseInt(text);
        } catch (Exception e) {
            try {
                String text = driver.findElement(cartBadgeFallback).getText().trim().replaceAll("[^0-9]", "");
                return text.isEmpty() ? 0 : Integer.parseInt(text);
            } catch (Exception ex) {
                return 0;
            }
        }
    }
}
