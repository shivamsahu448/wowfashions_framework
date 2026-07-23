package stepdefinitions;

import com.aventstack.extentreports.Status;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.SignInPage;
import utils.ConfigReader;
import utils.DriverFactory;
import utils.ExtentReportManager;
import utils.Log;

public class SignInSteps {

    private final SignInPage signInPage;
    private final ConfigReader config;

    public SignInSteps() {
        WebDriver driver = DriverFactory.getDriver();
        this.signInPage = new SignInPage(driver);
        this.config     = new ConfigReader();
    }

    // ════════════════════════════════════════════════════
    // BACKGROUND
    // ════════════════════════════════════════════════════

    @Given("user is on the sign-in page")
    public void user_is_on_sign_in_page() {
        Log.info("STEP: Navigating to sign-in page");
        ExtentReportManager.getTest().log(Status.INFO, "Opening sign-in page");
        try {
            signInPage.navigateToSignIn();
            Assert.assertTrue(signInPage.isSignInPageLoaded(), "Sign-in page did not load");
            Log.pass("Sign-in page loaded");
            ExtentReportManager.getTest().log(Status.PASS, "Sign-in page loaded");
        } catch (AssertionError | Exception e) {
            Log.fail("Sign-in page failed to load", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Sign-in page load failed: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // POSITIVE — PAGE LOAD
    // ════════════════════════════════════════════════════

    @Then("the email input field is visible")
    public void email_input_is_visible() {
        Log.info("STEP: Verifying email input is visible");
        ExtentReportManager.getTest().log(Status.INFO, "Checking email input visibility");
        try {
            Assert.assertTrue(signInPage.isEmailInputVisible(), "Email input should be visible");
            Log.pass("Email input is visible");
            ExtentReportManager.getTest().log(Status.PASS, "Email input visible");
        } catch (AssertionError e) {
            Log.fail("Email input not visible", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Email input not visible");
            throw e;
        }
    }

    @Then("the Send OTP button is visible")
    public void send_otp_button_is_visible() {
        Log.info("STEP: Verifying Send OTP button is visible");
        ExtentReportManager.getTest().log(Status.INFO, "Checking Send OTP button visibility");
        try {
            Assert.assertTrue(signInPage.isSendOtpButtonVisible(), "Send OTP button should be visible");
            Log.pass("Send OTP button is visible");
            ExtentReportManager.getTest().log(Status.PASS, "Send OTP button visible");
        } catch (AssertionError e) {
            Log.fail("Send OTP button not visible", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Send OTP button not visible");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // POSITIVE — OTP FLOW
    // ════════════════════════════════════════════════════

    @When("user enters valid email and clicks Send OTP")
    public void user_enters_valid_email_and_clicks_send_otp() {
        String email = config.getUserEmail();
        Log.info("STEP: Entering valid email: " + email);
        ExtentReportManager.getTest().log(Status.INFO, "Entering email: " + email);
        try {
            signInPage.enterEmail(email);
            signInPage.clickSendOtp();
            Log.pass("Email entered and Send OTP clicked");
            ExtentReportManager.getTest().log(Status.PASS, "Send OTP clicked for: " + email);
        } catch (Exception e) {
            Log.fail("Failed to enter email / click Send OTP", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Send OTP failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("the OTP input section is displayed")
    public void otp_input_section_is_displayed() {
        Log.info("STEP: Verifying OTP input section appeared");
        ExtentReportManager.getTest().log(Status.INFO, "Checking OTP section visibility");
        try {
            Assert.assertTrue(signInPage.isOtpSectionVisible(), "OTP input section should appear after Send OTP");
            Log.pass("OTP input section is displayed");
            ExtentReportManager.getTest().log(Status.PASS, "OTP section visible");
        } catch (AssertionError e) {
            Log.fail("OTP input section not displayed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "OTP section not visible");
            throw e;
        }
    }

    @And("user enters valid OTP and clicks Verify and Continue")
    public void user_enters_valid_otp_and_clicks_verify() {
        String otp = config.getUserOtp();
        Log.info("STEP: Entering valid OTP and clicking Verify and Continue");
        ExtentReportManager.getTest().log(Status.INFO, "Entering OTP and verifying");
        try {
            signInPage.enterOtp(otp);
            signInPage.clickVerifyAndContinue();
            Log.pass("OTP entered and Verify clicked");
            ExtentReportManager.getTest().log(Status.PASS, "Verify and Continue clicked");
        } catch (Exception e) {
            Log.fail("Failed to enter OTP / click Verify", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Verify failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("the user is successfully signed in")
    public void user_is_successfully_signed_in() {
        Log.info("STEP: Verifying user is signed in");
        ExtentReportManager.getTest().log(Status.INFO, "Checking sign-in success");
        try {
            Assert.assertTrue(signInPage.isSignedIn(), "User should be signed in after valid OTP");
            Log.pass("User successfully signed in");
            ExtentReportManager.getTest().log(Status.PASS, "User signed in successfully");
        } catch (AssertionError e) {
            Log.fail("User is not signed in", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Sign-in verification failed");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // NEGATIVE — EMAIL VALIDATION
    // ════════════════════════════════════════════════════

    @Then("the Send OTP button is disabled")
    public void send_otp_button_is_disabled() {
        Log.info("STEP: Verifying Send OTP button is disabled with empty email");
        ExtentReportManager.getTest().log(Status.INFO, "Checking Send OTP button disabled state");
        try {
            Assert.assertFalse(signInPage.isSendOtpButtonEnabled(),
                "Send OTP button should be disabled when email is empty");
            Log.pass("Send OTP button is disabled as expected");
            ExtentReportManager.getTest().log(Status.PASS, "Send OTP button disabled");
        } catch (AssertionError e) {
            Log.fail("Send OTP button is enabled — expected disabled", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Send OTP button should be disabled");
            throw e;
        }
    }

    @When("user enters {string} and clicks Send OTP")
    public void user_enters_email_and_clicks_send_otp(String email) {
        Log.info("STEP: Entering email '" + email + "' and clicking Send OTP");
        ExtentReportManager.getTest().log(Status.INFO, "Entering email: " + email);
        try {
            signInPage.enterEmail(email);
            signInPage.clickSendOtp();
            Log.pass("Email '" + email + "' entered and Send OTP clicked");
            ExtentReportManager.getTest().log(Status.PASS, "Send OTP clicked for: " + email);
        } catch (Exception e) {
            Log.fail("Failed to enter email / click Send OTP", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Send OTP failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("a validation or error message is displayed")
    public void validation_or_error_message_is_displayed() {
        Log.info("STEP: Verifying error/validation message is shown");
        ExtentReportManager.getTest().log(Status.INFO, "Checking for error message");
        try {
            Assert.assertTrue(signInPage.isErrorMessageDisplayed(),
                "A validation or error message should be displayed");
            String msg = signInPage.getErrorMessage();
            Log.pass("Error message displayed: " + msg);
            ExtentReportManager.getTest().log(Status.PASS, "Error message: " + msg);
        } catch (AssertionError e) {
            Log.fail("No error/validation message found", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Error message not displayed");
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // NEGATIVE — OTP VALIDATION
    // ════════════════════════════════════════════════════

    @And("user leaves the OTP field empty")
    public void user_leaves_otp_empty() {
        Log.info("STEP: Leaving OTP field empty (no entry)");
        ExtentReportManager.getTest().log(Status.INFO, "OTP field left empty");
        // Wait for OTP section to appear, then do nothing
        boolean visible = signInPage.isOtpSectionVisible();
        Log.pass("OTP section visible: " + visible + " — field left empty");
        ExtentReportManager.getTest().log(Status.PASS, "OTP field intentionally left empty");
    }

    @Then("the Verify and Continue button is disabled")
    public void verify_button_is_disabled() {
        Log.info("STEP: Verifying that Verify and Continue button is disabled");
        ExtentReportManager.getTest().log(Status.INFO, "Checking Verify button state");
        try {
            Assert.assertFalse(signInPage.isVerifyButtonEnabled(),
                "Verify and Continue button should be disabled when OTP is empty");
            Log.pass("Verify button is disabled as expected");
            ExtentReportManager.getTest().log(Status.PASS, "Verify button disabled");
        } catch (AssertionError e) {
            Log.fail("Verify button is enabled — expected disabled", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Verify button should be disabled");
            throw e;
        }
    }

    @And("user enters short OTP {string}")
    public void user_enters_short_otp(String otp) {
        Log.info("STEP: Entering short OTP '" + otp + "' (no Verify click)");
        ExtentReportManager.getTest().log(Status.INFO, "Entering short OTP: " + otp);
        try {
            signInPage.enterOtp(otp);
            Log.pass("Short OTP '" + otp + "' entered");
            ExtentReportManager.getTest().log(Status.PASS, "Short OTP entered: " + otp);
        } catch (Exception e) {
            Log.fail("Failed to enter short OTP", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Short OTP entry failed: " + e.getMessage());
            throw e;
        }
    }

    // ════════════════════════════════════════════════════
    // LOGOUT
    // ════════════════════════════════════════════════════

    @And("user clicks account menu and logs out")
    public void user_clicks_account_menu_and_logs_out() {
        Log.info("STEP: Clicking account menu and selecting Logout");
        ExtentReportManager.getTest().log(Status.INFO, "Clicking account menu → Logout");
        try {
            signInPage.clickAccountMenu();
            signInPage.clickLogout();
            Log.pass("Logout clicked successfully");
            ExtentReportManager.getTest().log(Status.PASS, "Logout action performed");
        } catch (Exception e) {
            Log.fail("Logout failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Logout failed: " + e.getMessage());
            throw e;
        }
    }

    @Then("the user is redirected to the home page")
    public void user_is_redirected_to_home_page() {
        Log.info("STEP: Verifying user is redirected to home page after logout");
        ExtentReportManager.getTest().log(Status.INFO, "Checking home page redirect");
        try {
            Assert.assertTrue(signInPage.isLoggedOut(),
                "User should be on home page after logout");
            Log.pass("User redirected to home page — logout successful");
            ExtentReportManager.getTest().log(Status.PASS, "Redirected to home page");
        } catch (AssertionError e) {
            Log.fail("Logout redirect failed", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Home page redirect not detected");
            throw e;
        }
    }

    @And("user enters OTP {string} and clicks Verify and Continue")
    public void user_enters_otp_and_clicks_verify(String otp) {
        Log.info("STEP: Entering OTP '" + otp + "' and clicking Verify and Continue");
        ExtentReportManager.getTest().log(Status.INFO, "Entering OTP: " + otp);
        try {
            signInPage.enterOtp(otp);
            signInPage.clickVerifyAndContinue();
            Log.pass("OTP '" + otp + "' entered and Verify clicked");
            ExtentReportManager.getTest().log(Status.PASS, "Verify clicked with OTP: " + otp);
        } catch (Exception e) {
            Log.fail("Failed to enter OTP / click Verify", e);
            ExtentReportManager.getTest().log(Status.FAIL, "Verify click failed: " + e.getMessage());
            throw e;
        }
    }
}
