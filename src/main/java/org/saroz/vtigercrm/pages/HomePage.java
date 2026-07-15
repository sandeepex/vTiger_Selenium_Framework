package org.saroz.vtigercrm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.saroz.vtigercrm.base.BasePage;

import com.aventstack.extentreports.ExtentTest;

public class HomePage extends BasePage {

	    public HomePage(WebDriver driver, ExtentTest extentTest) {
		super(driver, extentTest);
		
	}

		@FindBy(xpath = "//img[@src='themes/softed/images/user.PNG']")
	    private WebElement administratorIcon;

	    @FindBy(linkText = "Sign Out")
	    private WebElement signOutLink;

	    @FindBy (xpath = "//a[@href='index.php?module=Leads&action=index']")
	    private WebElement leadsMenu;
	    
	    public void ClickOnLeadsMenu() {
			click(leadsMenu, "Leads Menu");
		}
	    
	    public void verifyUserIsLoggedIn() {
	        utils.validateElementVisibleHard(
	            administratorIcon,
	            "Administrator",
	            "User Administrator Icon"
	        );
	    }

	    public void logout() {
	        utils.mouseOver(administratorIcon, "Logout Icon");
	        click(signOutLink, "Sign Out Link");
	    }
	}

