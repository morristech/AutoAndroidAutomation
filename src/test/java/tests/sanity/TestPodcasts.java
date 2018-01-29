package tests.sanity;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.Menu;
import Pages.Player;
import Pages.Player.PlayerButton;
import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.StablePR;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.CategoryInterfaces.UnstablePR;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.Flaky;

public class TestPodcasts extends TestUtilities {

	@Test
	@Flaky
	@Category({Sanity.class, UnstableSanity.class, UnstablePR.class})
	public void testNavigateAndStreamPodcast () {
		Assert.assertTrue("Unable to log in!", Pages.SignUpLogInGate.logIn(driver, true).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());		
		checkMainMenuItems();
		Assert.assertTrue("Cannot tap podcast!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.PODCASTS).noErrors());
		
		
		// Check if all Podcast topic items are present
		// Currently removing this part of the test because the categories change too often.
//		List<String> expectedItems = Pages.Menu.getPodcastMenuItemTextList();
//		List<String> actualItems = getAllItemTextOnMultiplePages(driver, 5);
//		
//		int numMissing = getNumOfMissingItems(expectedItems, actualItems);
//		String errorMessage = String.format("Missing %d menu items: %s", numMissing, getMissingItemsString(expectedItems, actualItems));
//		Assert.assertEquals(errorMessage, 0, numMissing);
		
		// Test Podcast Item
		Assert.assertTrue("Unable to tap podcast topic!", Menu.tapItem(driver, 0, 1).noErrors());
		Assert.assertTrue("Unable to tap podcast item!", Menu.tapItem(driver, 0).noErrors());
		Assert.assertTrue("Error! Not on player screen for some reason!", isVisible(Player.getCoverImage(driver)));
	}
	
	@Test
	@Flaky
	@Ignore
	@Category({Sanity.class, UnstableSanity.class})
	public void testPodcastSeek () {
		testPathToPodcastAndPlayItem(SignInType.LOG_IN);
		int numRedPixelsBegin = Player.getNumOfRedPixelsOnProgressBar(driver);
		Assert.assertTrue("Unable to tap +30 seconds!", Player.tapPlayerButton(driver, PlayerButton.PLUS_THIRTY_SECONDS).noErrors());
		int numRedPixelsPlusThirty = Player.getNumOfRedPixelsOnProgressBar(driver);
		Assert.assertTrue("Unable to tap -15 seconds!", Player.tapPlayerButton(driver, PlayerButton.MINUS_FIFTHTEEN_SECONDS).noErrors());
		int numRedPixelsMinusFifthteen = Player.getNumOfRedPixelsOnProgressBar(driver);
		
		Assert.assertTrue("More red pixels in the beginning than after tapping +30 seconds!", numRedPixelsBegin < numRedPixelsPlusThirty);
		Assert.assertTrue("More red pixels after tapping -15 seconds than before!", numRedPixelsMinusFifthteen < numRedPixelsPlusThirty);
		Assert.assertTrue("More red pixels in beginning than +30 follow by -15 secs!", numRedPixelsBegin < numRedPixelsMinusFifthteen);
	}
	
	@Test
	@Ignore
	@Flaky
	@Category({Sanity.class, UnstableSanity.class})
	public void testPreviewFeaturesForPodcast () {
		testPreviewFeatures(() -> testPathToPodcastAndPlayItem(SignInType.LOG_IN));
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, UnstableSanity.class})
	public void testFavoritesForPodcasts () {
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
		testSignIn(type);
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap podcast!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.PODCASTS).noErrors());
		Assert.assertTrue("Unable to tap podcast topic!", Menu.tapItem(driver, 0, 1).noErrors());
		Assert.assertTrue("Unable to tap podcast item!", Menu.tapItem(driver, 0).noErrors());
	}
	
}
