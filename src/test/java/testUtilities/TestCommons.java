package testUtilities;

import java.util.List;

import org.junit.Assert;

import Utilities.TestRoot;

public class TestCommons extends TestRoot {

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
		int MAX_TRIES = 12;
		while (tries < MAX_TRIES && !isEnabled(Pages.Player.getPlayerButton(driver, Pages.Player.PlayerButton.THUMBS))) {
			Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
			sleep(5000);
			tries++;
			Assert.assertTrue("Cannot tap close menu button!", Pages.Menu.tapMenuCloseButton(driver).noErrors());
		}
		return tries == MAX_TRIES;
	};
}
