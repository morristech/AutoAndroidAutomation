package Pages;

import org.openqa.selenium.By;

import Pages.Page;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import testCommons.Account.Gender;
import testCommons.Errors;

public class SignUp extends Page {
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String emailConfirmEditTextId = Page.connectId + "email_confirm_text";
	private static String birthYearEditTextId = Page.connectId + "birth_year_text";
	private static String zipCodeEditTextId = Page.connectId + "zip_code_text";
	private static String femaleRadioButtonId = Page.connectId + "gender_female_radio";
	private static String maleRadioButtonId = Page.connectId + "gender_male_radio";
	private static String agreeCheckBoxId = Page.connectId + "agree_check_box";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getEmailConfirmEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(emailConfirmEditTextId), 7);
	}
	
	public static AndroidElement getBirthYearEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(birthYearEditTextId), 7);
	}
	
	public static AndroidElement getZipCodeEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(zipCodeEditTextId), 7);
	}
	
	public static AndroidElement getGenderRadioButton (AndroidDriver<MobileElement> d, Gender gender) {
		String id = (gender == Gender.MALE) ? maleRadioButtonId : femaleRadioButtonId;
		return waitForVisible(d, By.id(id), 7);
	}
	
	public static AndroidElement getAgreeCheckBox (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(agreeCheckBoxId), 7);
	}
	
	public static AndroidElement getSignUpButton (AndroidDriver<MobileElement> d) {
		return getNextButton(d);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors enterEmailConfirmation (AndroidDriver<MobileElement> d, String email) {
		return sendKeys(d, getEmailConfirmEditText(d), email, true);
	}
	
	public static Errors enterBirthYear (AndroidDriver<MobileElement> d, String year) {
		return sendKeys(d, getBirthYearEditText(d), year, true);
	}
	
	public static Errors enterZipCode (AndroidDriver<MobileElement> d, String zipCode) {
		return sendKeys(d, getZipCodeEditText(d), zipCode, true);
	}
	
	public static Errors checkGender(AndroidDriver<MobileElement> d, Gender gender) {
		return click(d, getGenderRadioButton(d, gender), "Cannot check gender radio button.", "checkGender");
	}
	
	public static Errors checkAgree (AndroidDriver<MobileElement> d) {
		return click(d, getAgreeCheckBox(d), "Cannot check the agree check box.", "checkAgree");
	}
	
	public static Errors tapSignUpButton (AndroidDriver<MobileElement> d) {
		return click(d, getNextButton(d), "Cannot tap sign up button!", "tapSignUpButton");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String generateEmailAddress () {
		String timeStamp = String.valueOf(System.currentTimeMillis());
		return timeStamp + "androidAutomationTester@mailinator.com";
	}

}
