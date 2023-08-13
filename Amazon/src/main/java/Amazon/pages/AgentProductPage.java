package Amazon.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Amazon.base.TestBase;

public class AgentProductPage extends TestBase{
	
	//Page Factory
	@FindBy(id="f_name") 
	WebElement filterName;
	
	@FindBy(id="level") 
	WebElement filterTier;
	
	@FindBy(id="category") 
	WebElement filterCategory;
	
	@FindBy(xpath="//button[normalize-space()='Submit']") 
	WebElement filterBtn;
	
	//Select (Account Status Combo Box) Element
	@FindBy(id="f_status") 
	WebElement accountSelect;
	
	//Apply Button
	@FindBy(id="btnSubmit") 
	WebElement submitBtn;
	
	@FindBy(css="button[class='btn btn-primary btn-ladda btn-sm modal-button']") 
	WebElement ModalConfirmBtn;
	
	@FindBy(css="th") 
	List<WebElement> products;
	
	By productCategoryBy = By.id("category");
	
	By submitBy = By.cssSelector("#btnSubmit");
	
	By filterBy = By.xpath("//button[normalize-space()='Submit']");
	
	String ModalBtnCSS = "button[class='btn btn-primary btn-ladda btn-sm modal-button']";
	
	
	public AgentProductPage() {
		PageFactory.initElements(driver, this);
	}
	
	public void findTestingAccount(String agCode, String tier, String category) {
		waitVisibilityLocate(productCategoryBy);
		waitClickable(filterBtn);
		clearText(filterName);
		writeText(filterName, agCode);
		//selectItem(filterTier,tier);
		selectItem(filterCategory,category);
		click(filterBtn);
		waitVisibilityLocate(submitBy);
	}
	
	public void ProductSettingSingleTest(String prdName, String prdStatus, String category) throws InterruptedException {
		waitVisibilityLocate(submitBy);
		checkCategory(category);
		this.updateSingleProductSetting(prdName, prdStatus);
		click(submitBtn);	
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
	}
	
	public void ProductSettingMultipleTest(String prdStatus, Boolean CATest) throws InterruptedException {
		waitVisibilityLocate(submitBy);
		int catcount = selectOptionIndex(filterCategory)-1;
		int liveCount = 0;
		products = driver.findElements(By.cssSelector("th"));
		do {
			for(int i=2; i < products.size() ; i ++) {
				waitVisibilityLocate(submitBy);
				boolean thisFieldStatus = false;
				int j = i;
				if(CATest) {
					j ++;
				}
					WebElement productSelect = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
					thisFieldStatus = elementDisplayCheck(productSelect);
					if(thisFieldStatus == true) {
						selectToggleTrue(productSelect);
					}
			}
			click(submitBtn);
			CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
			waitVisibilityLocate(submitBy);
			liveCount ++;
			if(liveCount <= catcount) {
				selectItemIndex(filterCategory,liveCount);
				waitVisibilityLocate(filterBy);
				click(filterBtn);
				waitVisibilityLocate(submitBy);
				products = driver.findElements(By.cssSelector("th"));
			}
		}while(liveCount <= catcount);
	}
	
	public void updateSingleProductSetting(String prdName, String prdStatus) {
		boolean productStat = false;
		if(prdStatus == prop.getProperty("active")) {
			productStat = true;
		}else if(prdStatus == prop.getProperty("disable")) {
			productStat = false;
		}
		
		for(int i=0; i < products.size() ; i ++) {
			String name = products.get(i).getText();
			if(name.contains(prdName)) {
				int j = i + 1;
				WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
				selectToggle(ProductToggle, productStat);
				break;
			}
		}
	}
	
	
	public void checkCategory(String category) {
		//Check category and refresh Table Header for latest product list
		String currentCat = selectItemFirstItem(filterCategory);
		if(!currentCat.equals(category)) {
			selectItem(filterCategory,category);
			click(filterBtn);
			waitVisibilityLocate(submitBy);
		}
		products = driver.findElements(By.cssSelector("th"));
	}
	
	public String getProductStatus(String prdName, String category) {
		waitVisibilityLocate(submitBy);
		String productStatus = "";
		String newCategory = category;
		String currentCat = selectItemFirstItem(filterCategory);
		checkCategory(category);
		
		for(int i=0; i < products.size() ; i ++) {
			waitVisibilityLocate(submitBy);
			String name = products.get(i).getText();
			if(name.contains(prdName)) {
				int j = i + 1;
				WebElement productStatuInput = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
				productStatus = readToggle(productStatuInput);
				break;
			}
		}	
		return productStatus;
	}
	
	public List<List<String>> getALLProductList(Boolean CATest) {

		waitVisibilityLocate(submitBy);
		products = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + products.size());
		String currentCat = selectItemFirstItem(filterCategory);
		System.out.println("Current Category : " + currentCat);
		int catcount = selectOptionIndex(filterCategory)-1;
		int liveCount = 0;
		System.out.println("Category Count : " + catcount);
		
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		
		do {
			for (int i = 2; i < products.size(); i++) {
				waitVisibilityLocate(submitBy);
				boolean thisFieldStatus = false;
				int t;
				if(!CATest) {
					t = i - 1;
				}else {
					t = i;
				}
				int w = t + 1;
				do {
					System.out.println("W value : " + w);
					String ProductName = products.get(t).getText();
					WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]/input"));
					thisFieldStatus = elementDisplayCheck(statusOrigin);
					System.out.println("List field status : " + thisFieldStatus);
					if (thisFieldStatus == true) {
						String innitialStatus = readToggle(statusOrigin);
						System.out.println("AG Code: " + products.get(1).getText());
						System.out.println("List i value : " + t);
						System.out.println("List Product : " + ProductName);
						System.out.println("List State : " + innitialStatus);
						currentCat = selectItemFirstItem(filterCategory);
						System.out.println("List Category : " + currentCat);
						elementDataList.add(Arrays.asList(ProductName, innitialStatus, currentCat));
					}
				}while(thisFieldStatus == false);
				
			}
			liveCount ++;
			if(liveCount <= catcount) {
				selectItemIndex(filterCategory,liveCount);
				click(filterBtn);
				waitVisibilityLocate(submitBy);
				products = driver.findElements(By.cssSelector("th"));
			}
			
		}while(liveCount <= catcount);
		return elementDataList;	
	}

}
