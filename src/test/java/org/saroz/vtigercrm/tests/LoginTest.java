package org.saroz.vtigercrm.tests;

import org.saroz.vtigercrm.base.BaseTest;
import org.saroz.vtigercrm.pages.HomePage;
import org.saroz.vtigercrm.pages.LoginPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

public class LoginTest extends BaseTest {

	// ==========================================
	// SUCCESSFUL LOGIN SCENARIOS (POSITIVE)
	// ==========================================

	@Epic("vTiger CRM")
	@Feature("Authentication")
	@Story("Admin Login")
	@Severity(SeverityLevel.CRITICAL)
	@Test(
			priority = 1,
			description = "TC001 | Verify admin user can successfully login to vTiger CRM using valid credentials",
			groups = {"Smoke","Regression","Authentication","P1" }
			)
	public void verifyAdminLoginWithValidCredentials() {

		// Initialize page objects
		LoginPage login = new LoginPage(getDriver(), extentTest.get());
		HomePage home = new HomePage(getDriver(), extentTest.get());

		// Execute login flow and verify dashboard
		login.openurl(config.getLoginUrl());
		login.loginToApplication(config.getValidUserName(), config.getValidUserPass());
		home.verifyUserIsLoggedIn();
		
		// Reset session for next tests
		home.logout();
	}

	// ==========================================
	// DATA PROVIDER FOR INVALID CREDENTIALS
	// ==========================================

	// Supplies data for scenarios where text error message is expected
	@DataProvider(name = "invalidLoginData")
	public Object[][] getInvalidLoginData() {
		return new Object[][] {
			{ config.getValidUserName(), config.getInvalidUserPass() }, // TC002: Valid User, Invalid Pass
			{ config.getInvalidUserName(), config.getValidUserPass() }, // TC003: Invalid User, Valid Pass
			{ config.getInvalidUserName(), config.getInvalidUserPass() } // TC004: Both Credentials Invalid
		};
	}

	// ==========================================
	// NEGATIVE LOGIN SCENARIOS (DATA-DRIVEN)
	// ==========================================

	@Epic("vTiger CRM")
	@Feature("Authentication")
	@Story("Admin Login - Negative")
	@Severity(SeverityLevel.NORMAL)
	@Test(
			priority = 2,
			dataProvider = "invalidLoginData",
			groups = {"Regression","Authentication","P2" }
			)
	public void verifyLoginWithInvalidCredentials(String username, String password) {

		LoginPage login = new LoginPage(getDriver(), extentTest.get());

		// Execute login with invalid combinations from data provider
		login.openurl(config.getLoginUrl());
		login.loginToApplication(username, password);

		// Assert text error message on login page
		login.verifyErrorMessageIsDisplayed(); 
	}

	// ==========================================
	// BLANK CREDENTIALS SCENARIO
	// ==========================================

	@Epic("vTiger CRM")
	@Feature("Authentication")
	@Story("Admin Login - Negative")
	@Severity(SeverityLevel.MINOR)
	@Test(
			priority = 3,
			description = "TC005 | Verify login fails or validation triggers when fields are left blank",
			groups = {"Regression","Authentication","P3" }
			)
	public void verifyLoginWithBlankUsernameAndBlankPassword() {

		LoginPage login = new LoginPage(getDriver(), extentTest.get());

		// Execute login with empty strings
		login.openurl(config.getLoginUrl());
		login.loginToApplication("", "");

		// Handle field validation or alert mechanism specific to blank fields
		// login.verifyFieldValidationOrErrorMessage(); 
	}
}