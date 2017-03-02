package Pages;

import org.openqa.selenium.By;

import Pages.SignUp.Gender;
import Utilities.Errors;
import Utilities.TestRoot;
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
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static Errors signUp (AndroidDriver<MobileElement> d) {
		String email = Pages.SignUp.generateEmailAddress();
		return signUp(d, email, TestRoot.NEWACCOUNTPASSWORD, "1995", "11013", Pages.SignUp.Gender.MALE);
	}
	
	public static Errors signUp (AndroidDriver<MobileElement> d, String email, String password, String year, String zipCode, Gender gender) {
		Errors err = new Errors();
		err.add(d, tapSignUpButton(d));
		err.add(d, Pages.SignUp.signUp(d));
		return err;
	}
	
	public static Errors logIn (AndroidDriver<MobileElement> d) {
		return logIn(d, TestRoot.IHEARTUSERNAME, TestRoot.IHEARTPASSWORD);
	}
	
	public static Errors logIn (AndroidDriver<MobileElement> d, String email, String password) {
		Errors err = new Errors();
		err.add(d, tapLogInButton(d));
		err.add(d, Pages.LogIn.logIn(d, email, password));
		return err;
	}
}
