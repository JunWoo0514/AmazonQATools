package Amazon.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Amazon.pages.*;
import Amazon.util.*;
import Amazon.base.TestBase;

public class AgentProductTest extends TestBase{
	
	LoginPage loginPage;
	HomePage homePage;
	AgentProductPage agentProductPage;
	AmazonUtil AmazonUtil;
	String level, menu, item, active, disable, WalletType, TestAccount, newStatus, paraCode, resellCode, operateCode, tier, initialCategory, currentCategory;
	boolean CAtest;
	List<List<String>> ProductDataList = new ArrayList<List<String>>();
	
	public AgentProductTest() {
		super();
	}
	
	@DataProvider(name = "RealTimeProductData")
	public Object[][] getProductData() {
		Object[][] data = new Object[ProductDataList.size()][3];
		// Iterate through the 2D list using nested loops
		for (int i = 0; i < ProductDataList.size(); i++) {
			System.out.println("Product Data Size : " + ProductDataList.size());
		    List<String> innerList = ProductDataList.get(i);
		    for (int j = 0; j < innerList.size(); j++) {
		    	String value = innerList.get(j);
		    	data[i][j] = value;
		    	System.out.println("Product Value : " + data[i][j]);
		    }
		}
        return data;
	}
	
	public void checkLevelSkip() {
		if (!CAtest) {
		    throw new SkipException("Skipping tests because resource was not available.");
		  }
	}
	
	@BeforeClass
	public void setUp(){
		initialization();
		menu = prop.getProperty("AgentManagement");
		item = prop.getProperty("AgentProduct");
		active = prop.getProperty("active");
		disable = prop.getProperty("disable");
		WalletType = System.getProperty("walletType")!=null ? System.getProperty("walletType") : prop.getProperty("walletType");
		/*if(WalletType.equals(prop.getProperty("Seamless"))) {
			TestAccount = prop.getProperty("SeamlessAccount");
		}else if(WalletType.equals(prop.getProperty("Transfer"))) {
			TestAccount = prop.getProperty("WalletAccount");
		}*/
		paraCode = prop.getProperty("parallelAcc");
		resellCode = prop.getProperty("resellerAcc");
		operateCode = prop.getProperty("operatorAcc");
		tier = prop.getProperty("parallelTier");
		initialCategory = prop.getProperty("casino");
		AmazonUtil = new AmazonUtil();
		agentProductPage = new AgentProductPage();
		loginPage = new LoginPage();
		homePage = loginPage.loginAction();
		level = homePage.getTierLevel();
		System.out.println("Level : "+ level);
		if(level.equals(prop.getProperty("levelAgent"))) {
			CAtest = false;
		}else {
			CAtest = true;
		}
		System.out.println("CA Boolean : "+ CAtest);
		agentProductPage = homePage.clickOnAgentProduct(menu,item);
		//agentProductPage.findTestingAccount(paraCode, tier, initialCategory);
	}
	
	@Test(priority=1)
    public void SectionSetUpParallel(ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Get Parallel Tier Active Product Data");
		tier = prop.getProperty("parallelTier");
		currentCategory = initialCategory;
		agentProductPage.findTestingAccount(paraCode, tier, currentCategory);
		ProductDataList = null;
		ProductDataList = agentProductPage.getALLProductList(CAtest);
    }
	
	@Test(priority=2, dataProvider = "RealTimeProductData")
	public void ProductSettingSingleUpdate_Parallel(String prdName, String innitialStatus, String category, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		String newStatus = "";
		if(innitialStatus.equals("ON")) {
			newStatus = "OFF";
		}else if(innitialStatus.equals("OFF")) {
			newStatus = "ON";
		}
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Parallel Tier Test with Product : " + prdName + " with Status Value : " + newStatus);
		agentProductPage.findTestingAccount(paraCode, tier, category);
		agentProductPage.ProductSettingSingleTest(prdName,newStatus, category);
		String result = agentProductPage.getProductStatus(prdName, category);
		context.setAttribute("Result", "Result expected value : " + newStatus + " and received value : " + result);
		System.out.println("Get : "+ result);
		System.out.println("Give : "+ newStatus);
		Assert.assertEquals(result,newStatus);
	}
	
	@Test(priority=3)
	public void ProductSettingGroupUpdate_Parallel(ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Parallel Tier Test All Product with status Value : Positive");
		agentProductPage.findTestingAccount(paraCode, tier, initialCategory);
		agentProductPage.ProductSettingMultipleTest(prop.getProperty("active"), CAtest);
	}
	
	@Test(priority=4, enabled=false, dataProvider = "RealTimeProductData")
	public void ProductSettingGroupCheck_Parallel(String prdName, String innitialStatus,  String category, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Parallel Tier Check on Product : " + prdName + " with Status Value : " + active);		
		agentProductPage.findTestingAccount(paraCode, tier, category);
		String result = agentProductPage.getProductStatus(prdName, category);
		context.setAttribute("Result", "Result expected value : " + active + " and received value : " + result);
		Assert.assertEquals(result, active);
	}
	
	@Test(priority=5)
	public void ProceedToReseller(ITestContext context) throws InterruptedException{
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Search Reseller Tier");
		tier = prop.getProperty("resellerTier");
		currentCategory = initialCategory;
		agentProductPage.findTestingAccount(resellCode, tier, currentCategory);
		ProductDataList = null;
		ProductDataList = agentProductPage.getALLProductList(CAtest);
	}
	
	@Test(priority=6, dataProvider = "RealTimeProductData")
	public void ProductSettingSingleUpdate_Reseller(String prdName, String innitialStatus, String category, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		String newStatus = "";
		if(innitialStatus.equals("ON")) {
			newStatus = "OFF";
		}else if(innitialStatus.equals("OFF")) {
			newStatus = "ON";
		}
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Reseller Tier Test with Product : " + prdName + " with Status Value : " + newStatus);
		agentProductPage.findTestingAccount(resellCode, tier, category);
		agentProductPage.ProductSettingSingleTest(prdName, newStatus, category);
		String result = agentProductPage.getProductStatus(prdName, category);
		context.setAttribute("Result", "Result expected value : " + newStatus + " and received value : " + result);
		Assert.assertEquals(result,newStatus);
	}
	
	@Test(priority=7)
	public void ProductSettingGroupUpdate_Reseller(ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Reseller Tier Test All Product with status Value : Positive");
		agentProductPage.findTestingAccount(resellCode, tier, initialCategory);
		agentProductPage.ProductSettingMultipleTest(prop.getProperty("active"), CAtest);
	}
	
	@Test(priority=8, enabled=false, dataProvider = "RealTimeProductData")
	public void ProductSettingGroupCheck_Reseller(String prdName, String innitialStatus,  String category, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Reseller Tier Check on Product : " + prdName + " with Status Value : " + active);		
		agentProductPage.findTestingAccount(resellCode, tier, category);
		String result = agentProductPage.getProductStatus(prdName, category);
		context.setAttribute("Result", "Result expected value : " + active + " and received value : " + result);
		Assert.assertEquals(result, active);
	}
	
	@Test(priority=9)
	public void ProceedToOperator(ITestContext context) throws InterruptedException{
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Search Operator Tier");
		tier = prop.getProperty("operatorTier");
		currentCategory = initialCategory;
		agentProductPage.findTestingAccount(operateCode, tier, currentCategory);
		ProductDataList = null;
		ProductDataList = agentProductPage.getALLProductList(CAtest);
	}
	
	@Test(priority=10, dataProvider = "RealTimeProductData")
	public void ProductSettingSingleUpdate_Operator(String prdName, String innitialStatus, String category, ITestContext context) throws InterruptedException {
		String newStatus = "";
		if(innitialStatus.equals("ON")) {
			newStatus = "OFF";
		}else if(innitialStatus.equals("OFF")) {
			newStatus = "ON";
		}
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Operator Tier Test with Product : " + prdName + " with Status Value : " + newStatus);
		agentProductPage.findTestingAccount(operateCode, tier, category);
		agentProductPage.ProductSettingSingleTest(prdName, newStatus, category);
		String result = agentProductPage.getProductStatus(prdName, category);
		context.setAttribute("Result", "Result expected value : " + newStatus + " and received value : " + result);
		Assert.assertEquals(result,newStatus);
	}
	
	@Test(priority=11)
	public void ProductSettingGroupUpdate_Operator(ITestContext context) throws InterruptedException {
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Operator Tier Test All Product with status Value : Positive");
		agentProductPage.findTestingAccount(operateCode, tier, initialCategory);
		agentProductPage.ProductSettingMultipleTest(prop.getProperty("active"), CAtest);
	}
	
	@Test(priority=12, enabled=false, dataProvider = "RealTimeProductData")
	public void ProductSettingGroupCheck_Operator(String prdName, String innitialStatus,  String category, ITestContext context) throws InterruptedException {
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Operator Tier Check on Product : " + prdName + " with Status Value : " + active);		
		agentProductPage.findTestingAccount(operateCode, tier, category);
		String result = agentProductPage.getProductStatus(prdName, category);
		context.setAttribute("Result", "Result expected value : " + active + " and received value : " + result);
		Assert.assertEquals(result, active);
	}

}
