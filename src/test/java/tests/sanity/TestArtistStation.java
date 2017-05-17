package tests.sanity;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.Menu;
import Pages.Page;
import Pages.Player;
import Pages.Player.DiscoveryMode;
import Pages.Player.PlayerButton;

import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.S7PR;
import testUtilities.CategoryInterfaces.S7StableSanity;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.Flaky;

public class TestArtistStation extends TestUtilities {
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testNavigateAndStreamByGenreForArtistStations () {
		Assert.assertTrue("Unable to log in!", Page.logIn(driver, true).noErrors());
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());		
		TestLiveStations.checkMainMenuItems();
		
		// Test Artist Radio By Genre Items
		Assert.assertTrue("Unable to tap artist Radio!", Menu.tapMenuItem(driver, Menu.MainMenuItem.ARTIST_RADIO).noErrors());
		List<String> expectedItems = Pages.Menu.getArtistRadioByGenreMenuItemTextList();
		List<String> actualItems = getAllItemTextOnMultiplePages(driver, 3);
		int numMissing = TestLiveStations.getNumOfMissingItems(expectedItems, actualItems);
		String errorMessage = String.format("Missing %d By Genre menu items: %s", numMissing, getMissingItemsString(expectedItems, actualItems));
		Assert.assertEquals(errorMessage, 0, numMissing);
		
		Assert.assertTrue("Unable to tap artist radio by genre item!", Menu.tapItem(driver, 0, 0).noErrors());
		
		int itemIndex = 0;
		String artistName = Menu.getItemTitle(driver, itemIndex); 
		Assert.assertTrue("Unable to tap artist radio item!", Menu.tapItem(driver, itemIndex).noErrors());
		String playingArtistStation = Player.getPlayerMetaLineText(driver, 1);
		errorMessage = String.format("Error! Expected %s. Actual %s!", artistName, playingArtistStation);
		Assert.assertTrue(errorMessage, isSameStation(playingArtistStation, artistName));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class})
	public void testLimitedSkipsForArtistStations () {
		goToArtistRadioAndPlayItem(SignInType.SIGN_UP);
		
		int MAX_SKIP_LIMIT_PLUS_TWO = 8; // Plus one for getting the skip limit popup. An extra one just to be sure.
		for (int i = 0; i < MAX_SKIP_LIMIT_PLUS_TWO; i++) {
			Assert.assertTrue("Unable to tap skip!", Player.tapPlayerButton(driver, PlayerButton.SKIP).noErrors());
			sleep(2000);
		}
		
		String actualDialogText = Page.getCustomDialogText(driver);
		String expectedDialogText = "skip limit"; // There seems to be multiple dialog text. They all have "skip limit" in common so let's go with that.
		Assert.assertTrue(String.format("Unexpected skip limit dialog text: %s.", actualDialogText), actualDialogText.contains(expectedDialogText));
		
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testPreviewFeaturesForArtistStations () {
		testPreviewFeatures(() -> goToArtistRadioAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testCreateStationForArtistStations () {
		testCreateStation(() -> goToArtistRadioAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testThumbsForArtistStations () {
		testThumbs(() -> goToArtistRadioAndPlayItem(SignInType.LOG_IN), TestType.ARTIST_STATIONS);
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testFavoritesForArtistStations () {	
		Runnable goToFavorites = () -> {
			Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
			Assert.assertTrue("Unable to tap menu back button!", Pages.Menu.tapMenuBackButton(driver).noErrors());
			Assert.assertTrue("Unable to tap favorites menu item!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.FAVORITES).noErrors());
		};
		
		testFavorites(() -> goToArtistRadioAndPlayItem(SignInType.SIGN_UP), goToFavorites);
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testDiscoveryForArtistStations () {
		goToArtistRadioAndPlayItem(SignInType.LOG_IN);
		Set<DiscoveryMode> discoveryModes = EnumSet.allOf(DiscoveryMode.class);
		
		for (DiscoveryMode mode : discoveryModes) {
			// Tap button
			String errorMessage = String.format("Unable to tap discovery item: %s", mode.toString());
			Assert.assertTrue(errorMessage, Player.tapDiscoveryModeButton(driver, mode).noErrors());
			
			// Verify message.
			String actualMessage = Page.getCustomDialogText(driver);
			errorMessage = String.format("Error! Expected %s. Actual %s!", mode.getMessage(), actualMessage);
			Assert.assertEquals(errorMessage, mode.getMessage(), actualMessage);
			
			Page.waitForDialogToDisappear(driver);
		}
	}
	
	private static void goToArtistRadioAndPlayItem (SignInType type) {
		testSignIn(type);
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());	
		Assert.assertTrue("Unable to tap next button!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Unable to tap artist Radio!", Menu.tapMenuItem(driver, Menu.MainMenuItem.ARTIST_RADIO).noErrors());	
		Assert.assertTrue("Unable to tap artist radio by genre item!", Menu.tapItem(driver, 0, 0).noErrors());
		Assert.assertTrue("Unable to tap artist radio item!", Menu.tapItem(driver, 0).noErrors());
	}
}
