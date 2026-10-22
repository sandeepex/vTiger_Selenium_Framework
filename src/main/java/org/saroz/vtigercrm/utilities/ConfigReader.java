package org.saroz.vtigercrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private Properties prop;
    private final String path = System.getProperty("user.dir") + "/src/test/resources/config.properties";

 
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

    public String getBaseurl() {
        return prop.getProperty("agile.Baseurl");
    }

    public String getApiEmail() {
        return prop.getProperty("agile.email");
    }

   
    public String getApiKey() {
        return prop.getProperty("agile.apiKey");
    }

    // ==========================================
    // vTIGER CRM - LOGIN CONFIGURATIONS
    // ==========================================

   
    public String getLoginUrl() {
        return prop.getProperty("vt.loginurl");
    }

   
    public String getValidUserName() {
        return prop.getProperty("vt.username");
    }

    
    public String getValidUserPass() {
        return prop.getProperty("vt.userpasword");
    }

    
    public String getInvalidUserName() {
        return prop.getProperty("vt.invalidUserName");
    }

   
    public String getInvalidUserPass() {
        return prop.getProperty("vt.invalidUserPass");
    }

    
    public String getDebugStatus() {
        return prop.getProperty("debug", "false");
    }
}