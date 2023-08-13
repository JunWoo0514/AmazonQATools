package Amazon.base;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;

//import AASAutomation.base.DriverManager;
//import AASAutomation.base.DriverManagerFactory;
//import AASAutomation.base.DriverType;
import Amazon.util.AmazonUtil;

public class TestBase {
	

	//public static DriverManager driverManager;
	public static WebDriver driver;
	public static Properties prop;
	public static WebDriverWait wait, waitE;
	Wait<WebDriver> fwait;
	
	//Gets data from properties file
	public TestBase(){
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/Amazon/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void initialization(){
		String browserName = System.getProperty("browser")!=null ? System.getProperty("browser") :  prop.getProperty("browser");
		
		if(browserName.equals("chrome")){
			//driverManager = DriverManagerFactory.getDriverManager(DriverType.CHROME);
			//driver = driverManager.getWebDriver();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("--remote-allow-origins=*");
			driver = new ChromeDriver(options);
		}
		else if(browserName.equals("FF")){
			//For Fire Fox
			driver = new FirefoxDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		//driver.manage().timeouts().getPageLoadTimeout(TestUtil.PAGE_LOAD_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(AmazonUtil.PAGE_LOAD_TIMEOUT);
		driver.manage().timeouts().implicitlyWait(AmazonUtil.IMPLICIT_WAIT);
		
		driver.get(prop.getProperty("url"));
		
		//wait = new WebDriverWait(driver, TestUtil.ELEMENT_WAIT);

		//actions = ActionChains(driver);
		wait = (WebDriverWait) new WebDriverWait(driver, AmazonUtil.ELEMENT_WAIT_MID);
		//waitE = (WebDriverWait) new FluentWait<WebDriver>(driver)
			    //.withTimeout(Duration.ofSeconds(1))
			    //.pollingEvery(Duration.ofMillis(500));
			    //.ignoring(NoSuchElementException.class);
		
	}
	
	//Wait
	public void waitVisibility(WebElement element){
		wait.until(ExpectedConditions.visibilityOf(element));
    }
	
	public void waitClickable(WebElement element){

		wait.until(ExpectedConditions.elementToBeClickable(element));
    }
	
	public void waitVisibilityLocate(By Byelement){
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(Byelement));

    }
	
	public void waitInVisibilityLocate(By Byelement){
		
		wait.until(ExpectedConditions.invisibilityOfElementLocated(Byelement));

    }
	
	//Click Method
    public void click(WebElement element) {
    	//System.out.print("1:"+element.toString());
    	waitVisibility(element);
        //waitVisibilityLocate(element);
        element.click();
    }
    
    public void clickableClick(WebElement element) {
    	//System.out.print("1:"+element.toString());
    	waitVisibility(element);
    	waitClickable(element);
        //waitVisibilityLocate(element);
        element.click();
    }
    
    public void CustomWaitClick(final WebElement element, final String cssText) throws InterruptedException {
		for(int i=0;i<10;i++)
			
		{
			boolean t = driver.findElement(By.cssSelector(cssText)).isDisplayed();
			System.out.println("Btn State: " +t);
			if(t == true) {
				Thread.sleep(150);
				System.out.println("Custom " + i + "  OK");
				try {
					element.click();
					System.out.println("Custom " + i + "  OK");
				}catch(StaleElementReferenceException exc) {
					break;
				}
				
			}

			else

			{
				System.out.println("Custom " + i + "  NO");

			}

		}
	}
    
    public void CustomWaitClick2(final WebElement element, final String cssText) throws InterruptedException {
    	boolean clicked = false;
		for(int i=0;i<10;i++)
		{
			boolean t = driver.findElement(By.cssSelector(cssText)).isDisplayed();
			System.out.println("2 Btn State: " +t);
			if(t == true) {
				Thread.sleep(150);
				System.out.println("2 Custom " + i + "  OK");
				try {
					if(!clicked) {
						element.click();
						clicked = true;
					}
					System.out.println("Custom " + i + "  OK");
				}catch(StaleElementReferenceException exc) {
					break;
				}
				
			}

			else

			{
				System.out.println("2 Custom " + i + "  NO");

			}

		}
	}
    
    //Write Text
    public void writeText(WebElement element, String text) {
    	waitVisibility(element);
    	clearText(element);
        element.sendKeys(text);
    }
    
    public void OnclickUP(WebElement element) {
    	waitVisibility(element);
    	element.sendKeys(Keys.UP);
    }
    
    public void OnclickDOWN(WebElement element) {
    	waitVisibility(element);
    	element.sendKeys(Keys.DOWN);
    }
    
  //Clear Text
    public void clearText(WebElement element) {
    	waitVisibility(element);
        element.clear();
    }
 
    //Read Text
    public String readText(WebElement element) {
        waitVisibility(element);
        return element.getText();
    }
    
    //Checking
    public boolean elementDisplayCheck(WebElement element) {
        return element.isDisplayed();
    }
    
    //Read Text Box
    public String readTextBox(WebElement element) {
        waitVisibility(element);
        return element.getAttribute("value");
    }
    
    public String readToggle(WebElement element) {
    	if (element.isSelected())
        {
            return "Active";
        }else {
        	return "Disabled";
        }
    }
    
    public void selectToggle(WebElement element, boolean status) {
    	
    	if (status == false && element.isSelected() == true)
        {
    		element.click();
        }else if (status != true && element.isSelected() == false){
        	element.click();
        }
    }
    
    public void selectToggleTrue(WebElement element) {
    	
    	if (element.isSelected() != true)
        {
    		element.click();
        }
    }
    
    public void selectToggleFalse(WebElement element) {
    	
    	if (element.isSelected() == true)
        {
    		element.click();
        }
    }
    
    //Select Item From Dropdown List
    public void selectItem(WebElement element, String text) {
    	waitVisibility(element);
        Select select = new Select(element);	
        select.selectByVisibleText(text);	
    }
    
    public void selectItemValue(WebElement element, int text) {
        waitVisibility(element);
        Select select = new Select(element);	
        select.selectByValue(Integer.toString(text));	
    }
    
    public void selectItemIndex(WebElement element, int text) {
        waitVisibility(element);
        Select select = new Select(element);	
        select.selectByIndex(text);	
    }
    
    public String selectItemFirstItem(WebElement element) {
        waitVisibility(element);
        Select select = new Select(element);	
        return select.getFirstSelectedOption().getText();
    }
    
    public List<WebElement> selectItemAllItem(WebElement element) {
        waitVisibility(element);
        Select select = new Select(element);	
        return select.getOptions();
    }
    
    public int selectOptionIndex(WebElement element) {
        waitVisibility(element);
        Select select = new Select(element);
        List<WebElement> options = select.getOptions();
        int optionCount = options.size();
        return optionCount;
    }
    
    @AfterClass
	public void tearDown() {
    	driver.close();
		driver.quit();
	}
    

}
