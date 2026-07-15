package org.saroz.vtigercrm.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.saroz.vtigercrm.base.BasePage;
import com.aventstack.extentreports.ExtentTest;

/**
 * Page Object Model (POM) Class representing the Leads Page in vTiger CRM.
 * Implements a dynamic, parameterless architecture using a class-level record index 
 * to fetch clean, production-grade test data from an underlying JSON repository.
 */
public class LeadsPage extends BasePage {

	// Dynamic record pointer managing data selection (Defaults to 0 for the first record)
	private int recordIndex = 0; 

	// Shared constant pointing to the source data repository file name
	private final String jsonFile = "LeadsData.json";

	// =========================================================================
	// WEB ELEMENT LOCATORS (PageFactory Structure)
	// =========================================================================

	// Global Action Elements
	@FindBy(xpath = "//img[@title='Create Lead...']") private WebElement clickOnCreateLeadsButton;
	@FindBy(xpath = "//input[contains(@class,'crmbutton small save')]") private WebElement submitButtn;
	@FindBy(xpath = "//a[normalize-space()='edit']") private WebElement editButton;
	@FindBy(xpath = "//body[1]/table[4]/tbody[1]/tr[1]/td[2]/div[4]/form[1]/table[1]/tbody[1]/tr[1]/td[1]/table[2]/tbody[1]/tr[1]/td[1]/input[1]") private WebElement deleteButton;
	@FindBy(id = "selectCurrentPageRec") private WebElement clickChekBox;
	@FindBy(xpath = "//table[@class='lvt small']/tbody/tr[contains(@class,'lvtColData')]/td[3]/a") 
	private List<WebElement> allLeadsLastNames;
	@FindBy(xpath = "//span[@class='dvHeaderText']") private WebElement leadsInfo;

	// Core Identification Inputs
	@FindBy(name = "salutationtype") private WebElement salutationtype;
	@FindBy(name = "firstname") private WebElement firstNameInput;
	@FindBy(name = "lastname") private WebElement lastNameInput;
	@FindBy(name = "company") private WebElement companyInput;
	@FindBy(name = "designation") private WebElement titleInput;

	// Dropdowns and Segment Selectors
	@FindBy(name = "leadsource") private WebElement leadSourceDropdown;
	@FindBy(name = "industry") private WebElement industryDropdown;
	@FindBy(name = "leadstatus") private WebElement leadStatusDropdown;
	@FindBy(name = "rating") private WebElement ratingDropdown;

	// Corporate and Metrics Fields
	@FindBy(name = "annualrevenue") private WebElement annualRevenueInput;
	@FindBy(name = "noofemployees") private WebElement numberOfEmployeesInput;

	// Communication Channels
	@FindBy(name = "phone") private WebElement phoneInput;
	@FindBy(name = "mobile") private WebElement mobileInput;
	@FindBy(name = "fax") private WebElement faxInput;
	@FindBy(name = "email") private WebElement emailInput;
	@FindBy(name = "secondaryemail") private WebElement secondaryEmailInput;
	@FindBy(name = "website") private WebElement websiteInput;

	// Account Governance Selectors
	@FindBy(name = "assigned_user_id") private WebElement assignedToDropdown;

	// Address Block Fields
	@FindBy(name = "lane") private WebElement streetInput;
	@FindBy(name = "pobox") private WebElement poBoxInput;
	@FindBy(name = "code") private WebElement postalCodeInput;
	@FindBy(name = "city") private WebElement cityInput;
	@FindBy(name = "state") private WebElement stateInput;
	@FindBy(name = "country") private WebElement countryInput;

	// =========================================================================
	// INITIALIZATION CONSTRUCTORS
	// =========================================================================

	/**
	 * Default Constructor using the primary record dataset index (0).
	 * @param driver The active WebDriver session instance.
	 * @param extentTest The target thread's ExtentReports logging node.
	 */
	public LeadsPage(WebDriver driver, ExtentTest extentTest) {
		super(driver, extentTest);
	}

	/**
	 * Overloaded Constructor enabling dynamic runtime record pointer switching for parallel data testing.
	 * @param driver The active WebDriver session instance.
	 * @param extentTest The target thread's ExtentReports logging node.
	 * @param recordIndex The 0-based data iteration tracking index matching the external JSON map layout.
	 */
	public LeadsPage(WebDriver driver, ExtentTest extentTest, int recordIndex) {
		super(driver, extentTest);
		this.recordIndex = recordIndex;
	}

	// =========================================================================
	// ACTION METHOD WORKFLOWS
	// =========================================================================

	/**
	 * Navigates to the Create Lead creation interface screen.
	 */
	public void clickOnCreateLeadsButon() {
		click(clickOnCreateLeadsButton, "Create Leads Button");
	}

	/**
	 * Selects the target structural lead prefix / salutation identifier.
	 */
	public void selectsalutation() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "salutationtype");
		System.out.println(data);
		selectByVisibleText(salutationtype, data, "Leads salutationtype");
	}

	/**
	 * Fills the lead's First Name text field.
	 */
	public void enterFirstName() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "firstName");
		type(firstNameInput, data, "First Name");
	}

	/**
	 * Fills the lead's Last Name text field.
	 */
	public void enterLastName() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "lastName");
		type(lastNameInput, data, "Last Name");
	}

	/**
	 * Fills the corporate Company name designation text field.
	 */
	public void enterCompany() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "company");
		type(companyInput, data, "Company");
	}

	/**
	 * Fills the corporate Title / Position text field.
	 */
	public void enterTitle() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "title");
		type(titleInput, data, "Title");
	}

	/**
	 * Selects an entry option from the Lead Source tracking selector.
	 */
	public void selectLeadSource() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "leadSource");
		selectByVisibleText(leadSourceDropdown, data, "Lead Source Dropdown");
	}

	/**
	 * Selects an entry option from the target Industry type selector.
	 */
	public void selectIndustry() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "industry");
		selectByVisibleText(industryDropdown, data, "Industry Dropdown");
	}

	/**
	 * Fills out the financial Annual Revenue metric text field.
	 */
	public void enterAnnualRevenue() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "annualRevenue");
		type(annualRevenueInput, data, "Annual Revenue");
	}

	/**
	 * Fills out the total Number of Employees workspace field metrics.
	 */
	public void enterNumberOfEmployees() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "numberOfEmployees");
		type(numberOfEmployeesInput, data, "Number of Employees");
	}

	/**
	 * Fills out the Secondary backup Email verification contact element.
	 */
	public void enterSecondaryEmail() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "secondaryEmail");
		type(secondaryEmailInput, data, "Secondary Email");
	}

	/**
	 * Fills the general primary contact Phone input field.
	 */
	public void enterPhone() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "phone");
		type(phoneInput, data, "Phone");
	}

	/**
	 * Fills the primary Mobile contact input node.
	 */
	public void enterMobile() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "mobile");
		type(mobileInput, data, "Mobile");
	}

	/**
	 * Fills the system corporate Fax tracking line.
	 */
	public void enterFax() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "fax");
		type(faxInput, data, "Fax");
	}

	/**
	 * Fills the primary corporate routing Email target block.
	 */
	public void enterEmail() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "email");
		type(emailInput, data, "Email");
	}

	/**
	 * Fills the absolute corporate digital Website address endpoint.
	 */
	public void enterWebsite() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "website");
		type(websiteInput, data, "Website");
	}

	/**
	 * Selects an operational state within the Lead Status pipeline tracking block.
	 */
	public void selectLeadStatus() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "leadStatus");
		selectByVisibleText(leadStatusDropdown, data, "Lead Status Dropdown");
	}

	/**
	 * Selects a prioritizing status mapping from the structural Lead Rating tracker.
	 */
	public void selectRating() {
		String data = jsonReader.getArrayValue(jsonFile, recordIndex, "rating");
		selectByVisibleText(ratingDropdown, data, "Rating Dropdown");
	}

	// =========================================================================
	// NESTED JSON REPOSITORIES HANDLING (Relational Object Parsing Blocks)
	// =========================================================================

	/**
	 * Extracts and selects assignment visibility permissions from the assigned user tree maps.
	 */
	public void selectAssignedToName() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "assignedTo", "name");
		selectByVisibleText(assignedToDropdown, data, "Assigned To User");
	}

	/**
	 * Fills the primary physical Street Address field parameters.
	 */
	public void enterStreet() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "address", "street");
		type(streetInput, data, "Street Address");
	}

	/**
	 * Fills out the standard PO Box mailing route indicator.
	 */
	public void enterPoBox() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "address", "poBox");
		type(poBoxInput, data, "PO Box");
	}

	/**
	 * Fills out the structural validation Postal Code identifier field.
	 */
	public void enterPostalCode() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "address", "postalCode");
		type(postalCodeInput, data, "Postal Code");
	}

	/**
	 * Fills out the localization City parameter field.
	 */
	public void enterCity() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "address", "city");
		type(cityInput, data, "City");
	}

	/**
	 * Fills out the local State or Province organizational boundary context input.
	 */
	public void enterState() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "address", "state");
		type(stateInput, data, "State");
	}

	/**
	 * Fills the overarching political Country destination parameter boundary.
	 */
	public void enterCountry() {
		String data = jsonReader.getArrayNestedValue(jsonFile, recordIndex, "address", "country");
		type(countryInput, data, "Country");
	}

	/**
	 * Executes execution submission by triggering the main entity Save control pipeline.
	 */
	public void clickOnSubmitButton() {
		click(submitButtn, "Leads Submit Button");
	}

	// =========================================================================
	// DYNAMIC DATA MANIPULATION & PIPELINE LIFECYCLE MANAGEMENT
	// =========================================================================

	/**
	 * Triggers the operational edit layer view context on the active Lead record dashboard.
	 */
	public void clickOnEditButton() {
		click(editButton, "Edit Lead Button");
	}

	/**
	 * Programmatically updates current entity record configurations cleanly without arguments, 
	 * retrieving data nodes instantly using decoupled static key reference indexing points.
	 */
	public void updateLeadInformation() {
		String companyData = jsonReader.getArrayValue(jsonFile, recordIndex, "updatedCompany");
		String phoneData = jsonReader.getArrayValue(jsonFile, recordIndex, "updatedPhone");

		type(companyInput, companyData, "Updated Company Name");
		type(phoneInput, phoneData, "Updated Phone Number");
	}

	/**
	 * Triggers a hard deletion call command request directly against the current Lead configuration.
	 */
	public void clickOnDeleteButton() {
		click(deleteButton, "Delete Lead Button");
	}

	/**
	 * Checks the master selection checkbox in the list view header.
	 */
	public void selectAllLeads() {
		click(clickChekBox, "Select All Leads Checkbox");
	}

	/**
	 * Safely targets the system native alert framework modal stack to accept deletion context queries.
	 */
	public void acceptDeleteAlert() {
		utils.acceptAlert();
	}

	/**
	 * Validates if the newly created lead exists anywhere across the visible records grid table.
	 */
	public void verifyLeadsinList() {
		// Decoupled the inline assignment to fix the compilation crash
		String expectedLastName = jsonReader.getArrayValue(jsonFile, recordIndex, "lastName");

		// Correct invocation targeting the utility base framework node
		utils.validateAllElementsContainKeyword(allLeadsLastNames, expectedLastName, "Verify created lead is present in the table grid");
	}

	public void verifyLeadsCreated() {
		// Decoupled the inline assignment to fix the compilation crash
		String expectedLastName = jsonReader.getArrayValue(jsonFile, recordIndex, "lastName");

		// Correct invocation targeting the utility base framework node
		
		utils.validateTextContainsHard(utils.getText(leadsInfo, "Get Leads Info"), expectedLastName, "Verify Leads Created Succesfully");
	}

	// =========================================================================
	// AUTOMATION SEQUENCE FLOW MANAGER
	// =========================================================================

	/**
	 * Completely maps out and runs sequential script automation populating all input arrays across the creation page layout.
	 */
	public void fillFullLeadForm() {
		selectsalutation(); 
		enterFirstName();
		enterLastName();
		enterCompany();
		enterTitle();
		selectLeadSource();
		selectIndustry();
		enterAnnualRevenue();
		enterNumberOfEmployees();
		enterSecondaryEmail();
		enterPhone();
		enterMobile();
		enterFax();
		enterEmail();
		enterWebsite();
		selectLeadStatus();
		selectRating();
		selectAssignedToName();
		enterStreet();
		enterPoBox();
		enterPostalCode();
		enterCity();
		enterState();
		enterCountry();
	}
}