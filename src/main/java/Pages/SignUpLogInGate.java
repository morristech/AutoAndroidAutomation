package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class SignUpLogInGate extends Page {

	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String signUpButtonId = Page.connectId + "signup_btn";
	private static String logInButtonId = Page.connectId + "login_btn";
	private static String maybeLaterButtonId = Page.connectId + "maybe_later_btn";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getSignUpButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(signUpButtonId), 3);
	}
	
	public static AndroidElement getLogInButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(logInButtonId), 3);
	}
	
	public static AndroidElement getMaybeLaterButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(maybeLaterButtonId), 3);
	}
	
	/********************/
	/* *** Behavior *** */
	/********************/
	
	public static Errors tapSignUpButton (AndroidDriver<MobileElement> d) {
		return click(d, getSignUpButton(d), "Cannot tap sign up button!", "tapSignUpButton");
	}
	
	public static Errors tapLogInButton (AndroidDriver<MobileElement> d) {
		return click(d, getLogInButton(d), "Cannot tap log in button!", "tapLogInButton");
	}
	
	public static Errors tapMaybeLaterButton (AndroidDriver<MobileElement> d) {
		return click(d, getMaybeLaterButton(d), "Cannot tap maybe later button!", "tapMaybeLaterButton");
	}
	
}
