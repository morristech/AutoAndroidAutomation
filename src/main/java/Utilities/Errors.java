package Utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

/**
 * Essentially a container for a StringBuilder, ensures the new line character is placed at the end of each new error
 * Puts a new line after each error (and before the first one for easier output)
 * Keeps track of number of failures
 * @author daniellegolinsky
 *
 */
public class Errors {
	// List of errors in the order they're added
	private List<String> err;
	
	public Errors(){
		err = new ArrayList<String>();
	}
	public Errors(AndroidDriver<MobileElement> d, String errorMessage){
		err = new ArrayList<String>();
		add(d, errorMessage);
	}
	public Errors(AndroidDriver<MobileElement> d, String errorMessage, String method){
		err = new ArrayList<String>();
		add(d, errorMessage, method);
	}
	
	/**
	 * Output errors as a list
	 * @return
	 */
	public List<String> getErrorList(){
		return err;
	}
	
	/**
	 * Output the error list as a string
	 * @return
	 */
	public String getErrors(){
		if(err.size() > 0){
			StringBuilder errorString = new StringBuilder();
			errorString.append("Number of errors: " + err.size() + " \nErrors:\n");
			for(int i = 0; i < err.size(); i++){
				errorString.append((i + 1) + ": " + err.get(i) + "\n");
			}
			
			return errorString.toString();
		}
		return "";
	}
	// Makes conversion from StringBuilder a little easier.
	public String toString(){
		return getErrors();
	}
	
	/**
	 * Returns the number of errors stored in this instance
	 */
	public int howMany(){
		return err.size();
	}
	
	public boolean contains(String s){
		return err != null && err.toString().contains(s);
	}
	
	/**
	 * Adds an error ~without a method name to help identify the screenshot~
	 * @param d
	 * @param error
	 */
	public void add(AndroidDriver<MobileElement> d, String errorMessage){
		add(d, errorMessage, "");
	}
	
	/**
	 * Takes a screenshot when adding the error with a method name to identify screenshot
	 * @param d
	 * @param errorMessage
	 * @param methodName
	 */
	public void add(AndroidDriver<MobileElement> d, String errorMessage, String methodName){
		add(d, errorMessage, methodName, true); // Take screenshot by default
	}
	
	/**
	 * Adds an error, uses the methodName for the screenshot, 
	 * @param d
	 * @param error
	 * @param methodName
	 * @param takeScreenshot
	 */
	public void add(AndroidDriver<MobileElement> d, String errorMessage, String methodName, boolean takeScreenshot){
		if(errorMessage != null && errorMessage.length() > 0){
			// Add a time stamp to the error
			String ts = timeStamp();
			errorMessage += " Timestamp: " + ts.substring(11).replace("-", ":");
			if (takeScreenshot){
				if(d != null){
					// Take a screenshot and add it's path to the error
					String screenshotPath = captureScreenshot(d, methodName);
					if(!"".equals(screenshotPath)){
						errorMessage += " - Screenshot: " + screenshotPath;
					}
				}
				else{
					System.out.println("ATTENTION!\n--Error will not have screenshot, did not receive driver.");
				}
			}
			// Append the error to the List
			err.add(errorMessage);
			announceError(errorMessage);
		}
	}
	
	/**
	 * Add all errors from an existing list of errors
	 * @param d
	 * @param newErrors
	 */
	public void add(AndroidDriver<MobileElement> d, Errors newErrors){
		if(!newErrors.noErrors()){
			err.addAll(newErrors.getErrorList());
		}
	}
	
	// Added to make converting from StringBuilder easier, but the add is likely preferred (less typing)
	public void append(AndroidDriver<MobileElement> d, String s){
		add(d, s);
	}
	
	/**
	 * Removes a string from the error list, optionally allows it to reduce the number of errors by 1
	 * @param s
	 * @param removeError
	 */
	public void remove(String s){
		if(err.size() > 0 && err.toString().contains(s)){
			// Figure out which one it is and remove it
			// This would be easier with a HashSet, but because we want errors in order for output, and removing is infrequent, we can search
			int i;
			boolean remove = false;
			for(i = 0; i < s.length(); i++){
				if(err.get(i).equals(s)){
					remove = true;
					break;
				}
			}
			if(remove)
				err.remove(i);
		}
	}
	
	public boolean noErrors(){
		return err.size() == 0;
	}
	
	/**
	 * Just a simple timestamp
	 * Format:yyyy.MM.dd.HH.mm.ss
	 * 
	 * @return
	 */
	private static String timeStamp(){
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}
	
	/**
	 * Announce new errors as they come in to help with debugging
	 * @param s
	 */
	private void announceError(String s){
		// Output the error
		System.out.println(s + "\n");
	}
	
	
	public static String captureScreenshot(AndroidDriver<MobileElement> d){
		return captureScreenshot(d, "");
	}
	/**
	 * Using the Appium Driver, this method creates a screenshot and returns its path
	 * @param d
	 * @return
	 */
	public static String captureScreenshot(AndroidDriver<MobileElement> d, String methodName){
		String ts = timeStamp();
		String filepath = ""; 
		if ("".equals(methodName)){
			filepath = TestRoot.SCREENSHOT_DIRECTORY 
				+ "/" + ts.substring(0, 10) // All the day's screenshots in one place
				+ "/" + "TestError_" + ts + ".png";
		}
		else{
			filepath = TestRoot.SCREENSHOT_DIRECTORY 
					+ "/" + ts.substring(0, 10) // All the day's screenshots in one place
					+ "/" + methodName + "-" + ts + ".png";
		}
		// Create the file and store the screenshot
		File tempScreenshot = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
		
		// Test the screenshot directory
		File sd = new File(TestRoot.SCREENSHOT_DIRECTORY);
		// Make the new directory
		if(!sd.exists() && !sd.isDirectory()){
			sd.mkdir();
			sd.setExecutable(true);
		}
		
		// Create the file that will store the screenshot after test execution ends
		File screenshotFile = new File(filepath);
		
		try {
			FileUtils.copyFile(tempScreenshot, screenshotFile);
			if(!"".equals(TestRoot.SCREENSHOT_URL)){
				System.out.println("Took screenshot, link:" + TestRoot.SCREENSHOT_URL + screenshotFile);
			}
			else{
				System.out.println("Took screenshot, stored at: " + screenshotFile);
			}
		}
		catch (IOException e) {
			System.err.println("Could not copy file destined for: " + filepath + "\n");
			e.printStackTrace();
		}
		
		// Remove the temp screenshot
		tempScreenshot.delete();
		
		// Return the filepath to this screenshot
		if (screenshotFile.exists()){
			return screenshotFile.getPath();
		}
		
		return "FILEPATH INVALID: "  + filepath;
	}
}
