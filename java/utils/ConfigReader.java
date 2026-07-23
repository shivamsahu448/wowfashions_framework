package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    Properties prop;

    public ConfigReader() {

        prop = new Properties();

        try (InputStream fis = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config/config.properties")) {

            if (fis == null) {
                throw new RuntimeException("config/config.properties not found on classpath");
            }

            prop.load(fis);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getBrowser() {
        return prop.getProperty("browser");
    }

    public String getUrl() {
        return prop.getProperty("base.url") + "/";
    }

    public String getCartUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.cart");
    }

    public String getReadymadeUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.readymade");
    }

    public String getDarziyaUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.darziya");
    }

    public String getFabricsUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.fabrics");
    }

    public String getJalabiyaUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.jalabiya");
    }

    public String getSignInUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.signin");
    }

    public String getAeReadymadeUrl() {
        return prop.getProperty("base.url") + prop.getProperty("path.readymadeAE");
    }

    public String getUserEmail() {
        return prop.getProperty("user.email");
    }

    public String getUserOtp() {
        return prop.getProperty("user.otp");
    }
}
