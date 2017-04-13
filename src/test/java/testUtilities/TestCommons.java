package testUtilities;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;

import Utilities.TestRoot;

public class TestCommons extends TestRoot {

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
	
	public static void checkMainMenuItems () {
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