package Pages;


import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class MyLocation extends OptionsSubpage {

	public enum MyLocationOption {
		CURRENT_CITY("current_city_item"),
		UPDATE_AUTOMATICALLY("auto_refresh_item"),
		UPDATE_NOW("use_current_location_item"),
		CHOOSE_A_CITY("choose_city_item"),
		ENTER_ZIP_CODE("enter_zipcode_item");
		
		private String id;
		
		private MyLocationOption (String id) {
			this.id = id;
		}
		
		public String getId () {
			return Page.connectId + id;
		}
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String currentCityAndStateId = Page.connectId + "tv_current_city";
	private static String enterZIPDialogEditTextId = Page.connectId + "edit_text";
	private static String chooseACityListItemId = Page.connectId + "option_list_item_text";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getCurrentCityAndState (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(currentCityAndStateId), 3);
	}
	
	public static AndroidElement getEnterZIPDialogEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(enterZIPDialogEditTextId), 3);
	}
	
	public static AndroidElement scrollToGetMyLocationItem (AndroidDriver<MobileElement> d, int direction, MyLocationOption option) {
		return scrollUntil(d, direction, By.id(option.getId()));
	}
	
	public static AndroidElement getChooseACityListItem (AndroidDriver<MobileElement> d, int index) {
		return getListItem(d, By.id(chooseACityListItemId), index);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors enterZIP (AndroidDriver<MobileElement> d, String zip) {
		return sendKeys(d, getEnterZIPDialogEditText(d), zip, true);
	}
	
	public static Errors scrollAndTapMyLocationItem (AndroidDriver<MobileElement> d, int direction, MyLocationOption option) {
		String errorMessage = String.format("Cannot tap My Location item: %s.", option.toString());
		return click(d, scrollToGetMyLocationItem(d, direction, option), errorMessage, "scrollAndTapMyLocationItem");
	}
	
	public static Errors tapChooseACityListItem (AndroidDriver<MobileElement> d, int index) {
		return click(d, getChooseACityListItem(d, index), "Unable to tap choose a city list item!", "tapChooseACityListItem");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getCurrentCityAndStateText (AndroidDriver<MobileElement> d) {
		return getText(getCurrentCityAndState(d));
	}
	
	public static String getChooseACityListItemText (AndroidDriver<MobileElement> d, int index) {
		return getText(getChooseACityListItem(d, index));
	}

	public static Errors enterZIPAndContinue (AndroidDriver<MobileElement> d, String zip) {
		Errors errors = new Errors();
		errors.add(d, enterZIP(d, zip));
		errors.add(d, tapDialogButton(d, DialogOptions.ENTER_ZIP_OKAY));
		errors.add(d, Page.tapDialogButton(d, DialogOptions.CONTINUE));
		return errors;
	}
}
