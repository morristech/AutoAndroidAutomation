package Pages;

import org.openqa.selenium.By;

import Utilities.Errors;
import Utilities.TestRoot;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class Player extends Page {
	
	public enum PlayerButton {
		SKIP(0), SCAN(0), THUMBS(1), CREATE_STATION(2), DISCOVERY(3);
	
		public final int mPosition;
		
		private PlayerButton (int position) {
			mPosition = position;
		}
		
		public String toString () {
			return name().replace('_', ' ').replaceAll("AND", "&");
		}
		
	}
	
	public enum Thumb {
		UP, DOWN;
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String menuButtonId = Page.connectId + "menu_heart_btn";
	private static String currentMenuTitleTextViewId = Page.connectId + "bread_crumb_text";
	private static String favoriteButtonId = Page.connectId + "menu_add_favorites_btn";
	private static String coverImageId = Page.connectId + "cover_image";
	private static String playerMetaLineId = Page.connectId + "meta_line_%d";
	private static String playerButtonId = Page.connectId + "player_btn_%d";
	private static String returnToPlayerButtonId = Page.connectId + "return_to_player_btn";
	private static String thumbUpButtonId = Page.connectId + "thumb_up_button";
	private static String thumbDownButtonId = Page.connectId + "thumb_down_button";
	private static String createStationTextId = Page.connectId + "text_title";
	private static String playButtonID = Page.connectId + "play_pause_btn";
	
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getMenuButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(menuButtonId), 3);
	}
	
	public static AndroidElement getCurrentMenuTitleTextView (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(currentMenuTitleTextViewId), 3);
	}
	
	public static AndroidElement getFavoriteButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(favoriteButtonId), 3);
	}
	
	public static AndroidElement getCoverImage (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(coverImageId), 3);
	}
	
	public static AndroidElement getPlayerMetaLine (AndroidDriver<MobileElement> d, int index) {
		String id = String.format(playerMetaLineId, index);
		return waitForVisible(d, By.id(id), 3);
	}
	
	public static AndroidElement getPlayerButton (AndroidDriver<MobileElement> d, PlayerButton option) {
		String id = String.format(playerButtonId, option.mPosition);
		AndroidElement button = waitForVisible(d, By.id(id), 3);
		if (isVisible(button) && TestRoot.getText(button).equalsIgnoreCase(option.toString())) {
			return button;
		}
		return null;
	}
	
	public static AndroidElement getReturnToPlayerButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(returnToPlayerButtonId), 3);
	}
	
	public static AndroidElement getThumbUpOrDownButton (AndroidDriver<MobileElement> d, Thumb option) {
		String id = (option == Thumb.UP) ? thumbUpButtonId : thumbDownButtonId;
		return waitForVisible(d, By.id(id), 3);
	}
	
	public static AndroidElement getCreateStationYesButton (AndroidDriver<MobileElement> d) {
		return getAcceptButton(d);
	}
	
	public static AndroidElement getCreateStationNoButton (AndroidDriver<MobileElement> d) {
		return getDenyButton(d);
	}
	
	public static AndroidElement getCreateStationMessageTextView (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(createStationTextId), 3);
	}
	
	public static AndroidElement getPlayButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(playButtonID), 3);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapMenuButton (AndroidDriver<MobileElement> d) {
		return click(d, getMenuButton(d), "Cannot tap menu button!", "tapMenuButton");
	}
	
	public static Errors tapFavoriteButton (AndroidDriver<MobileElement> d) {
		return click(d, getFavoriteButton(d), "Cannot tap favorite button!", "tapFavoriteButton");
	}
	
	public static Errors tapCoverImage (AndroidDriver<MobileElement> d) {
		return click(d, getCoverImage(d), "Cannot tap cover image!", "tapCoverImage");
	}
	
	public static Errors tapPlayerButton (AndroidDriver<MobileElement> d, PlayerButton option) {
		String errorMessage = String.format("Cannot tap %s button!", option.toString());
		return click(d, getPlayerButton(d, option), errorMessage, "tapPlayerButton");
	}
	
	public static Errors tapReturnToPlayerButton (AndroidDriver<MobileElement> d) {
		return click(d, getReturnToPlayerButton(d), "Cannot tap return to player button!","tapReturnToPlayerButton");
	}
	
	public static Errors tapThumbUpOrDownButton (AndroidDriver<MobileElement> d, Thumb option) {
		if (isVisible(getPlayerButton(d, PlayerButton.THUMBS))) {	
			tapPlayerButton(d, PlayerButton.THUMBS);
		}
		String errorMessage = String.format("Cannot tap %s button!", option.name());
		return click(d, getThumbUpOrDownButton(d, option), errorMessage, "tapThumbUpOrDownButton");
	}
	
	public static Errors tapCreateStationYesButton (AndroidDriver<MobileElement> d) {
		return click(d, getAcceptButton(d), "Cannot click Create Station yes button!", "tapCreateStationYesButton");
	}
	
	public static Errors tapCreateStationNoButton (AndroidDriver<MobileElement> d) {
		return click(d, getCreateStationNoButton(d), "Cannot click Create Station no button!", "tapCreateStationNoButton");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getCurrentMenuTitle (AndroidDriver<MobileElement> d) {
		return getText(getCurrentMenuTitleTextView(d));
	}
	
	public static String getPlayerMetaLineText (AndroidDriver<MobileElement> d, int index) {
		return getText(getPlayerMetaLine(d, index));
	}
	
	public static String getCreateStationMessageText (AndroidDriver<MobileElement> d) {
		return getText(getCreateStationMessageTextView(d));
	}

}
