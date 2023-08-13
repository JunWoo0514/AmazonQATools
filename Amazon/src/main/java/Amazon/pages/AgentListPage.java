package Amazon.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import Amazon.base.TestBase;

public class AgentListPage extends TestBase {
	
	//Page Factory
	@FindBy(id="f_name") 
	WebElement filterName;
	
	@FindBy(id="tier") 
	WebElement filterTier;
	
	@FindBy(id="ag_type") 
	WebElement filterType;
	
	@FindBy(id="user_activation") 
	WebElement filterStatus;
	
	@FindBy(xpath="//button[normalize-space()='Submit']") 
	WebElement filterBtn;
	
	@FindBy(xpath="//table[contains(@class,'table table-bordered')]/tbody/tr/td[3]") 
	List<WebElement> agentResult;
	
	@FindBy(xpath="//table[contains(@class,'table table-bordered')]/tbody/tr/td[7]") 
	List<WebElement> agentRate;
	
	//Edit Modal Element to get all columns of AgentList
	@FindBy(css="th") 
	List<WebElement> agent;
	
	//Button Element
	@FindBy(xpath="//i[@title='Add Credit']") 
	WebElement addBtn;
	
	@FindBy(xpath="//i[@title='Deduct Credit']") 
	WebElement deductBtn;
	
	@FindBy(xpath="//i[@title='Edit Agent']") 
	WebElement editBtn;

	@FindBy(id="addCreditUpdate") 
	WebElement addCreditBtn;
	
	@FindBy(id="deductCreditUpdate") 
	WebElement deductCreditBtn;
	
	@FindBy(id="editUpdate") 
	WebElement updateBtn;
	
	//Text Box Element
	@FindBy(id="addCredit") 
	WebElement addCreditText;
	
	@FindBy(id="addCreditNote") 
	WebElement addCreditNoteText;
	
	@FindBy(id="deductCredit") 
	WebElement deductCreditText;
	
	@FindBy(xpath="(//input[@name='note'])[2]") 
	WebElement deductCreditNoteText;
	
	@FindBy(id="rate") 
	WebElement rateText;
	
	@FindBy(id="memo") 
	WebElement memoText;
	
	@FindBy(id="telegram") 
	WebElement telegramText;
	
	@FindBy(id="company_site") 
	WebElement siteText;
	
	@FindBy(id="password") 
	WebElement passwordText;
	
	//By Element
	By agentTypeBy = By.id("ag_type");
	
	By addCreditBy = By.id("addCreditUpdate");
	
	By deductCreditBy = By.id("deductCreditUpdate");
	
	By updateBy = By.id("editUpdate");
	
	By editBy = By.xpath("//i[@title='Edit Agent']");
	
	@FindBy(css="button[class='btn btn-primary btn-ladda btn-sm modal-button']") 
	WebElement ModalConfirmBtn;
	
	String ModalBtnCSS = "button[class='btn btn-primary btn-ladda btn-sm modal-button']";
	
	public AgentListPage() {
		PageFactory.initElements(driver, this);
	}
	
	public void findTestingAccount(String agCode, String tier, String type, String status) {
		waitVisibilityLocate(agentTypeBy);
		waitClickable(filterBtn);
		clearText(filterName);
		writeText(filterName, agCode);
		selectItem(filterTier,tier);
		selectItem(filterType,type);
		selectItem(filterStatus,status);
		click(filterBtn);
		waitVisibilityLocate(editBy);
	}
	
	public void UpdateCreditTest(String updateType, String creditValue, String note) throws InterruptedException {
		waitVisibilityLocate(addCreditBy);
		if(updateType.equals(prop.getProperty("AddCredit"))) {
			click(addBtn);
			waitVisibilityLocate(addCreditBy);
			clearText(addCreditText);
			clearText(addCreditNoteText);
			writeText(addCreditText, creditValue);
			writeText(addCreditNoteText, note);
			click(addCreditBtn);
			
		}else if(updateType.equals(prop.getProperty("DeductCredit"))) {
			click(deductBtn);
			waitVisibilityLocate(deductCreditBy);
			clearText(deductCreditText);
			clearText(deductCreditNoteText);
			writeText(deductCreditText, creditValue);
			writeText(deductCreditNoteText, note);
			click(deductCreditBtn);
		}
		CustomWaitClick(ModalConfirmBtn, ModalBtnCSS);
	}
	
	
	public List<List<String>> getAgentDetail() {

		waitVisibilityLocate(editBy);
		agent = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + agent.size());
		
		List<List<String>> elementDataList = new ArrayList<List<String>>();
	
		for (int i = 2; i < agent.size(); i++) {
			waitVisibilityLocate(editBy);
			int w = i + 1;

				System.out.println("W value : " + w);
				String TitletName = agent.get(i).getText();		
				System.out.println("Title Name : " + TitletName);
				if (!TitletName.equals(prop.getProperty("AddCredit")) || !TitletName.equals(prop.getProperty("DeductCredit")) || !TitletName.equals(prop.getProperty("Edit"))) {
					WebElement valueOrigin = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]"));
					String valueData = readText(valueOrigin);
					System.out.println("AG Code: " + agent.get(1).getText());
					System.out.println("List i value : " + i);
					System.out.println("List title 1 : " + TitletName);
					System.out.println("List value 1 : " + valueData);
					elementDataList.add(Arrays.asList(TitletName, valueData));
				}
		}
		return elementDataList;	
	}
	
	/*public List<List<String>> getAgentDetail2() {

		waitVisibilityLocate(editBy);
		agent = driver.findElements(By.cssSelector("th"));
		System.out.println("List size set up : " + agent.size());
		
		List<List<String>> elementDataList = new ArrayList<List<String>>();
		String[] valueDataList = new String[6];
		
			for (int i = 2; i < agent.size(); i++) {
				waitVisibilityLocate(editBy);
				int w = i + 1;

					System.out.println("W value : " + w);
					String TitletName = agent.get(i).getText();		
					System.out.println("Title Name : " + TitletName);
					if (!TitletName.equals(prop.getProperty("AddCredit")) || !TitletName.equals(prop.getProperty("DeductCredit")) || !TitletName.equals(prop.getProperty("Edit"))) {
						WebElement valueOrigin = driver.findElement(By.xpath("//div[contains(@class,'table-responsive')]/table/tbody/tr/td["+w+"]"));
						String valueData = readText(valueOrigin);
						System.out.println("AG Code: " + agent.get(1).getText());
						System.out.println("List i value : " + i);
						System.out.println("List title 1 : " + TitletName);
						System.out.println("List value 1 : " + innitialStatus);
						System.out.println("List Category : " + currentCat);
						
					}

				
			}
		elementDataList.add(Arrays.asList(ProductName, innitialStatus, currentCat));
		return elementDataList;	
	}*/
	
}
