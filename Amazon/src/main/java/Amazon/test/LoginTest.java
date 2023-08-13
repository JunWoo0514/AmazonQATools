package Amazon.test;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Amazon.base.TestBase;
import Amazon.pages.HomePage;
import Amazon.pages.LoginPage;

public class LoginTest extends TestBase {
	
	LoginPage loginPage;
	HomePage homePage;
	
	public LoginTest(){
		super();
	}
	
	@BeforeClass
	public void setUp() {
		initialization();
		loginPage = new LoginPage();	
	}
	
	@Test(priority=1)
	public void LoginPageTitleTest(ITestContext context){
		context.setAttribute("Steps", "1");
		String title = loginPage.validateLoginPageTitle();
		context.setAttribute("Process", "Test with Correct Website");
		Assert.assertEquals(title, prop.getProperty("siteTitle"));
	}
	
	@Test(priority=2)
	public void loginTestNegative01(ITestContext context) throws InterruptedException {
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Test with Admin ID : " + prop.getProperty("username") + " and Password : " + prop.getProperty("wrongPassword"));
		loginPage.login(prop.getProperty("username"), prop.getProperty("wrongPassword"));
		Assert.assertEquals(loginPage.getErrorMessage(), prop.getProperty("Credentials_not_match"));
	}
	
	@Test(priority=3)
	public void loginTestNegative02(ITestContext context) throws InterruptedException {
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Test with Admin ID : " + prop.getProperty("wrongUserName") + " and Password : " + prop.getProperty("wrongPassword"));
		loginPage.login(prop.getProperty("wrongUserName"), prop.getProperty("wrongPassword"));
		Assert.assertEquals(loginPage.getErrorMessage(), prop.getProperty("Credentials_not_exists"));
	}
	
	@Test(priority=4)
	public void LoginTest(ITestContext context){
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Test with Admin ID : " + prop.getProperty("username") + " and Password : " + prop.getProperty("password"));
		homePage = loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
	}

}
