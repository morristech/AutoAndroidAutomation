package tests.sanity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.GenrePicker;
import Pages.Page;
import testCommons.Account;
import testCommons.AccountBuilder;
import testUtilities.CategoryInterfaces.StablePR;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.TestUtilities;

public class TestSignUpLogIn extends TestUtilities {
	
	@Test
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testSignUp () {
		Account account = new AccountBuilder().build();
		
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUpLogInGate.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Cannot enter e-mail!", Pages.SignUp.enterEmail(driver, account.email).noErrors());
		Assert.assertTrue("Cannot enter e-mail confirmation!", Pages.SignUp.enterEmailConfirmation(driver, account.email).noErrors());
		Assert.assertTrue("Cannot tap next button!", Pages.SignUp.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot enter password!", Pages.SignUp.enterPassword(driver, account.password).noErrors());
		Assert.assertTrue("Cannot enter birth year!", Pages.SignUp.enterBirthYear(driver, account.birthYear).noErrors());
		Assert.assertTrue("Cannot enter zip code!", Pages.SignUp.enterZipCode(driver, account.zip).noErrors());
		Assert.assertTrue("Cannot enter gender!", Pages.SignUp.checkGender(driver, account.gender).noErrors());
		Assert.assertTrue("Cannot check agree!", Pages.SignUp.checkAgree(driver).noErrors());
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUp.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Unable to select first genre item and continue!", Pages.GenrePicker.selectFirstGenreItemAndContinue(driver).noErrors());
		
		// We should be at Connection Gate. Make sure that we are in the right page.
		Assert.assertTrue("Options button not visible!", isVisible(Pages.ConnectionGate.getOptionsButton(driver)));
		Assert.assertTrue("Help button not visible!", isVisible(Pages.ConnectionGate.getHelpButton(driver)));
	}
	
	@Test
	@Category({Sanity.class, StableSanity.class, StablePR.class})
	public void testLogInAndOptionAndHelp () {
		Assert.assertTrue("Unable to tap Log In Button!", Pages.SignUpLogInGate.tapLogInButton(driver).noErrors());
		Assert.assertTrue("Unable to enter e-mail!", Page.enterEmail(driver, FREE_ACCOUNT.email).noErrors());
		Assert.assertTrue("Unable to enter password!", Page.enterPassword(driver, FREE_ACCOUNT.password).noErrors());
		Assert.assertTrue("Unable to tap log in button!", Pages.LogIn.tapLogInButton(driver).noErrors());
		Assert.assertTrue("Unable to select first genre item and continue!", Pages.GenrePicker.selectFirstGenreItemAndContinue(driver).noErrors());
		Assert.assertTrue("Unable to tap options button!", Pages.ConnectionGate.tapOptionsButton(driver).noErrors());
		Assert.assertTrue("Unable to tap done button!", Pages.Options.tapDoneButton(driver).noErrors());
		Assert.assertTrue("Unable to tap help button!", Pages.ConnectionGate.tapHelpButton(driver).noErrors());
		Assert.assertTrue("Unable to tap close button!", Pages.ConnectionGate.tapCloseButton(driver).noErrors());
	}
	
}
