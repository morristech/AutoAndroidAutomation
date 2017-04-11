package Pages;

import org.openqa.selenium.By;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import testCommons.Errors;

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
		return waitForVisible(d, By.id(optionsDoneButtonId), 7);
	}
	
	public static AndroidElement getLoggedInEmailTextView (AndroidDriver<MobileElement> d) {
		return scrollUntil(d, DOWN, By.id(loggedInEmailTextViewId));
	}
	
	public static AndroidElement scrollToGetOptionItem (AndroidDriver<MobileElement> d, int direction, OptionItem option) {
		return scrollUntil(d, direction, By.id(option.mId));
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
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getLoggedInEmail (AndroidDriver<MobileElement> d) {
		return getText(getLoggedInEmailTextView(d));
	}
	
	public static Errors logOut (AndroidDriver<MobileElement> d) {
		Errors err = new Errors();
		err.add(d, scrollAndTapOptionItem(d, DOWN, OptionItem.LOGGED_IN_AS));
		err.add(d, tapRedDialogButton(d)); // Log out button
		err.add(d, Page.tapWhiteDialogButton(d)); // Okay button
		return err;
	}
}
