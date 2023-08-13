package Amazon.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Amazon.base.TestBase;

public class AgentMinMaxPage extends TestBase{

	//Page Factory
	@FindBy(id="f_name") 
	WebElement filterName;
	
	@FindBy(id="level") 
	WebElement filterTier;
	
	@FindBy(id="category") 
	WebElement filterCategory;
	
	@FindBy(xpath="//button[normalize-space()='Submit']") 
	WebElement filterBtn;
	
	@FindBy(xpath="//i[@title='Edit Max Bet']") 
	WebElement editBtn;
	
	@FindBy(id="minMaxUpdate") 
	WebElement UpdateBtn;
	
	@FindBy(id="btnCancel") 
	WebElement CancelBtn;
	
	//Table Header Element to get all products
	@FindBy(css="th") 
	List<WebElement> products;
	
	//Edit Modal Element to get all products
	@FindBy(xpath="//div[contains(@class,'col-4')]/label") 
	List<WebElement> productsEdit;
	
	By productCategoryBy = By.id("category");
	
	By editBy = By.xpath("//i[@title='Edit Max Bet']");
	
	By updateBy = By.id("minMaxUpdate");
	
	@FindBy(css="button[class='btn btn-primary btn-ladda btn-sm modal-button']") 
	WebElement ModalConfirmBtn;
	
	String ModalBtnCSS = "button[class='btn btn-primary btn-ladda btn-sm modal-button']";
	
	public AgentMinMaxPage() {
		PageFactory.initElements(driver, this);
	}
	
	public void findTestingAccount(String agCode, String tier, String category) {
		waitVisibilityLocate(productCategoryBy);
		waitClickable(filterBtn);
		clearText(filterName);
		writeText(filterName, agCode);
		selectItem(filterTier,tier);
		selectItem(filterCategory,category);
		click(filterBtn);
		waitVisibilityLocate(editBy);
	}
	
	public void LimitSettingSingleTest(String prdName, String minAmount, String maxAmount, String category) throws InterruptedException {
		waitVisibilityLocate(editBy);
		checkCategory(category);
		click(editBtn);	
		waitVisibilityLocate(updateBy);
		this.updateSingleLimitSetting(prdName, minAmount, maxAmount);
		click(UpdateBtn);	
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		waitVisibilityLocate(editBy);
	}
	
	public void checkCategory(String category) {
		//Check category and refresh Table Header for latest product list
		String currentCat = selectItemFirstItem(filterCategory);
		if(!currentCat.equals(category)) {
			selectItem(filterCategory,category);
			click(filterBtn);
			waitVisibilityLocate(editBy);
		}
		products = driver.findElements(By.cssSelector("th"));
	}
	
	public void updateSingleLimitSetting(String prdName, String minAmt, String maxAmt) {
		productsEdit = driver.findElements(By.xpath("//div[contains(@class,'col-4')]/label"));
		System.out.println("Im in 01 : " + products.size());
		for(int i=0; i < productsEdit.size() ; i ++) {
			String name = productsEdit.get(i).getText();
			System.out.println("Im in 02 : " + products.size());
			if(name.contains(prdName)) {
				int j = i + 1;
				WebElement minAmount = driver.findElement(By.xpath("//div[contains(@id,'prd_update_list')]/div["+j+"]/div[2]/input"));
				WebElement maxAmount = driver.findElement(By.xpath("//div[contains(@id,'prd_update_list')]/div["+j+"]/div[3]/input"));
				clearText(minAmount);
				clearText(maxAmount);
				writeText(minAmount, minAmt);
				writeText(maxAmount, maxAmt);
				break;
			}
		}
	}
	
	public void LimitSettingMultipleTest(String newMin, String newMax) throws InterruptedException {
		int catcount = selectOptionIndex(filterCategory)-1;
		int liveCount = 0;
		waitVisibilityLocate(editBy);
		click(editBtn);	
		waitVisibilityLocate(updateBy);
		productsEdit = driver.findElements(By.xpath("//div[contains(@class,'col-4')]/label"));
		do {
			for(int i=0; i < productsEdit.size() ; i ++) {
				waitVisibilityLocate(updateBy);
				int j = i + 1;
					WebElement minAmount = driver.findElement(By.xpath("//div[contains(@id,'prd_update_list')]/div["+j+"]/div[2]/input"));
					WebElement maxAmount = driver.findElement(By.xpath("//div[contains(@id,'prd_update_list')]/div["+j+"]/div[3]/input"));
					clearText(minAmount);
					clearText(maxAmount);
					writeText(minAmount, newMin);
					writeText(maxAmount, newMax);
			}
			click(UpdateBtn);
			CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
			waitVisibilityLocate(editBy);
			liveCount ++;
			if(liveCount <= catcount) {
				selectItemIndex(filterCategory,liveCount);
				click(filterBtn);
				waitVisibilityLocate(editBy);
				click(editBtn);	
				waitVisibilityLocate(updateBy);
				productsEdit = driver.findElements(By.xpath("//div[contains(@class,'col-4')]/label"));
			}
		}while(liveCount <= catcount);
	}
	
	public String[] getLimitStatus(String prdName, String category) {
		waitVisibilityLocate(editBy);
		String productStatus = "";
		String[] limitData = new String[2];
		checkCategory(category);
		for(int i=0; i < products.size() ; i ++) {
			waitVisibilityLocate(editBy);
			String name = products.get(i).getText();
			if(name.contains(prdName)) {
				int j = i + 1;
				WebElement MinMaxEle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]"));
				String innitialLimit = readText(MinMaxEle);
				int minEnd = innitialLimit.indexOf('N') + 1;
				int maxEnd = innitialLimit.indexOf('X') + 1;
				int midEnd = innitialLimit.lastIndexOf('M')-1;
				limitData[0] = innitialLimit.substring(minEnd,midEnd);
				limitData[1] = innitialLimit.substring(maxEnd);
				break;
			}
		}	
		return limitData;
	}
	
	public List<List<String>> getALLProductList() {

		waitVisibilityLocate(editBy);
		products = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + products.size());
		String currentCat = selectItemFirstItem(filterCategory);
		System.out.println("Current Category : " + currentCat);
		int catcount = selectOptionIndex(filterCategory)-1;
		int liveCount = 0;
		System.out.println("Category Count : " + catcount);
		
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		
		do {
			for (int i = 3; i < products.size(); i++) {
				waitVisibilityLocate(editBy);
				boolean thisFieldStatus = false;
				int w = i + 1;
				do {
					System.out.println("W value : " + w);
					String ProductName = products.get(i).getText();
					WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]"));
					thisFieldStatus = elementDisplayCheck(statusOrigin);
					System.out.println("List field status : " + thisFieldStatus);
					if (thisFieldStatus == true) {
						WebElement MinMaxEle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]"));
						String innitialLimit = readText(MinMaxEle);
						int minEnd = innitialLimit.indexOf('N') + 1;
						int maxEnd = innitialLimit.indexOf('X') + 1;
						int midEnd = innitialLimit.lastIndexOf('M')-1;
						System.out.println("AG Code: " + products.get(1).getText());
						System.out.println("List i value : " + i);
						System.out.println("List Product : " + ProductName);
						System.out.println("List Min : " + innitialLimit.substring(minEnd,midEnd));
						System.out.println("List Max : " + innitialLimit.substring(maxEnd));
						currentCat = selectItemFirstItem(filterCategory);
						System.out.println("List Category : " + currentCat);
						elementDataList.add(Arrays.asList(ProductName, currentCat, innitialLimit.substring(minEnd,midEnd), innitialLimit.substring(maxEnd)));
					}
				}while(thisFieldStatus == false);
				
			}
			liveCount ++;
			if(liveCount <= catcount) {
				selectItemIndex(filterCategory,liveCount);
				click(filterBtn);
				waitVisibilityLocate(editBy);
				products = driver.findElements(By.cssSelector("th"));
			}
			
		}while(liveCount <= catcount);
		return elementDataList;	
	}
	
}
