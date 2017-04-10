package testUtilities;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import Pages.Menu;
import Pages.Page;
import Pages.Player;
import Pages.Player.PlayerButton;
import Pages.Player.Thumb;
import Utilities.TestRoot;

public class TestCommons extends TestRoot {

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
		ARTIST_STATIONS;
	}
	
	public enum SignInType {
		SIGN_UP, LOG_IN;
	}
	
	public static void checkMainMenuItems () {
		List<String> actualMenuItemTextList = Pages.Menu.getAllItemTextOnScreen(driver);
		Assert.assertTrue("Unable to tap next button!", Pages.Menu.tapNextButton(driver).noErrors());
		actualMenuItemTextList.addAll(Pages.Menu.getAllItemTextOnScreen(driver));
		int numMissing = getNumOfMissingItems(Pages.Menu.getMainMenuItemTextList(), actualMenuItemTextList);
		Assert.assertEquals(String.format("Missing %d menu items!", numMissing), 0, numMissing);
	}
	
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
		Assert.assertTrue(errorMessage, playingArtistStation.contains(previewStation));
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
			Assert.assertTrue(errorMessage, expectedStationName.contains(actualStationName));
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
		
		BiConsumer<Thumb, TestType> testThumb = (option, t) -> {
				if (!isCommercialPlaying()) {
					String expectedText = "";
					switch (t) {
						case ARTIST_STATIONS:	
							expectedText = (option == Thumb.UP) ? "You like this song!\nWe'll try to play more like it." : "You dislike this song!\nWe won't play it again on this station.";
							break;
						
						case LIVE_STATIONS:
							expectedText = (option == Thumb.UP) ? "Glad you like it!\nWe'll let our DJs know." : "Thanks for the feedback.\nWe'll let our DJs know.";
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
		};
		
		testThumb.accept(Thumb.UP, type);
		testThumb.accept(Thumb.DOWN, type);
	}
	
	/**
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
	 * Returns the number of items in expected that is not in actual.
	 * E.g. Expected = [A, B], Actual = [A, C, D], Return value = 1 b/c B is not in actual. 
	 */
	public static int getNumOfMissingItems (List<String> expected, List<String> actual) {
		return (int) expected.stream()
		                     .filter(item -> !actual.stream().anyMatch(i -> i.equalsIgnoreCase(item)))
		                     .count();
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
