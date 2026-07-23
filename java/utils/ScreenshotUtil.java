package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String name) {
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        return saveScreenshotBytes(bytes, name);
    }

    public static String saveScreenshotBytes(byte[] bytes, String name) {
        String safe = name.replaceAll("[\\\\/:*?\"<>|]", "_");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filePath = System.getProperty("user.dir")
                + "/screenshots/"
                + safe + "_" + timeStamp + ".png";

        File dest = new File(filePath);
        try {
            Files.createDirectories(dest.getParentFile().toPath());
            Files.write(dest.toPath(), bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}