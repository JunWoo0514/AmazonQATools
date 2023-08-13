package Amazon.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Amazon.base.TestBase;


public class SlotGameListPage extends TestBase{
	
	//Page Factory
	@FindBy(id="submit-btn") 
	WebElement SubmitBtn;
	
	@FindBy(id="edit-btn") 
	WebElement EditBtn;
	
	@FindBy(id="save-btn") 
	WebElement SaveBtn;
	
	@FindBy(id="cancel-btn") 
	WebElement CancelBtn;
	
	@FindBy(id="provider") 
	WebElement providerSelect;
	
	@FindBy(id="pageValues") 
	WebElement paginationSelect;
	
	
	//Edit Element to get all products MT List
	@FindBy(css="th") 
	List<WebElement> titleList;
	
	By submitBy = By.id("submit-btn");
	
	By editBy = By.id("edit-btn");
	
	By saveBy = By.id("save-btn");
	
	By cancelBy = By.id("cancel-btn");
	
	By paginationBy = By.id("pageValues");
	
	@FindBy(css="button[class='btn btn-primary btn-ladda btn-sm modal-button']") 
	WebElement ModalConfirmBtn;
	
	String ModalBtnCSS = "button[class='btn btn-primary btn-ladda btn-sm modal-button']";
	
	//Initializing the Page Objects:
	public SlotGameListPage() {
		PageFactory.initElements(driver, this);
	}
	
	//Actions:
	
	public void findTestingProduct(String productName) {
		waitVisibilityLocate(submitBy);
		//selectItem(filterTier,tier);
		selectItem(providerSelect,productName);
		click(SubmitBtn);
		waitVisibilityLocate(paginationBy);
	}
	
	public void GameTypeStatusTestSingle(String prdCode, String prdStatus) throws InterruptedException {
		waitVisibilityLocate(paginationBy);
		click(EditBtn);	
		this.updateSingleProduct(prdCode, prdStatus);
		click(SaveBtn);	
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		waitVisibilityLocate(paginationBy);
	}
	
	public void updateSingleProduct(String title, String typeValue) {
		boolean typeStat = false;
		if(typeValue == prop.getProperty("yes")) {
			typeStat = true;
		}else if(typeValue == prop.getProperty("no")) {
			typeStat = false;
		}
		for(int i=3; i < titleList.size() ; i ++) {
			int w = i+1;
			String name = titleList.get(i).getText();
			if(name.contains(title)) {	
				WebElement ProductToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr[1]/td["+w+"]/input"));
				selectToggle(ProductToggle, typeStat);
				break;
				}
		}
	}
	
	public void updateALLType() throws InterruptedException {
		//boolean typeStat = false;
		waitVisibilityLocate(paginationBy);
		click(EditBtn);	
		for(int i=3; i < titleList.size() ; i ++) {
			int w = i+1;
			String name = titleList.get(i).getText();
			WebElement TypeToggle = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr[1]/td["+w+"]/input"));
			if(name.contains("Slot") || name.contains("Status")) {	
				selectToggleTrue(TypeToggle);
			}else {
				selectToggleFalse(TypeToggle);
			}
		}
		click(SaveBtn);	
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
		waitVisibilityLocate(paginationBy);
	}
	
	public String[][] getGameTypeStatus() {
		waitVisibilityLocate(paginationBy);
		String[][] typeData = new String[titleList.size()-3][2];
		int j = 0;
		for(int i=3; i < titleList.size() ; i ++) {
			waitVisibilityLocate(paginationBy);
			boolean thisFieldStatus = false;
			int w = i + 1;
			WebElement gameType = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr[1]/td["+w+"]"));
			thisFieldStatus = elementDisplayCheck(gameType);
			if(thisFieldStatus) {
				String TitleName =  titleList.get(i).getText();
				String typeValue = readText(gameType);
				System.out.println("Title Name : " + TitleName);
				System.out.println("Title value : " + typeValue);
				typeData[j][0] = TitleName;
				typeData[j][1] = typeValue;
				j++;
				}
			}
		return typeData;
	}	
	
	public String getGameTypeStatusSingle(String typeTitle) {
		waitVisibilityLocate(paginationBy);
		String typeData = "";
		for(int i=3; i < titleList.size() ; i ++) {
			waitVisibilityLocate(paginationBy);
			boolean thisFieldStatus = false;
			int w = i + 1;
			String name = titleList.get(i).getText();
			if(name.contains(typeTitle)) {	
				WebElement gameType = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr[1]/td["+w+"]/span"));
				thisFieldStatus = elementDisplayCheck(gameType);
				if(thisFieldStatus) {
					typeData = readText(gameType);
					System.out.println("Single Type Value : " + typeData);
				}
			}
			
			}
		return typeData;
	}	
		
	public List<List<String>> getALLProductList() {
		waitVisibilityLocate(submitBy);
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		List<WebElement> allOptions = selectItemAllItem(providerSelect);
		for (int i = 0; i < allOptions.size(); i++) {
			String product = allOptions.get(i).getText();
			if (product != null) {
				elementDataList.add(Arrays.asList(product));
			}
		}
		return elementDataList;	
	}
	
}
