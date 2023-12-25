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

public class SlotGameListTest extends TestBase{
	
	LoginPage loginPage;
	HomePage homePage;
	SlotGameListPage slotGameListPage;
	AmazonUtil AmazonUtil;
	String level, menu, item, yes, no, on, off, WalletType, TestAccount, newStatus, paraCode, resellCode, operateCode, tier, initialCategory, currentCategory;
	boolean CAtest;
	List<List<String>> ProductDataList = new ArrayList<List<String>>();
	
	public SlotGameListTest() {
		super();
	}
	
	@DataProvider(name = "RealTimeProductData")
	public Object[][] getProductData() {
		Object[][] data = new Object[ProductDataList.size()][1];
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
		item = prop.getProperty("SlotGameList");
		yes = prop.getProperty("yes");
		no = prop.getProperty("no");
		on = prop.getProperty("on");
		off = prop.getProperty("off");
		WalletType = System.getProperty("walletType")!=null ? System.getProperty("walletType") : prop.getProperty("walletType");
		/*if(WalletType.equals(prop.getProperty("Seamless"))) {
			TestAccount = prop.getProperty("SeamlessAccount");
		}else if(WalletType.equals(prop.getProperty("Transfer"))) {
			TestAccount = prop.getProperty("WalletAccount");
		}*/
		paraCode = prop.getProperty("parallelAcc");
		resellCode = prop.getProperty("resellerAcc");
		operateCode = prop.getProperty("operatorAcc");
		AmazonUtil = new AmazonUtil();
		slotGameListPage = new SlotGameListPage();
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
		slotGameListPage = homePage.clickOnGameList(menu,item);
		//agentProductPage.findTestingAccount(paraCode, tier, initialCategory);
	}
	
	@Test(priority=1)
    public void SectionSetUpTestProduct(ITestContext context) {
		checkLevelSkip();
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", "Get Test Product Data");
		tier = prop.getProperty("parallelTier");
		ProductDataList = null;
		ProductDataList = slotGameListPage.getALLProductList();
    }
	
	@Test(priority=2, dataProvider = "RealTimeProductData")
	public void SlotGameListSingleUpdate(String prdName, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		String newStatus = "";
		slotGameListPage.findTestingProduct(prdName);
		String[][] typeData  = slotGameListPage.getGameTypeStatus();
		for(int i=0; i < typeData.length; i++) {
			System.out.println("Data Type Length : " + typeData.length);
			newStatus = "";
			String typeTitle = typeData[i][0];
			String innitialStatus = typeData[i][1];
			System.out.println("Title title Main : " + typeTitle);
			System.out.println("Title value Main : " + innitialStatus);
			if(innitialStatus.equals(yes)) {
				newStatus = no;
			}else if(innitialStatus.equals(no)) {
				newStatus = yes;
			}
			slotGameListPage.GameTypeStatusTestSingle(typeTitle, newStatus);
			String resultTypeValue = slotGameListPage.getGameTypeStatusSingle(typeTitle);
			System.out.println("Result Value Main : " + resultTypeValue);
			Assert.assertEquals(resultTypeValue,newStatus);
		}
		
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Slot Game List Test Single Update with Product : " + prdName );
	}
	
	@Test(priority=3, dataProvider = "RealTimeProductData")
	public void SlotGameListMultipleUpdate(String prdName, ITestContext context) throws InterruptedException {
		checkLevelSkip();
		String newStatus = "";
		slotGameListPage.findTestingProduct(prdName);
		slotGameListPage.updateALLType();
		String[][] typeData  = slotGameListPage.getGameTypeStatus();
		for(int i=0; i < typeData.length; i++) {
			newStatus = "";
			String typeTitle = typeData[i][0];
			String resultStatus = typeData[i][1];
			System.out.println("Title title Main : " + typeTitle);
			System.out.println("Title value Main : " + resultStatus);
			if(typeTitle.equals("Slot") || typeTitle.equals("Status")) {
				newStatus = yes;
			}else {
				newStatus = no;
			}
			//slotGameListPage.GameTypeStatusTestSingle(typeTitle, newStatus);
			//String resultTypeValue = slotGameListPage.getGameTypeStatusSingle(typeTitle);
			System.out.println("Result Value Main : " + resultStatus);
			Assert.assertEquals(resultStatus,newStatus);
		}
		
		context.setAttribute("Steps", "1");
		context.setAttribute("Process", WalletType + "Wallet Test - Slot Game List Test Multiple Update with Product : " + prdName );
	}

}
