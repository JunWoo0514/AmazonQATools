package Amazon.test;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import Amazon.base.TestBase;
import Amazon.pages.AgentMinMaxPage;
import Amazon.pages.AgentProductPage;
import Amazon.pages.HomePage;
import Amazon.pages.LoginPage;
import Amazon.pages.MinMaxBetLimitPage;
import Amazon.util.AmazonUtil;

public class AgentMinMaxTest extends TestBase{
	
	LoginPage loginPage;
	HomePage homePage;
	AgentMinMaxPage agentMinMaxPage;
	MinMaxBetLimitPage minMaxBetLimitPage;
	AmazonUtil AmazonUtil;
	String level, menu, item, active, disable, WalletType, TestAccount, newMin, innitialMin, innitialMax, newMax, paraCode, resellCode, operateCode, tier, initialCategory, currentCategory;
	boolean CAtest;
	int newMins, newMaxs, adjustAmt01, adjustAmt02;
	List<List<String>> ProductDataList = new ArrayList<List<String>>();
	List<List<String>> DownlineProductDataList = new ArrayList<List<String>>();
	
	public AgentMinMaxTest() {
		super();
	}
	
	@DataProvider(name = "RealTimeProductData")
	public Object[][] getProductData() {
		Object[][] data = new Object[ProductDataList.size()][4];
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
	
	@DataProvider(name = "DownLineProductData")
	public Object[][] getProductDataDownline() {
		Object[][] data = new Object[DownlineProductDataList.size()][3];
		// Iterate through the 2D list using nested loops
		for (int i = 0; i < DownlineProductDataList.size(); i++) {
			System.out.println("Product Data Size : " + DownlineProductDataList.size());
		    List<String> innerList = DownlineProductDataList.get(i);
		    for (int j = 0; j < innerList.size(); j++) {
		    	String value = innerList.get(j);
		    	data[i][j] = value;
		    	System.out.println("Product Value : " + data[i][j]);
		    }
		}
        return data;
	}
	
	public void AgentLevelSkip() {
		if (!CAtest) {
		    throw new SkipException("Skipping tests because resource was not available.");
		  }
	}
	
	public void CALevelSkip() {
		if (CAtest) {
		    throw new SkipException("Skipping tests because resource was not available.");
		  }
	}
	
	@BeforeClass
	public void setUp(){
		initialization();
		
		active = prop.getProperty("active");
		disable = prop.getProperty("disable");
		WalletType = System.getProperty("walletType")!=null ? System.getProperty("walletType") : prop.getProperty("walletType");
		paraCode = prop.getProperty("parallelAcc");
		resellCode = prop.getProperty("resellerAcc");
		operateCode = prop.getProperty("operatorAcc");
		tier = prop.getProperty("parallelTier");
		initialCategory = prop.getProperty("casino");
		innitialMin = prop.getProperty("minLimit");
		innitialMax = prop.getProperty("maxLimit");
		adjustAmt01 = Integer.parseInt(prop.getProperty("adjustAmt01"));
		adjustAmt02 = Integer.parseInt(prop.getProperty("adjustAmt02"));
		AmazonUtil = new AmazonUtil();
		loginPage = new LoginPage();
		homePage = loginPage.loginAction();
		level = homePage.getTierLevel();
		System.out.println("Level : "+ level);
		if(level.equals(prop.getProperty("levelAgent"))) {
			CAtest = false;
			menu = prop.getProperty("MyInfo");
			item = prop.getProperty("MinMaxBetLimit");
			minMaxBetLimitPage = new MinMaxBetLimitPage();
			minMaxBetLimitPage = homePage.clickOnMinMaxBetLimit(menu,item);
		}else {
			CAtest = true;
			menu = prop.getProperty("AgentManagement");
			item = prop.getProperty("AgentMinMax");
			agentMinMaxPage = new AgentMinMaxPage();
			agentMinMaxPage = homePage.clickOnAgentMinMax(menu,item);
			agentMinMaxPage.findTestingAccount(paraCode, tier, initialCategory);
		}
	}
	
	@Test(priority=1)
    public void SectionSetUpCAView(ITestContext context) {
		AgentLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Get Downline Tier Active Product Data");
		ProductDataList = null;
		ProductDataList = agentMinMaxPage.getALLProductList();
		currentCategory = initialCategory;
		agentMinMaxPage.findTestingAccount(paraCode, tier, currentCategory);
    }
	
	@Test(priority=2, dataProvider = "RealTimeProductData")
	public void LimitSettingSingleUpdate_CAView(String prdName, String category, String innMin, String innMax, ITestContext context) throws InterruptedException {
		AgentLevelSkip();
		newMins = Integer.parseInt(innMin.replace(",", "")) + adjustAmt01;
		newMaxs = Integer.parseInt(innMax.replace(",", "")) + adjustAmt02;
		newMin = Integer.toString(newMins);
		newMax = Integer.toString(newMaxs);
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Downline Tier Test with Product : " + prdName + " with Min Value : " + newMins + " and Max Value : "+ newMaxs);
		agentMinMaxPage.LimitSettingSingleTest(prdName, newMin, newMax, category);
		String[] limitData  = agentMinMaxPage.getLimitStatus(prdName, category);
		String finalMin = limitData[0].replace(",", "");
		String finalMax = limitData[1].replace(",", "");
		context.setAttribute("Result", "Result expected Min value : " + newMin + " Max value : "+ newMax + 
				" and received Min value : " + finalMin + " Max value : "+ finalMax);
		Assert.assertEquals(finalMin,newMin);
		Assert.assertEquals(finalMax,newMax);
	}
	
	@Test(priority=3)
	public void LimitSettingGroupUpdate_CAView(ITestContext context) throws InterruptedException {
		AgentLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Downline Tier Test All Product with Min Value : " + newMins + " and Max Value : "+ newMaxs);
		agentMinMaxPage.findTestingAccount(paraCode, tier, initialCategory);
		agentMinMaxPage.LimitSettingMultipleTest(innitialMin, innitialMax);
	}
	
	@Test(priority=4, dataProvider = "RealTimeProductData")
	public void LimitSettingGroupCheck_CAView(String prdName, String category, String innMin, String innMax, ITestContext context) throws InterruptedException {
		AgentLevelSkip();
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Downline Tier Check on Product : " + prdName + " with Status Value : " + active);		
		String[] limitData  = agentMinMaxPage.getLimitStatus(prdName, category);
		String finalMin = limitData[0].replace(",", "");
		String finalMax = limitData[1].replace(",", "");
		context.setAttribute("Result", "Result expected Min value : " + innitialMin + " Max value : "+ innitialMax + 
				" and received Min value : " + finalMin + " Max value : "+ finalMax);
		Assert.assertEquals(finalMin,innitialMin);
		Assert.assertEquals(finalMax,innitialMax);
	}
	
	@Test(priority=5)
    public void SectionSetUpDownlineView(ITestContext context) {
		CALevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Get Own Tier Active Product Data");
		DownlineProductDataList = null;
		DownlineProductDataList = minMaxBetLimitPage.getALLProductList();
    }
	
	@Test(priority=6, dataProvider = "DownLineProductData")
	public void LimitSettingSingleUpdate_DownlineView(String prdName, String innMin, String innMax, ITestContext context) throws InterruptedException {
		CALevelSkip();
		newMins = Integer.parseInt(innMin.replace(",", "")) + adjustAmt01;
		newMaxs = Integer.parseInt(innMax.replace(",", "")) + adjustAmt02;
		newMin = Integer.toString(newMins);
		newMax = Integer.toString(newMaxs);
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Downline Tier Test with Product : " + prdName + " with Min Value : " + newMins + " and Max Value : "+ newMaxs);
		minMaxBetLimitPage.LimitSettingSingleTest(prdName, newMin, newMax);
		String[] limitData  = minMaxBetLimitPage.getLimitStatus(prdName);
		String finalMin = limitData[0].replace(",", "");
		String finalMax = limitData[1].replace(",", "");
		context.setAttribute("Result", "Result expected Min value : " + newMin + " Max value : "+ newMax + 
				" and received Min value : " + finalMin + " Max value : "+ finalMax);
		Assert.assertEquals(finalMin,newMin);
		Assert.assertEquals(finalMax,newMax);
	}
	
	@Test(priority=7)
	public void LimitSettingGroupUpdate_DownlineView(ITestContext context) throws InterruptedException {
		CALevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Downline Tier Test All Product with Min Value : " + newMins + " and Max Value : "+ newMaxs);
		minMaxBetLimitPage.LimitSettingMultipleTest(innitialMin, innitialMax);
	}
	
	@Test(priority=8, dataProvider = "DownLineProductData")
	public void LimitSettingGroupCheck_DownlineView(String prdName, String innMin, String innMax, ITestContext context) throws InterruptedException {
		CALevelSkip();
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Downline Tier Check on Product : " + prdName + " with Status Value : " + active);		
		String[] limitData  = minMaxBetLimitPage.getLimitStatus(prdName);
		String finalMin = limitData[0].replace(",", "");
		String finalMax = limitData[1].replace(",", "");
		context.setAttribute("Result", "Result expected Min value : " + innitialMin + " Max value : "+ innitialMax + 
				" and received Min value : " + finalMin + " Max value : "+ finalMax);
		Assert.assertEquals(finalMin,innitialMin);
		Assert.assertEquals(finalMax,innitialMax);
	}

}
