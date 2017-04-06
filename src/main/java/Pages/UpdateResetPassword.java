package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class UpdateResetPassword extends OptionsSubpage {

	/********************/
	/* *** Elements *** */
	/********************/
	
	// Update Password Ids
	private static String currentPasswordEditTextId = Page.connectId + "current_password";
	private static String newPasswordEditTextId = Page.connectId + "new_password";
	private static String confirmNewPasswordEditTextId = Page.connectId + "confirm_new_password";
	private static String updatePasswordButtonId = Page.connectId + "update_password_button";
	private static String forgotPasswordLinkId = Page.connectId + "forgot_password_link";
	
	// Reset Password Ids
	private static String emailEditTextId = Page.connectId + "edit_text";
	private static String resetPasswordButtonId = Page.connectId + "reset_password_button";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	// Update Password Getters
	public static AndroidElement getCurrentPasswordEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(currentPasswordEditTextId), 7);
	}
	
	public static AndroidElement getNewPasswordEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(newPasswordEditTextId), 7);
	}
	
	public static AndroidElement getConfirmNewPasswordEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(confirmNewPasswordEditTextId), 7);
	}
	
	public static AndroidElement getUpdatePasswordButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(updatePasswordButtonId), 7);
	}
	
	public static AndroidElement getForgotPasswordLink (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(forgotPasswordLinkId), 7);
	}
	
	
	// Reset Password Getters
	public static AndroidElement getEmailEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(emailEditTextId), 7);
	}
	
	public static AndroidElement getResetPasswordButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(resetPasswordButtonId), 7);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	// Update Password Behaviors	
	public static Errors enterCurrentPassword (AndroidDriver<MobileElement> d, String password) {
		return sendKeys(d, getCurrentPasswordEditText(d), password, true);
	}
	
	public static Errors enterNewPassword (AndroidDriver<MobileElement> d, String password) {
		return sendKeys(d, getNewPasswordEditText(d), password, true);
	}
	
	public static Errors enterConfirmNewPassword (AndroidDriver<MobileElement> d, String password) {
		return sendKeys(d, getConfirmNewPasswordEditText(d), password, true);
	}
	
	public static Errors tapUpdatePasswordButton (AndroidDriver<MobileElement> d) {
		return click(d, getUpdatePasswordButton(d), "Cannot tap update password button!", "tapUpdatePasswordButton");
	}
	
	public static Errors tapForgotPasswordLink (AndroidDriver<MobileElement> d) {
		return click(d, getForgotPasswordLink(d), "Cannot tap forgot password link!", "tapForgotPasswordLink");
	}
	
	
	// Reset Password Behaviors
	public static Errors enterEmail (AndroidDriver<MobileElement> d, String email) {
		return sendKeys(d, getEmailEditText(d), email, true);
	}
	
	public static Errors tapResetPasswordButton (AndroidDriver<MobileElement> d) {
		return click(d, getResetPasswordButton(d), "Cannot tap reset password button!", "tapResetPasswordButton");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
}
