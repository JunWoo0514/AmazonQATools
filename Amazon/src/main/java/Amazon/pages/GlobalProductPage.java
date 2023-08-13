package Amazon.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Amazon.base.TestBase;


public class GlobalProductPage extends TestBase{
	
	//Page Factory
	@FindBy(id="btnSubmit") 
	WebElement UpdateBtn;
	
	//Edit Element to get all products MT List
	@FindBy(css="div[class='col-sm-6 col-md-4 col-lg-4 col-xl-4'] input") 
	List<WebElement> productsList;
	
	By submitBy = By.id("btnSubmit");
	
	@FindBy(css="button[class='btn btn-primary btn-ladda btn-sm modal-button']") 
	WebElement ModalConfirmBtn;
	
	String ModalBtnCSS = "button[class='btn btn-primary btn-ladda btn-sm modal-button']";
	
	//Initializing the Page Objects:
	public GlobalProductPage() {
		PageFactory.initElements(driver, this);
	}
	
	//Actions:
	public void GlobalSettingSingleTest(String prdCode, String prdStatus) throws InterruptedException {
		waitVisibilityLocate(submitBy);
		this.updateSingleProductSetting(prdCode, prdStatus);
		click(UpdateBtn);	
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		waitVisibilityLocate(submitBy);
	}
	
	public void updateSingleProductSetting(String prdCode, String prdStatus) {
		boolean productStat = false;
		if(prdStatus == prop.getProperty("active")) {
			productStat = true;
		}else if(prdStatus == prop.getProperty("disable")) {
			productStat = false;
		}
		for(int i=0; i < productsList.size() ; i ++) {
			int w = i+1;
			boolean thisFieldStatus = false;
			WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/input"));
			thisFieldStatus = elementDisplayCheck(ProductToggle);
			if(thisFieldStatus) {
				String productID = ProductToggle.getAttribute("id");
				if(productID.contains(prdCode)) {
					selectToggle(ProductToggle, productStat);
					break;
				}
			}else {
				break;
			}
			
		}
	}
	
	public void ProductSettingMultipleTest(String prdStatus) throws InterruptedException {
		waitVisibilityLocate(submitBy);
		for(int i=0; i < productsList.size() ; i ++) {
			int w = i+1;
			waitVisibilityLocate(submitBy);
			boolean thisFieldStatus = false;
			WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/input"));
			thisFieldStatus = elementDisplayCheck(ProductToggle);
			if(thisFieldStatus == true) {
				selectToggleTrue(ProductToggle);
			}
		}
		click(UpdateBtn);
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		waitVisibilityLocate(submitBy);
			
	}
	
	public String getProductStatus(String prdCode) {
		waitVisibilityLocate(submitBy);
		String productStatus = "";
		for(int i=0; i < productsList.size() ; i ++) {
			waitVisibilityLocate(submitBy);
			int w = i+1;
			boolean thisFieldStatus = false;
			WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/input"));
			thisFieldStatus = elementDisplayCheck(ProductToggle);
			if(thisFieldStatus) {
				String productID = ProductToggle.getAttribute("id");
				
				if(productID.contains(prdCode)) {
					productStatus = readToggle(ProductToggle);
					break;
				}
			}
		}	
		return productStatus;
	}
	
	public List<List<String>> getALLProductList() {
		waitVisibilityLocate(submitBy);
		productsList = driver.findElements(By.cssSelector("div[class='col-sm-6 col-md-4 col-lg-4 col-xl-4'] input"));		
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		for (int i = 0; i < productsList.size(); i++) {
			waitVisibilityLocate(submitBy);
			boolean thisFieldStatus = false;
			int w = i + 1;
			WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/input"));
			thisFieldStatus = elementDisplayCheck(statusOrigin);
			System.out.println("List field status : " + thisFieldStatus);
			if (thisFieldStatus == true) {
				String productID = statusOrigin.getAttribute("id");
				String innitialStatus = readToggle(statusOrigin);
				elementDataList.add(Arrays.asList(productID, innitialStatus));
			}
		}
		return elementDataList;	
	}
	
}
