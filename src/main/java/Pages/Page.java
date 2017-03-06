package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import Utilities.TestRoot;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class Page extends TestRoot {

	/********************/
	/* *** Elements *** */
	/********************/
	
	// Base IDs
	public static String connectId = "com.clearchannel.iheartradio.connect:id/";
	public static String androidId = "android:id/";
	
	private static String cancelButtonId = Page.connectId + "back_btn";
	private static String previousButtonId = Page.connectId + "prev_btn";
	private static String nextButtonId = Page.connectId + "next_btn";
	
	private static String emailEditTextId = Page.connectId + "email_text";
	private static String passwordEditTextId = Page.connectId + "password_text";

	
	/*******************/
	/* *** Getters *** */
	/*******************/

	public static AndroidElement getCancelButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(cancelButtonId), 3);
	}
	
	public static AndroidElement getPreviousButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(previousButtonId), 3);
	}
	
	public static AndroidElement getNextButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(nextButtonId), 3);
	}

	public static AndroidElement getEmailEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(emailEditTextId), 3);
	}
	
	public static AndroidElement getPasswordEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(passwordEditTextId), 3);
	}
	
	public static Errors enterEmail (AndroidDriver<MobileElement> d, String email) {
		return sendKeys(d, getEmailEditText(d), email, true);
	}
	
	public static Errors enterPassword (AndroidDriver<MobileElement> d, String password) {
		return sendKeys(d, getPasswordEditText(d), password, true);
	}
	
	/********************/
	/* *** Behavior *** */
	/********************/ 
	
	public static Errors tapCancelButton (AndroidDriver<MobileElement> d) {
		return TestRoot.click(d, getCancelButton(d), "Cannot tap cancel button!", "tapCancelButton");
	}
	
	public static Errors tapNextButton (AndroidDriver<MobileElement> d) {
		return TestRoot.click(d, getNextButton(d), "Cannot tap next button!", "tapNextButton");
	}
	
	/**
	 * Taps the hardware (or overlayed) Android system back button
	 * 
	 * @param d
	 */
	public static void back(AndroidDriver<MobileElement> d) {
		try {
			hideKey(d);
		}
		catch (Exception e) {
		}
		d.navigate().back();
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
	
	public static Errors sendKeys(AndroidDriver<MobileElement> d, AndroidElement element, String text, boolean clear){
		Errors err = new Errors();
		if (isVisible(element)) {
			if (clear) {
				element.clear();
			}
			element.sendKeys(text);
			hideKey(d);
		}
		else {
			err.add(d, String.format("Unable to send text: %s", text));
		}
		return err;
	}
}
