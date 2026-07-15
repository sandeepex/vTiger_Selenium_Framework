package org.saroz.vtigercrm.tests;
import org.testng.annotations.Test;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.saroz.vtigercrm.pages.HomePage;
import org.saroz.vtigercrm.pages.LeadsPage;


@Epic("vTiger CRM")
@Feature("Leads Management")
public class LeadsTest extends Authenticated{

	@Story("Create New Lead")
	@Severity(SeverityLevel.CRITICAL)
	@Test(
			priority = 1,
			description = "TC001 | Verify admin user can successfully create a new lead",
			groups = {"Smoke", "Regression", "Leads", "P1"}
			)
	public void verifyLeadsCreate() {
		HomePage home = new HomePage(getDriver(), extentTest.get());
		LeadsPage leads = new LeadsPage(getDriver(), extentTest.get());

		// Navigate to Leads and fill the creation form
		home.ClickOnLeadsMenu();
		leads.clickOnCreateLeadsButon();
		leads.fillFullLeadForm();
		leads.clickOnSubmitButton();

		// Verifies the lead inside the list view grid
		leads.verifyLeadsCreated();
	}

	@Story("Edit Existing Lead")
	@Severity(SeverityLevel.NORMAL)
	@Test(
			priority = 2,
			dependsOnMethods = {"verifyLeadsCreate"},
			description = "TC002 | Verify admin user can successfully edit an existing lead",
			groups = {"Regression", "Leads", "P2"}
			)
	public void verifyLeadsEdit() {
		LeadsPage leads = new LeadsPage(getDriver(), extentTest.get(),1);
		HomePage home =new HomePage(getDriver(), extentTest.get());

		home.ClickOnLeadsMenu();
		// Note: Ensure your 'clickOnEditButton' handling matches the UI context.
		// If you are on the List View Grid, you might need to click on the Lead link first.
		leads.clickOnEditButton();
		leads.updateLeadInformation();
		leads.clickOnSubmitButton();
	}

	@Story("Delete Lead")
	@Severity(SeverityLevel.CRITICAL)
	@Test(
			priority = 3,
			dependsOnMethods = {"verifyLeadsEdit"},
			description = "TC003 | Verify admin user can successfully delete a lead",
			groups = {"Regression", "Leads", "P1"}
			)
	public void verifyLeadsAllDelete() {
		LeadsPage leads = new LeadsPage(getDriver(), extentTest.get());
		HomePage home =new HomePage(getDriver(), extentTest.get());

		home.ClickOnLeadsMenu();
		// Trigger deletion and handle the browser confirmation alert
		leads.selectAllLeads();
		leads.clickOnDeleteButton();
		leads.acceptDeleteAlert();
	}
}