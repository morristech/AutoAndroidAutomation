package Pages;

import org.openqa.selenium.By;

import Pages.SignUp.Gender;
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
	
	private static String acceptButtonId = Page.connectId + "button_red_top";
	private static String denyButtonId = Page.connectId + "button_white_top";

	private static String cardItemId = Page.connectId + "card_%d_%d";
	
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
	

	public static AndroidElement getCardItem (AndroidDriver<MobileElement> d, int index1, int index2) {
		String id = String.format(cardItemId, index1, index2);
		return waitForVisible(d, By.id(id), 3);

    
	public static AndroidElement getAcceptButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(acceptButtonId), 3);
	}
	
	public static AndroidElement getDenyButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(denyButtonId), 3);
	}
	
	/********************/
	/* *** Behavior *** */
	/********************/ 
	
	public static Errors enterEmail (AndroidDriver<MobileElement> d, String email) {
		return sendKeys(d, getEmailEditText(d), email, true);
	}
	
	public static Errors enterPassword (AndroidDriver<MobileElement> d, String password) {
		return sendKeys(d, getPasswordEditText(d), password, true);
	}
	
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
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static Errors signUp (AndroidDriver<MobileElement> d) {
		String email = Pages.SignUp.generateEmailAddress();
		return signUp(d, email, TestRoot.NEWACCOUNTPASSWORD, "1995", "11013", Pages.SignUp.Gender.MALE);
	}
	
	public static Errors signUp (AndroidDriver<MobileElement> d, String email, String password, String year, String zipCode, Gender gender) {
		Errors err = new Errors();
		if (isVisible(Pages.SignUpLogInGate.getSignUpButton(d))) {
			err.add(d, Pages.SignUpLogInGate.tapSignUpButton(d));
		}
		err.add(d, Pages.SignUp.enterEmail(d, email));
		err.add(d, Pages.SignUp.enterEmailConfirmation(d, email));
		err.add(d, Pages.SignUp.tapNextButton(d));
		err.add(d, Pages.SignUp.enterPassword(d, password));
		err.add(d, Pages.SignUp.enterBirthYear(d, year));
		err.add(d, Pages.SignUp.enterZipCode(d, zipCode));
		err.add(d, Pages.SignUp.checkGender(d, gender));
		err.add(d, Pages.SignUp.checkAgree(d));
		err.add(d, Pages.SignUp.tapSignUpButton(d));
		err.add(d, Pages.GenrePicker.selectFirstGenreItemAndContinue(d));
		err.add(d, Pages.ConnectionGate.byPassAndAcceptDisclaimer(d));
		return err;
	}
	
	public static Errors logIn (AndroidDriver<MobileElement> d) {
		return logIn(d, TestRoot.IHEARTUSERNAME, TestRoot.IHEARTPASSWORD);
	}
	
	public static Errors logIn (AndroidDriver<MobileElement> d, String email, String password) {
		Errors err = new Errors();
		if (isVisible(Pages.SignUpLogInGate.getLogInButton(d))) {
			err.add(d, Pages.SignUpLogInGate.tapLogInButton(d));
		}
		err.add(d, Page.enterEmail(d, email));
		err.add(d, Page.enterPassword(d, password));
		err.add(d, Pages.LogIn.tapLogInButton(d));
		err.add(d, Pages.GenrePicker.selectFirstGenreItemAndContinue(d));
		err.add(d, Pages.ConnectionGate.byPassAndAcceptDisclaimer(d));
		return err;
	}

}
