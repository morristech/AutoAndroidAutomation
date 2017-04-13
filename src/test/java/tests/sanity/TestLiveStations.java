package tests.sanity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.Page;
import testUtilities.TestCommons;
import testUtilities.CategoryInterfaces.Sanity;

public class TestLiveStations extends TestCommons {
	
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
	public void testByGenreForLiveStations () {
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
	public void testScanForLiveStations () {
		testPathToNearYouAndPlayItem(SignInType.LOG_IN);
		
		String stationNameBeforeScan = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertTrue("Unable to tap scan button!", Pages.Player.tapPlayerButton(driver, Pages.Player.PlayerButton.SCAN).noErrors());
		String stationNameAfterScan = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertNotEquals("Error! Station name is still the same after scan!", stationNameBeforeScan, stationNameAfterScan);
	}
	
	@Test
	@Category(Sanity.class)
	public void testPreviewFeaturesForLiveStations () {
		testPreviewFeatures(() -> testPathToNearYouAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Category(Sanity.class)
	public void testCreateStationForLiveStations () {
		testCreateStation(() -> testPathToNearYouAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Category(Sanity.class)
	public void testThumbsForLiveStations () {
		testThumbs(() -> testPathToNearYouAndPlayItem(SignInType.LOG_IN), TestType.LIVE_STATIONS);
	}
	
	@Test
	@Category(Sanity.class)
	public void testFavoritesForLiveStations () {
		Runnable goToFavorites = () -> {
			Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
			Assert.assertTrue("Unable to tap menu back button!", Pages.Menu.tapMenuBackButton(driver).noErrors());
			Assert.assertTrue("Unable to tap menu back button!", Pages.Menu.tapMenuBackButton(driver).noErrors());
			Assert.assertTrue("Cannot tap previous button!", Pages.Menu.tapPreviousButton(driver).noErrors());
			Assert.assertTrue("Unable to tap favorites menu item!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.FAVORITES).noErrors());
		};
		
		testFavorites(() -> testPathToNearYouAndPlayItem(SignInType.SIGN_UP), goToFavorites);
	}
	
	private static void testPathToNearYouAndPlayItem (SignInType type) {
		if (type == SignInType.LOG_IN) {
			Assert.assertTrue("Unable to log in!", Page.logIn(driver, true).noErrors());
		}
		else {
			Assert.assertTrue("Unable to sign up!", Page.signUp(driver, true).noErrors());
		}
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		Assert.assertTrue("Unable to tap NEAR YOU!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.NEAR_YOU).noErrors());
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 1).noErrors());
	}
	
}
