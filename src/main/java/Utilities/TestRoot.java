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
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import Pages.Xpath;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testCommons.Account;
import testCommons.Account.Subscription;
import testCommons.CommandExecutor;
import testCommons.Errors;
import testCommons.LoadProperties;
import testCommons.OutputFormatter;
import testCommons.TestLogger;


/**
 * All tests will extend testroot
 * Testroot will pull info from properties, 
 * 	save the driver, and do setup/takedown, 
 * 	as all tests will perform these tasks
 * @author daniellegolinsky 
 *
 */
public class TestRoot {
	
	// Logger
	public static TestLogger testRootLog;
	
	protected static int implicitWaitTimeout = 375;
	
	private static final int PORT_SPACING = 2; // The space between port numbers
	
	protected static boolean testOD;
	
	private static List<String> deviceNames; // Just the names
	protected static List<AndroidDevice> devices; // Device name, UDID, model, package, path, address, port, etc
	private static int DEVICE_INDEX = 0; // The index of the device being used for this driver, in case we need to track or retry
	private static int MAX_DEVICES = 1; // The total number of provided devices
	public static AndroidDevice testDevice = null; // The device, once created, that will remain static throughout execution
	
	
	public static Account FREE_ACCOUNT;
	public static String IHEARTUSERNAME;
	public static String IHEARTPASSWORD;
	
	public static boolean ISMARSHMALLOW = false;
	public static String APPIUM_VERSION;
	public static String SCREENSHOT_DIRECTORY;
	public static String SCREENSHOT_URL;
	
	protected static int NUMBER_OF_THREADS = 1;
	
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
	
	private static AndroidDriver<MobileElement> loadDriver() throws Exception{
		AndroidDriver<MobileElement> dr = null;
		if (testDevice != null){
			DesiredCapabilities dc = testDevice.getCapabilities();
			OutputFormatter.printConsoleHeader("Attempting to start driver...");
			System.out.println(testDevice.toString());
			dr = testDevice.getDriver(dc);
		}
		else if (devices != null && devices.size() >= 1){
			AndroidDevice device = devices.get(DEVICE_INDEX);
			DesiredCapabilities dc = device.getCapabilities();
			OutputFormatter.printConsoleHeader("Attempting to start driver...");
			System.out.println(device.toString());
			dr = device.getDriver(dc); // Throws an exception if unsuccessful
			testDevice = device; // Set it for later
		}
		
		return dr;
	}
	
	private static boolean resetADB(){
		// Restart ADB (it's now failed twice), try again
		System.out.println("\n\nAttempting to restart ADB, "
				+ "in case this is the problem. Make sure a device is attached!\n");
		// Get ADB
		String adbDefaultLocation = "/usr/local/bin/adb";
		String adb = CommandExecutor.executeCommand(new String [] {"bash", "-c", String.format("find %s -name adb", adbDefaultLocation)}, false);
		
		if (adbDefaultLocation.equals(adb)){
			System.out.println("Using adb found in default location: " + adbDefaultLocation);
		}
		
		else {
			System.out.println(String.format("adb is not in default location: %s.\nAttempting to find adb location in $HOME.", adbDefaultLocation));
			String unparsedOutput = CommandExecutor.executeCommand(new String [] {"bash", "-c", "find $HOME -name adb"}, false);
			String [] adbPaths = unparsedOutput.split("\n");
			
			if (adbPaths.length >= 1) {
				System.out.println("Attempting first search result: " + adbPaths[0]);
				adb = adbPaths[0].trim(); // We are going with the first one that we find.
			}
			else {
				System.out.println("Cannot find adb! Quitting setup!");
				return false;
			}
		}
		
		// Execute the commands to restart adb
		System.out.println("Shutting down ADB.\n" 
				+ CommandExecutor.executeCommand(adb + " kill-server"));
		System.out.println("Restarting ADB and listing attached devices:\n"
				+ CommandExecutor.executeCommand(adb + " devices"));
		
		return true;
	}
	
	private static void countDown(int time, int interval){
		for (int i = time; i >= 0; i--){
			if (i % interval == 0){
				System.out.println(i + "...");
			}
			sleep(1000);
		}
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
		// Set up logger
		testRootLog = new TestLogger(TestRoot.class);
		testRootLog.logInfo("Beginning setup...");
				
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
		//	this makes Jenkins configurations easier, allows us to have specific and easily configured 
		//	  settings in Jenkins
		if (devices == null){ // We only want to do this if we haven't already in this suite
			String deviceList = LoadProperties.getProperties(testProperties, "APPIUM.DEVICE.NAME");
			if (strGood(deviceList)){
				String[] dvs = deviceList.split(",");
				for (String dv : dvs){
					// Create a new device object for each device listed
					// TODO Each of these should be split out eventually as well, may have to change properties files
					String address = LoadProperties.getProperties(testProperties, "APPIUM.WEBDRIVER.URL");
					String port = LoadProperties.getProperties(testProperties, "APPIUM.WEBDRIVER.PORT");
					// This could be a comma separated list as well
					String platformVersion = LoadProperties.getProperties(testProperties, "APPIUM.DEVICE.PLATFORMVERSION");
					String model = LoadProperties.getProperties(testProperties, "APPIUM.DEVICE.MODEL");
					String appPath = LoadProperties.getProperties(testProperties, "APPIUM.APP.PATH");
					String appPackage = LoadProperties.getProperties(testProperties, "APPIUM.APP.PACKAGE");
					String appActivity = LoadProperties.getProperties(testProperties, "APPIUM.APP.ACTIVITY");
					boolean useEmulator = LoadProperties.getBoolean(testProperties, "APPIUM.USEEMULATOR");
					testOD = Boolean.parseBoolean(LoadProperties.getProperties(testProperties, "APPIUM.TESTOD"));
					
					// Get the correct platform version
					if (strGood(platformVersion) && platformVersion.contains(",")){
						String[] pvs = platformVersion.split(",");
						int pvsIndex = 0;
						if (devices != null){
							pvsIndex = devices.size();
							if (pvs.length <= pvsIndex){ // More devices than versions
								pvsIndex = 0; // Just go with the first and report the problem with the properties
								System.err.println("WARNING: More devices listed than device versions, defaulting to the first version provided.");
							}
						}
						platformVersion = pvs[pvsIndex];
					}
					
					AndroidDevice ad = new AndroidDevice(address, port, dv.trim(), platformVersion, 
							model, appPath, appPackage, appActivity, testOD, useEmulator);
					if (devices == null){
						devices = new ArrayList<AndroidDevice>();
						deviceNames = new ArrayList<String>();
					}
					else{
						// Increment the port by the length of the list * port interval 
						int portIncrement = devices.size() * PORT_SPACING;
						try{
							port = String.valueOf(Integer.parseInt(port) + portIncrement);
						}
						catch(Exception e){
						}
						ad.setPort(port);
						MAX_DEVICES++;
					}
					
					devices.add(ad);
					deviceNames.add(ad.getDevice());
				}
			}
		}
		
		// Set the screenshot directory
		SCREENSHOT_DIRECTORY = LoadProperties.getProperties(testProperties, "OPTIONS.SCREENSHOT.DIRECTORY");
		if(!strGood(SCREENSHOT_DIRECTORY)){
			SCREENSHOT_DIRECTORY = "screenshots/";
		}
		SCREENSHOT_URL = LoadProperties.getProperties(testProperties, "OPTIONS.SCREENSHOT.URL");
		if(!strGood(SCREENSHOT_URL)){
			SCREENSHOT_URL = "";
		}

		try{
			NUMBER_OF_THREADS = Integer.parseInt(LoadProperties.getProperties(testProperties, "OPTIONS.NUMBER_OF_THREADS"));
		}
		catch(Exception e){
			System.out.println("OPTIONS.NUMBER_OF_THREADS not found or poorly formatted (should be int). Defaulting to 1");
			NUMBER_OF_THREADS = 1;
		}
		
		// Set the screenshot directory and URL for all instances of the Errors class
		Errors.setScreenshotDirectory(SCREENSHOT_DIRECTORY);
		Errors.setScreenshotURL(SCREENSHOT_URL);
        
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
		}
		else{
			// Load from system properties (Jenkins automation uses this method)
			IHEARTUSERNAME = System.getProperty("IHEART.USERNAME");
			IHEARTPASSWORD = System.getProperty("IHEART.PASSWORD");
		}
		
		FREE_ACCOUNT = new Account (IHEARTUSERNAME, IHEARTPASSWORD, Subscription.FREE);

		/* ** Try to start the driver for the FIRST time ** */
		try{
			driver = loadDriver();
		}
		catch(Exception exc){
			// Output some failure information before retrying to allow someone to quickly fix a hardware problem
			testRootLog.logError("Driver failed to load, will try other devices and retry.");
			StringBuilder errorMessage = new StringBuilder();
			errorMessage.append("\n\nCOULD NOT START DRIVER\n\n");
			errorMessage.append("Attempted on one of these devices:\n");
			for (AndroidDevice dev : devices){
				errorMessage.append(dev.toString() + "\n");
			}
			System.err.println(errorMessage.toString());
			System.out.println("\n\nOriginal Error:");
			exc.printStackTrace();

			// Make sure ADB is working by restarting it
			// If we can't locate/reset ADB, quit, it's not installed or running
			if (!resetADB()){
				return false;
			}
			
			// Now retry for each device X times
			int maxRetries = 15;
			int secondDelay = 5;
			for (int i = 0; i < maxRetries; i++){
				// If we're multithreading, we may just need to advance the port number
				if (testDevice == null && devices != null){
					/* ** Try to set up a driver on another port/device ** */
					DEVICE_INDEX++;
					
					while (driver == null
							&& DEVICE_INDEX < MAX_DEVICES 
							&& DEVICE_INDEX < NUMBER_OF_THREADS){
						
						// Retry initialization with the next device on the list
						try{
							driver = loadDriver();
						}
						catch(Exception setupEx){
							System.out.println("Failed to set up driver on alternate device, trying other devices, if available.");
							DEVICE_INDEX++;
						}
					}
				}
				
				// Only keep doing this if the driver is still null
				// No delays between these retries to help prevent potential conflicts
				if (driver == null){
					// Reset the device index and start from 0 for the next loop
					// Only reset it if we haven't stored a device yet
					if (testDevice == null){
						DEVICE_INDEX = 0;
					}
					// TODO See if we can get this to reboot Appium, to resolve potential issues
					// Try again on the original device
					try{
						driver = loadDriver();
					}
					catch(Exception e){
						driver = null;
						System.out.println("\nFailed to start driver.\nRetrying in " + secondDelay + " seconds...");
						countDown(secondDelay, 1);
						
						// Reset the test device and index, because it might not be available anymore
						testDevice = null;
						DEVICE_INDEX = 0;
					}
					// If the driver is set up, quit trying
					if (driver != null){
						break;
					}
				}
			}
		}
		
		// If none of the retries worked, quit
		if (driver == null){
			testRootLog.logError("FAILED TO START DRIVER. QUITTING.");
			return false;
		}
		
		APPIUM_VERSION = getAppiumVersion(driver);
		
		if (driver != null){
			OutputFormatter.printConsoleHeader("Driver initialized, starting test.");
			testRootLog.logAction("App installed, driver initialized, starting test");
		}
		
		// Set this for quicker access in other methods
	 	ISMARSHMALLOW = isMarshmallow(devices.get(DEVICE_INDEX));
		
		// We have our own timeouts, don't need this as much, reduced it
		driver.manage().timeouts().implicitlyWait(implicitWaitTimeout, TimeUnit.MILLISECONDS);
		
		return driver != null;
	}
	
	protected static String getDevices(){
		String retDevs = "";
		if (devices != null && devices.size() > 0){
			retDevs = devices.stream()
					.map(Object::toString)
					.collect(Collectors.joining(", "));
		}
		return retDevs;
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
			URL statusUrl = new URL("http://" + devices.get(DEVICE_INDEX).getUrl() + ":" + devices.get(DEVICE_INDEX).getPort() + "/wd/hub/status");
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
				driver = null;
			}
			catch(Exception e){
				System.err.println("ERROR SHUTTING DOWN DRIVER");
				e.printStackTrace();
				driver = null;
			}
		}
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
	
	public static boolean isMarshmallow(AndroidDevice device){
		return device.isMarshmallow();
	}

	public static AndroidElement getListItem(AndroidDriver<MobileElement> d, By listItem, int index){
		List<MobileElement> list = findElements(d, listItem);
		if (list.size() > index){
			return (AndroidElement) list.get(index);
		}
		return null;
	}
	
	public static MobileElement findElement(AndroidDriver<MobileElement> d, By by){
		MobileElement e = null;
		try{
			d.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
			e = d.findElement(by);
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
		catch(Exception e){}
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
	
	public static AndroidElement findByFiltering(AndroidDriver<MobileElement> d, String id, String filterString) {
		return (AndroidElement) findElements(d, By.id(id)).stream()
		                                                  .filter(i -> filterString.equalsIgnoreCase(getText(d, i)))
		                                                  .findFirst()
		                                                  .orElse(null);
	}
	
	public static AndroidElement scrollUntil(AndroidDriver<MobileElement> d, int direction, By by){
		AndroidElement foundElement = waitForVisible(d, by, 3);
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
	
	public static boolean waitForElementToBeVisible(AndroidDriver<MobileElement> d, MobileElement ele, int timeoutInSec){
		boolean found = false;
		
		if (!isVisible(ele)){
			long timeLeftMil = timeoutInSec * 1000;
			while (timeLeftMil > 0){
				long beforeSearch = System.currentTimeMillis();
				try {
					found = ele.isDisplayed();
				}
				catch (Exception e) {
				}
				long afterSearch = System.currentTimeMillis();
				long duration = afterSearch - beforeSearch;
				// Always wait at least 100 ms between checks
				if (duration < 100){
					sleep (100);
					timeLeftMil -= 100;
				}
				else {
					timeLeftMil -= duration;
				}
			}
		}
		else{
			found = true;
		}
		
		return found;
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
			if (clear) {
				//clearText(d, element);
				//hideKey(d);
			}
			type(d, element, text);
			hideKey(d);
		}
		else {
			if (waitForElementToBeVisible(d, element, 2)){
				type(d, element, text);
				hideKey(d);
			}
			else{
				err.add(d, String.format("Unable to send text: %s", text));
			}
		}
		return err;
	}
	
	/**
	 * Used to close a popup with no regard for retrying
	 * Simple, clicks, does nothing else
	 * @param ele
	 * @return
	 */
	public static boolean simpleClick(AndroidElement ele){
		boolean tapped = false;
		
		if (ele != null) {
			try{
				ele.click();
				tapped = true;
			}
			catch(Exception e){
			}
		}
		
		return tapped;
	}
	
	public static Errors click(AndroidDriver<MobileElement> d, MobileElement element, String errorMessage, String methodName) {
		Errors errors = new Errors();
		if (isVisible(element)) {
			element.click();
			testRootLog.logAction("Clicked", element);
		}
		else {
			testRootLog.logAction("Tried to dismiss any popups...");
			if (waitForElementToBeVisible(d, element, 7)){
				element.click();
				testRootLog.logAction("Clicked", element);
			}
			else {
				errors.add(d, errorMessage, methodName);
			}
		}
		
		return errors;
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
		MobileElement foundElement = waitForVisible(d, by, duration);
		type(d, foundElement, type);
	}
	public static void type(AndroidDriver<MobileElement> d, String element, String type){
		MobileElement foundElement = waitForVisible(d, find(element), 1);
		type(d, foundElement, type);
	}
	public static void type(AndroidDriver<MobileElement> d, By by, String type){
		MobileElement e = waitForVisible(d, by, 3);
		type(d, e, type);
	}
	
	public static void type(AndroidDriver<MobileElement> d, MobileElement ele, String type){
		if (isVisible(ele)){
			ele.sendKeys(type);
			testRootLog.logAction("Typed: " + type, ele);
		}
		else{
			if (waitForElementToBeVisible(d, ele, 2)){
				ele.sendKeys(type);
				testRootLog.logAction("Typed: " + type, ele);
			}
		}
	}
	
	// Method is not reliable. It seems to work perfectly fine in emulators but not on physical devices.
	public static void clearText(AndroidDriver<MobileElement> d, MobileElement ele){
		if (isVisible(ele)){
			simpleClick((AndroidElement) ele);
			try{
				ele.clear();
				testRootLog.logAction("Cleared text", ele);
			}
			catch(Exception ex){
			}
		}
		else{
			if (waitForElementToBeVisible(d, ele, 2)){
				simpleClick((AndroidElement) ele);
				try{
					ele.clear();
				}
				catch(Exception ex){
				}
			}
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
		if (!isVisible(e)){
			e = TestRoot.waitForVisible(d, by, 2);
		}
		if(e == null){
			return null;
		}
		else{
			return e.getText();
		}
	}
	
	public static String getText (AndroidDriver<MobileElement> d, MobileElement element) {
		String text = "";
		if (isVisible(element)) {
			text = element.getText();
		}
		else {
			if (waitForElementToBeVisible(d, element, 2)){
				text = element.getText();
			}
		}
		return text;
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

}

