package org.saroz.vtigercrm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.saroz.vtigercrm.base.BasePage;
import com.aventstack.extentreports.ExtentTest;

public class LoginPage extends BasePage {

    // Locators for Login Page Elements
    @FindBy(name = "user_name") 
    private WebElement usernameField;
    
    @FindBy(name = "user_password") 
    private WebElement passwordField;
    
    @FindBy(id = "submitbutton") 
    private WebElement loginButton;
    
    @FindBy(className = "errorMessage") 
    private WebElement errorMess;

    // Constructor to pass driver and reporting instance to parent class
    public LoginPage(WebDriver driver, ExtentTest extentTest) {
        super(driver, extentTest);
    }

    // Action to open the application login URL
    public void openurl(String url) {
        utils.openPageUrl(url, "vtiger Login Url");
    }
    
    // Action to enter text in username field
    public void enterUsername(String username) {
        type(usernameField, username, "UserName TextBox");
    }

    // Action to enter text in password field
    public void enterPassword(String password) {
        type(passwordField, password, "Password TextBox");
    }

    // Action to click the login button
    public void clickLogin() {
        click(loginButton, "Login Button");
    }

    // Combined business flow to login in a single step
    public void loginToApplication(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }
    
    // Verification method to assert error message for invalid login
    public void verifyErrorMessageIsDisplayed() {
        utils.validateTextContainsHard(
            getText(errorMess, "Error Message"), 
            "You must specify a valid username and password", 
            "Get error Message When user input Wrong Credential"
        );
    }
}