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
	@FindBy(css="div[class='col-sm-3 col-md-3 col-lg-4 col-xl-4 prd_status'] label div") 
	List<WebElement> productsList;
	
	By submitBy = By.id("btnSubmit");
	
	@FindBy(css="button[class='btn btn-md modal-button']") 
	WebElement ModalConfirmBtn;
	
	String ModalBtnCSS = "button[class='btn btn-md modal-button']";
	
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
		String productStat = "";
		if(prdStatus == prop.getProperty("on")) {
			productStat = prop.getProperty("off");
		}else if(prdStatus == prop.getProperty("off")) {
			productStat = prop.getProperty("on");
		}
		for(int i=0; i < productsList.size() ; i ++) {
			int w = i+1;
			boolean thisFieldStatus = false;
			WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/div"));
			WebElement statusInout = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/input"));
			thisFieldStatus = elementDisplayCheck(ProductToggle);
			if(thisFieldStatus) {
				String productID = statusInout.getAttribute("id");
				if(productID.contains(prdCode)) {
					String innitialStatus = readText(ProductToggle);
					if(!innitialStatus.equals(productStat)) {
						click(ProductToggle);
					}
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
			WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/div"));
			//WebElement statusInput = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/input"));
			thisFieldStatus = elementDisplayCheck(ProductToggle);
			if(thisFieldStatus == true) {
				String innitialStatus = readText(ProductToggle);
				if(innitialStatus.equals(prop.getProperty("off"))) {
					click(ProductToggle);
				}
				//break;
				//selectToggleTrue(ProductToggle);
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
			WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/div"));
			WebElement statusInput = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/input"));
			thisFieldStatus = elementDisplayCheck(ProductToggle);
			if(thisFieldStatus) {
				String productID = statusInput.getAttribute("id");
				if(productID.contains(prdCode)) {
					productStatus = readText(ProductToggle);
					break;
				}
			}
		}	
		return productStatus;
	}
	//html/body/main/div[2]/div/div[2]/form/div[2]/div[1]/div[2]/div[1]/div/div[2]/label/div
	public List<List<String>> getALLProductList() {
		waitVisibilityLocate(submitBy);
		productsList = driver.findElements(By.cssSelector("div[class='col-sm-3 col-md-3 col-lg-4 col-xl-4 prd_status'] label div"));		
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		for (int i = 0; i < productsList.size(); i++) {
			waitVisibilityLocate(submitBy);
			boolean thisFieldStatus = false;
			int w = i + 1;
			WebElement statusOrigin = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/div"));
			WebElement statusInput = driver.findElement(By.xpath("//div[contains(@class,'card-body')]/div[1]/div[2]/div["+w+"]/div/div[2]/label/input"));
			thisFieldStatus = elementDisplayCheck(statusOrigin);
			System.out.println("List field status : " + thisFieldStatus);
			if (thisFieldStatus == true) {
				String productID = statusInput.getAttribute("id");
				String innitialStatus = readText(statusOrigin);
				elementDataList.add(Arrays.asList(productID, innitialStatus));
			}
		}
		return elementDataList;	
	}
	
}
