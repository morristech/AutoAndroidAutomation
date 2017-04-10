package Pages;


import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class MyLocation extends OptionsSubpage {

	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String myLocationCurrentCityItemId = Page.connectId + "current_city_item";
	private static String myLocationUpdateAutomaticallyItemId = Page.connectId + "auto_refresh_item";
	private static String myLocationUpdateNowItemId = Page.connectId + "use_current_location_item";
	private static String myLocationChooseCityItemId = Page.connectId + "choose_city_item";
	private static String myLocationEnterZipCodeItemId = Page.connectId + "enter_zipcode_item";
	private static String currentCityAndStateId = Page.connectId + "tv_current_city";
	private static String enterZIPDialogEditTextId = Page.connectId + "edit_text";
	private static String chooseACityListItemId = Page.connectId + "option_list_item_text";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getCurrentCityItem (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(myLocationCurrentCityItemId), 7);
	}
	
	public static AndroidElement getUpdateAutomaticallyItem (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(myLocationUpdateAutomaticallyItemId), 7);
	}
	
	public static AndroidElement getUpdateNowItem (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(myLocationUpdateNowItemId), 7);
	}
	
	public static AndroidElement getChooseCityItem (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(myLocationChooseCityItemId), 7);
	}
	
	public static AndroidElement getEnterZipCodeItem (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(myLocationEnterZipCodeItemId), 7);
	}
	
	public static AndroidElement getCurrentCityAndState (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(currentCityAndStateId), 7);
	}
	
	public static AndroidElement getEnterZIPDialogEditText (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(enterZIPDialogEditTextId), 7);
	}
	
	public static AndroidElement getChooseACityListItem (AndroidDriver<MobileElement> d, int index) {
		return getListItem(d, By.id(chooseACityListItemId), index);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapCurrentCityItem (AndroidDriver<MobileElement> d) {
		return click(d, getCurrentCityItem(d), "Unable to tap current city item!", "tapCurrentCityItem");
	}
	
	public static Errors tapUpdateAutomaticallyItem (AndroidDriver<MobileElement> d) {
		return click(d, getUpdateAutomaticallyItem(d), "Unable to tap update automatically item!", "tapUpdateAutomaticallyItem");
	}
	
	public static Errors tapUpdateNowItem (AndroidDriver<MobileElement> d) {
		return click(d, getUpdateNowItem(d), "Unable to tap update now item!", "tapUpdateNowItem");
	}
	
	public static Errors tapChooseCityItem (AndroidDriver<MobileElement> d) {
		return click(d, getChooseCityItem(d), "Unable to tap choose city item!", "tapChooseCityItem");
	}
	
	public static Errors tapEnterZipCodeItem (AndroidDriver<MobileElement> d) {
		return click(d, getEnterZipCodeItem(d), "Unable to tap enter zip code item!", "tapEnterZipCodeItem");
	}
	
	public static Errors enterZIP (AndroidDriver<MobileElement> d, String zip) {
		return sendKeys(d, getEnterZIPDialogEditText(d), zip, true);
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
		errors.add(d, tapRedDialogButton(d)); // Enter Zip okay button
		errors.add(d, Page.tapWhiteDialogButton(d)); // Continue button
		return errors;
	}
}
