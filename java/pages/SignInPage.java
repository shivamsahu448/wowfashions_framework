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

public class SignInPage {

    private final String signInUrl;
    private final WebDriver driver;
    private final WaitUtils wait;
    private final WebDriverWait shortWait;

    // ── Email Input ───────────────────────────────────────
    private final By emailInput =
        By.xpath("//input[@type='email' "
               + "or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'email') "
               + "or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'mobile')]");
    private final By emailInputFallback =
        By.cssSelector("input[type='email'], input[name*='email'], input[id*='email'], input[name*='mobile']");

    // ── Send OTP Button ───────────────────────────────────
    private final By sendOtpBtn =
        By.xpath("//button[normalize-space(.)='Send OTP' or normalize-space(.)='SEND OTP' "
               + "or contains(normalize-space(.),'Send OTP')]");
    private final By sendOtpBtnFallback =
        By.cssSelector("button[class*='otp'], button[id*='otp'], button[id*='sendOtp']");

    // ── OTP Input — single 6-char field ──────────────────
    private final By otpSingleInput =
        By.xpath("//input[@maxlength='6' "
               + "or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'otp') "
               + "or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'code') "
               + "or contains(translate(@placeholder,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz'),'enter otp')]");
    private final By otpSingleInputFallback =
        By.cssSelector("input[maxlength='6'], input[class*='otp'], input[id*='otp']");

    // ── OTP Input — 6 individual digit boxes ─────────────
    private final By otpBoxes =
        By.xpath("//input[@maxlength='1' and (@type='text' or @type='number' or @type='tel')]");

    // ── Verify and Continue Button ────────────────────────
    private final By verifyBtn =
        By.xpath("//button[contains(normalize-space(.),'Verify') and contains(normalize-space(.),'Continue')]");
    private final By verifyBtnFallback =
        By.xpath("//button[normalize-space(.)='Verify' or normalize-space(.)='VERIFY' "
               + "or contains(normalize-space(.),'Verify and Continue') "
               + "or contains(normalize-space(.),'verify')]");

    // ── Error / Validation Message ────────────────────────
    private final By errorMessage =
        By.xpath("//*[@role='alert' "
               + "or contains(@class,'error') or contains(@class,'Error') "
               + "or contains(@class,'invalid') or contains(@class,'Invalid')]");
    private final By errorMessageFallback =
        By.cssSelector("[class*='toast'], [class*='notification'], [class*='message'][class*='error']");

    // ── Sign-in Success — account indicator in header ─────
    // After sign-in the header replaces "Sign In" with a profile/account link
    private final By accountIndicator =
        By.xpath("//header//*[contains(@class,'account') or contains(@class,'profile') "
               + "or contains(@class,'user') or contains(@class,'User')]");
    private final By accountIndicatorFallback =
        By.cssSelector("header a[href*='account'], header a[href*='profile'], "
                     + "header [aria-label*='account'], header [aria-label*='Account']");

    // ── Account Menu Button (header — visible when signed in) ─
    private final By accountMenuBtn =
        By.xpath("//button[@aria-label='Account menu']");
    private final By accountMenuBtnFallback =
        By.cssSelector("button[aria-label='Account menu']");

    // ── Logout Option in dropdown ──────────────────────────
    private final By logoutOption =
        By.xpath("//button[normalize-space(.)='Logout' or normalize-space(.)='Log Out' "
               + "or normalize-space(.)='Sign Out'] "
               + "| //a[normalize-space(.)='Logout' or normalize-space(.)='Log Out' "
               + "or normalize-space(.)='Sign Out']");
    private final By logoutOptionFallback =
        By.cssSelector("a[href*='logout'], button[class*='logout'], [data-testid*='logout']");

    public SignInPage(WebDriver driver) {
        this.driver    = driver;
        this.wait      = new WaitUtils(driver);
        this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
        this.signInUrl = new ConfigReader().getSignInUrl();
    }

    // ── Navigation ────────────────────────────────────────

    public void navigateToSignIn() {
        driver.get(signInUrl);
        wait.waitForDomStable();
    }

    public boolean isSignInPageLoaded() {
        try {
            wait.waitForDomStable();
            boolean urlOk = driver.getCurrentUrl().contains("sign-in");
            boolean inputVisible = isEmailInputVisible();
            return urlOk && inputVisible;
        } catch (Exception e) {
            return false;
        }
    }

    // ── Email Section ─────────────────────────────────────

    public boolean isEmailInputVisible() {
        try {
            return wait.waitForVisible(emailInput).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.waitForVisible(emailInputFallback).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public boolean isSendOtpButtonVisible() {
        try {
            return wait.waitForVisible(sendOtpBtn).isDisplayed();
        } catch (Exception e) {
            try {
                return wait.waitForVisible(sendOtpBtnFallback).isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public boolean isSendOtpButtonEnabled() {
        try {
            WebElement btn = wait.waitForVisible(sendOtpBtn);
            return btn.isEnabled() && !"true".equals(btn.getAttribute("disabled"));
        } catch (Exception e) {
            try {
                WebElement btn = wait.waitForVisible(sendOtpBtnFallback);
                return btn.isEnabled() && !"true".equals(btn.getAttribute("disabled"));
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public void enterEmail(String email) {
        try {
            WebElement input = wait.waitForVisible(emailInput);
            input.clear();
            input.sendKeys(email);
        } catch (Exception e) {
            System.out.println("[SignInPage] Email primary locator failed, trying fallback: " + e.getMessage());
            WebElement input = wait.waitForVisible(emailInputFallback);
            input.clear();
            input.sendKeys(email);
        }
    }

    public void clickSendOtp() {
        try {
            WebElement btn = wait.waitForClickable(sendOtpBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("[SignInPage] Send OTP primary locator failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForClickable(sendOtpBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.waitForDomStable();
    }

    // ── OTP Section ───────────────────────────────────────

    public boolean isOtpSectionVisible() {
        WebDriverWait otpWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            otpWait.until(ExpectedConditions.visibilityOfElementLocated(otpSingleInput));
            return true;
        } catch (Exception e) {
            try {
                otpWait.until(d -> d.findElements(otpBoxes).size() >= 6);
                return true;
            } catch (Exception ex) {
                try {
                    return otpWait.until(ExpectedConditions.visibilityOfElementLocated(otpSingleInputFallback)) != null;
                } catch (Exception exx) {
                    return false;
                }
            }
        }
    }

    public void enterOtp(String otp) {
        // Try 6 individual digit boxes first (most common pattern)
        try {
            List<WebElement> boxes = driver.findElements(otpBoxes);
            if (boxes.size() >= 6) {
                for (int i = 0; i < 6 && i < otp.length(); i++) {
                    boxes.get(i).click();
                    boxes.get(i).sendKeys(String.valueOf(otp.charAt(i)));
                }
                return;
            }
        } catch (Exception ignored) {}
        // Fall back to single OTP input
        try {
            WebElement input = wait.waitForVisible(otpSingleInput);
            input.clear();
            input.sendKeys(otp);
        } catch (Exception e) {
            System.out.println("[SignInPage] OTP single input fallback: " + e.getMessage());
            WebElement input = wait.waitForVisible(otpSingleInputFallback);
            input.clear();
            input.sendKeys(otp);
        }
    }

    public boolean isVerifyButtonEnabled() {
        try {
            WebElement btn = wait.waitForVisible(verifyBtn);
            return btn.isEnabled() && !"true".equals(btn.getAttribute("disabled"));
        } catch (Exception e) {
            try {
                WebElement btn = wait.waitForVisible(verifyBtnFallback);
                return btn.isEnabled() && !"true".equals(btn.getAttribute("disabled"));
            } catch (Exception ex) {
                return false;
            }
        }
    }

    public void clickVerifyAndContinue() {
        try {
            WebElement btn = wait.waitForClickable(verifyBtn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("[SignInPage] Verify primary locator failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForClickable(verifyBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.waitForDomStable();
    }

    // ── Post-Login Verification ───────────────────────────

    public boolean isSignedIn() {
        // User is signed in when: no longer on sign-in page AND account element visible in header
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> !d.getCurrentUrl().contains("sign-in"));
        } catch (Exception ignored) {}
        try {
            return driver.findElements(accountIndicator).stream().anyMatch(WebElement::isDisplayed);
        } catch (Exception e) {
            try {
                return driver.findElements(accountIndicatorFallback).stream().anyMatch(WebElement::isDisplayed);
            } catch (Exception ex) {
                // Last resort: URL no longer on sign-in page
                return !driver.getCurrentUrl().contains("sign-in");
            }
        }
    }

    // ── Error Messages ────────────────────────────────────

    // HTML5 native browser validation — not in DOM, read via JS
    private boolean hasHtmlValidationError() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            WebElement input = driver.findElement(emailInput);
            js.executeScript("arguments[0].focus();", input);           // 1. focus
            js.executeScript("arguments[0].reportValidity();", input);  // 2. trigger tooltip
            String msg = (String) js.executeScript(
                "return arguments[0].validationMessage;", input);        // 3. read message
            return msg != null && !msg.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed() {
        try {
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return true;
        } catch (Exception e) {
            try {
                shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageFallback));
                return true;
            } catch (Exception ex) {
                return hasHtmlValidationError();
            }
        }
    }

    public String getErrorMessage() {
        try {
            return wait.waitForVisible(errorMessage).getText().trim();
        } catch (Exception e) {
            try {
                return wait.waitForVisible(errorMessageFallback).getText().trim();
            } catch (Exception ex) {
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    WebElement input = driver.findElement(emailInput);
                    String msg = (String) js.executeScript(
                        "return arguments[0].validationMessage;", input);
                    return msg != null ? msg.trim() : "";
                } catch (Exception exx) {
                    return "";
                }
            }
        }
    }

    // ── Logout ────────────────────────────────────────────

    public void clickAccountMenu() {
        try {
            WebElement btn = wait.waitForClickable(accountMenuBtn);
            ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", btn);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        } catch (Exception e) {
            System.out.println("[SignInPage] Account menu primary locator failed, trying fallback: " + e.getMessage());
            WebElement btn = wait.waitForClickable(accountMenuBtnFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
        wait.waitForDomStable();
    }

    public void clickLogout() {
        try {
            WebElement opt = wait.waitForClickable(logoutOption);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
        } catch (Exception e) {
            System.out.println("[SignInPage] Logout primary locator failed, trying fallback: " + e.getMessage());
            WebElement opt = wait.waitForClickable(logoutOptionFallback);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", opt);
        }
        wait.waitForDomStable();
    }

    public boolean isLoggedOut() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> d.findElements(accountMenuBtn).isEmpty()
                          || !d.findElements(accountMenuBtn).get(0).isDisplayed());
            return true;
        } catch (Exception e) {
            String url = driver.getCurrentUrl();
            return !url.contains("sign-in") && !url.contains("account/");
        }
    }
}
