package org.saroz.vtigercrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * <h2>ConfigReader</h2>
 * This utility class is responsible for loading configuration settings from the
 * {@code config.properties} file. It provides centralized access to environment 
 * URLs, credentials, and API keys.
 *
 * <p>Standardized to support both vTiger CRM and AgileCRM configurations.</p>
 *
 * @author Saroz
 */
public class ConfigReader {

    private Properties prop;
    private final String path = System.getProperty("user.dir") + "/src/test/resources/config.properties";

    /**
     * Constructor: Initializes the {@link Properties} object and loads the file.
     * Uses try-with-resources to ensure the {@link FileInputStream} is closed automatically.
     */
    public ConfigReader() {
        try (FileInputStream fis = new FileInputStream(path)) {
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            System.err.println("[ERROR] Failed to load configuration file at: " + path);
            throw new RuntimeException("Check if config.properties exists in src/test/resources/", e);
        }
    }

    // ==========================================
    // AGILE CRM - GENERIC API CONFIGURATION
    // ==========================================

    /**
     * Retrieves the Base URL for AgileCRM API interactions.
     * @return String Agile Base URL
     */
    public String getBaseurl() {
        return prop.getProperty("agile.Baseurl");
    }

    /**
     * Retrieves the registered email for AgileCRM API authentication.
     * @return String Agile Email
     */
    public String getApiEmail() {
        return prop.getProperty("agile.email");
    }

    /**
     * Retrieves the secret API Key for AgileCRM.
     * @return String Agile API Key
     */
    public String getApiKey() {
        return prop.getProperty("agile.apiKey");
    }

    // ==========================================
    // vTIGER CRM - LOGIN CONFIGURATIONS
    // ==========================================

    /**
     * Retrieves the target login URL for the vTiger CRM application.
     * @return String vTiger Login URL
     */
    public String getLoginUrl() {
        return prop.getProperty("vt.loginurl");
    }

    /**
     * Retrieves the valid username for vTiger functional testing.
     * @return String Valid Username
     */
    public String getValidUserName() {
        return prop.getProperty("vt.username");
    }

    /**
     * Retrieves the valid password for vTiger functional testing.
     * Note: Key matches 'vt.userpasword' as defined in properties.
     * @return String Valid Password
     */
    public String getValidUserPass() {
        return prop.getProperty("vt.userpasword");
    }

    /**
     * Retrieves an incorrect username for negative login scenarios.
     * @return String Invalid Username
     */
    public String getInvalidUserName() {
        return prop.getProperty("vt.invalidUserName");
    }

    /**
     * Retrieves an incorrect password for negative login scenarios.
     * @return String Invalid Password
     */
    public String getInvalidUserPass() {
        return prop.getProperty("vt.invalidUserPass");
    }

    // ==========================================
    // UTILITY METHODS
    // ==========================================

    /**
     * Checks if debug mode is enabled in the configuration.
     * @return String "true" or "false"
     */
    public String getDebugStatus() {
        return prop.getProperty("debug", "false");
    }
}