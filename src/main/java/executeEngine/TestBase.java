package executeEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public class TestBase {
	
	public static Properties prop;
    public static AppiumDriver driver;
//    private static Logger logger;
    
    public static Logger log = Logger.getLogger("ActionKeywords");

    static{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss");
        System.setProperty("current.date", dateFormat.format(new Date()));
    }

    public TestBase() {
        try {
            prop = new Properties();
            FileInputStream inputStream = new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\java\\config\\config.properties");
            prop.load(inputStream);
            System.out.println(inputStream+"TEST BASE");
//            PropertyConfigurator.configure(System.getProperty("user.dir") + "/src/main/resource/log4j.properties");
            log = Logger.getLogger(MethodHandles.lookup().lookupClass());
           
        } catch (FileNotFoundException Ex) {
            log.info("File not found: " + Ex.getMessage());

        } catch (IOException Ex) {
            log.info("Exception occurred: " + Ex.getMessage());
        }
    }

    /**
     * This is singleton driver initialization method
     * @throws MalformedURLException - In case of invalid appium server url
     */
    public static AppiumDriver driverInitialization() throws MalformedURLException{
    
        if (driver == null) {
            switch (prop.getProperty("Platform")){
                case "android":
                    log.info("Running Tests On Android Platform.");
                    driver = androidSetup();
                    break;
                case "browser_stack":
                    log.info("Running Tests On Browser Stack.");
                    driver = browserStack();
                    break;
                case "ios":
                    log.info("Running Tests On IOS Platform.");
                    driver = iosSetup();
                    break;
                default:
                    log.info("Running Tests On Browser Stack.");
                    browserStack();
            }
        }
        return driver;
    }

    /**
     * This method is used for android driver setup
     * @throws MalformedURLException - In case of invalid appium server url
     */
    private static AppiumDriver androidSetup() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("automationName", prop.getProperty("AutomationName"));
        caps.setCapability("deviceName", prop.getProperty("DeviceName"));
//        caps.setCapability("udid", prop.getProperty("UDID"));
        caps.setCapability("platformName", prop.getProperty("Platform"));
        caps.setCapability("platformVersion", prop.getProperty("PlatformVersion"));
        caps.setCapability("appPackage", prop.getProperty("AppPackage"));
        caps.setCapability("appActivity", prop.getProperty("AppActivity"));
//        caps.setCapability("app", System.getProperty("user.dir") + "/Apps/" + prop.getProperty("AppName"));
        caps.setCapability("noReset", prop.getProperty("NoReset"));
//        caps.setCapability("autoGrantPermissions", prop.getProperty("AutoGrantPermissions"));
        driver = new AndroidDriver<MobileElement>(new URL(prop.getProperty("AppiumServer")), caps);
        log.info("Starting Android Driver.");
        return driver;
    }

    /**
     * This method is used for browser stack driverInitialization
     * @throws MalformedURLException - In case of invalid browser stack server url
     */
    private static AppiumDriver browserStack() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        String bs_url = "https://"+prop.getProperty("bsUserName")+":"+prop.getProperty("bsAccessKey")+"@hub-cloud.browserstack.com/wd/hub";
        caps.setCapability("device", prop.getProperty("CloudDeviceName"));
        caps.setCapability("os_version", prop.getProperty("CloudPlatformVersion"));
        caps.setCapability("app", prop.getProperty("bsAppHash"));
        driver = new AndroidDriver<>(new URL(bs_url), caps);
        log.info("Starting Android Driver on BrowserStack.\nBrowser Stack Server Details:\n"+bs_url);
        return driver;
    }

    /**
     * This method is used for ios driver setup
     * @throws MalformedURLException - In case of invalid appium server url
     */
    private static AppiumDriver iosSetup() throws MalformedURLException{
        DesiredCapabilities caps = new DesiredCapabilities();
        // To be implemented
        driver = new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), caps);
        log.info("Starting IOS Driver.");
        return driver;
    }


}
