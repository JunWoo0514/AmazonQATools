package Amazon.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Amazon.base.TestBase;


public class HomePage extends TestBase{
	
	//Page Factory
	//Menu List
	//Manu Category Element List
	@FindBy(css="a[class='nav-link nav-dropdown-toggle']") 
	List<WebElement> SideMenuTitle;
	
	//Sub Menu Item Element List
	@FindBy(css="li[data-name='1'] ul li a") 
	List<WebElement> SideMenuItem1;
	
	@FindBy(css="li[data-name='2'] ul li a") 
	List<WebElement> SideMenuItem2;
	
	@FindBy(xpath="//b[@id='header_tier']") 
	WebElement tierLevel;
	
	//Edit Element to get all products MT List
	@FindBy(css="table[id='maintenance-table'] td") 
	List<WebElement> productsMT;
	
	//Initializing the Page Objects:
	public HomePage() {
		PageFactory.initElements(driver, this);
	}
	
	//Actions:
	public String validateHomePageTitle(){
		return driver.getTitle();
	}
	
	public String getTierLevel(){
		String level= readText(tierLevel);
		int start = level.indexOf('(') + 1;
		int end = level.indexOf(')');
		return level.substring(start,end);
	}
	
	public void ModuleFinder(String Menu, String Item) {
		WebElement menuObject = driver.findElement(By.xpath("//a[normalize-space()='"+Menu+"']"));
		WebElement itemObject = driver.findElement(By.xpath("//a[normalize-space()='"+Item+"']"));
		click(menuObject);
		click(itemObject);
	}
	
	public AgentProductPage clickOnAgentProduct(String menu, String item) {
		ModuleFinder(menu, item);
		return new AgentProductPage();
	}
	
	public AgentMinMaxPage clickOnAgentMinMax(String menu, String item) {
		ModuleFinder(menu, item);
		return new AgentMinMaxPage();
	}
	
	public MinMaxBetLimitPage clickOnMinMaxBetLimit(String menu, String item) {
		ModuleFinder(menu, item);
		return new MinMaxBetLimitPage();
	}
	
	public GlobalProductPage clickOnGlobalProduct(String item, String category) {
		ModuleFinder(item, category);
		return new GlobalProductPage();
	}
	
	public SlotGameListPage clickOnGameList(String menu, String item) {
		ModuleFinder(menu, item);
		return new SlotGameListPage();
	}


}
