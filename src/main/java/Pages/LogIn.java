package Pages;

import Utilities.Errors;
import Utilities.TestRoot;
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
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static Errors logIn (AndroidDriver<MobileElement> d) {
		return logIn(d, TestRoot.IHEARTUSERNAME, TestRoot.IHEARTPASSWORD);
	}
	
	public static Errors logIn (AndroidDriver<MobileElement> d, String email, String password) {
		Errors err = new Errors();
		err.add(d, Page.enterEmail(d, email));
		err.add(d, Page.enterPassword(d, password));
		tapLogInButton(d);
		return err;
	}
}
