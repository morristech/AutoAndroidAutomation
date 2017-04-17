package Pages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import Utilities.TestRoot;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import testCommons.Errors;

public class Player extends Page {
	
	public enum PlayerButton {
		SKIP("Skip", 0),
		SCAN("Scan", 0),
		THUMBS("Thumbs", 1),
		CREATE_STATION("Create Station", 2),
		MINUS_TEN_SECONDS("-10 Seconds", 2),
		DISCOVERY("Discovery", 3),
		PLUS_THIRTY_SECONDS("+30 Seconds", 3);
	
		private final String mDisplayName;
		private final int mPosition;
		
		private PlayerButton (String displayName, int position) {
			mDisplayName = displayName;
			mPosition = position;
		}
		
		public String getId () {
			return String.format(playerButtonId, mPosition);
		}
		
		public String toString () {
			return mDisplayName;
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
	private static String songProgressBarId = Page.connectId + "song_progress_bar";
	
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
	
	public static AndroidElement getSongProgressBar (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(songProgressBarId), 7);
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
	
	public static boolean isCustomPlaying (AndroidDriver<MobileElement> d, int delay) {
		int numBefore = getNumOfRedPixelsOnProgressBar(d);
		sleep(delay);
		int numAfter = getNumOfRedPixelsOnProgressBar(d);
		return numAfter > numBefore;
	}
	
	/**
	 * Technically, it only gives you the number of red pixels on the longest side in order to save computation time.
	 * 
	 * Example: 
	 * 
	 * Progress bar looks like this:
	 * 
	 * R R R R R X X X X X
	 * R R R R R X X X X X
	 * R R R R R X X X X X
	 * 
	 * After a while, it looks like this:
	 * 
	 * R R R R R R R X X X
	 * R R R R R R R X X X
	 * R R R R R R R X X X
	 * 
	 * We only really need to look at the longest side (top row) to know that the player is currently playing since
	 * it went from 5 reds to 7 reds.
	 * 
	 */
	
	public static int getNumOfRedPixelsOnProgressBar (AndroidDriver<MobileElement> d) {
		AndroidElement progressBar = getSongProgressBar(d);
		int numOfRedPixels = 0;
		
		if (isVisible(progressBar)) {
			int x = progressBar.getLocation().getX();
			int y = progressBar.getLocation().getY();
			int width = progressBar.getSize().getWidth();
			int height = progressBar.getSize().getHeight();
			
			int screenHeight = d.manage().window().getSize().getHeight();
			
			sleep(500); // Wait a while before taking the screenshot because tapping the button can be a bit slow.
			// Create the file and store the screenshot
			File tempScreenshot = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
			
			try {
				BufferedImage screenShot = ImageIO.read(tempScreenshot);
				BufferedImage img;
				
				int subScreenShotLengthOfLongestSide;
				boolean isScreenShotPortrait;
				
				// Portrait mode screenshot
				if (screenShot.getHeight() > screenShot.getWidth()) {
				
					/* 
					 * Subimage method requires the x and y values for the upper-left corner.
					 * However, because the auto app is locked in landscape mode only and also because the screenshot taken
					 * is always in portrait mode, we need new coordinates to reflect this change.
					 * 
					 * The landscape bottom-left corner will be the portrait mode's top-right corner.
					 * 
					 * Example:
					 * 
					 * Landscape Mode:
					 * 
					 *    0 1 2 3 4 5 6 7             (X,Y)
					 *  0 X X X X X X A X         A = (6,0)
					 *  1 X X X X X X B X         B = (6,1)
					 *  2 X X X X X X C X         C = (6,2)
					 *  3 X X X X X X X X
					 *  
					 *  Portrait Mode:
					 *  
					 *    0 1 2 3 
					 *  0 X X X X
					 *  1 X X X X 
					 *  2 X X X X
					 *  3 X X X X                     (X,Y)
					 *  4 X X X X                 A = (3,6)
					 *  5 X X X X                 B = (2,6)
					 *  6 X C B A                 C = (1,6)
					 *  7 X X X X
					 *  
					 *  We need an algorithm that when given A(6,0) in landscape will give us C(1,6) in portrait.
					 *  In our example, 
					 *  screenheight (landscape) = 4
					 *  height of object (landscape) = 3.
					 *  A's X/width = 6
					 *  A's Y/height = 0
					 *  
					 *  C's X (in portrait) = 4 - (0 + 3) = 1
					 *  C's Y (in portrait) = A's X (in landscape) = 6
					 *  The height becomes the new width and vice versa because of the rotation.
					 */
					img = screenShot.getSubimage(screenHeight - (y + height), x, height, width);
					subScreenShotLengthOfLongestSide = img.getHeight();
					isScreenShotPortrait = true;
				}
				// Landscape mode screenshot
				else {
					img = screenShot.getSubimage(x, y, width, height);
					subScreenShotLengthOfLongestSide = img.getWidth();
					isScreenShotPortrait = false;
				}
                
				Predicate<Color> isRed = (c) -> c.getRed() > 200 && c.getGreen() < 100 && c.getBlue() < 100;
				
				numOfRedPixels = (int) IntStream.range(0, subScreenShotLengthOfLongestSide - 1)
				                                .map(i -> (isScreenShotPortrait) ? img.getRGB(3, i) : img.getRGB(i, 3))
				                                .filter(rgb -> isRed.test(new Color(rgb)))
				                                .count();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			finally {
				tempScreenshot.delete();
			}
		}
		
		return numOfRedPixels;
	}

}
