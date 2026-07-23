package java.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private static final Logger logger = LoggerFactory.getLogger(Log.class);

    // INFO
    public static void info(String message) {
        logger.info(message);
    }

    // WARN
    public static void warn(String message) {
        logger.warn(message);
    }

    // ERROR with message
    public static void error(String message) {
        logger.error(message);
    }

    // ERROR with exception (IMPORTANT for debugging failures)
    public static void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }

    // DEBUG (useful for locator/debug logs)
    public static void debug(String message) {
        logger.debug(message);
    }

    // STEP SUCCESS (for reporting clarity in Cucumber)
    public static void pass(String message) {
        logger.info("✅ PASS: " + message);
    }

    // STEP FAIL (clean test failure logs)
    public static void fail(String message) {
        logger.error("❌ FAIL: " + message);
    }

    public static void fail(String message, Throwable throwable) {
        logger.error("❌ FAIL: " + message, throwable);
    }
}