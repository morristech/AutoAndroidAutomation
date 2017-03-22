package Pages;

import java.util.List;

import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class Menu extends Page {
	
	public enum MenuItem {
		FOR_YOU,
		FAVORITES,
		RECENT_STATIONS,
		LIVE_RADIO,
		ARTIST_RADIO,
		PODCASTS;
		
		public String toString () {
			return name().replace('_', ' ').replaceAll("AND", "&");
		}
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String menuBackButton = Page.connectId + "menu_back_btn";
	private static String menuCloseButton = Page.connectId + "menu_close_btn";
	private static String menuItemTitle = Page.connectId + "title";
	
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getMenuBackButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(menuBackButton), 3);
	}
	
	public static AndroidElement getMenuCloseButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(menuCloseButton), 3);
	}
	
	public static AndroidElement getItem (AndroidDriver<MobileElement> d, int position) {
		return getCardItem(d, position, 0);
	}
	
	public static AndroidElement getItem (AndroidDriver<MobileElement> d, int row, int column) {
		return getCardItem(d, column, row);
	}
	
	public static AndroidElement getItem (AndroidDriver<MobileElement> d, String title) {
		List<MobileElement> menuItemList = findElements(d, By.id(menuItemTitle));
		for (MobileElement menuItem : menuItemList) {
			if (getText(menuItem).equalsIgnoreCase(title)) {
				return (AndroidElement) menuItem;
			}
		}
		return null;
	}
	
	public static AndroidElement getMenuItem (AndroidDriver<MobileElement> d, MenuItem item) {
		return getItem(d, item.toString());
	}
	

	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapMenuBackButton (AndroidDriver<MobileElement> d) {
		return click(d, getMenuBackButton(d), "Cannot tap menu back button!", "tapMenuBackButton");
	}
	
	public static Errors tapMenuCloseButton (AndroidDriver<MobileElement> d) {
		return click(d, getMenuCloseButton(d), "Cannot tap menu close button!", "tapMenuCloseButton");
	}
	
	public static Errors tapItem (AndroidDriver<MobileElement> d, int position) {
		String errorMessage = String.format("Cannot tap item with position: %d.", position);
		return click(d, getItem(d, position), errorMessage, "tapItem");
	}
	
	public static Errors tapItem (AndroidDriver<MobileElement> d, int row, int column) {
		String errorMessage = String.format("Cannot tap item with row position %d and column posiiton %d.", row, column);
		return click(d, getItem(d, row, column), errorMessage, "tapItem");
	}
	
	public static Errors tapItem (AndroidDriver<MobileElement> d, String item) {
		String errorMessage = String.format("Cannot find item: %s.", item);
		return click(d, getItem(d, item), errorMessage,"tapItem");
	}
	
	public static Errors tapMenuItem (AndroidDriver<MobileElement> d, MenuItem item) {
		String errorMessage = String.format("Cannot tap menu item: %s.", item.toString());
		return click(d, getMenuItem(d, item), errorMessage, "tapMenuItem");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getItemTitle (AndroidDriver<MobileElement> d, int position) {
		return getText(getItem(d, position));
	}
	
	public static String getItemTitle (AndroidDriver<MobileElement> d, int row, int column) {
		return getText(getItem(d, row, column));
	}
}
