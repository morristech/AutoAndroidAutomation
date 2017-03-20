package tests.sanity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import Pages.Page;
import Utilities.TestRoot;

public class TestSignUpLogIn extends TestRoot {
	
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
	
	@Test
	public void testSignUp () {
		String randomlyGeneratedEmail = Pages.SignUp.generateEmailAddress();
		
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUpLogInGate.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Cannot enter e-mail!", Pages.SignUp.enterEmail(driver, randomlyGeneratedEmail).noErrors());
		Assert.assertTrue("Cannot enter e-mail confirmation!", Pages.SignUp.enterEmailConfirmation(driver, randomlyGeneratedEmail).noErrors());
		Assert.assertTrue("Cannot tap next button!", Pages.SignUp.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot enter password!", Pages.SignUp.enterPassword(driver, "dskjfds564655").noErrors());
		Assert.assertTrue("Cannot enter birth year!", Pages.SignUp.enterBirthYear(driver, "1990").noErrors());
		Assert.assertTrue("Cannot enter zip code!", Pages.SignUp.enterZipCode(driver, "11013").noErrors());
		Assert.assertTrue("Cannot enter gender!", Pages.SignUp.checkGender(driver, Pages.SignUp.Gender.FEMALE).noErrors());
		Assert.assertTrue("Cannot check agree!", Pages.SignUp.checkAgree(driver).noErrors());
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUp.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Unable to select first genre item and continue!", Pages.GenrePicker.selectFirstGenreItemAndContinue(driver).noErrors());
		
		// We should be at Connection Gate. Make sure that we are in the right page.
		Assert.assertTrue("Options button not visible!", isVisible(Pages.ConnectionGate.getOptionsButton(driver)));
		Assert.assertTrue("Help button not visible!", isVisible(Pages.ConnectionGate.getHelpButton(driver)));
	}
	
	@Test
	public void testLogInAndOptionAndHelp () {
		Assert.assertTrue("Unable to tap Log In Button!", Pages.SignUpLogInGate.tapLogInButton(driver).noErrors());
		Assert.assertTrue("Unable to enter e-mail!", Page.enterEmail(driver, TestRoot.IHEARTUSERNAME).noErrors());
		Assert.assertTrue("Unable to enter password!", Page.enterPassword(driver, TestRoot.IHEARTPASSWORD).noErrors());
		Assert.assertTrue("Unable to tap log in button!", Pages.LogIn.tapLogInButton(driver).noErrors());
		Assert.assertTrue("Unable to select first genre item and continue!", Pages.GenrePicker.selectFirstGenreItemAndContinue(driver).noErrors());
		Assert.assertTrue("Unable to tap options button!", Pages.ConnectionGate.tapOptionsButton(driver).noErrors());
		Assert.assertTrue("Unable to tap done button!", Pages.Options.tapDoneButton(driver).noErrors());
		Assert.assertTrue("Unable to tap help button!", Pages.ConnectionGate.tapHelpButton(driver).noErrors());
		Assert.assertTrue("Unable to tap close button!", Pages.ConnectionGate.tapCloseButton(driver).noErrors());
	}
	
}
