package Amazon.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Amazon.base.TestBase;

public class MinMaxBetLimitPage extends TestBase{

	//Page Factory
	
	@FindBy(id="btnSubmit") 
	WebElement UpdateBtn;
	
	@FindBy(id="btnReset") 
	WebElement ResetBtn;
	
	//Edit Element to get all products
	@FindBy(xpath="//div[@class='//div[@class='col-lg-3 col-md-3 col-sm-3 col-3 my-auto product-container']/h6") 
	List<WebElement> products;
	
	By updateBy = By.id("btnSubmit");
	
	By ModalUpdateBy = By.cssSelector("button[class='rightBtn btn']");

	By confirmBy = By.cssSelector("button[class='btn btn-md modal-button']");
	
	@FindBy(css="button[class='btn btn-md modal-button']") 
	WebElement ModalConfirmBtn;
	
	@FindBy(css="button[class='rightBtn btn']") 
	WebElement ModalUpdateBtn;
	
	String ModalBtnCSS = "button[class='btn btn-md modal-button']";
	
	String ModalUptBtnCSS = "button[class='rightBtn btn']";
	
	public MinMaxBetLimitPage() {
		PageFactory.initElements(driver, this);
	}
	

	public void LimitSettingSingleTest(String prdName, String minAmount, String maxAmount) throws InterruptedException {
		waitVisibilityLocate(updateBy);
		this.updateSingleLimitSetting(prdName, minAmount, maxAmount);
		click(UpdateBtn);
		waitVisibilityLocate(ModalUpdateBy);
		CustomWaitClick2(ModalUpdateBtn, ModalUptBtnCSS);
		//click(ModalUpdateBtn);
		//Thread.sleep(1000);
		//waitInVisibilityLocate(ModalUpdateBy);
		waitVisibilityLocate(confirmBy);
		//click(ModalConfirmBtn);
		// Create an instance of Actions class
        //Actions actions = new Actions(driver);
		//Thread.sleep(10000);
        // Simulate pressing the Enter key
        //actions.sendKeys(Keys.ENTER).perform();
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		//waitInVisibilityLocate(confirmBy);
		waitVisibilityLocate(updateBy);
	}
	
	public void updateSingleLimitSetting(String prdName, String minAmt, String maxAmt) {
		products = driver.findElements(By.xpath("//div[@class='col-lg-3 col-md-3 col-sm-3 col-3 my-auto product-container']/h6"));
		System.out.println("Im in 01 : " + products.size());
		for(int i=0; i < products.size() ; i ++) {
			String name = products.get(i).getText();
			System.out.println("Im in 02 : " + products.size());
			if(name.contains(prdName)) {
				int j = i;
				WebElement minAmount = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+j+"]/div[2]/input"));
				WebElement maxAmount = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+j+"]/div[3]/input"));
				clearText(minAmount);
				clearText(maxAmount);
				writeText(minAmount, minAmt);
				writeText(maxAmount, maxAmt);
				break;
			}
		}
	}
	
	public void LimitSettingMultipleTest(String newMin, String newMax) throws InterruptedException {

		waitVisibilityLocate(updateBy);

		products = driver.findElements(By.xpath("//div[@class='col-lg-3 col-md-3 col-sm-3 col-3 my-auto product-container']/h6"));
		for(int i=1; i < products.size() ; i ++) {
			waitVisibilityLocate(updateBy);
			int j = i;
			WebElement minAmount = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+j+"]/div[2]/input"));
			WebElement maxAmount = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+j+"]/div[3]/input"));
			clearText(minAmount);
			clearText(maxAmount);
			writeText(minAmount, newMin);
			writeText(maxAmount, newMax);
		}
		click(UpdateBtn);
		waitVisibilityLocate(ModalUpdateBy);
		CustomWaitClick2(ModalUpdateBtn, ModalUptBtnCSS);
		waitVisibilityLocate(confirmBy);
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		waitVisibilityLocate(updateBy);

	}
	
	public String[] getLimitStatus(String prdName) {
		waitVisibilityLocate(updateBy);
		products = driver.findElements(By.xpath("//div[@class='col-lg-3 col-md-3 col-sm-3 col-3 my-auto product-container']/h6"));
		String[] limitData = new String[2];
		for(int i=0; i < products.size() ; i ++) {
			waitVisibilityLocate(updateBy);
			String name = products.get(i).getText();
			if(name.contains(prdName)) {
				int j = i;
				WebElement min = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+j+"]/div[2]/input"));
				WebElement max = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+j+"]/div[3]/input"));
				String minLimit = readTextBox(min);
				String maxLimit = readTextBox(max);
				limitData[0] = minLimit;
				limitData[1] = maxLimit;
				break;
			}
		}	
		return limitData;
	}
	
	public List<List<String>> getALLProductList() {

		waitVisibilityLocate(updateBy);
		products = driver.findElements(By.xpath("//div[@class='col-lg-3 col-md-3 col-sm-3 col-3 my-auto product-container']/h6"));
		System.out.println("List size set up : " + products.size());	
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		
			for (int i = 1; i < products.size(); i++) {
				waitVisibilityLocate(updateBy);
				boolean thisFieldStatus = false;
				int w = i + 1;
				do {
					System.out.println("W value : " + w);
					String ProductName = products.get(i).getText();
					WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+i+"]"));
					thisFieldStatus = elementDisplayCheck(statusOrigin);
					System.out.println("List field status : " + thisFieldStatus);
					if (thisFieldStatus == true) {
						WebElement min = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+i+"]/div[2]/input"));
						WebElement max = driver.findElement(By.xpath("//div[contains(@id,'product-list')]/div["+i+"]/div[3]/input"));
						String minLimit = readTextBox(min);
						String maxLimit = readTextBox(max);
						System.out.println("AG Code: " + products.get(1).getText());
						System.out.println("List i value : " + i);
						System.out.println("List Product : " + ProductName);
						System.out.println("List Min : " + minLimit);
						System.out.println("List Max : " + maxLimit);
						elementDataList.add(Arrays.asList(ProductName, minLimit, maxLimit));
					}
				}while(thisFieldStatus == false);
				
			}
			
		return elementDataList;	
	}
	
}
