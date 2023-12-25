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
	
	@FindBy(id="//button[@id='left-btn']") 
	WebElement tableLeftButton;
	
	@FindBy(xpath="//button[@id='right-btn']") 
	WebElement tableRightButton;
	
	@FindBy(xpath="//button[normalize-space()='Submit']") 
	WebElement filterBtn;
	
	//Select (Account Status Combo Box) Element
	@FindBy(id="f_status") 
	WebElement accountSelect;
	
	//Apply Button
	@FindBy(id="btnSubmit") 
	WebElement submitBtn;
	
	//@FindBy(css="button[class='btn btn-primary btn-ladda btn-sm modal-button']") 
	@FindBy(css="button[class='btn btn-md modal-button']") 
	WebElement ModalConfirmBtn;
	
	@FindBy(css="th") 
	List<WebElement> products;
	
	By productCategoryBy = By.id("category");
	
	By submitBy = By.cssSelector("#btnSubmit");
	
	By filterBy = By.xpath("//button[normalize-space()='Submit']");

	String sideButtonXpath = "//button[@id='right-btn']";
	
	String sideButtonXpathL = "//button[@id='left-btn']";
	
	//String ModalBtnCSS = "button[class='btn btn-primary btn-ladda btn-sm modal-button']";
	String ModalBtnCSS = "button[class='btn btn-md modal-button']";
	
	
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
		waitVisibilityLocate(submitBy);
		click(filterBtn);
		waitVisibilityLocate(submitBy);
	}
	
	public void ProductSettingSingleTest(String prdName, String prdStatus, String category) throws InterruptedException {
		waitVisibilityLocate(submitBy);
		checkCategory(category);
		waitVisibilityLocate(submitBy);
		products = driver.findElements(By.cssSelector("th"));
		this.updateSingleProductSetting(prdName, prdStatus);
		waitVisibilityLocate(submitBy);
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
				String thisFieldStatus = "";
				int j = i;
				if(CATest) {
					j ++;
				}
					String name = checkProductDisplay(i);
					System.out.println("Group Product: " + name);
					//WebElement productSelect = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
					WebElement productSelect = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/label/div"));
					thisFieldStatus = readText(productSelect);
					if(thisFieldStatus.equals("OFF")) {
						click(productSelect);
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
	
	/*public void updateSingleProductSetting2(String prdName, String prdStatus) throws InterruptedException {
		boolean productStat = false;
		if(prdStatus == prop.getProperty("active")) {
			productStat = true;
		}else if(prdStatus == prop.getProperty("disable")) {
			productStat = false;
		}
		products = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + products.size());
		System.out.println("List size set up : " + products.get(14).getText());
		for(int i=0; i < products.size() ; i ++) {
			String name = products.get(i).getText();
			System.out.println("Product Name : " + name);
			System.out.println("Product Given Name : " + prdName);
			if(name.contains(prdName)) {
				int j = i + 1;
				boolean thisFieldStatus = false;
				do {
					WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
					thisFieldStatus = elementDisplayCheck(ProductToggle);
					System.out.println("List field status : " + thisFieldStatus);
					if(thisFieldStatus == true) {
						boolean sideButtonStatus = false;
						sideButtonStatus = checkElementExist(sideButtonXpath);
						if(sideButtonStatus == true) {
							OnclickRelease(tableRightButton);
						}
						selectToggle(ProductToggle, productStat);
					}else if(thisFieldStatus == false) {
						//click(tableRightButton);
						OnclickHold(tableRightButton);
					}
				}while(thisFieldStatus == false);
				break;
			}
		}
	}*/
	
	public void updateSingleProductSetting(String prdName, String prdStatus) throws InterruptedException {
		boolean productStat = false;
		if(prdStatus == prop.getProperty("active")) {
			productStat = true;
		}else if(prdStatus == prop.getProperty("disable")) {
			productStat = false;
		}
		products = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + products.size());
		//System.out.println("List size set up : " + products.get(15).getText());		
		for(int i=0; i < products.size() ; i ++) {
			//String name = products.get(i).getText();
			String name = checkProductDisplay(i);
			System.out.println("Product Name : " + name);
			System.out.println("Product Given Name : " + prdName);	
			/*boolean productShow = true;
			if(i>1) {
				do {
					boolean thisFieldStatus = false;
					name = products.get(i).getText();
					int j = i + 1;
					WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
					thisFieldStatus = elementDisplayCheck(ProductToggle);
					if (name.length()==0 || thisFieldStatus == false) {
						productShow = false;
						OnclickHold(tableRightButton);
						OnclickRelease(tableRightButton);
					} else if(name.length()>=1 && thisFieldStatus == true) {
						productShow = true;
					}
				}while(productShow == false);
				
			}*/
			
			if(name.contains(prdName)) {
				int j = i + 1;
				buttonRelease();
				/*boolean sideButtonStatus = false;
				sideButtonStatus = checkElementExist(sideButtonXpath);
				if(sideButtonStatus == true) {
					OnclickRelease(tableRightButton);
				}*/
				//WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
				WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/label/div"));
				//selectToggle(ProductToggle, productStat);
				click(ProductToggle);
				break;
			}  
		}
	}
	
	public void checkCategory(String category) throws InterruptedException {
		//Check category and refresh Table Header for latest product list
		String currentCat = selectItemFirstItem(filterCategory);
		
		if(!currentCat.equals(category)) {
			selectItem(filterCategory,category);
			click(filterBtn);
			buttonRelease();
		}
		
	}
	
	public String checkProductDisplay(int i) throws InterruptedException {
		boolean productShow = true;
		String name = products.get(i).getText();
		String productStatus = "";
		if(i>1) {
			do {
				boolean thisFieldStatus = false;
				name = products.get(i).getText();
				int j = i + 1;
				//WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
				WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/label/div"));
				thisFieldStatus = elementDisplayCheck(ProductToggle);
				if (thisFieldStatus == true) {
					productStatus = readText(ProductToggle);
				}	
				if (name.length()==0 || thisFieldStatus == false || productStatus.length()==0) {
					productShow = false;
					OnclickHold(tableRightButton);
					OnclickRelease(tableRightButton);
				} else if(name.length()>=1 && thisFieldStatus == true && productStatus.length()>=1) {
					productShow = true;
				}
				
			}while(productShow == false);
		}
		return name;
	}
	
	public String getProductStatus(String prdName, String category) throws InterruptedException {
		waitVisibilityLocate(submitBy);
		String productStatus = "";
		String newCategory = category;
		String currentCat = selectItemFirstItem(filterCategory);
		checkCategory(category);
		products = driver.findElements(By.cssSelector("th"));
		for(int i=0; i < products.size() ; i ++) {
			waitVisibilityLocate(submitBy);
			//String name = products.get(i).getText();
			String name = checkProductDisplay(i);
			System.out.println("Get Name : " + name);
				
			if(name.contains(prdName)) {
				int j = i + 1;
				//WebElement productStatuInput = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/input"));
				WebElement productStatuInput = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+j+"]/label/div"));
				//productStatus = readToggle(productStatuInput);
				waitVisibility(productStatuInput);
				productStatus = readText(productStatuInput);
				break;
			}
		}	
		return productStatus;
	}
	
	public List<List<String>> getALLProductList(Boolean CATest) throws InterruptedException {

		waitVisibilityLocate(submitBy);
		products = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + products.size());
		System.out.println("List size set up : " + products.get(16).getText());
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
					String productStatus = "";
					//WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]/input"));
					WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]/label/div"));
					thisFieldStatus = elementDisplayCheck(statusOrigin);
					//String a = readText(statusOrigin);
					if (thisFieldStatus == true) {
						productStatus = readText(statusOrigin);
					}	
					System.out.println("List field status : " + thisFieldStatus);
					if (thisFieldStatus == true && productStatus.length()>=1) {
						buttonRelease();
						//String innitialStatus = readToggle(statusOrigin);
						String innitialStatus = readText(statusOrigin);
						System.out.println("AG Code: " + products.get(1).getText());
						System.out.println("List i value : " + t);
						System.out.println("List Product : " + ProductName);
						System.out.println("List State : " + innitialStatus);
						currentCat = selectItemFirstItem(filterCategory);
						System.out.println("List Category : " + currentCat);
						elementDataList.add(Arrays.asList(ProductName, innitialStatus, currentCat));
					}else {
						OnclickHold(tableRightButton);
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
	
	public void buttonRelease() throws InterruptedException {
		boolean sideButtonStatus = false;
		sideButtonStatus = checkElementExist(sideButtonXpath);
		if(sideButtonStatus == true) {
			OnclickRelease(tableRightButton);
		}
	}

}
