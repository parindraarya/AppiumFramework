package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import executeEngine.TestBase;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class ActionKeywords {
	
	public AppiumDriver driver;
	String report[] = new String[] { "Fail", null };
	public String pth;
	private static int timeout;
	FileReader reader;
	Properties prop;
	
	public void readProp() throws IOException {
		reader=new FileReader("config.properties");  
	    
	    prop=new Properties();
	    prop.load(reader);
	}
    
	
	Logger log = Logger.getLogger("ActionKeywords");

	/**
	 * 
	 */
	public void waitDriver() {
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
	}
	
	public void sleep() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public String[] initialDriverMobile() throws IOException {
		try {
            prop = new Properties();
            FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\config\\config.properties");
            prop.load(inputStream);
//            PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/main/resource/log4j.properties");
            log = Logger.getLogger(MethodHandles.lookup().lookupClass());
            
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("automationName", prop.getProperty("AutomationName"));
            caps.setCapability("deviceName", prop.getProperty("DeviceName"));
//            caps.setCapability("udid", prop.getProperty("UDID"));
            caps.setCapability("platformName", prop.getProperty("Platform"));
            caps.setCapability("platformVersion", prop.getProperty("PlatformVersion"));
            caps.setCapability("appPackage", prop.getProperty("AppPackage"));
            caps.setCapability("appActivity", prop.getProperty("AppActivity"));
//            caps.setCapability("app", System.getProperty("user.dir") + "/Apps/" + prop.getProperty("AppName"));
            caps.setCapability("noReset", prop.getProperty("NoReset"));
//            caps.setCapability("autoGrantPermissions", prop.getProperty("AutoGrantPermissions"));
            driver = new AndroidDriver<MobileElement>(new URL(prop.getProperty("AppiumServer")), caps);
            log.info("Starting Android Driver.");
           
        } catch (FileNotFoundException Ex) {
            log.info("File not found: " + Ex.getMessage());

        } catch (IOException Ex) {
            log.info("Exception occurred: " + Ex.getMessage());
        }
		return report;
	}
	
	private int timeoutValue(){
        try{
            timeout = Integer.parseInt(prop.getProperty("Timeout"));
        }catch (Exception Ex) {
            log.error("Exception Occurred While Getting Timeout Property:" + Ex.getMessage());
        }
        return timeout;
    }

	/**
	 * 
	 * @return
	 */
	public String timeStamp() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
		String formatDate = sdf.format(date);
		return formatDate;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String screenShot() throws IOException {
		String formatDate = timeStamp();
		File ss = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String ssPath = System.getProperty("user.dir") + "\\Reports\\ScreenShots\\" + formatDate + ".png";
		// String ssPath = "/Report/ScreenShots/"+formatDate+".png";
		FileUtils.copyFile(ss, new File(ssPath));
		return ssPath;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public String[] openBrowser() throws IOException {
		try {
//			ExtentReport.create("TestCase_00");
			System.setProperty("webdriver.chrome.driver", "ChromeDriver\\chromedriver.exe");
//			driver = new ChromeDriver();
			driver.manage().window().maximize();
			report[0] = "Pass";
			report[1] = screenShot();
//			ExtentReport.info("Browser open", report[1]);
			log.info("Browser is opening");
			return report;
		} catch (Exception e) {
			pth = screenShot();
//			ExtentReport.fail("Unable to open browser", pth);
			log.fatal("Browser is not opening");
			System.out.println(e);
		}
		return report;
	}

	/**
	 * 
	 * @param dataValue
	 * @return
	 * @throws IOException
	 */
	public String[] navigate(String dataValue) throws IOException {
		try {
			driver.get(dataValue);
			waitDriver();
			report[0] = "Pass";
			report[1] = screenShot();
//			ExtentReport.pass("Navigate to url", report[1]);
			log.info("Going to url");
			return report;
		} catch (Exception e) {
			pth = screenShot();
//			ExtentReport.fail("Url not opened", pth);
			log.error("Url not opening");
			System.out.println(e);
		}
		return report;
	}

	/**
	 * 
	 * @param locatorValue
	 * @return
	 * @throws IOException
	 */
	public String[] click(By locatorValue) throws IOException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locatorValue));
			element.click();
			//driver.findElement(locatorValue).click();
			waitDriver();
			report[0] = "Pass";
			report[1] = screenShot();
//			ExtentReport.pass("Click", report[1]);
			log.info("Click");
			return report;
		} catch (Exception e) {
			pth = screenShot();
//			ExtentReport.fail("Can't click", pth);
			log.error("Not clicking");
			System.out.println(e);
		}
		return report;
	}

	/**
	 * 
	 * @param locatorValue
	 * @param dataValue
	 * @return
	 * @throws IOException
	 */
	public String[] input(By locatorValue, String dataValue) throws IOException {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locatorValue));
			element.sendKeys(dataValue);
			//driver.findElement(locatorValue).sendKeys(dataValue);
			waitDriver();
			report[0] = "Pass";
			report[1] = screenShot();
//			ExtentReport.pass("Data inserted", report[1]);
			log.info("Enter data");
			return report;
		} catch (Exception e) {
			pth = screenShot();
//			ExtentReport.fail("Can,t enter data", pth);
			log.error("Not entering data");
			System.out.println(e);
		}
		return report;
	}
	
	public String getPageTitle() {
		String title = driver.getTitle();
		return title;
	}
	
	/**
	 * 
	 * @param dataValue
	 * @return
	 * @throws IOException
	 */
	public String[] switchTab(String dataValue) throws IOException {
		try {
		ArrayList<String> tabs = new ArrayList<String>();
		driver.switchTo().window(tabs.get(Integer.parseInt(dataValue)));
		waitDriver();
		report[0] = "Pass";
		report[1] = screenShot();
//		ExtentReport.pass("Switch tab successfully", report[1]);
		log.info("Switch tab successfully");
		return report;
	} catch (Exception e) {
		pth = screenShot();
//		ExtentReport.fail("Switch tab failed", pth);
		log.error("Switch tab failed");
		System.out.println(e);
	}
	return report;
	}
	
	/**
	 * 
	 * @param locatorValue
	 * @param dataValue
	 * @return
	 * @throws IOException
	 */
	public String[] verifyText(By locatorValue, String dataValue) throws IOException {
		try {
		sleep();
		String s = driver.findElement(locatorValue).getText().replaceAll("\\r\\n|\\r|\\n", " ");
		dataValue = dataValue.replaceAll("\\r\\n|\\r|\\n", " ");
		Boolean b = s.equalsIgnoreCase(dataValue);
		System.out.println("Actual: " + s + "Expected: " + dataValue);
		waitDriver();
		if(b) {
			report[0] = "Pass";
			report[1] = screenShot();
//			ExtentReport.pass("Text matching", report[1]);
			log.info("Text matching");
			
		}else {
			report[0] = "Fail";
			report[1] = screenShot();
//			ExtentReport.fail("Text not matching", report[1]);
			log.info("Text not matching");
		}
		return report;
	} catch (Exception e) {
		pth = screenShot();
		report[0] = "Fail";
		report[1] = screenShot();
//		ExtentReport.fail("Can,t find data", pth);
		log.error("Data not found");
		System.out.println(e.getMessage());
	}
	return report;
	}
	
	/**
	 * 
	 * @param locatorValue
	 * @return
	 * @throws IOException
	 */
	public String[] objExist(By locatorValue) throws IOException {
		try {
		sleep();
		WebElement e = driver.findElement(locatorValue);
		Boolean b = e.isDisplayed();
		waitDriver();
		if(b) {
			report[0] = "Pass";
			report[1] = screenShot();
//			ExtentReport.pass("Element is present", report[1]);
			log.info("Element present");
			
		}else {
			report[0] = "Fail";
			report[1] = screenShot();
//			ExtentReport.fail("Element not present", report[1]);
			log.info("Element not present");
		}
		return report;
	} catch (Exception e) {
		pth = screenShot();
		report[0] = "Fail";
		report[1] = screenShot();
//		ExtentReport.fail("Element not found", pth);
		log.error("Element not found");
		System.out.println(e.getMessage());
	}
	return report;
	}

	/**
	 * 
	 */
	public void closeBrowser() {
		// Open browser
		log.info("Close browser");		
//		ExtentReport.endTc();
//		ExtentReport.ReportFlush();
		driver.close();
	}
	
	/**
	 * 
	 * @param locator
	 * @return
	 * @throws IOException
	 */
	public String[] waitForElementPresent(By locator) throws IOException{
		WebDriverWait wait = new WebDriverWait(driver, timeoutValue());
        try{
            wait.ignoring(StaleElementReferenceException.class).
                    until(ExpectedConditions.presenceOfElementLocated(locator));
            waitDriver();
    		report[0] = "Pass";
    		report[1] = screenShot();
    		return report;
        }catch(Exception e) {
        	pth = screenShot();
			log.error("Element not present");
			System.out.println(e.getMessage());    
        }
        return report;
    }
	
	/**
	 * 
	 * @param locator
	 * @return
	 * @throws IOException
	 */
	public String[] waitForElementVisible(By locator) throws IOException{
        WebDriverWait wait = new WebDriverWait(driver, timeoutValue());
        try{
            wait.ignoring(StaleElementReferenceException.class).
                    until(ExpectedConditions.visibilityOfElementLocated(locator));
            waitDriver();
    		report[0] = "Pass";
    		report[1] = screenShot();
    		return report;
        }catch(Exception e) {
        	pth = screenShot();
			System.out.println(e);  
            log.error("Exception Occurred While Locating The Element: " + e.getMessage());
        }
        return report;
    }
	
	/**
	 * 
	 * @param locator
	 * @return
	 * @throws IOException
	 */
	public String[] pressEnterKey(By locator) throws IOException{
        try{
                Thread.sleep(500);
                MobileElement element = (MobileElement)driver.findElement(locator);
                element.sendKeys(Keys.ENTER);
                Thread.sleep(500);
                report[0] = "Pass";
        		report[1] = screenShot();
        		return report;
        }catch (Exception e) {
        	pth = screenShot();
			System.out.println(e); 
            log.error("Exception Occurred While Sending Enter Key" + e.getMessage());
        }
        return report;
    }
	
	/**
	 * 
	 * @param locator
	 * @return
	 * @throws IOException
	 */
	public String[] mouseClick(By locator) throws IOException{
		try {
		Thread.sleep(2000);
                MobileElement element = (MobileElement)driver.findElement(locator);
                element.click();
                log.info("Clicking on element: " + locator.toString());
                report[0] = "Pass";
        		report[1] = screenShot();
        		return report;
        }catch(Exception e) {
        	pth = screenShot();
			System.out.println(e); 
            log.error("Exception Occurred While Clicking The Element: " + e.getMessage());
        }
        	return report;
    }
	
	/**
	 * 
	 * @param locator
	 * @return
	 * @throws IOException
	 */
	public String[] moveToElementAndClick(By locator) throws IOException{
        try{
            Thread.sleep(2000);
                MobileElement element = (MobileElement)driver.findElement(locator);
                Actions action = new Actions(driver);
                action.moveToElement(element).wait(timeoutValue());
                action.click();
                log.info("Clicking on element: " + locator.toString());
                report[0] = "Pass";
        		report[1] = screenShot();
        		return report;
        }catch(Exception e) {
        	pth = screenShot();
			System.out.println(e); 
            log.error("Exception Occurred While Clicking The Element: " + e.getMessage());
        }
        	return report;
    }

}
