package tests.sanity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.Menu;
import Pages.Page;
import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.StablePR;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.Flaky;

public class TestLiveStations extends TestUtilities {
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testLiveStationMainMenuItems () {
		Assert.assertTrue("Unable to log in!", Pages.SignUpLogInGate.logIn(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());		
		checkMainMenuItems();
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		
		// Check if all Live Radio items are present
		List<String> expectedLiveRadioItems = Pages.Menu.getLiveRadioMenuItemTextList();
		List<String> actualLiveRadioItems = Pages.Menu.getAllItemTextOnScreen(driver);
		int numMissing = getNumOfMissingItems(expectedLiveRadioItems, actualLiveRadioItems);
		String errorMessage = String.format("Missing %d menu items: %s", numMissing, getMissingItemsString(expectedLiveRadioItems, actualLiveRadioItems));
		Assert.assertEquals(errorMessage, 0, numMissing);
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testNearYou () {
		testPathToLiveRadio();
		Assert.assertTrue("Unable to tap Near You!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.NEAR_YOU).noErrors());
		String stationName = Pages.Menu.getItemTitle(driver, 1);
		Assert.assertTrue(String.format("Unable to tap station %s.", stationName), Pages.Menu.tapItem(driver, 1).noErrors());
		
		String stationCurrentlyPlaying = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertTrue(String.format("Stations don't match! Expected: %s Actual: %s.", stationName, stationCurrentlyPlaying),
							isSameStation(stationName, stationCurrentlyPlaying));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testByLocation () {
		testPathToLiveRadio();
		Assert.assertTrue("Unable to tap By Location!", Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.BY_LOCATION).noErrors());
		Assert.assertTrue("Unable to tap first item", Menu.tapItem(driver, 0).noErrors());
		Assert.assertTrue("Unable to tap state item!", Menu.tapItem(driver, 0, 0).noErrors());
		Assert.assertTrue("Unable to tap city item!", Menu.tapItem(driver, 0, 0).noErrors());
		Assert.assertTrue("Unable to tap station item!", Menu.tapItem(driver, 0).noErrors());
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testByGenreForLiveStations () {
		testPathToLiveRadio();
		
		// Check if By Genre items are all present
		Assert.assertTrue("Unable to tap By Genre!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.BY_GENRE).noErrors());
		List<String> expectedByGenreItems = Pages.Menu.getLiveRadioByGenreMenuItemTextList();
		List<String> actualByGenreItems = getAllItemTextOnMultiplePages(driver, 5);
		int numMissing = getNumOfMissingItems(expectedByGenreItems, actualByGenreItems);
		String errorMessage = String.format("Missing %d By Genre items: %s", numMissing, getMissingItemsString(expectedByGenreItems, actualByGenreItems));
		Assert.assertEquals(errorMessage, 0, numMissing);
		
		Assert.assertTrue("Unable to tap EDM", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioByGenreMenuItem.EDM).noErrors());
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testScanForLiveStations () {
		testPathToNearYouAndPlayItem(SignInType.LOG_IN);
		
		String stationNameBeforeScan = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertTrue("Unable to tap scan button!", Pages.Player.tapPlayerButton(driver, Pages.Player.PlayerButton.SCAN).noErrors());
		String stationNameAfterScan = Pages.Player.getPlayerMetaLineText(driver, 1);
		Assert.assertFalse("Error! Station name is still the same after scan!", isSameStation(stationNameBeforeScan, stationNameAfterScan));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testPreviewFeaturesForLiveStations () {
		testPreviewFeatures(() -> testPathToNearYouAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testCreateStationForLiveStations () {
		testCreateStation(() -> testPathToNearYouAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testThumbsForLiveStations () {
		testThumbs(() -> testPathToNearYouAndPlayItem(SignInType.LOG_IN), TestType.LIVE_STATIONS);
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
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
		testSignIn(type);
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		Assert.assertTrue("Unable to tap NEAR YOU!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.NEAR_YOU).noErrors());
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 1).noErrors());
	}
	
	private static void testPathToLiveRadio () {
		Assert.assertTrue("Unable to log in!", Pages.SignUpLogInGate.logIn(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());		
		Assert.assertTrue("Unable to tap next button!", Pages.Player.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
	}
	
}
