package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitUtils;
import java.time.Duration;
import java.util.List;

/**
 * FilterPage — Page Object Model
 * Screenshot ke UI ke hisaab se:
 *   - "Filters & Sort" heading
 *   - Price Range slider (black bar, 2 circle thumbs)
 *   - Min Price input box (Rs 0)
 *   - Max Price input box (Rs 50000)
 */
public class FilterPage {

    private final WebDriver driver;
    private final WaitUtils wait;
    // ── Filter panel heading ─────────────────────────────
    private final By filterPanelHeading =
        By.xpath("//*[contains(text(),'Filters') and contains(text(),'Sort')]");

    // ── Price Range Slider thumbs ────────────────────────
    // Left  circle = Min thumb (input[type='range'] OR role="slider" — works for both native and custom React sliders)
    private final By sliderThumbMin =
        By.xpath("(//input[@type='range'] | //*[@role='slider'])[1]");
    // Right circle = Max thumb
    private final By sliderThumbMax =
        By.xpath("(//input[@type='range'] | //*[@role='slider'])[last()]");

    // ── Min / Max input boxes ────────────────────────────
    // Primary locators — inspect actual HTML and update if needed
    private final By minPriceInput =
        By.xpath("//label[contains(text(),'Min')]/following::input[1]");
    private final By maxPriceInput =
        By.xpath("//label[contains(text(),'Max')]/following::input[1]");

    // Fallback if labels don't exist
    private final By minPriceInputFallback =
        By.cssSelector("input[name*='min'], input[placeholder*='Min'], input[name*='Min']");
    private final By maxPriceInputFallback =
        By.cssSelector("input[name*='max'], input[placeholder*='Max'], input[name*='Max']");

    // ── Product Grid ─────────────────────────────────────
    private final By productCards =
        By.cssSelector("[class*='product-card'], [class*='ProductCard'], [data-testid='product-card']");
    private final By productPrices =
        By.cssSelector("[class*='product-price'], [class*='ProductPrice'], [data-testid='price']");
    private final By noResultsMsg =
        By.cssSelector("[class*='no-results'], [class*='empty'], [class*='Empty']");
    private final By loadingSpinner =
        By.cssSelector("[class*='loading'], [class*='spinner'], [class*='Loader']");

    // ── Clear / Reset ────────────────────────────────────
    // normalize-space(.) = full text including descendants; no @class (CSS modules = hashed names)
    private final By clearAllBtn =
        By.xpath(
            "//button[contains(normalize-space(.),'Clear') or contains(normalize-space(.),'Reset')]" +
            " | //*[@role='button'][contains(normalize-space(.),'Clear') or contains(normalize-space(.),'Reset')]" +
            " | //a[contains(normalize-space(.),'Clear') or contains(normalize-space(.),'Reset')]"
        );

    // ── Sort ─────────────────────────────────────────────
    private final By sortDropdown =
        By.cssSelector("select[name*='sort'], [class*='sort'] select");

    public FilterPage(WebDriver driver) {
        this.driver  = driver;
        this.wait    = new WaitUtils(driver);
        new Actions(driver);
    }

    // ════════════════════════════════════════════════════
    // NAVIGATION
    // ════════════════════════════════════════════════════

    public void navigateTo(String url) {
        driver.get(url);
        wait.waitForDomStable();
        try {
            wait.waitForVisible(filterPanelHeading);
        } catch (Exception e) {
            System.out.println("[INFO] Filter heading not immediately visible — continuing.");
        }
        // Wait until the min price input stops being replaced by React re-renders.
        // Locates the element twice with a 500 ms gap; JS === confirms same DOM node.
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> {
                try {
                    WebElement e1 = d.findElement(minPriceInput);
                    Thread.sleep(500);
                    WebElement e2 = d.findElement(minPriceInput);
                    Object same = ((JavascriptExecutor) d)
                        .executeScript("return arguments[0] === arguments[1];", e1, e2);
                    return Boolean.TRUE.equals(same);
                } catch (Exception ex) {
                    return false;
                }
            });
        } catch (Exception ignored) {}
    }

    // ════════════════════════════════════════════════════
    // MIN PRICE INPUT
    // ════════════════════════════════════════════════════

    public void setMinPrice(String value) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement input = findPriceInput(minPriceInput, minPriceInputFallback);
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", input);
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                input = findPriceInput(minPriceInput, minPriceInputFallback);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
                input = findPriceInput(minPriceInput, minPriceInputFallback);
                clearInputFully(input);
                input.sendKeys(value);
                return;
            } catch (StaleElementReferenceException e) {
                if (attempt == 2) throw e;
                wait.waitForDomStable();
            }
        }
    }

    public String getMinPriceValue() {
        WebElement input = findPriceInput(minPriceInput, minPriceInputFallback);
        String val = input.getAttribute("value");
        return val != null ? val.trim() : "";
    }

    // ════════════════════════════════════════════════════
    // MAX PRICE INPUT
    // ════════════════════════════════════════════════════

    public void setMaxPrice(String value) {
        for (int attempt = 0; attempt < 3; attempt++) {
            try {
                WebElement input = findPriceInput(maxPriceInput, maxPriceInputFallback);
                ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView({block:'center'});", input);
                try { Thread.sleep(200); } catch (InterruptedException ignored) {}
                input = findPriceInput(maxPriceInput, maxPriceInputFallback);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", input);
                input = findPriceInput(maxPriceInput, maxPriceInputFallback);
                clearInputFully(input);
                input.sendKeys(value);
                return;
            } catch (StaleElementReferenceException e) {
                if (attempt == 2) throw e;
                wait.waitForDomStable();
            }
        }
    }

    public String getMaxPriceValue() {
        WebElement input = findPriceInput(maxPriceInput, maxPriceInputFallback);
        String val = input.getAttribute("value");
        return val != null ? val.trim() : "";
    }

    public void pressTabToConfirm() {
        // Tab = onBlur event — React/Next.js me state update trigger karta hai
        driver.switchTo().activeElement().sendKeys(Keys.TAB);
        waitForGridRefresh();
    }

    public void pressEnterToConfirm() {
        driver.switchTo().activeElement().sendKeys(Keys.ENTER);
        waitForGridRefresh();
    }

    // ════════════════════════════════════════════════════
    // PRICE SLIDER
    // ════════════════════════════════════════════════════

    /**
     * Min slider thumb ko arrow keys se desired value tak le jao
     * e.g. dragMinSliderTo(1000) — slider 0 se 1000 tak jayega
     */
    public void dragMinSliderTo(int targetValue) {
        moveSliderByArrowKeys(sliderThumbMin, targetValue);
    }

    public void dragMaxSliderTo(int targetValue) {
        moveSliderByArrowKeys(sliderThumbMax, targetValue);
    }

    public int getSliderMinValue() {
        try {
            return readSliderValue(sliderThumbMin);
        } catch (Exception e) {
            // Custom slider (no input[type='range'] or role='slider') — read from price input
            String val = getMinPriceValue();
            try { return Integer.parseInt(val.trim()); } catch (Exception ex) { return 0; }
        }
    }

    public int getSliderMaxValue() {
        return readSliderValue(sliderThumbMax);
    }

    private void moveSliderByArrowKeys(By thumbBy, int targetValue) {
        WebElement thumb = wait.waitForClickable(thumbBy);
        wait.scrollAndClick(thumb);

        int current = readSliderValue(thumbBy);
        int step    = getSliderStep(thumb);
        int diff    = targetValue - current;
        int presses = Math.abs(diff) / Math.max(step, 1);
        Keys direction = diff > 0 ? Keys.ARROW_RIGHT : Keys.ARROW_LEFT;

        for (int i = 0; i < presses; i++) {
            thumb.sendKeys(direction);
        }
        waitForGridRefresh();
    }

    private int readSliderValue(By thumbBy) {
        WebElement thumb = driver.findElement(thumbBy);
        String val = thumb.getAttribute("value");
        if (val == null || val.isEmpty()) val = thumb.getAttribute("aria-valuenow");
        try { return Integer.parseInt(val.trim()); }
        catch (Exception e) { return 0; }
    }

    private int getSliderStep(WebElement thumb) {
        String step = thumb.getAttribute("step");
        try { return (step != null && !step.isEmpty()) ? Integer.parseInt(step.trim()) : 1; }
        catch (NumberFormatException e) { return 1; }
    }

    // ════════════════════════════════════════════════════
    // PRODUCT GRID HELPERS
    // ════════════════════════════════════════════════════

    public int getProductCount() {
        try { return driver.findElements(productCards).size(); }
        catch (Exception e) { return 0; }
    }

    public boolean isNoResultsMessageDisplayed() {
        try { return driver.findElement(noResultsMsg).isDisplayed(); }
        catch (NoSuchElementException e) { return false; }
    }

    /** Verify all visible product prices are within [minPrice, maxPrice] */
    public boolean areAllProductPricesInRange(int minPrice, int maxPrice) {
        List<WebElement> priceEls = driver.findElements(productPrices);
        for (WebElement el : priceEls) {
            String raw = el.getText().replaceAll("[^0-9.]", "").trim();
            if (raw.isEmpty()) continue;
            try {
                double price = Double.parseDouble(raw);
                if (price < minPrice || price > maxPrice) {
                    System.out.println("[FAIL] Price " + price + " is outside " + minPrice + "–" + maxPrice);
                    return false;
                }
            } catch (NumberFormatException ignored) {}
        }
        return true;
    }

    public void waitForGridRefresh() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                .until(ExpectedConditions.invisibilityOfElementLocated(loadingSpinner));
        } catch (Exception ignored) {}
        wait.waitForDomStable();
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    // ════════════════════════════════════════════════════
    // CLEAR / RESET
    // ════════════════════════════════════════════════════

    public void clickClearFilters() {
        try {
            // 5s timeout — if no button, fall back to manual reset
            WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.presenceOfElementLocated(clearAllBtn));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            // No dedicated Clear/Reset button on this page — manually reset price inputs to defaults
            setMinPrice("0");
            setMaxPrice("50000");
            pressTabToConfirm();
        }
        waitForGridRefresh();
    }

    // ════════════════════════════════════════════════════
    // SORT
    // ════════════════════════════════════════════════════

    public void selectSortOption(String label) {
        try {
            new Select(wait.waitForClickable(sortDropdown)).selectByVisibleText(label);
        } catch (Exception e) {
            By custom = By.xpath(
                "//*[contains(@class,'sort-option')][normalize-space(text())='" + label + "']");
            wait.scrollAndClick(wait.waitForClickable(custom));
        }
        waitForGridRefresh();
    }

    // ════════════════════════════════════════════════════
    // PRIVATE UTILS
    // ════════════════════════════════════════════════════

    private WebElement findPriceInput(By primary, By fallback) {
        try {
            return wait.waitForVisible(primary);
        } catch (Exception e) {
            System.out.println("[INFO] Primary locator failed, trying fallback...");
            return wait.waitForVisible(fallback);
        }
    }

    private void clearInputFully(WebElement input) {
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.clear();
    }
 // ════════════════════════════════════════════════════
    // FABRIC MATERIAL CHECKBOXES
    // (Screenshot: Chiffon Zari, Cotton Italy, etc.)
    // ════════════════════════════════════════════════════

    /**
     * Fabric Material section visible hai ya nahi
     */
    public boolean isFabricMaterialSectionVisible() {
        By fabricHeading = By.xpath(
            "//*[normalize-space(text())='Fabric Material']"
        );
        try {
            boolean headingDisplayed = driver.findElement(fabricHeading).isDisplayed();
            boolean sectionExpanded  = isFilterSectionExpanded("Fabric Material");
            return headingDisplayed && sectionExpanded;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Fabric checkbox click karna by label text
     * e.g. clickFabricCheckbox("Linen")
     */
    public void clickFabricCheckbox(String fabricName) {
        expandFilterSection("Fabric Material");

        // Try 1: checkbox inside/preceding a label whose descendant OR direct text matches
        By checkboxByLabel = By.xpath(
            "//label[.//*[normalize-space(text())='" + fabricName + "'] or normalize-space(text())='" + fabricName + "']" +
            "//input[@type='checkbox']" +
            " | //label[.//*[normalize-space(text())='" + fabricName + "'] or normalize-space(text())='" + fabricName + "']" +
            "/preceding-sibling::input[@type='checkbox']"
        );

        // Try 2: click label itself — browser toggles its checkbox automatically
        By labelWithText = By.xpath(
            "//label[.//*[normalize-space(text())='" + fabricName + "']]" +
            " | //label[normalize-space(text())='" + fabricName + "']" +
            " | //label[normalize-space(.)='" + fabricName + "']"
        );

        // Try 3: climb 2 ancestor levels from text, find nearest checkbox — class-independent
        By checkboxNearText = By.xpath(
            "//*[normalize-space(text())='" + fabricName + "']/ancestor::*[2]//input[@type='checkbox'][1]"
        );

        // Try 4: leaf element with exact text — JS click bypasses Selenium interactability check
        By textLeaf = By.xpath(
            "//*[normalize-space(text())='" + fabricName + "'][not(*)]"
        );

        try {
            wait.scrollAndClick(wait.waitForClickable(checkboxByLabel));
        } catch (Exception e1) {
            try {
                wait.scrollAndClick(wait.waitForClickable(labelWithText));
            } catch (Exception e2) {
                try {
                    wait.scrollAndClick(wait.waitForClickable(checkboxNearText));
                } catch (Exception e3) {
                    // waitForVisible use karo — span/text nodes "clickable" nahi hote Selenium mein
                    WebElement el = wait.waitForVisible(textLeaf);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
                }
            }
        }
        waitForGridRefresh();
    }

    /**
     * Check karo ki fabric checkbox selected hai ya nahi
     */
    public boolean isFabricCheckboxSelected(String fabricName) {
        // Same class-independent approach as clickFabricCheckbox
        By checkboxInput = By.xpath(
            "//label[.//*[normalize-space(text())='" + fabricName + "'] or normalize-space(text())='" + fabricName + "']//input[@type='checkbox']" +
            " | //label[.//*[normalize-space(text())='" + fabricName + "'] or normalize-space(text())='" + fabricName + "']/preceding-sibling::input[@type='checkbox']" +
            " | //*[normalize-space(text())='" + fabricName + "']/ancestor::*[2]//input[@type='checkbox'][1]"
        );
        try {
            WebElement checkbox = driver.findElement(checkboxInput);
            // JS .checked reads actual boolean state — works on display:none hidden inputs too
            Boolean jsChecked = (Boolean) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].checked;", checkbox);
            if (jsChecked != null) return jsChecked;
            return checkbox.isSelected();
        } catch (Exception e) {
            // Fallback: aria-checked or visual class on ancestor container (3 levels up)
            By container = By.xpath(
                "//*[normalize-space(text())='" + fabricName + "']/ancestor::*[3]"
            );
            try {
                WebElement el = driver.findElement(container);
                String aria = el.getAttribute("aria-checked");
                if (aria != null) return "true".equals(aria);
                String cls = el.getAttribute("class");
                return cls != null && (cls.contains("active") || cls.contains("selected") || cls.contains("checked"));
            } catch (Exception ex) {
                return false;
            }
        }
    }

    // ════════════════════════════════════════════════════
    // ACCORDION SECTIONS (Neck Pattern, Brands, etc.)
    // Screenshot: "Neck Pattern ˅" = collapsed
    //             "Fabric Material ^" = expanded
    // ════════════════════════════════════════════════════

    /**
     * Section collapsed hai check karna
     * (down arrow ˅ = collapsed, up arrow ^ = expanded)
     */
    public boolean isFilterSectionCollapsed(String sectionName) {
        By heading = By.xpath(
            "//*[normalize-space(text())='" + sectionName + "']/ancestor::*[contains(@class,'filter') or contains(@class,'accordion')][1]"
        );
        try {
            WebElement el = driver.findElement(heading);
            String aria = el.getAttribute("aria-expanded");
            if (aria != null) return "false".equals(aria);

            // Fallback: check if child options are hidden
            By options = By.xpath(
                "//*[normalize-space(text())='" + sectionName + "']" +
                "/following-sibling::*[contains(@class,'option') or contains(@class,'list')]"
            );
            return driver.findElements(options).isEmpty();
        } catch (Exception e) {
            return true; // assume collapsed
        }
    }

    /**
     * Section expand karna — heading pe click karo
     */
    public void expandFilterSection(String sectionName) {
        // Already expanded hai toh return — warna click se collapse ho jayega
        if (isFilterSectionExpanded(sectionName)) return;

        By headingLocator = By.xpath(
            "//button[contains(normalize-space(.), '" + sectionName + "')]" +
            " | //div[@role='button'][contains(normalize-space(.), '" + sectionName + "')]" +
            " | //*[normalize-space(text())='" + sectionName + "']"
        );
        try {
            // presenceOfElementLocated — visibility nahi chahiye, sirf DOM mein ho
            // (filter panel below-the-fold ho sakta hai, waitForVisible fail karta hai)
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(headingLocator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
            try { Thread.sleep(200); } catch (InterruptedException ignored) {}
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception e) {
            throw new RuntimeException("Section heading nahi mila: " + sectionName, e);
        }
        waitForSectionExpanded(sectionName);
    }

    private void waitForSectionExpanded(String sectionName) {
        wait.waitForDomStable();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(d ->
                isFilterSectionExpanded(sectionName)
            );
        } catch (Exception ignored) {}
    }

    /**
     * Section expanded hai ya nahi check karna
     */
    public boolean isFilterSectionExpanded(String sectionName) {
        // Strategy 1: aria-expanded on ancestor (works if site uses ARIA)
        By ariaContainer = By.xpath(
            "//*[normalize-space(text())='" + sectionName + "']/ancestor::*[@aria-expanded][1]"
        );
        try {
            List<WebElement> els = driver.findElements(ariaContainer);
            if (!els.isEmpty()) {
                String aria = els.get(0).getAttribute("aria-expanded");
                if (aria != null) return "true".equals(aria);
            }
        } catch (Exception ignored) {}

        // Strategy 2: ancestor levels 2-5 mein visible checkboxes dhundho — class-independent
        // ancestor::*[contains(@class,'filter')] nahi use karte — CSS modules mein class hashed hoti hai
        for (int lvl = 2; lvl <= 5; lvl++) {
            By xp = By.xpath(
                "//*[normalize-space(text())='" + sectionName + "']/ancestor::*[" + lvl + "]//input[@type='checkbox']"
            );
            try {
                List<WebElement> cbs = driver.findElements(xp);
                for (WebElement cb : cbs) {
                    if (cb.isDisplayed()) return true;
                }
            } catch (Exception ignored) {}
        }

        return false;
    }

    /**
     * Kisi section ka pehla checkbox select karna
     * e.g. Neck Pattern expand ke baad pehla option select karo
     */
    public void selectFirstOptionInSection(String sectionName) {
        // isFilterSectionExpanded wala same loop — level 2-5 mein visible checkbox dhundho
        for (int lvl = 2; lvl <= 5; lvl++) {
            By xp = By.xpath(
                "//*[normalize-space(text())='" + sectionName + "']/ancestor::*[" + lvl + "]//input[@type='checkbox'][1]"
            );
            try {
                List<WebElement> cbs = driver.findElements(xp);
                if (!cbs.isEmpty() && cbs.get(0).isDisplayed()) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cbs.get(0));
                    waitForGridRefresh();
                    return;
                }
            } catch (Exception ignored) {}
        }
        throw new RuntimeException("'" + sectionName + "' mein koi option nahi mila");
    }
}
