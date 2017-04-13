package Pages;

import org.openqa.selenium.By;

import Utilities.TestRoot;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import testCommons.Errors;

public class Player extends Page {
	
	public enum PlayerButton {
		SKIP(0), SCAN(0), THUMBS(1), CREATE_STATION(2), DISCOVERY(3);
	
		private final int mPosition;
		
		private PlayerButton (int position) {
			mPosition = position;
		}
		
		public String getId () {
			return String.format(playerButtonId, mPosition);
		}
		
		public String toString () {
			return name().replace('_', ' ').replaceAll("AND", "&");
		}
		
	}
	
	public enum Thumb {
		UP, DOWN;
	}
	
	public enum DiscoveryMode {
		TOP_HITS(1, "The most popular songs for this station."),
		MIX(2, "Additional artists and songs for increased variety."),
		VARIETY(3, "The widest variety of music for the most discovery.");
		
		private final int mPosition;
		private final String mMessage;
		
		private DiscoveryMode (int position, String message) {
			mPosition = position;
			mMessage = message;
		}
		
		public String getId () {
			return String.format(varietyButtonId, mPosition);
		}
		
		public String getMessage () {
			return mMessage;
		}
		
		public String toString () {
			return name().replace('_', ' ').replaceAll("AND", "&");
		}
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

	private static String thumbUpButtonId = Page.connectId + "thumb_up_button";
	private static String thumbDownButtonId = Page.connectId + "thumb_down_button";
	private static String varietyButtonId = Page.connectId + "variety_btn_%d";
	private static String createStationTextId = Page.connectId + "text_title";
	private static String playButtonID = Page.connectId + "play_pause_btn";
	
	// Preview
	private static String previewStationCoverImageId = Page.connectId + "station_image";
	private static String previewStationNameTextViewId = Page.connectId + "station_name_text";
	private static String returnToPlayerButtonId = Page.connectId + "return_to_player_btn";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getMenuButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(menuButtonId), 7);
	}
	
	public static AndroidElement getCurrentMenuTitleTextView (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(currentMenuTitleTextViewId), 7);
	}
	
	public static AndroidElement getFavoriteButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(favoriteButtonId), 7);
	}
	
	public static AndroidElement getCoverImage (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(coverImageId), 7);
	}
	
	public static AndroidElement getPlayerMetaLine (AndroidDriver<MobileElement> d, int index) {
		String id = String.format(playerMetaLineId, index);
		return waitForVisible(d, By.id(id), 7);
	}
	
	private static AndroidElement getEnumButton (AndroidDriver<MobileElement> d, String id, String text) {
		AndroidElement button = waitForVisible(d, By.id(id), 7);
		if (isVisible(button) && TestRoot.getText(button).equalsIgnoreCase(text)) {
			return button;
		}
		return null;
	}
	
	public static AndroidElement getPlayerButton (AndroidDriver<MobileElement> d, PlayerButton option) {
		return getEnumButton(d, option.getId(), option.toString());
	}
	
	public static AndroidElement getDiscoveryModeButton (AndroidDriver<MobileElement> d, DiscoveryMode option) {
		return getEnumButton(d, option.getId(), option.toString());
	}
	
	public static AndroidElement getThumbUpOrDownButton (AndroidDriver<MobileElement> d, Thumb option) {
		String id = (option == Thumb.UP) ? thumbUpButtonId : thumbDownButtonId;
		return waitForVisible(d, By.id(id), 7);
	}
	
	public static AndroidElement getCreateStationMessageTextView (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(createStationTextId), 7);
	}
	
	public static AndroidElement getPlayButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(playButtonID), 7);
	}
	
	
	// Preview
	
	public static AndroidElement getReturnToPlayerButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(returnToPlayerButtonId), 7);
	}
	
	public static AndroidElement getPreviewStationCoverImage (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(previewStationCoverImageId), 7);
	}
	
	public static AndroidElement getPreviewStationNameTextView (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(previewStationNameTextViewId), 7);
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
	
	public static Errors tapDiscoveryModeButton (AndroidDriver<MobileElement> d, DiscoveryMode option) {
		if (isVisible(getPlayerButton(d, PlayerButton.DISCOVERY))) {
			tapPlayerButton(d, PlayerButton.DISCOVERY);
		}
		String errorMessage = String.format("Cannot tap %s button!", option.toString());
		return click(d, getDiscoveryModeButton(d, option), errorMessage, "tapDiscoveryModeButton");
	}
	
	public static Errors tapThumbUpOrDownButton (AndroidDriver<MobileElement> d, Thumb option) {
		if (isVisible(getPlayerButton(d, PlayerButton.THUMBS))) {	
			tapPlayerButton(d, PlayerButton.THUMBS);
		}
		String errorMessage = String.format("Cannot tap %s button!", option.name());
		return click(d, getThumbUpOrDownButton(d, option), errorMessage, "tapThumbUpOrDownButton");
	}
	
	// Preview
	
	public static Errors tapReturnToPlayerButton (AndroidDriver<MobileElement> d) {
		return click(d, getReturnToPlayerButton(d), "Cannot tap return to player button!","tapReturnToPlayerButton");
	}
	
	public static Errors tapPreviewStationCoverImage (AndroidDriver<MobileElement> d) {
		return click(d, getPreviewStationCoverImage(d), "Cannot tap preview station cover image!", "tapPreviewStationCoverImage");
	}
	
	public static Errors tapPreviewStationNameTextView (AndroidDriver<MobileElement> d) {
		return click(d, getPreviewStationNameTextView(d), "Cannot tap preview station name text view!", "tapPreviewStationNameTextView");
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
	
	public static String getPreviewStationName (AndroidDriver<MobileElement> d) {
		return getText(getPreviewStationNameTextView(d));
	}

}
