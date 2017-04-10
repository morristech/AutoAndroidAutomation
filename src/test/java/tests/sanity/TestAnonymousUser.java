package tests.sanity;

import java.util.function.BiConsumer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.ConnectionGate;
import Pages.GenrePicker;
import Pages.Menu;
import Pages.Page;
import Pages.Player;
import Pages.SignUpLogInGate;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.TestCommons;

public class TestAnonymousUser extends TestCommons {

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
	
	@Test
	@Category(Sanity.class)
	public void testPlayerBehavior () {
		testMaybeLaterToMenu();
		Assert.assertTrue("Cannot tap live radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.LIVE_RADIO).noErrors());
		Assert.assertTrue("Unable to tap NEAR YOU!", Pages.Menu.tapMenuItem(driver, Pages.Menu.LiveRadioMenuItem.NEAR_YOU).noErrors());
		Assert.assertTrue("Unable to tap station", Pages.Menu.tapItem(driver, 1).noErrors());
		
		BiConsumer<Integer, String> waitForChange = (maxSleepTime, oldText) -> {
			double sleepTime = 250.0;
			int maxLoops = (int) Math.ceil(maxSleepTime / 250.0);
			for (int i = 0; i < maxLoops; i++) {
				sleep( (long) sleepTime);
				if (!Player.getPlayerMetaLineText(driver, 1).equals(oldText)) {
					break;
				}
			}
		};
		
		/*** Test Player ***/
		
		// Test Scan
		String textBeforeScan = Player.getPlayerMetaLineText(driver, 1);
		Assert.assertTrue("Unable to tap scan!", Player.tapPlayerButton(driver, Player.PlayerButton.SCAN).noErrors());
		waitForChange.accept(5000, textBeforeScan); // Wait for scan
		String textAfterScan = Player.getPlayerMetaLineText(driver, 1);
		Assert.assertNotEquals("Text are equal before and after scan!", textBeforeScan, textAfterScan);
		
		waitForChange.accept(10000, textAfterScan); // Wait so that we get a new station due to the scan not stopping until we press scan again
		String textAfterScanWait = Player.getPlayerMetaLineText(driver, 1);
		Assert.assertNotEquals("Text are equal before and after scan wait!", textAfterScan, textAfterScanWait);
		Assert.assertTrue("Unable to tap scan!", Player.tapPlayerButton(driver, Player.PlayerButton.SCAN).noErrors());
		
		// Test Play/Stop Live Station 
		// TODO: Add asserts to test play status if we ever gain the ability to do so.
		Assert.assertTrue("Unable to tap cover image", Player.tapCoverImage(driver).noErrors());
		Assert.assertTrue("Unable to tap cover image", Player.tapCoverImage(driver).noErrors());
		
		// Test Thumbs and Create Station		
		if (!isCommercialPlaying()) {
			Assert.assertTrue("Unable to tap on thumbs!", Player.tapPlayerButton(driver, Player.PlayerButton.THUMBS).noErrors());
			Assert.assertTrue("Unable to tap cancel button!", Page.tapWhiteDialogButton(driver).noErrors()); // Cancel button
		}
		else {
			System.out.println("Cannot test thumbs due to commercials or lack of meta data!");
		}
		
		if (!isCommercialPlaying()) {
			Assert.assertTrue("Unable to tap on Create Station!", Player.tapPlayerButton(driver, Player.PlayerButton.CREATE_STATION).noErrors());
			Assert.assertTrue("Unable to tap sign up button!", Page.tapRedDialogButton(driver).noErrors()); // Sign Up button
			Assert.assertTrue("Sign Up button not present!", isVisible(SignUpLogInGate.getSignUpButton(driver)));
		}
		else {
			System.out.println("Cannot test create station due to commercials or lack of meta data!");
		}
	}
	
	@Test
	@Category(Sanity.class)
	public void testRegGateForArtistsAndPodcast () {
		testMaybeLaterToMenu();
		Assert.assertTrue("Cannot tap artist radio!", Menu.tapMenuItem(driver, Menu.MainMenuItem.ARTIST_RADIO).noErrors());
		Assert.assertTrue("Cannot tap Pop!", Menu.tapItem(driver, "Pop").noErrors());
		Assert.assertTrue("Unable to tap station", Menu.tapItem(driver, 1).noErrors());
		
		Page.back(driver);
		
		Assert.assertTrue("Cannot tap menu back button!", Menu.tapMenuBackButton(driver).noErrors());
		Assert.assertTrue("Cannot tap menu back button!", Menu.tapMenuBackButton(driver).noErrors());
		Assert.assertTrue("Cannot tap podcasts!", Menu.tapMenuItem(driver, Menu.MainMenuItem.PODCASTS).noErrors());
		Assert.assertTrue("Cannot tap Featured!", Menu.tapItem(driver, "Featured").noErrors());
		Assert.assertTrue("Unable to tap station", Menu.tapItem(driver, 1).noErrors());
	}
	
	public static void testMaybeLaterToMenu () {
		Assert.assertTrue("Unable to tap Maybe Later!", SignUpLogInGate.tapMaybeLaterButton(driver).noErrors());
		Assert.assertTrue("Unable to select first genre item and continue!", GenrePicker.selectFirstGenreItemAndContinue(driver).noErrors());
		Assert.assertTrue("Unable to bypass and accept disclaimer!", ConnectionGate.byPassAndAcceptDisclaimer(driver).noErrors());
		Assert.assertTrue("Cannot tap menu button!", Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Menu.tapNextButton(driver).noErrors());
	}
}
