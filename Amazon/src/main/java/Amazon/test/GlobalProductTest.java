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

public class GlobalProductTest extends TestBase{
	
	LoginPage loginPage;
	HomePage homePage;
	GlobalProductPage globalProductPage;
	AmazonUtil AmazonUtil;
	String level, menu, item, on, off, WalletType;
	boolean CAtest;
	List<List<String>> ProductDataList = new ArrayList<List<String>>();
	
	public GlobalProductTest() {
		super();
	}
	
	@DataProvider(name = "RealTimeProductData")
	public Object[][] getProductData() {
		Object[][] data = new Object[ProductDataList.size()][2];
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
		menu = prop.getProperty("Setting");
		item = prop.getProperty("GlobalProduct");
		on = prop.getProperty("on");
		off = prop.getProperty("off");
		WalletType = System.getProperty("walletType")!=null ? System.getProperty("walletType") : prop.getProperty("walletType");
		AmazonUtil = new AmazonUtil();
		globalProductPage = new GlobalProductPage();
		loginPage = new LoginPage();
		homePage = loginPage.loginAction();
		level = homePage.getTierLevel();
		System.out.println("Level : "+ level);
		if(level.equals(prop.getProperty("levelAgent"))) {
			CAtest = false;
		}else {
			CAtest = true;
		}
		globalProductPage = homePage.clickOnGlobalProduct(menu,item);
	}
	
	@Test(priority=1)
    public void SectionSetUpGlobalProductList(ITestContext context) {
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Get All Product Data");
		ProductDataList = null;
		ProductDataList = globalProductPage.getALLProductList();
    }
	
	@Test(priority=2, dataProvider = "RealTimeProductData")
	public void GlobalProductSettingSingleUpdate(String prdCode, String innitialStatus, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		String newStatus = "";
		if(innitialStatus.equals(on)) {
			newStatus = off;
		}else if(innitialStatus.equals(off)) {
			newStatus = on;
		}
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Parallel Tier Test with Product : " + prdCode + " with Status Value : " + newStatus);
		globalProductPage.GlobalSettingSingleTest(prdCode,newStatus);
		String result = globalProductPage.getProductStatus(prdCode);
		context.setAttribute("Result", "Result expected value : " + newStatus + " and received value : " + result);
		Assert.assertEquals(result,newStatus);
	}
	
	@Test(priority=3)
	public void GlobalProductSettingGroupUpdate(ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Global Product Activation Test All Product with status Value : Positive");
		globalProductPage.ProductSettingMultipleTest(prop.getProperty("active"));
	}
	
	@Test(priority=4, dataProvider = "RealTimeProductData")
	public void GlobalProductSettingGroupCheck(String prdCode, String innitialStatus, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		context.setAttribute("Steps", "2");
		context.setAttribute("Process", WalletType + "Wallet Test - Parallel Tier Check on Product : " + prdCode + " with Status Value : " + on);		
		String result = globalProductPage.getProductStatus(prdCode);
		context.setAttribute("Result", "Result expected value : " + on + " and received value : " + result);
		Assert.assertEquals(result, on);
	}
}
