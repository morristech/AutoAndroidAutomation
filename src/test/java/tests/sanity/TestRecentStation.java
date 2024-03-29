package tests.sanity;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.*;
import testUtilities.CategoryInterfaces.StablePR;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.Flaky;
import testUtilities.TestUtilities;

public class TestRecentStation extends TestUtilities {

	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testNavigateAndStreamForRecentStations () {
		Assert.assertTrue("Unable to log in!", Page.logIn(driver, true).noErrors());
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap Recent Station!", Menu.tapMenuItem(driver, Menu.MainMenuItem.RECENT_STATIONS).noErrors());
		
		int index = 1;
		String expectedStationName = Menu.getItemTitle(driver, index);
		Assert.assertTrue("Unable to tap recent station item!", Menu.tapItem(driver, index).noErrors());
		String actualStationNameInPlayer = Player.getPlayerMetaLineText(driver, 1);
		String errorMessage = String.format("Station names do not match! Expected: %s Actual: %s", expectedStationName, actualStationNameInPlayer);
		Assert.assertTrue(errorMessage, isSameStation(expectedStationName, actualStationNameInPlayer));
		
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());
		String expectedStationNameInPositionZero = Menu.getItemTitle(driver, 0);
		errorMessage = String.format("Station not in left most position! Expected: %s Actual: %s", expectedStationNameInPositionZero, expectedStationName);
		Assert.assertTrue(errorMessage, isSameStation(expectedStationNameInPositionZero, expectedStationName));
	}
	
	@Test
	@Flaky
	@Ignore
	@Category({Sanity.class, StableSanity.class})
	public void testPreviewFeaturesForRecentStations () {
		testPreviewFeatures(() -> testGoToRecentStationAndPlayItem(SignInType.LOG_IN));
	}
	
	private void testGoToRecentStationAndPlayItem (SignInType type) {
		testSignIn(type);
		Assert.assertTrue("Unable to tap menu", Player.tapMenuButton(driver).noErrors());	
		Assert.assertTrue("Unable to tap Recent Station!", Menu.tapMenuItem(driver, Menu.MainMenuItem.RECENT_STATIONS).noErrors());	
		Assert.assertTrue("Unable to tap recent station item!", Menu.tapItem(driver, 0).noErrors());
	}
}
