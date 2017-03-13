package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class Options extends Page {
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String optionsDoneButtonId = Page.connectId + "option_done_btn";
	private static String loggedInEmailTextViewId = Page.connectId + "loggedIn_email";
	private static String signUpOrLogInItemId = Page.connectId + "signup_or_login_item";
	
	public enum OptionItem {
		SIGN_UP_OR_LOG_IN(signUpOrLogInItemId),
		LOGGED_IN_AS(signUpOrLogInItemId),
		UPDATE_PASSWORD(Page.connectId + "update_password_item"),
		FACEBOOK(Page.connectId + "facebook_login_item"),
		GOOGLE_PLUS(Page.connectId + "google_plus_login_item"),
		MY_LOCATION(Page.connectId + "reset_location_item"),
		MY_GENRE(Page.connectId + "my_genres_item"),
		AUTOPLAY_ON_OPEN(Page.connectId + "play_last_station_item"),
		EXPLICIT_CONTENT(Page.connectId + "explicit_content_item"),
		CAR_CONNECTIONS(Page.connectId + "bluetooth_connections_item"),
		TERMS_OF_USE(Page.connectId + "terms_and_conditions_item"),
		PRIVACY_POLICY(Page.connectId + "private_policy_item"),
		ABOUT(Page.connectId + "about_item"),
		HELP(Page.connectId + "help_item");
		
		public final String mId;
		
		private OptionItem (String id) {
			mId = id;
		}
		
		public String toString () {
			return name().replace('_', ' ').replaceAll("AND", "&");
		}
		
	}
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getDoneButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(optionsDoneButtonId), 3);
	}
	
	public static AndroidElement getLoggedInEmailTextView (AndroidDriver<MobileElement> d) {
		return scrollUntil(d, DOWN, By.id(loggedInEmailTextViewId));
	}
	
	public static AndroidElement scrollToGetOptionItem (AndroidDriver<MobileElement> d, int direction, OptionItem option) {
		return scrollUntil(d, direction, By.id(option.mId));
	}
	
	public static AndroidElement getLogOutDialogButton (AndroidDriver<MobileElement> d) {
		return getAcceptButton(d);
	}
	
	public static AndroidElement getCancelDialogButton (AndroidDriver<MobileElement> d) {
		return getDenyButton(d);
	}
	
	public static AndroidElement getLoggedOutOkayDialogButton (AndroidDriver<MobileElement> d) {
		return getDenyButton(d);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapDoneButton (AndroidDriver<MobileElement> d) {
		return click(d, getDoneButton(d), "Cannot click on done button!", "tapDoneButton");
	}
	
	public static Errors scrollAndTapOptionItem (AndroidDriver<MobileElement> d, int direction, OptionItem option) {
		String errorMessage = String.format("Cannot tap option item: %s.", option.toString());
		return click(d, scrollToGetOptionItem(d, direction, option), errorMessage, "scrollAndTapOptionItem");
	}
	
	public static Errors tapLogOutDialogButton (AndroidDriver<MobileElement> d) {
		return click(d, getLogOutDialogButton(d), "Cannot tap log out dialog button!", "tapLogOutDialogButton");
	}
	
	public static Errors tapCancelDialogButton (AndroidDriver<MobileElement> d) {
		return click(d, getCancelDialogButton(d), "Cannot tap cancel dialog button!", "tapCancelDialogButton");
	}
	
	public static Errors tapLoggedOutOkayDialogButton (AndroidDriver<MobileElement> d) {
		return click(d, getLoggedOutOkayDialogButton(d), "Cannot tap logged out okay dialog button!", "tapLoggedOutOkayDialogButton");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getLoggedInEmail (AndroidDriver<MobileElement> d) {
		return getText(getLoggedInEmailTextView(d));
	}
	
	public static Errors logOut (AndroidDriver<MobileElement> d) {
		Errors err = new Errors();
		err.add(d, scrollAndTapOptionItem(d, DOWN, OptionItem.LOGGED_IN_AS));
		err.add(d, tapLogOutDialogButton(d));
		err.add(d, tapLoggedOutOkayDialogButton(d));
		return err;
	}
}
