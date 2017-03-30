package Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import Pages.Xpath;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testCommons.CommandExecutor;
import testCommons.LoadProperties;


/**
 * All tests will extend testroot
 * Testroot will pull info from properties, 
 * 	save the driver, and do setup/takedown, 
 * 	as all tests will perform these tasks
 * @author daniellegolinsky 
 *
 */
public class TestRoot {
	
	protected static int implicitWaitTimeout = 375;
	
	protected static boolean useEmulator; 
	protected static boolean testOD; // TODO Not necessary once all builds have OD features
	protected static String device; // or the avd
	protected static String model; 
	protected static String platformVersion;
	protected static String appPath;
	protected static String appPackage;
	protected static String address;
	protected static String port;
	
	public static String IHEARTUSERNAME;
	public static String IHEARTPASSWORD;
	public static String FACEBOOKUSERNAME;
	public static String FACEBOOKPASSWORD;
	public static String NEWACCOUNTPASSWORD;
	public static String IHEARTUSERNAMEODFREE;
	public static String IHEARTPASSWORDODFREE;
	public static String IHEARTUSERNAMEODPLUS;
	public static String IHEARTPASSWORDODPLUS;
	public static String IHEARTUSERNAMEODPREMIUM;
	public static String IHEARTPASSWORDODPREMIUM;
	
	public static boolean ISMARSHMALLOW = false;
	public static String APPIUM_VERSION;
	public static String SCREENSHOT_DIRECTORY;
	public static String SCREENSHOT_URL;
	
	public static AndroidDriver<MobileElement> driver;
	
	// Directions for swiping
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	
	public static boolean getTestOD(){
		String propertiesFile = "android.properties.local";
		Properties testProperties = null;
		if(TestRoot.class.getClassLoader().getResource(propertiesFile) != null){
			try{
				testProperties= LoadProperties.loadProperties(propertiesFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		testOD = Boolean.parseBoolean(LoadProperties.getProperties(testProperties, "APPIUM.TESTOD"));
		
		return testOD;
	}
	
	/**
	 * Load properties file and create driver with the basic Android test properties
	 */
	protected static boolean setup(){
		return setup("android.properties.local");
	}
	
	/**
	 * Load from the provided properties file, if system properties do not exist. 
	 * @param propertiesFile
	 */
	protected static boolean setup(String propertiesFile){
		// Load properties file
		Properties testProperties = null;
		if(TestRoot.class.getClassLoader().getResource(propertiesFile) != null){
			try{
				testProperties = LoadProperties.loadProperties(propertiesFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		// Fill capabilities strings from properties
		// If a System Property exists, it takes precedence over a property from the file
		//    this makes Jenkins configurations easier, allows us to have specific and easily configured 
		//      settings in Jenkins
		useEmulator = Boolean.parseBoolean(LoadProperties.getProperties(testProperties, "APPIUM.USEEMULATOR"));
		testOD = Boolean.parseBoolean(LoadProperties.getProperties(testProperties, "APPIUM.TESTOD"));
		device = LoadProperties.getProperties(testProperties, "APPIUM.DEVICE.NAME");
		model = LoadProperties.getProperties(testProperties, "APPIUM.DEVICE.MODEL");
		platformVersion = LoadProperties.getProperties(testProperties, "APPIUM.DEVICE.PLATFORMVERSION");
		appPath = LoadProperties.getProperties(testProperties, "APPIUM.APP.PATH");
		appPackage = LoadProperties.getProperties(testProperties, "APPIUM.APP.PACKAGE");
		
		// Set the screenshot directory
		SCREENSHOT_DIRECTORY = LoadProperties.getProperties(testProperties, "OPTIONS.SCREENSHOT.DIRECTORY");
		if(!strGood(SCREENSHOT_DIRECTORY)){
			SCREENSHOT_DIRECTORY = "screenshots/";
		}
		SCREENSHOT_URL = LoadProperties.getProperties(testProperties, "OPTIONS.SCREENSHOT.URL");
		if(!strGood(SCREENSHOT_URL)){
			SCREENSHOT_URL = "";
		}
		// Get desired capabilities from property
		DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", device);
        if(useEmulator)
        	capabilities.setCapability("avd", device);
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("app", appPath);
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("fullReset", true);
        
        // Load other properties
        address = LoadProperties.getProperties(testProperties, "APPIUM.WEBDRIVER.URL");
        port = LoadProperties.getProperties(testProperties, "APPIUM.WEBDRIVER.PORT");
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
        
        // Grab passwords
        Properties passwords = null;
        String passwordsFile = "passwords.local";
		if(TestRoot.class.getClassLoader().getResource(passwordsFile) != null){
			try{
				passwords = LoadProperties.loadProperties(passwordsFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		if(passwords != null){
			// Load from passwords file
			IHEARTUSERNAME = passwords.getProperty("IHEART.USERNAME");
			IHEARTPASSWORD = passwords.getProperty("IHEART.PASSWORD");
			IHEARTUSERNAMEODFREE = passwords.getProperty("IHEART.USERNAMEOD.FREE");
			IHEARTPASSWORDODFREE = passwords.getProperty("IHEART.PASSWORDOD.FREE");
			IHEARTUSERNAMEODPLUS = passwords.getProperty("IHEART.USERNAMEOD.PLUS");
			IHEARTPASSWORDODPLUS = passwords.getProperty("IHEART.PASSWORDOD.PLUS");
			IHEARTUSERNAMEODPREMIUM = passwords.getProperty("IHEART.USERNAMEOD.PREMIUM");
			IHEARTPASSWORDODPREMIUM = passwords.getProperty("IHEART.PASSWORDOD.PREMIUM");
			FACEBOOKUSERNAME = passwords.getProperty("FACEBOOK.USERNAME");
			FACEBOOKPASSWORD = passwords.getProperty("FACEBOOK.PASSWORD");
			NEWACCOUNTPASSWORD = passwords.getProperty("NEWACCOUNTPASSWORD");
		}
		else{
			// Load from system properties (Jenkins automation uses this method)
			IHEARTUSERNAME = System.getProperty("IHEART.USERNAME");
			IHEARTPASSWORD = System.getProperty("IHEART.PASSWORD");
			IHEARTUSERNAMEODFREE = System.getProperty("IHEART.USERNAMEOD.FREE");
			IHEARTPASSWORDODFREE = System.getProperty("IHEART.PASSWORDOD.FREE");
			IHEARTUSERNAMEODPLUS = System.getProperty("IHEART.USERNAMEOD.PLUS");
			IHEARTPASSWORDODPLUS = System.getProperty("IHEART.PASSWORDOD.PLUS");
			IHEARTUSERNAMEODPREMIUM = System.getProperty("IHEART.USERNAMEOD.PREMIUM");
			IHEARTPASSWORDODPREMIUM = System.getProperty("IHEART.PASSWORDOD.PREMIUM");
			FACEBOOKUSERNAME = System.getProperty("FACEBOOK.USERNAME");
			FACEBOOKPASSWORD = System.getProperty("FACEBOOK.PASSWORD");
			NEWACCOUNTPASSWORD = System.getProperty("NEWACCOUNTPASSWORD");
		}

        try{
        	System.out.println("Attempting to start driver...");
        	driver = new AndroidDriver<MobileElement>(appiumUrl, capabilities);
        }
        catch(Exception exc){
        	System.err.println("Could not start driver. Emulator or device may be unavailable. Appium may have disconnected or stopped. Sleeping a few seconds to retry (will retry twice).\n"
        			+ "Properties:\n"
        			+ "Device name: " + device + "\n"
        			+ "Platform version: " + platformVersion+ "\n"
        			+ "IPA/App file name: " + appPath + "\n"
        			+ "Using Emulator: " + useEmulator + "\n"
        			+ "Appium URL: " + appiumUrl + "\n"
        			+ "Appium port: " + port + "\n"
        			+ "Model name: " + model + "\n"
        			);
        	System.out.println("\n\nOriginal Error:");
        	exc.printStackTrace();
        	
        	int maxRetries = 7;
        	int secondDelay = 30;
        	System.out.println("\n\n");
        	// TODO See if we can get this to reboot Appium, to resolve potential issues
        	for (int retry = 0; retry < maxRetries; retry++){
        		// Delay for X seconds before retrying
	        	for(int i = secondDelay; i > 0; i--){
	        		if (i % 5 == 0){
	        			System.err.println("Retrying in: " + i + "...");
	        		}
	        		sleep(1000);
	        	}
	        	
	        	// Attempt to restart the driver
	        	System.err.println("RETRYING INITIALIZATION (" + (retry + 1) + " tries so far).");
	        	try{
	        		driver = new AndroidDriver<MobileElement>(appiumUrl, capabilities);
	        	}
	        	catch(Exception e2){
        			driver = null;
	        	}
	        	
	        	if (driver != null){
	        		break;
	        	}
	        	// Restart ADB (it's now failed twice), try again
        		System.out.println("\n\nAttempting to restart ADB, "
        				+ "in case this is the problem. Make sure a device is attached!\n");
        		// Get ADB
        		String adb = CommandExecutor.executeCommand("which adb", false);
        		if (!strGood(adb)){
        			// Use a default location
        			adb = "/usr/local/bin/adb";
        		}
        		
        		// Execute the commands to restart adb
    			System.out.println("Using adb from: " + adb);
    			System.out.println("Shutting down ADB.\n" 
    					+ CommandExecutor.executeCommand(adb + " kill-server"));
    			System.out.println("Restarting ADB and listing attached devices:\n"
    					+ CommandExecutor.executeCommand(adb + " devices"));
        	}
        	
        	if (driver == null){
        		System.err.println("\n\n\nFAILED TO START DRIVER. QUITTING.");
        		return false;
        	}
        }
        
        // Set this for quicker access in other methods
     	ISMARSHMALLOW = isMarshmallow();
        
        // We have our own timeouts, don't need this as much, reduced it
		driver.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
		
		APPIUM_VERSION = getAppiumVersion(driver);
		
		if (driver != null)
			System.out.println("Driver initialized, starting test.");
		
		return driver != null;
	}
	
	/**
	 * Get the appium version from the server
	 * Some versions of appium have bugs, but we can't upgrade due to bugs in new versions
	 * This allows the tests to adapt to older versions of Appium
	 * @param d
	 * @return
	 */
	public static String getAppiumVersion(AndroidDriver<MobileElement> d){
		String appiumVersion = "";
		BufferedReader output = null;
		HttpURLConnection http = null;
		// Get the Appium version
		try{
			URL statusUrl = new URL("http://" + address + ":" + port + "/wd/hub/status");
			http = (HttpURLConnection) statusUrl.openConnection();
			if(http != null && http.getResponseCode() == HttpURLConnection.HTTP_OK){ // We have a connection and 200 response code
				output = new BufferedReader(new InputStreamReader(http.getInputStream()));
				StringBuffer out = new StringBuffer();
				String line;
				while ((line = output.readLine()) != null){
					out.append(line);
				}
				if (out != null && out.length() > 0){
					JSONObject response = new JSONObject(out.toString());
					appiumVersion = response.getJSONObject("value").getJSONObject("build").getString("version");
				}
				output.close();
			}
		}
		catch(Exception e){
			System.err.println("\nCOULD NOT READ APPIUM VERSION:");
			e.printStackTrace();
		}
		finally{
			// Ensure we are disconnected so driver may connect
			if(output != null){
				try {
					output.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(http != null){
				http.disconnect();
			}
		}
		
		return appiumVersion;
	}
	
	public static void quit(){
		if(driver != null){
			// Quit the driver
			try{
				sleep(2000); // Allow commands to terminate naturally
				driver.quit();
			}
			catch(Exception e){
				System.err.println("ERROR SHUTTING DOWN DRIVER");
				e.printStackTrace();
			}
		}
		// Sleeping to ensure Appium has time to reset
		// Actually a bit faster in local testing of long suites due to fewer crashes
		sleep(7000);
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
	
	public static boolean isMarshmallow(){
		try{
			String platformFromAppium = (String) driver.getCapabilities().getCapability("platformVersion");
			if(strGood(platformFromAppium) && !platformFromAppium.equals(platformVersion)){
				// Correct the platform version we passed in
				platformVersion = platformFromAppium;
				System.out.println("Corrected passed in driver capabilities for platform version, now equals: " + platformVersion);
			}
		}
		catch(Exception e){
			
		}
		// Load the platform version from the passed in vars (may be incorrect)
		int version = 0;
		try{
			version = Integer.parseInt(platformVersion.substring(0, 1));
		}
		catch(Exception e){
			System.out.println("COULD NOT GET PLATFORM VERSION!");
		}
		
		if(version >= 6){
			return true;
		}
		
		return false;
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
	
	public static List<MobileElement> findElements(AndroidDriver<MobileElement> d, By by){
		List<MobileElement> foundElements = null;
		try{
			d.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			foundElements = d.findElements(by);
		}
		catch(Exception e){
			System.out.println("Could not find elements");
		}
		finally{
			d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
		}
		
		return foundElements;
	}
	
	/**
	 * Get the child element of an element by string (ID or Xpath)
	 * @param ele
	 * @param findBy
	 * @return
	 */
	public static AndroidElement findChildElement(AndroidDriver<MobileElement> d, AndroidElement ele, String findBy){
		if(findBy.startsWith("/") || findBy.startsWith("./")){
			findBy = Xpath.subXpath(findBy);
			return findChildElement(d, ele, By.xpath(findBy));
		}
		return findChildElement(d, ele, By.id(findBy));
	}
	
	/**
	 * Find the child element of an element with a By
	 * @param ele
	 * @param by
	 * @return
	 */
	public static AndroidElement findChildElement(AndroidDriver<MobileElement> d, AndroidElement ele, By by){
		AndroidElement child = null;
		
		if (isVisible(ele)){
			try{
				d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
				child = (AndroidElement) ele.findElement(by);
			}
			catch(Exception e){
			}
			finally{
				d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
			}
		}
		
		return child;
	}
	
	/**
	 * Find the child element of an element with a By
	 * @param ele
	 * @param by
	 * @return
	 */
	public static List<MobileElement> findChildElements(AndroidDriver<MobileElement> d, AndroidElement ele, By by){
		List<MobileElement> children = new ArrayList<MobileElement>();
		
		if (isVisible(ele)){
			try{
				d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
				children.addAll(ele.findElements(by));
			}
			catch(Exception e){
			}
			finally{
				d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
			}
		}
		
		return children;
	}
	
	public static AndroidElement scrollUntil(AndroidDriver<MobileElement> d, int direction, By by){
		AndroidElement foundElement = waitForVisible(d, by, 1);
		int maxSwipes = 10;
		int count = 0;
		// Loop until it's found
		while (count < maxSwipes && !isVisible(foundElement)){
			count++;
			swipeOnScreen(d, direction, 750);
			foundElement = (AndroidElement) findElement(d, by);
		}
		
		return foundElement;
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
			try{
				AndroidElement testElement = (AndroidElement) findElement(d, by);
				if(testElement != null && testElement.isDisplayed()){
					break;
				}
			}
			catch(Exception e){}
			sleep(50); // Intentionally mismatched to make up for time searching for element
			timeLeftMil -= 200;
		}
		
		timeoutInSec = (timeLeftMil / 1000);
		if(timeLeftMil >= 500){
			timeoutInSec = 1;
		}
	
		AndroidElement returnElement = (AndroidElement) findElement(d, by);
		
		return returnElement;
	}
	
	public static boolean waitForNotVisible(AndroidDriver<MobileElement> d, By by, int maxWaitTimeSeconds){
		boolean elementGone = false;
		for(int i = 0; i < maxWaitTimeSeconds; i++){
			try{
				MobileElement element = waitForVisible(d, by, 1);
				if(!isVisible(element)){
					elementGone = true;
					break;
				}
			}catch(Exception e){}
		}
		return elementGone;
	}
	
	public static boolean waitForNotVisible(AndroidDriver<MobileElement> d, MobileElement ele, int maxWaitTimeSeconds){
		boolean elementGone = false;
		for(int i = 0; i < maxWaitTimeSeconds; i++){
			try{
				if(!isVisible(ele)){
					elementGone = true;
					break;
				}
			}catch(Exception e){}
			sleep(1000);
		}
		return elementGone;
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
		if(e != null)
			return e.isDisplayed();
		return false;
	}
	public static boolean isEnabled(MobileElement e){
		if(e != null)
			return e.isEnabled();
		return false; 
	}
	
	public static void hideKey(AndroidDriver<MobileElement> d) {
		try{
			d.hideKeyboard();
		}
		catch(Exception e){
			// We want to be able to use this even if the keyboard is not visible.
			// No error or exception will be reported if this is the case. 
		}
	}
	
	public static Errors sendKeys(AndroidDriver<MobileElement> d, AndroidElement element, String text, boolean clear){
		Errors err = new Errors();
		if (isVisible(element)) {
//			Appium's clear is currently broken.			
//			if (clear) {
//				element.clear();
//			}
			element.sendKeys(text);
			hideKey(d);
		}
		else {
			err.add(d, String.format("Unable to send text: %s", text));
		}
		return err;
	}
	
	public static Errors click(AndroidDriver<MobileElement> d, MobileElement element, String errorMessage) {
		return click(d, element, errorMessage, "");
	}
	
	public static Errors click(AndroidDriver<MobileElement> d, MobileElement element, String errorMessage, String methodName) {
		Errors errors = new Errors();
		if (isVisible(element)) {
			element.click();
		}
		else {
			errors.add(d, errorMessage, methodName);
		}
		
		return errors;
	}
	
	public static void click(AndroidDriver<MobileElement> d, By by){
		if (d != null && by != null) {
			click(d, d.findElement(by));
		}
	}
	public static void click(AndroidDriver<MobileElement> d, MobileElement e){
		if (d != null && e != null) {	
			d.tap(1, e, 150);
		}
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
		try{
			int clickX = e.getSize().getWidth() / 4 + e.getLocation().getX(); //3/4 in
			d.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			d.tap(1, clickX, e.getLocation().getY(), 5);
			d.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
		}
		catch(Exception ex){
			System.out.println("Quicktap failed on element");
			if(e == null){
				System.out.println("Element passed in was null");
			}
			ex.printStackTrace();
		}
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
	public static boolean waitToClick(AndroidDriver<MobileElement> d, By by, int duration){
		MobileElement e = waitForVisible(d, by, duration);
		if(e != null){
			click(d, e);
			return true;
		}
		return false;
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
	 * Swipes in the middle of the screen. Useful for scrolling or panning
	 * @param d
	 * @param direction
	 * @param duration
	 */
	public static void swipeOnScreen(AndroidDriver<MobileElement> d, int direction, int duration){
		int startX = getAppWidth(d) / 2;
		int startY = getAppHeight(d) / 2;
		int endX = 0;
		int endY = 0;
		
		switch(direction){
		case 0:
			// Up
			endX = startX;
			endY = getAppHeight(d) / 4;
			break;
		case 1:
			// Right
			endX = (getAppWidth(d) / 4) * 3;
			endY = startY;
			break;
		case 2:
			// Down
			endX = startX;
			endY = (getAppHeight(d) / 4) * 3;
			break;
		case 3:
			endX = getAppWidth(d) / 4;
			endY = startY;
			break;
		default:
			System.err.println("0-3 for swipe direction.");
			break;
		}
		
		d.swipe(startX, startY, endX, endY, duration);
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
		MobileElement e = waitForVisible(d, by, 3);
		if(e != null){
			e.sendKeys(type);
		}
	}
	
	public static void clearText(AndroidDriver<MobileElement> d, MobileElement ele){
		if (isVisible(ele)){
			ele.tap(1, 100);
			ele.clear();
		}
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
	
	public static String getText (MobileElement element) {
		if (isVisible(element)) {
			return element.getText();
		}
		return "";
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
	 * Estimation, if it's +/- 2, it's "equal"
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

	/**
	 * Tests for podcast date format
	 * @param s
	 * @return
	 */
	public static boolean isDate(String s){
		if (s.startsWith("Jan") || s.startsWith("Feb") ||
				s.startsWith("Mar") || s.startsWith("Apr") ||
				s.startsWith("May") || s.startsWith("Jun") ||
				s.startsWith("Jul") || s.startsWith("Aug") ||
				s.startsWith("Sep") || s.startsWith("Oct") ||
				s.startsWith("Nov") || s.startsWith("Dec") ){
			return true;
		}
		return false;
	}
	
	public class RetryRule implements TestRule{
		int retries = 1; // default
		public RetryRule(){
			this.retries = 1;
		}
		public RetryRule(int r){
			this.retries = r;
		}
		
		@Override
		public Statement apply(Statement base, Description description) {
			return statement(base, description);
		}
		
		private Statement statement(final Statement base, final Description description){
			return new Statement(){
				@Override
				public void evaluate() throws Throwable {
					// Save the exception for the end of the retries
					Throwable caughtThrowable = null;
					
					for (int i = 0; i < retries; i++){
						try{
							base.evaluate();
							return; // End
						}
						catch (Throwable t){
							System.out.println(t);
							caughtThrowable = t;
							// Only retry if it was a driver issue, not an assertion
							if(!(t instanceof java.lang.AssertionError)
									&& t.getMessage().contains("session is either terminated or not started")){
								System.err.println("\n\nRun #" + (i + 1) + " failed, may retry.");
							}
							else{
								throw t;
							}
						}
					}
					if (caughtThrowable != null){
						throw caughtThrowable;
					}
				}
			};
		}
	}
	
	/**
	 * Screenshot Rule
	 * Runs after every test. 
	 * If the test passed, this runs the usual shutdown method
	 * If it failed, it takes a screenshot, then runs the usual shutdown method
	 * This replaces @after with the following:
	 * @Rule
	 * public ScreenshotRule screenshot = new ScreenshotRule();
	 * 
	 */
	public class ScreenshotRule implements MethodRule{
		
		@Override
		/**
		 * Tests run within this. Once it ends, it takes a screenshot for failures, but ends regardless
		 */
		public Statement apply(final Statement statement, final FrameworkMethod method, Object target) {
			return new Statement() {
				@Override
				/**
				 * Run the test, if it fails, catch the failure (an exception/throwable)
				 * 		and take a screenshot, before allowing the failure to continue.
				 * Then, fail the test. 		
				 */
				public void evaluate() throws Throwable {
					try{
						statement.evaluate();
					}
					catch(Throwable t){
						// Catch the failure, take the screenshot, then pass the failure on
						if(driver != null){
							String errorMethod = "assertFrom_" + method.getName();
							Errors.captureScreenshot(driver, errorMethod);
						}
						// Make sure we quit
						quit();
						// Throw the original error
						throw t;
					}
					finally{
						// Quit even if it did not fail
						quit();
					}
				}
			};
		}
	}
}

