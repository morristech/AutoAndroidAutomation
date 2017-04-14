package tests.sanity;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.Menu;
import Pages.Page;
import Pages.Player;
import Pages.Player.PlayerButton;
import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.Sanity;

public class TestPodcasts extends TestUtilities {

	@Test
	@Category(Sanity.class)
	public void testNavigateAndStreamPodcast () {
		Assert.assertTrue("Unable to log in!", Pages.SignUpLogInGate.logIn(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());		
		checkMainMenuItems();
		Assert.assertTrue("Cannot tap podcast!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.PODCASTS).noErrors());
		
		// Check if all Podcast topic items are present
		List<String> expectedItems = Pages.Menu.getPodcastMenuItemTextList();
		List<String> actualItems = getAllItemTextOnMultiplePages(driver, 3);
		
		int numMissing = getNumOfMissingItems(expectedItems, actualItems);
		String errorMessage = String.format("Missing %d menu items: %s", numMissing, getMissingItemsString(expectedItems, actualItems));
		Assert.assertEquals(errorMessage, 0, numMissing);
		
		// Test Podcast Item
		Assert.assertTrue("Unable to tap podcast topic!", Menu.tapItem(driver, 0, 0).noErrors());
		Assert.assertTrue("Unable to tap podcast item!", Menu.tapItem(driver, 0).noErrors());
		Assert.assertTrue("Error! Not on player screen for some reason!", isVisible(Player.getCoverImage(driver)));
	}
	
	@Test
	@Category(Sanity.class)
	public void testUnlimitedSkipsForPodcast () {
		testPathToPodcastAndPlayItem(SignInType.LOG_IN);
		
		int MAX_TRIES = 10;
		for (int i = 0; i < MAX_TRIES; i++) {
			Assert.assertTrue("Unable to tap skip!", Player.tapPlayerButton(driver, PlayerButton.SKIP).noErrors());
			String popUpText = Page.getCustomDialogText(driver);
			Assert.assertTrue(String.format("Unexpected popup with text: %s", popUpText), Page.getCustomDialogText(driver).length() == 0);
		}
	}
	
	@Test
	@Category(Sanity.class)
	public void testPodcastSeek () {
		testPathToPodcastAndPlayItem(SignInType.LOG_IN);
		int numRedPixelsBegin = Player.getNumOfRedPixelsOnProgressBar(driver);
		Assert.assertTrue("Unable to tap +30 seconds!", Player.tapPlayerButton(driver, PlayerButton.PLUS_THIRTY_SECONDS).noErrors());
		int numRedPixelsPlusThirty = Player.getNumOfRedPixelsOnProgressBar(driver);
		Assert.assertTrue("Unable to tap -10 seconds!", Player.tapPlayerButton(driver, PlayerButton.MINUS_TEN_SECONDS).noErrors());
		int numRedPixelsMinusTen = Player.getNumOfRedPixelsOnProgressBar(driver);
		
		Assert.assertTrue("More red pixels in the beginning than after tapping +30 seconds!", numRedPixelsBegin < numRedPixelsPlusThirty);
		Assert.assertTrue("More red pixels after tapping -10 seconds than before!", numRedPixelsMinusTen < numRedPixelsPlusThirty);
		Assert.assertTrue("More red pixels in beginning than +30 follow by -10 secs!", numRedPixelsBegin < numRedPixelsMinusTen);
	}
	
	@Test
	@Category(Sanity.class)
	public void testPreviewFeaturesForPodcast () {
		testPreviewFeatures(() -> testPathToPodcastAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Category(Sanity.class)
	public void testThumbsForPodcast () {
		testThumbs(() -> testPathToPodcastAndPlayItem(SignInType.LOG_IN), TestType.PODCASTS);
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
		
		testFavorites(() -> testPathToPodcastAndPlayItem(SignInType.SIGN_UP), goToFavorites);
	}
	
	
	private static void testPathToPodcastAndPlayItem (SignInType type) {
		if (type == SignInType.LOG_IN) {
			Assert.assertTrue("Unable to log in!", Page.logIn(driver, true).noErrors());
		}
		else {
			Assert.assertTrue("Unable to sign up!", Page.signUp(driver, true).noErrors());
		}
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap podcast!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.PODCASTS).noErrors());
		Assert.assertTrue("Unable to tap podcast topic!", Menu.tapItem(driver, 0, 0).noErrors());
		Assert.assertTrue("Unable to tap podcast item!", Menu.tapItem(driver, 0).noErrors());
	}
	
}
