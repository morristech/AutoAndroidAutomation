package Utilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.lang.model.util.Elements;

import org.openqa.selenium.By;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Elements.Page;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

/**
 * All tests will extend testroot
 * Testroot will pull info from properties, 
 * 	save the driver, and do setup/takedown, 
 * 	as all tests will perform these tasks
 * @author ChitraSelvaraj
 *
 */
public class TestRoot {
	
	protected static int implicitWaitTimeout = 100;
	
	protected static boolean useEmulator; 
	protected static String device; // or the avd
	//protected static String model; 
	protected static String platformVersion;
	protected static String appPath;
	protected static String appPackage;
	protected static String appActivity;
	protected static String address;
	protected static String port;
	
	protected static AndroidDriver<MobileElement> driver; 
	/**
	 * Load properties file and create driver with the basic Android test properties
	 */
	protected static boolean setup(){
		//return setup("android.properties.local");
		return setup("src/Utilities/iHeartAndroidAuto.properties");
	}
	
	/**
	 * Load from the provided properties file, if system properties do not exist. 
	 * @param propertiesFile
	 */
	protected static boolean setup(String propertiesFile){
		/*// Load properties file
		//Properties testProperties = null;
		//if(TestRoot.class.getClassLoader().getResource(propertiesFile) != null){
			try{
				testProperties= LoadProperties.loadProperties(propertiesFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}*/
			
			Properties testProperties = new Properties();
			FileInputStream propertiesFileStream = null;
			try 
			{
				propertiesFileStream = new FileInputStream(propertiesFile);
				testProperties.load(propertiesFileStream);

			} 
			catch(Exception e){
				e.printStackTrace();
			}
		//}
		// Fill capabilities strings from properties
		// If a System Property exists, it takes precidence over a property from the file
		//    this makes Jenkins configurations easier, allows us to have specific and easily configured 
		//      settings in Jenkins
		useEmulator = Boolean.parseBoolean(setTestProperty("APPIUM.USEEMULATOR", testProperties));
		device = setTestProperty("APPIUM.DEVICE.NAME", testProperties);
		//model = setTestProperty("APPIUM.DEVICE.MODEL", testProperties);
		platformVersion = setTestProperty("APPIUM.DEVICE.PLATFORMVERSION", testProperties);
		appPath = setTestProperty("APPIUM.APP.PATH", testProperties);
	
		appPackage = setTestProperty("APPIUM.APP.PACKAGE", testProperties);
		appActivity = setTestProperty("APPIUM.APP.ACTIVITY", testProperties);
		
		address = setTestProperty("APPIUM.WEBDRIVER.URL", testProperties);
		
		// Get desired capabilities from property
		DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", device);
        if(useEmulator)
        	capabilities.setCapability("avd", device);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("app", appPath);
        //capabilities.setCapability("bundleID", bundleID);
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity", appActivity);
        
        
        
        
        // Load other properties
        port = setTestProperty("APPIUM.WEBDRIVER.PORT", testProperties);
        URL appiumUrl = null;
        try{
        	appiumUrl = new URL("http://" + address + ":" + port + "/wd/hub");
        }
        catch(Exception e){
        	e.printStackTrace();
        	try{
        		appiumUrl = new URL("http://127.0.0.1:4723/wd/hub");
        	}
        	catch(MalformedURLException mue){
        		mue.printStackTrace(); // Driver creation will fail
        	}
        }
        try{
        	driver = new AndroidDriver<MobileElement>(appiumUrl, capabilities);
        }
        catch(SessionNotCreatedException | UnreachableBrowserException exc){
        	System.err.println("Could not start driver. Emulator or device may be unavailable. Appium may have disconnected or stopped. Sleeping 30 seconds to retry.");
        	for(int i = 30; i > 0; i -= 5){
        		System.err.println("Retrying in: " + i + "...");
        		sleep(5000);
        	}
        	System.err.println("LAST CHANCE... TRYING TO START APPIUM BEFORE RETRYING DRIVER INITIALIZATION...\n");
        	driver = new AndroidDriver<MobileElement>(appiumUrl, capabilities);
        }
        
        
		// We have our own timeouts, don't need this as much, reduced to half a second
		driver.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
		// Set the location
		if(useEmulator || waitForVisible(driver, MobileBy.name("Set"), 15) != null){
			// Skip the iHeart location request
			enterZip();
		}
		
		return driver != null;
	}
	
	public static void quit(){
		if(driver != null){
			try{
				sleep(3000); // Allow commands to terminate naturally
				driver.quit();
			}
			catch(Exception e){
				System.err.println("ERROR SHUTTING DOWN DRIVER");
				e.printStackTrace();
			}
		}
		// Sleeping to ensure appium has time to reset
		// Actually a bit faster in local testing of long suites due to fewer crashes
		sleep(5000);
	}
	
	
	private static void enterZip(){
		waitForVisible(driver, MobileBy.id(Page.connectId + "edit_text"), 10).sendKeys("07661");
		driver.navigate().back(); // Already d.navigate is defined in Elements.Page but since this is root had to put driver
		
		click(driver, MobileBy.name("Set"));
		//waitForVisible(driver, MobileBy.name("Set"), 15
		
	}
	
	private static String setTestProperty(String property, Properties loadedProps){
		String propValue = "";
		
		try{
			propValue = System.getProperty(property);
		}
		catch(Exception e){}
		
		if(!strGood(propValue) && loadedProps != null){
			try{
				propValue = loadedProps.getProperty(property);
			}
			catch(Exception e){
				System.err.println("Could not load: " + property);
			}
		}
		
		return propValue;
	}
	
	
	////  Utility methods  ////
	public static boolean strGood(String s){
		return s != null && s.length() > 0;
	}
	
	public static void sleep(long mil){
		if(mil > 0){
			try {
				Thread.sleep(mil);
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static MobileElement findElement(AndroidDriver<MobileElement> d, By by){
		MobileElement e = null;
		try{
			d.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			e = d.findElement(by);
			if(e != null){
				return e;
			}
		}
		catch(Exception exc){}
		finally{
			d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
		}
		return e;
	}
	
	public static AndroidElement waitForPresent(AndroidDriver<MobileElement> d, By by, long timeoutInSec){
		long timeLeftMil = timeoutInSec * 1000;
		while(timeLeftMil > 0){
			
			if(findElement(d, by) != null){
				break;
			}
			sleep(200);
			timeLeftMil -= 200;
		}
		return (AndroidElement) d.findElement(by);
	}
	
	public static AndroidElement waitForVisible(AndroidDriver<MobileElement> d, By by, long timeoutInSec){
		// Wait for it to be present (not just clickable/visible, but loaded)
		long timeLeftMil = timeoutInSec * 1000;
		while(timeLeftMil > 0){
			if(findElement(d, by) != null){
				break;
			}
			sleep(100); // Intentionally mismatched to make up for time searching for element
			timeLeftMil -= 200;
		}
		
		timeoutInSec = (timeLeftMil / 1000);
		if(timeLeftMil >= 500){
			timeoutInSec = 1;
		}
	
		AndroidElement returnElement = null;
		WebDriverWait wait = new WebDriverWait(d, timeoutInSec);
		try{
			returnElement = (AndroidElement) wait.until(ExpectedConditions.elementToBeClickable(d.findElement(by)));
		}
		catch(Exception e){
			// Attempt to grab it anyway
			try{
				returnElement = (AndroidElement) d.findElement(by);
			}
			catch(Exception e1){}
		}
		return returnElement;
	}
	
	/**
	 * Returns true if an element is not visible
	 * @param d (AndroidDriver<MobileElements>)
	 * @param by (By)
	 * @return
	 */
	public static boolean isNotVisible(AndroidDriver<MobileElement> d, By by){
		return isNotVisible(d, by, 0);
	}
	/**
	 * Returns true if an element is not visible
	 * @param d
	 * @param by
	 * @param delayInSeconds
	 * @return
	 */
	public static boolean isNotVisible(AndroidDriver<MobileElement> d, By by, int delayInSeconds){
		sleep(delayInSeconds*1000);
		boolean notFound = false;
		
		try{
			// Prevent any errors that could occur
			if(d.findElement(by) != null){
				notFound = !d.findElement(by).isDisplayed();
			}
			else{
				notFound = true;
			}
		}
		catch(Exception e){
			notFound = true;
		}
		
		return notFound;
	}
	
	public static boolean isVisible(String element){
		return driver.findElement(find(element)).isDisplayed();
	}
	public static boolean isVisible(By by){
		return driver.findElement(by).isDisplayed();
	}
	public static boolean isVisible(MobileElement e){
		return e.isDisplayed();
	}
	
	public static void click(AndroidDriver<MobileElement> d, By by){
		click(d, d.findElement(by));
	}
	public static void click(AndroidDriver<MobileElement> d, MobileElement e){
		d.tap(1, e, 150);
	}
	public static void tap(AndroidDriver<MobileElement> d, By by){
		click(d, by);
	}
	
	/**
	 * Some Elements need a VERY quick tap. This one has 0 duration
	 * @param d
	 * @param by
	 */
	public static void quickTap(AndroidDriver<MobileElement> d, By by){
		quickTap(d, d.findElement(by));
	}
	public static void quickTap(AndroidDriver<MobileElement> d, MobileElement e){
		int clickX = e.getSize().getWidth() / 4 + e.getLocation().getX(); //3/4 in
		d.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
		d.tap(1, clickX, e.getLocation().getY(), 5);
		d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
	}
	public static void quickClick(AndroidDriver<MobileElement> d, By by){
		quickTap(d, by);
	}
	
	/**
	 * Some Elements need a longer tap. This one has 1000ms (1 second) duration
	 * @param d
	 * @param by
	 */
	public static void longTap(AndroidDriver<MobileElement> d, By by){
		longTap(d, d.findElement(by));
	}
	public static void longTap(AndroidDriver<MobileElement> d, MobileElement e){
		d.tap(1, e, 1000);
	}
	public static void longClick(AndroidDriver<MobileElement> d, By by){
		longTap(d, by);
	}
	
	
	/**
	 * Waits until an item is visible before clicking/tapping on it
	 * @param d
	 * @param by
	 * @param duration
	 */
	public static void waitToClick(AndroidDriver<MobileElement> d, By by, int duration){
		click(d, waitForVisible(d, by, duration));
	}
	
	/**
	 * Direction: 0-3 
	 * 	0: Up
	 * 	1: Right
	 * 	2: Down
	 * 	3: Left
	 * Duration is in MS
	 * @param d
	 * @param by
	 * @param direction
	 * @param duration
	 */
	public static void swipeOnItem(AndroidDriver<MobileElement> d, By by, int direction, int duration){
		MobileElement e = waitForVisible(d, by, 15);
		if(e != null){
			int startX = e.getCenter().getX();
			int startY = e.getCenter().getY();
			int toX = startX;
			int toY = startY;
			
			switch(direction){
			case 0: // Up
				toY -= 150;
				if(toY < 0){
					toY = 0;
				}
				break;
			case 1: // Right
				toX += 150;
				if(toX > getAppWidth(d)){
					toX = getAppWidth(d);
				}
				break;
			case 2: // Down
				toY += 150;
				if(toY > getAppHeight(d)){
					toY = getAppHeight(d);
				}
				break;
			case 3: // Left
				toX -= 150;
				if(toX < 0){
					toX = 0;
				}
				break;
			default: // Bad input
				System.err.println("Bad directional input. What are we going to do with you? No swipe for you!");
				break;
			}
			
			d.swipe(startX, startY, toX, toY, duration);
		}
	}
	
	/**
	 * Waits for an item to be visible before sending text to the object
	 * @param d
	 * @param by
	 * @param type
	 * @param duration
	 */
	public static void waitToType(AndroidDriver<MobileElement> d, By by, String type, int duration){
		waitForVisible(d, by, duration).sendKeys(type);
	}
	public static void type(AndroidDriver<MobileElement> d, String element, String type){
		d.findElement(find(element)).sendKeys(type);
	}
	public static void type(AndroidDriver<MobileElement> d, By by, String type){
		d.findElement(by).sendKeys(type);
	}
	/**
	 * Used to find by xpath or ID, without needing to specify
	 * @param findBy
	 * @return
	 */
	public static By find(String find){
		if(find.startsWith("//")){
			return By.xpath(find);
		}
		else{
			return By.id(find);
		}
	}
	
	/** 
	 * Returns the width of the usable screen space
	 * @param d
	 * @return
	 */
	public static int getAppWidth(AndroidDriver<MobileElement> d){
		return d.manage().window().getSize().getWidth();
	}
	/**
	 * returns the height of the usable screen space
	 * @param d
	 * @return
	 */
	public static int getAppHeight(AndroidDriver<MobileElement> d){
		return d.manage().window().getSize().getHeight();
	}
	
	/**
	 * Returns the displayed text of an element
	 * @param d
	 * @param by
	 * @return
	 */
	public static String getText(AndroidDriver<MobileElement> d, By by){
		MobileElement e = TestRoot.waitForVisible(d, by, 20);
		if(e == null){
			return null;
		}
		else{
			return e.getText();
		}
	}
	
	public static boolean waitForAdditionalContext(AndroidDriver<MobileElement> d, int timeInMS){
		boolean foundContext = false;
		
		while(timeInMS > 0){
			if(d.getContextHandles().size() > 1){
				foundContext = true;
				break;
			}
			sleep(100);
			timeInMS -= 100;
		}
		
		return foundContext;
	}
	// Window context
	public static void switchToWebViewContext(AndroidDriver<MobileElement> d){
		d.context("WEBVIEW_com.clearchannel.iheartradio.controller");
	}
	//Main context
	public static void switchToNativeContext(AndroidDriver<MobileElement> d){
		d.context("NATIVE_APP");
	}
	
	/**
	 * Estimation, if it's +/- 1, it's "equal"
	 * @param x The given value
	 * @param y The expected value
	 * @return
	 */
	public static boolean isAbout(int testing, int expected){
		return isAbout(testing, expected, 2);
	}
	public static boolean isAbout(int testing, int expected, int variance){
		if(testing == expected || testing + variance >= expected && testing - variance <= expected){
			return true;
		}
		return false;
	}
}
