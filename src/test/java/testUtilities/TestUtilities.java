package testUtilities;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.experimental.categories.Category;

import Pages.Menu;
import Pages.Page;
import Pages.Player;
import Pages.Player.PlayerButton;
import Pages.Player.Thumb;
import Utilities.TestRoot;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.TestUtilities.SignInType;

public class TestUtilities extends TestRoot {

	public static String cannotTestErrorMessage = "Cannot test %s due to commercials or lack of meta-data.";
	
	@Before
	public void before () {
		if (!setup()) {
			Assert.fail("Could not load driver");
		}
	}
	
	// Replaces @After's quit() method, takes screenshot of last screen if test
	// fails
	@Rule
	public ScreenshotRule screenshot = new ScreenshotRule();
	
	@Rule
	public RetryRule retry = new RetryRule(1);
	
	/************************/
	/* *** Common Tests *** */
	/************************/
	
	public enum TestType {
		LIVE_STATIONS,
		ARTIST_STATIONS,
		PODCASTS;
	}
	
	public enum SignInType {
		SIGN_UP, LOG_IN;
	}
	
	public static void checkMainMenuItems () {
		List<String> expectedItems = Menu.getMainMenuItemTextList();
		List<String> actualItems = getAllItemTextOnMultiplePages(driver, 2);
		
		int numMissing = getNumOfMissingItems(expectedItems, actualItems);
		String errorMessage = String.format("Missing %d menu items: %s", numMissing, getMissingItemsString(expectedItems, actualItems));
		Assert.assertEquals(errorMessage, 0, numMissing);
	}
	
	public static void testSignIn (SignInType type) {
		if (type == SignInType.LOG_IN) {
			Assert.assertTrue("Unable to log in!", Page.logIn(driver, true).noErrors());
		}
		else {
			Assert.assertTrue("Unable to sign up!", Page.signUp(driver, true).noErrors());
		}
	}
	
	/**
	 * Use this only with existing accounts. If you are testing for Favorites or Recent Station, make
	 * sure to select an station in the middle. That way, previous and next exist.
	 * @param playStation, method that goes to the correct station and plays it.
	 */
	public void testPreviewFeatures (Runnable playStation) {
		playStation.run();
		
		// Player
		Assert.assertTrue("Unable to tap previous button!", Pages.Player.tapPreviousButton(driver).noErrors());
		Assert.assertTrue("Unable to tap return to player button!", Pages.Player.tapReturnToPlayerButton(driver).noErrors());
		Assert.assertFalse("Return to player button is visible for some reason!", isVisible(Pages.Player.getReturnToPlayerButton(driver)));
		Assert.assertTrue("Unable to tap next button!", Pages.Player.tapNextButton(driver).noErrors());
		Assert.assertTrue("Unable to tap return to player button!", Pages.Player.tapReturnToPlayerButton(driver).noErrors());
		Assert.assertFalse("Return to player button is visible for some reason!", isVisible(Pages.Player.getReturnToPlayerButton(driver)));
		
		Assert.assertTrue("Unable to tap next button!", Menu.tapNextButton(driver).noErrors());
		String previewStation = Player.getPreviewStationName(driver);
		if (previewStation.contains("Radio")) {	
			previewStation = previewStation.substring(0, previewStation.length() - 5);
		}
		Assert.assertTrue("Unable to tap preview station cover image!", Player.tapPreviewStationCoverImage(driver).noErrors());
		String playingArtistStation = Player.getPlayerMetaLineText(driver, 1);
		
		String errorMessage = String.format("Error! Expected %s. Actual %s!", previewStation, playingArtistStation);
		Assert.assertTrue(errorMessage, isSameStation(playingArtistStation, previewStation));
	}
	
	/**
	 * @param playStation, method that goes to the correct station and plays it.
	 */
	public void testCreateStation (Runnable playStation) {
		playStation.run();
		
		if (!isCommercialPlaying()) {
			Assert.assertTrue("Unable to tap Create Station!", Player.tapPlayerButton(driver, PlayerButton.CREATE_STATION).noErrors());
			String expectedStationName = Player.getCreateStationMessageText(driver); // Contains the station name + "created".
			Assert.assertTrue("Cannot tap Yes button!", Player.tapRedDialogButton(driver).noErrors()); // Yes button
			String actualStationName = Player.getPlayerMetaLineText(driver, 1);
			String errorMessage = String.format("Error! Expected: %s Actual: %s.", expectedStationName, actualStationName);
			Assert.assertTrue(errorMessage, isSameStation(expectedStationName, actualStationName));
		}
		else {
			System.out.println(String.format(cannotTestErrorMessage, "Create Station"));
		}
	}
	
	/**
	 * @param playStation, method that goes to the correct station and plays it.
	 * @param type, the type of station
	 */
	public void testThumbs (Runnable playStation, TestType type) {
		playStation.run();
		Set<Thumb> thumbs = EnumSet.allOf(Thumb.class);
		
		for (Thumb option : thumbs) {
			if (!isCommercialPlaying()) {
				String expectedText = "";
				switch (type) {
					case ARTIST_STATIONS:	
						expectedText = (option == Thumb.UP) ? "You like this song!\nWe'll try to play more like it." : "You dislike this song!\nWe won't play it again on this station.";
						break;
					
					case LIVE_STATIONS:
						expectedText = (option == Thumb.UP) ? "Glad you like it!\nWe'll let our DJs know." : "Thanks for the feedback.\nWe'll let our DJs know.";
						break;
							
					case PODCASTS:
						expectedText = (option == Thumb.UP) ? "Glad you like this show.\nWe appreciate your feedback." : "Thanks for letting us know.\nWe appreciate your feedback.";
						break;
							
					default:
						break;
				}
					
				Assert.assertTrue(String.format("Unable to thumbs %s!", option.toString()), Player.tapThumbUpOrDownButton(driver, option).noErrors());
				String actualText = Page.getCustomDialogText(driver);
				Assert.assertEquals(String.format("Unexpected thumbs %s dialog text: %s.", option.toString(), actualText), expectedText, actualText);
				Page.waitForDialogToDisappear(driver);
			}
			else {
				System.out.println(String.format(cannotTestErrorMessage, option.toString()));
			}
		}
	}
	
	/**
	 * Use this only with new accounts! Do not use with existing account! Existing test accounts already have 3
	 * favorite stations (which are needed for other tests). This test assumes that you begin with none.
	 * @param playStation, method that goes to the correct station and plays it.
	 * @param goToFavorites, method that goes to Favorites from the player screen.
	 */
	public void testFavorites (Runnable playStation, Runnable goToFavorites) {
		playStation.run();
		String stationName = Player.getPlayerMetaLineText(driver, 1);
		
		Consumer<String> tapFavorites = expectedText -> {
			Assert.assertTrue("Cannot tap favorite button!", Pages.Player.tapFavoriteButton(driver).noErrors());
			String actualText = Page.getCustomDialogText(driver);
			String errorMessage = String.format("Error! Expected: %s Actual %s.", expectedText, actualText);
			Assert.assertEquals(errorMessage, expectedText, actualText);
			Page.waitForDialogToDisappear(driver);
		};
		
		// Favorite our station
		String expectedAddToFavoritesText = stationName + " added to Favorites.";
		tapFavorites.accept(expectedAddToFavoritesText);
		
		// Verify that the station is in favorites
		goToFavorites.run();
		int numMissing = getNumOfMissingItems(Arrays.asList(stationName), Pages.Menu.getAllItemTextOnScreen(driver));
		Assert.assertEquals(String.format("%s not saved in favorites!", stationName), 0, numMissing);
		Assert.assertTrue("Unable to tap menu close button!", Pages.Menu.tapMenuCloseButton(driver).noErrors());
		
		// Unfavorite our station
		String expectedRemovedFromFavoritesText = stationName + " removed from Favorites.";
		tapFavorites.accept(expectedRemovedFromFavoritesText);
		
		// Verify that the station is not in favorites
		goToFavorites.run();
		Assert.assertTrue("Cannot tap no favorites continue button!", Pages.Menu.tapWhiteDialogButton(driver).noErrors()); // Continue button
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	/**
	 * Assumes that you are beginning on the first page.
	 * @param d
	 * @param pages, total number of pages
	 * @return
	 */
	public static List<String> getAllItemTextOnMultiplePages (AndroidDriver<MobileElement> d, int totalPages) {
		List<String> actualItems = Pages.Menu.getAllItemTextOnScreen(d);
		for (int numPages = 0; numPages < totalPages - 1; numPages++) {
			Assert.assertTrue("Unable to tap next button!", Pages.Menu.tapNextButton(d).noErrors());
			actualItems.addAll(Pages.Menu.getAllItemTextOnScreen(d));
		}
		return actualItems;
	}
	
	/**
	 * Due to different screen size, textview size, the addition of "Radio" to some
	 * stations names, etc, we end up comparing strings such as
	 * 
	 * "Ennio Morricone Ra..." vs "Ennio Morriocone Radio".
	 * "Britney Spears" vs "Britney Spears Radio"
	 * "WiLD" vs "WiLD!"
	 * 
	 * This method is just an easy way for us to make sure that
	 * we can easily identify the same stations even though the texts
	 * might a bit different.
	 */
	public static boolean isSameStation (String expected, String actual) {
		String standardizedExpected = standardizeString(expected);
		String standardizedActual = standardizeString(actual);
		System.out.println(String.format("Expected: %s Actual: %s", standardizedExpected, standardizedActual));
		return standardizedExpected.contains(standardizedActual) ||
			   standardizedActual.contains(standardizedExpected);
	}
	
	/**
	 * Standardizes strings by removing non-alphanumeric characters.
	 * Also converts all letters to lowercase.
	 */
	public static String standardizeString (String str) {
		return str.chars()
		          .mapToObj(i -> (char) i)
		          .filter(Character::isLetterOrDigit)
		          .map(Character::toLowerCase)
		          .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
		          .toString();
	}
	
	/**
	 * Returns the number of items in expected that is not in actual.
	 * E.g. Expected = [A, B], Actual = [A, C, D], Return value = 1 b/c B is not in actual. 
	 */
	public static int getNumOfMissingItems (List<String> expected, List<String> actual) {
		return (int) expected.stream()
		                     .filter(item -> !actual.stream().anyMatch(i -> isSameStation(item, i)))
		                     .count();
	}
	
	/**
	 * Returns the string of items in expected that is not in actual.
	 * E.g. Expected = [A, B, E], Actual = [A, C, D]
	 * Return value = "[B, E]"
	 */
	public static String getMissingItemsString (List<String> expected, List<String> actual) {
		return expected.stream()
		               .filter(item -> !actual.stream().anyMatch(i -> isSameStation(item, i)))
		               .collect(Collectors.joining(", ", "[ ", " ]"));
	}
	
	public static boolean isCommercialPlaying () {
		int tries = 0;
		int MAX_TRIES = 90;
		while (tries < MAX_TRIES && !isEnabled(Pages.Player.getPlayerButton(driver, Pages.Player.PlayerButton.THUMBS))) {
			sleep(500);
			tries++;
		}
		return tries == MAX_TRIES;
	};
}
