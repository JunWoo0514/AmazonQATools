package Amazon.pages;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Amazon.base.TestBase;

//import AASAutomation.pages.HomePage;

public class LoginPage extends TestBase{
	
	//Page Element
	@FindBy(id="username") 
	WebElement username;
	
	@FindBy(id="password") 
	WebElement password;
	
	//Error Message
	@FindBy(css="span[role='alert'] strong") 
	WebElement errorMessage;
	
	@FindBy(id="login-btn") 
	WebElement loginButton;
	
	//Language Selector
	@FindBy(css="select[name='locale']") 
	WebElement languageSelect;
	
	//Initializing the Page Objects:
	public LoginPage(){
		PageFactory.initElements(driver, this);
	}
	
	//Actions:
	public String validateLoginPageTitle(){
		return driver.getTitle();
	}
	
	public HomePage login(String userName, String passWord) {
		
		this.selectLangauge("English");
		clearText(username);
		clearText(password);
		writeText(username, userName);
		writeText(password, passWord);
		click(loginButton);
		
		return new HomePage();
	}
	
	public HomePage loginAction() {
		this.selectLangauge("English");
		clearText(username);
		clearText(password);
		String userName = System.getProperty("username")!=null ? System.getProperty("username") : prop.getProperty("username");
		String passWord = System.getProperty("password")!=null ? System.getProperty("password") : prop.getProperty("password");
		writeText(username, userName);
		writeText(password, passWord);
		click(loginButton);
		return new HomePage();
	}
	
	
	public void selectLangauge(String language) {	
		selectItem(languageSelect,language);
	}
	
	public void clickLogin() {
		loginButton.click();
	}
	
	public String getErrorMessage() {
		return readText(errorMessage);
	}

}
