package tests.sanity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.StablePR;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.UnstableSanity;
import testUtilities.Flaky;

public class TestForYou extends TestUtilities {
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testNavigateAndStreamForForYou () {
		testPathToForYou(SignInType.LOG_IN);

		int index = 0;
		String expectedStation = Pages.Menu.getItemTitle(driver, index);
		Assert.assertTrue("Cannot tap For You item!", Pages.Menu.tapItem(driver, index).noErrors());
		String actualStationPlaying = Pages.Player.getPlayerMetaLineText(driver, 1);
		String errorMessage = String.format("Expected %s Actual: %s", expectedStation, actualStationPlaying);
		Assert.assertTrue(errorMessage, isSameStation(expectedStation, actualStationPlaying));
		
	}
	
	@Test
	@Flaky
	@Category({Sanity.class, StableSanity.class})
	public void testPreviewFeaturesForForYou () {
		Runnable testPathToForYouAndPlayItem = () -> {
			testPathToForYou(SignInType.LOG_IN);
			Assert.assertTrue("Cannot tap For You item!", Pages.Menu.tapItem(driver, 0).noErrors());
		};
		testPreviewFeatures(testPathToForYouAndPlayItem);
	}
	
	private void testPathToForYou (SignInType type) {
		testSignIn(type);
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());		
		Assert.assertTrue("Cannot tap For You!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.FOR_YOU).noErrors());
	}
}
