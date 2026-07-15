package org.saroz.vtigercrm.tests;
import org.saroz.vtigercrm.base.BaseTest;
import org.saroz.vtigercrm.pages.HomePage;
import org.saroz.vtigercrm.pages.LoginPage;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Intermediate Base Class for all functional modules (Leads, Organizations, Contacts, etc.)
 * This executes a single global login session before running any functional module tests.
 */
public class Authenticated extends BaseTest {

	@BeforeClass(alwaysRun = true)
	public void loginPreCondition() {
		// Initialize the login page object using the inherited driver and extent nodes
		LoginPage login = new LoginPage(getDriver(), extentTest.get());
		
		// 1. Navigate to the global URL using config properties initialized by BaseTest
		getDriver().get(config.getLoginUrl()); 
		
		// 2. Perform the valid corporate authentication routine once for the class lifecycle
		login.loginToApplication(config.getValidUserName(), config.getValidUserPass());
		
		log.info("Session Authentication Status: Success. Active environment ready for functional modules execution pipeline.");
	}
	
	
	// =========================================================================
		// GLOBAL MODULE LOGOUT (Runs once after all tests in the class finish)
		// =========================================================================
		@AfterClass(alwaysRun = true)
		public void logoutPostCondition() {
			try {
				log.info("Executing global module logout post-condition...");
				HomePage home = new HomePage(getDriver(), extentTest.get());
				
				// Assuming your HomePage has a logout method (similar to your LoginTest)
				home.logout(); 
				
				log.info("Session Status: Logged out successfully.");
				extentTest.get().log(com.aventstack.extentreports.Status.INFO, "Global module session logged out.");
			} catch (Exception e) {
				log.error("Global logout failed: " + e.getMessage());
			}
		}
}