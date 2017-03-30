package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class OptionsSubpage extends Page {
	
	public enum OptionsOnOff {
		ON("custom_toggle_on"),
		OFF("custom_toggle_off");
		
		private String id;
		
		private OptionsOnOff (String id) {
			this.id = id;
		}
		
		public String getId () {
			return Page.connectId + id;
		}
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String optionsBackButtonId = Page.connectId + "option_back_btn";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getOptionsBackButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(optionsBackButtonId), 3);
	}
	
	public static AndroidElement getOptionsSubpageOnOffButton (AndroidDriver<MobileElement> d, OptionsOnOff option) {
		return waitForVisible(d, By.id(option.getId()), 3);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapOptionsBackButton (AndroidDriver<MobileElement> d) {
		return click(d, getOptionsBackButton(d), "Cannot tap options back button!", "tapOptionsBackButton");
	}
	
	public static Errors tapOptionsSubpageOnOffButton (AndroidDriver<MobileElement> d, OptionsOnOff option) {
		String errorMessage = String.format("Cannot tap %s button!", option.toString());
		return click(d, getOptionsSubpageOnOffButton(d, option), errorMessage, "tapUpdateAutomaticallyButton");
	}
	
}
