package tests.sanity;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.Menu;
import Pages.Page;
import Pages.Player;
import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.StablePR;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.Flaky;

public class TestFavorites extends TestUtilities{

	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testNavigateAndStreamForFavorites () {
		Assert.assertTrue("Unable to log in!", Page.logIn(driver, true).noErrors());
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap Favorites!", Menu.tapMenuItem(driver, Menu.MainMenuItem.FAVORITES).noErrors());
		
		int index = 0;
		String expectedStationName = Menu.getItemTitle(driver, index);
		Assert.assertTrue("Unable to tap favorites item!", Menu.tapItem(driver, index).noErrors());
		String actualStationNameInPlayer = Player.getPlayerMetaLineText(driver, 1);
		String errorMessage = String.format("Station names do not match! Expected: %s Actual: %s", expectedStationName, actualStationNameInPlayer);
		Assert.assertTrue(errorMessage, isSameStation(expectedStationName, actualStationNameInPlayer));
	}
	
	@Test
	@Flaky
	@Ignore
	@Category({Sanity.class, StableSanity.class})
	public void testPreviewFeaturesForFavorites () {
		testPreviewFeatures(() -> goToFavoritesAndPlayItem(SignInType.LOG_IN));
	}
	
	private static void goToFavoritesAndPlayItem (SignInType type) {
		testSignIn(type);
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap Favorites!", Menu.tapMenuItem(driver, Menu.MainMenuItem.FAVORITES).noErrors());
		Assert.assertTrue("Unable to tap favorites item!", Menu.tapItem(driver, 1).noErrors());
	}
}
