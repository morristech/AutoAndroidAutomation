package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class ConnectionGate extends Page{
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String byPassButtonId = Page.connectId + "bypass_btn";
	private static String optionsButtonId = Page.connectId + "options_btn";
	private static String helpButtonId = Page.connectId + "help_btn";
	private static String closeButtonId = Page.connectId + "close_btn";
	private static String dontshowDisclaimerAgainCheckBoxId = Page.connectId + "check_box_one";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getByPassButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(byPassButtonId), 7);
	}
	
	public static AndroidElement getOptionsButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(optionsButtonId), 7);
	}
	
	public static AndroidElement getHelpButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(helpButtonId), 7);
	}
	
	public static AndroidElement getCloseButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(closeButtonId), 7);
	}
	
	public static AndroidElement getDontShowDisclaimerAgainCheckBox (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(dontshowDisclaimerAgainCheckBoxId), 7);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapByPassButton (AndroidDriver<MobileElement> d) {
		return click(d, getByPassButton(d), "Cannot tap by pass button!", "tapByPassButton");
	}
	
	public static Errors tapOptionsButton (AndroidDriver<MobileElement> d) {
		return click(d, getOptionsButton(d), "Cannot tap options button!", "tapOptionsButton");
	}
	
	public static Errors tapHelpButton (AndroidDriver<MobileElement> d) {
		return click(d, getHelpButton(d), "Cannot tap help button!", "tapHelpButton");
	}
	
	public static Errors tapCloseButton (AndroidDriver<MobileElement> d) {
		return click(d, getCloseButton(d), "Cannot tap close button!", "tapCloseButton");
	}
	
	public static Errors checkDontShowDisclaimerAgainCheckBox (AndroidDriver<MobileElement> d) {
		return click(d, getDontShowDisclaimerAgainCheckBox(d), "Cannot check the don't show disclaimer again check box!", "checkDontShowDisclaimerAgainCheckBox");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static Errors byPassAndAcceptDisclaimer (AndroidDriver<MobileElement> d) {
		Errors err = new Errors();
		err.add(d, tapByPassButton(d));
		err.add(d, tapRedDialogButton(d)); // Accept Button
		return err;
	}
}
