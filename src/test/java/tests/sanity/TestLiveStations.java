package tests.sanity;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntConsumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Utilities.TestRoot;
import suites.CategoryInterfaces.Sanity;

public class TestLiveStations extends TestRoot {

	private static String cannotTestErrorMessage = "Cannot test %s due to commercials or lack of meta-data.";
	
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
	
	@Test
	@Category(Sanity.class)
	public void testNearYou () {
		// Logic for getting to Live Radio
		Assert.assertTrue("Unable to log in!", Pages.SignUpLogInGate.logIn(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());		
		checkMainMenuItems();
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		
		// Check if all Live Radio items are present
		List<String> expectedLiveRadioItems = Pages.Menu.getLiveRadioMenuItemTextList();
		List<String> actualLiveRadioItems = Pages.Menu.getAllItemTextOnScreen(driver);
		int numMissing = getNumOfMissingItems(expectedLiveRadioItems, actualLiveRadioItems);
		Assert.assertEquals(String.format("Missing %d menu items!", numMissing), 0, numMissing);
		
		// Near You
		Assert.assertTrue("Unable to tap Near You!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.NEAR_YOU).noErrors());
		String stationName = Pages.Menu.getItemTitle(driver, 1);
		Assert.assertTrue(String.format("Unable to tap station %s.", stationName), Pages.Menu.tapItem(driver, 1).noErrors());
		
		String stationCurrentlyPlaying = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertTrue(String.format("Stations don't match! Expected: %s Actual: %s.", stationName, stationCurrentlyPlaying),
							stationName.equalsIgnoreCase(stationCurrentlyPlaying));
	}
	
	// TODO: Create this test after locations page has been implemented.
	public void testByLocation () {
		
	}
	
	@Test
	@Category(Sanity.class)
	public void testByGenre () {
		// Logic for getting to Live Radio
		Assert.assertTrue("Unable to log in!", Pages.SignUpLogInGate.logIn(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next button!", Pages.Player.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		
		// Check if Live Radio items are all present
		List<String> expectedLiveRadioItems = Pages.Menu.getLiveRadioMenuItemTextList();
		List<String> actualLiveRadioItems = Pages.Menu.getAllItemTextOnScreen(driver);
		int numMissing = getNumOfMissingItems(expectedLiveRadioItems, actualLiveRadioItems);
		Assert.assertEquals(String.format("Missing %d menu items!", numMissing), 0, numMissing);
		
		// Check if By Genre items are all present
		Assert.assertTrue("Unable to tap By Genre!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.BY_GENRE).noErrors());
		List<String> expectedByGenreItems = Pages.Menu.getLiveRadioByGenreMenuItemTextList();
		List<String> actualByGenreItems = Pages.Menu.getAllItemTextOnScreen(driver);
		int MAX_PAGES = 4;
		for (int numPages = 0; numPages < MAX_PAGES; numPages++) {
			Assert.assertTrue("Unable to tap next button!", Pages.Menu.tapNextButton(driver).noErrors());
			actualByGenreItems.addAll(Pages.Menu.getAllItemTextOnScreen(driver));
		}
		numMissing = getNumOfMissingItems(expectedByGenreItems, actualByGenreItems);
		Assert.assertEquals(String.format("Missing %d By Genre menu items!", numMissing), 0, numMissing);
		
		Assert.assertTrue("Unable to tap EDM", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioByGenreMenuItem.EDM).noErrors());
	}
	
	@Test
	@Category(Sanity.class)
	public void testPreviewFeaturesLiveStations () {
		testPathToNearYou();
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 1).noErrors());
		
		// Player
		Assert.assertTrue("Unable to tap previous button!", Pages.Player.tapPreviousButton(driver).noErrors());
		Assert.assertTrue("Unable to tap return to player button!", Pages.Player.tapReturnToPlayerButton(driver).noErrors());
		Assert.assertFalse("Return to player button is visible for some reason!", isVisible(Pages.Player.getReturnToPlayerButton(driver)));
		Assert.assertTrue("Unable to tap next button!", Pages.Player.tapNextButton(driver).noErrors());
		Assert.assertTrue("Unable to tap return to player button!", Pages.Player.tapReturnToPlayerButton(driver).noErrors());
		Assert.assertFalse("Return to player button is visible for some reason!", isVisible(Pages.Player.getReturnToPlayerButton(driver)));
	}
	
	@Test
	@Category(Sanity.class)
	public void testScanLiveStations () {
		testPathToNearYou();
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 0).noErrors());
		
		String stationNameBeforeScan = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertTrue("Unable to tap scan button!", Pages.Player.tapPlayerButton(driver, Pages.Player.PlayerButton.SCAN).noErrors());
		String stationNameAfterScan = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertNotEquals("Error! Station name is still the same after scan!", stationNameBeforeScan, stationNameAfterScan);
	}
	
	@Test
	@Category(Sanity.class)
	public void testCreateStation () {
		testPathToNearYou();
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 0).noErrors());
		
		if (!isCommercialPlaying()) {
			Assert.assertTrue("Unable to tap Create Station", Pages.Player.tapPlayerButton(driver, Pages.Player.PlayerButton.CREATE_STATION).noErrors());
			String stationName = Pages.Player.getCreateStationMessageText(driver);
			Assert.assertTrue("Unable to tap Yes button!", Pages.Player.tapRedDialogButton(driver).noErrors()); // Yes button
			String actualStationCreated = Pages.Player.getPlayerMetaLineText(driver, 1);
			Assert.assertTrue("Created wrong station.", stationName.contains(actualStationCreated));
		}
		else {
			System.out.println(String.format(cannotTestErrorMessage, "Create Station"));
		}
	}
	
	@Test
	@Category(Sanity.class)
	public void testThumbUpAndDown () {
		testPathToNearYou();
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 0).noErrors());
		
		if (!isCommercialPlaying()) {
			Assert.assertTrue("Unable to thumbs up song!", Pages.Player.tapThumbUpOrDownButton(driver, Pages.Player.Thumb.UP).noErrors());
			sleep(5000); // wait for modal to disappear
		}
		else {
			System.out.println(String.format(cannotTestErrorMessage, "Thumbs Up"));
		}
		
		if (!isCommercialPlaying()) {
			Assert.assertTrue("Unable to thumbs down song!", Pages.Player.tapThumbUpOrDownButton(driver, Pages.Player.Thumb.DOWN).noErrors());
		}
		else {
			System.out.println(String.format(cannotTestErrorMessage, "Thumbs Down"));
		}
	}
	
	@Test
	@Category(Sanity.class)
	public void testAddAndRemoveFromFavorites () {
		testPathToNearYou();
		int itemIndex = 0;
		String stationName = Pages.Menu.getItemTitle(driver, itemIndex);
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, itemIndex).noErrors());
		
		// Favorite our station
		Assert.assertTrue("Cannot tap favorite button!", Pages.Player.tapFavoriteButton(driver).noErrors());
		
		IntConsumer goToFavorites = time -> {
			sleep(time); // wait for modal to disappear
			Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
			Assert.assertTrue("Unable to tap menu back button!", Pages.Menu.tapMenuBackButton(driver).noErrors());
			Assert.assertTrue("Unable to tap menu back button!", Pages.Menu.tapMenuBackButton(driver).noErrors());
			Assert.assertTrue("Cannot tap previous button!", Pages.Menu.tapPreviousButton(driver).noErrors());
			Assert.assertTrue("Unable to tap favorites menu item!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.FAVORITES).noErrors());
		};
		
		// Verify that the station is in favorites
		goToFavorites.accept(5000);
		int numMissing = getNumOfMissingItems(Arrays.asList(stationName), Pages.Menu.getAllItemTextOnScreen(driver));
		Assert.assertEquals(String.format("%s not saved in favorites!", stationName), 0, numMissing);
		Assert.assertTrue("Unable to tap menu close button!", Pages.Menu.tapMenuCloseButton(driver).noErrors());
		
		// Unfavorite our station
		Assert.assertTrue("Cannot tap favorite button!", Pages.Player.tapFavoriteButton(driver).noErrors());
		
		// Verify that the station is not in favorites
		goToFavorites.accept(5000);
		Assert.assertTrue("Cannot tap no favorites continue button!", Pages.Menu.tapWhiteDialogButton(driver).noErrors()); // Continue button
	}
	
	private static void testPathToNearYou () {
		Assert.assertTrue("Unable to sign up!", Pages.SignUpLogInGate.signUp(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		Assert.assertTrue("Unable to tap NEAR YOU!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.NEAR_YOU).noErrors());
	}
	
	private static void checkMainMenuItems () {
		List<String> actualMenuItemTextList = Pages.Menu.getAllItemTextOnScreen(driver);
		Assert.assertTrue("Unable to tap next button!", Pages.Menu.tapNextButton(driver).noErrors());
		actualMenuItemTextList.addAll(Pages.Menu.getAllItemTextOnScreen(driver));
		int numMissing = getNumOfMissingItems(Pages.Menu.getMainMenuItemTextList(), actualMenuItemTextList);
		Assert.assertEquals(String.format("Missing %d menu items!", numMissing), 0, numMissing);
	}
	
	/**
	 * Returns the number of items in expected that is not in actual.
	 * E.g. Expected = [A, B], Actual = [A, C, D], Return value = 1 b/c B is not in actual. 
	 */
	private static int getNumOfMissingItems (List<String> expected, List<String> actual) {
		return (int) expected.stream()
		                     .filter(item -> !actual.stream().anyMatch(i -> i.equalsIgnoreCase(item)))
		                     .count();
	}
	
	private static boolean isCommercialPlaying () {
		int tries = 0;
		int MAX_TRIES = 24;
		while (tries < MAX_TRIES && !isEnabled(Pages.Player.getPlayerButton(driver, Pages.Player.PlayerButton.THUMBS))) {
			Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
			sleep(5000);
			tries++;
			Assert.assertTrue("Cannot tap close menu button!", Pages.Menu.tapMenuCloseButton(driver).noErrors());
		}
		return tries == MAX_TRIES;
	};
	
}
