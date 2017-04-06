package tests.sanity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.*;
import Pages.OptionsSubpage.OptionsOnOff;
import Utilities.TestRoot;
import suites.CategoryInterfaces.Sanity;

public class TestSettings extends TestRoot {

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
	public void testMyLocation () {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		Assert.assertTrue("Unable to select My Location!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.MY_LOCATION).noErrors());
		
		Assert.assertTrue("Unable to turn update automatically off!", MyLocation.tapOptionsSubpageOnOffButton(driver, OptionsOnOff.OFF).noErrors());
		
		String originalCurrentCity = MyLocation.getCurrentCityAndStateText(driver);
		
		// Test Choose A City
		int chosenCityIndex = 0;
		Assert.assertTrue("Unable to tap Choose A City!", MyLocation.scrollAndTapMyLocationItem(driver, UP, MyLocation.MyLocationOption.CHOOSE_A_CITY).noErrors());
		String chosenCity = MyLocation.getChooseACityListItemText(driver, chosenCityIndex);
		Assert.assertTrue("Unable to tap choose a city list item!", MyLocation.tapChooseACityListItem(driver, chosenCityIndex).noErrors());
		Assert.assertTrue("Unable to tap continue button!", MyLocation.tapWhiteDialogButton(driver).noErrors()); // Continue Button
		Assert.assertTrue("Unable to scroll to current city!", isVisible(MyLocation.scrollToGetMyLocationItem(driver, DOWN, MyLocation.MyLocationOption.CURRENT_CITY)));
		String currentCityForChooseACity = MyLocation.getCurrentCityAndStateText(driver);
		Assert.assertEquals(String.format("Cities don't match! Expected: %s Actual: %s.", chosenCity, currentCityForChooseACity), chosenCity, currentCityForChooseACity);
		
		// Test Enter Zip
		Assert.assertTrue("Cannot tap Enter ZIP Code!", MyLocation.scrollAndTapMyLocationItem(driver, UP, MyLocation.MyLocationOption.ENTER_ZIP_CODE).noErrors());;
		Assert.assertTrue("Unable to enter ZIP!", MyLocation.enterZIPAndContinue(driver, "96701").noErrors());
		Assert.assertTrue("Unable to scroll to current city!", isVisible(MyLocation.scrollToGetMyLocationItem(driver, DOWN, MyLocation.MyLocationOption.CURRENT_CITY)));
		String currentCityForEnterZIP = MyLocation.getCurrentCityAndStateText(driver);
		Assert.assertNotEquals(String.format("City didn't change! Actual: %s.", currentCityForEnterZIP), currentCityForChooseACity, currentCityForEnterZIP);
		
		// Turn on Update Automatically
		Assert.assertTrue("Unable to turn update automatically on!", MyLocation.tapOptionsSubpageOnOffButton(driver, OptionsOnOff.ON).noErrors());
		Assert.assertTrue("Unable to tap continue button!", MyLocation.tapWhiteDialogButton(driver).noErrors()); // Continue Button
		String currentCityForUpdateAutomatically = MyLocation.getCurrentCityAndStateText(driver);
		Assert.assertEquals(String.format("Cities don't match! Expected: %s Actual: %s", originalCurrentCity, currentCityForUpdateAutomatically), originalCurrentCity, currentCityForUpdateAutomatically);
		
		Assert.assertTrue("Cannot tap back button!", MyLocation.tapOptionsBackButton(driver).noErrors());
	}
	
	@Test
	@Category(Sanity.class)
	public void testResetPassword () {
		String email;
		
		Assert.assertTrue("Unable to sign up!", Page.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap options button!", ConnectionGate.tapOptionsButton(driver).noErrors());
		email = Options.getLoggedInEmail(driver);
		
		// Reset Password
		Assert.assertTrue("Unable to tap on update password!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.UPDATE_PASSWORD).noErrors());
		Assert.assertTrue("Unable to click on forgot password link!", UpdateResetPassword.tapForgotPasswordLink(driver).noErrors());
		Assert.assertTrue("Unable to enter email!", UpdateResetPassword.enterEmail(driver, email).noErrors());
		Assert.assertTrue("Unable to tap reset button!", UpdateResetPassword.tapResetPasswordButton(driver).noErrors());
		Assert.assertTrue("Unable to tap OK!", UpdateResetPassword.tapWhiteDialogButton(driver).noErrors()); // Okay button
		
		// Verify that we are in the sign up log in gate.
		Assert.assertTrue("Unable to see maybe later!", isVisible(SignUpLogInGate.getMaybeLaterButton(driver)));
	}
	
	@Test
	@Category(Sanity.class)
	public void testUpdatePassword () {
		String email;
		String newPassword = "abc123";
		
		Assert.assertTrue("Unable to sign up!", Page.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap options button!", ConnectionGate.tapOptionsButton(driver).noErrors());
		email = Options.getLoggedInEmail(driver);
		
		// Change Password
		Assert.assertTrue("Unable to tap on update password!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.UPDATE_PASSWORD).noErrors());
		Assert.assertTrue("Unable to enter current password!", UpdateResetPassword.enterCurrentPassword(driver, TestRoot.NEWACCOUNTPASSWORD).noErrors());
		Assert.assertTrue("Unable to enter new password!", UpdateResetPassword.enterNewPassword(driver, newPassword).noErrors());
		Assert.assertTrue("Unable to enter new password confirmation!", UpdateResetPassword.enterConfirmNewPassword(driver, newPassword).noErrors());
		Assert.assertTrue("Unable to press update!", UpdateResetPassword.tapUpdatePasswordButton(driver).noErrors());
		
		Page.waitForDialogToDisappear(driver); // Wait for modal to disappear.
		
		// Logging Out
		Assert.assertTrue("Unable to log out!", Options.logOut(driver).noErrors());
		
		// Logging Back In
		Assert.assertTrue("Unable to tap Sign Up or Log In!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.SIGN_UP_OR_LOG_IN).noErrors());
		Assert.assertTrue("Unable to tap Log In Button!", Pages.SignUpLogInGate.tapLogInButton(driver).noErrors());
		Assert.assertTrue("Unable to enter e-mail!", Page.enterEmail(driver, email).noErrors());
		Assert.assertTrue("Unable to enter password!", Page.enterPassword(driver, newPassword).noErrors());
		Assert.assertTrue("Unable to tap log in button!", Pages.LogIn.tapLogInButton(driver).noErrors());
		
		// Verify that we are at the options page after logging back in.
		Assert.assertTrue("Unable to find option's done button!", isVisible(Options.getDoneButton(driver)));
	}
	
	@Test
	@Category(Sanity.class)
	public void testExplicitContentOff () {
		setExplicitContentAndGoToPop(OptionsOnOff.OFF);
		Assert.assertTrue("Unable to tap first item!", Menu.tapItem(driver, 0).noErrors());
		Assert.assertTrue("Unable to click cancel button!", Menu.tapWhiteDialogButton(driver).noErrors()); // Cancel button
		Assert.assertTrue("Unable to tap first item!", Menu.tapItem(driver, 0).noErrors());
		Assert.assertTrue("Unable to click options button!", Menu.tapRedDialogButton(driver).noErrors()); // Options button
	}
	
	@Test
	@Category(Sanity.class)
	public void testExplicitContentOn () {
		setExplicitContentAndGoToPop(OptionsOnOff.ON);
		Assert.assertTrue("Unable to tap first item!", Menu.tapItem(driver, 0).noErrors());
		Assert.assertFalse("Error! Options button is visible for some reason!", isVisible(Menu.getRedDialogButton(driver))); // Options button
	}
	
	
	private void setExplicitContentAndGoToPop (OptionsOnOff option) {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		Assert.assertTrue("Unable to select Explicit Content!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.EXPLICIT_CONTENT).noErrors());
		
		// Explicit Content ON is default for newly created accounts.
		if (option == OptionsOnOff.OFF) {
			Assert.assertTrue("Unable to switch explicit content to off!", OptionsSubpage.tapOptionsSubpageOnOffButton(driver, option).noErrors());
		}
		Assert.assertTrue("Cannot tap back button!", MyLocation.tapOptionsBackButton(driver).noErrors());
		Assert.assertTrue("Unable to tap done button!", Options.tapDoneButton(driver).noErrors());
		Assert.assertTrue("Unable to bypass connection page!", ConnectionGate.byPassAndAcceptDisclaimer(driver).noErrors());

		// Player
		Assert.assertTrue("Cannot tap menu button!", Pages.Player.tapMenuButton(driver).noErrors());
		Assert.assertTrue("Unable to tap next!", Pages.Menu.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot tap artist radio!", Pages.Menu.tapMenuItem(driver, Pages.Menu.MainMenuItem.ARTIST_RADIO).noErrors());
		Assert.assertTrue("Unable to tap Pop!", Pages.Menu.tapMenuItem(driver, Pages.Menu.ArtistRadioByGenreMenuItem.POP).noErrors());
	}
}
