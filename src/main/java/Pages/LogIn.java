package Pages;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class LogIn extends Page {


	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getLogInButton (AndroidDriver<MobileElement> d) {
		return getNextButton(d);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapLogInButton (AndroidDriver<MobileElement> d) {
		return click(d, getLogInButton(d), "Cannot tap log in button!", "tapLogInButton");
	}
	
}
