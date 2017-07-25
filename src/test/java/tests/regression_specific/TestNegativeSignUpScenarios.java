package tests.regression_specific;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.GenrePicker;
import testCommons.Account;
import testCommons.AccountBuilder;
import testUtilities.CategoryInterfaces.RegressionSpecific;
import testUtilities.TestUtilities;

public class TestNegativeSignUpScenarios extends TestUtilities {

	@Test
	@Category({RegressionSpecific.class})
	public void testSignUpWithInvalidInfo () {
		Account account = new AccountBuilder().build();
		
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUpLogInGate.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Cannot enter e-mail!", Pages.SignUp.enterEmail(driver, account.email).noErrors());
		Assert.assertTrue("Cannot enter e-mail confirmation!", Pages.SignUp.enterEmailConfirmation(driver, account.email + "o").noErrors());
		Assert.assertTrue("Cannot tap next button!", Pages.SignUp.tapNextButton(driver).noErrors());
		Assert.assertNotNull("Cannot find e-mail edit text!", Pages.SignUp.getEmailEditText(driver));		
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testSignUpUnder18YearsOld () {
		Account account = new AccountBuilder().setBirthYear("2010").build();
		
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
		
		Assert.assertNull("Error! In genre picker page even though we didn't check terms of use!", GenrePicker.getGenreItem(driver, 0,0));
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testNotCheckedTermsOfUse () {
		Account account = new AccountBuilder().build();
		
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUpLogInGate.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Cannot enter e-mail!", Pages.SignUp.enterEmail(driver, account.email).noErrors());
		Assert.assertTrue("Cannot enter e-mail confirmation!", Pages.SignUp.enterEmailConfirmation(driver, account.email).noErrors());
		Assert.assertTrue("Cannot tap next button!", Pages.SignUp.tapNextButton(driver).noErrors());
		Assert.assertTrue("Cannot enter password!", Pages.SignUp.enterPassword(driver, account.password).noErrors());
		Assert.assertTrue("Cannot enter birth year!", Pages.SignUp.enterBirthYear(driver, account.birthYear).noErrors());
		Assert.assertTrue("Cannot enter zip code!", Pages.SignUp.enterZipCode(driver, account.zip).noErrors());
		Assert.assertTrue("Cannot enter gender!", Pages.SignUp.checkGender(driver, account.gender).noErrors());
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUp.tapSignUpButton(driver).noErrors());
		
		Assert.assertNull("Error! In genre picker page even though we didn't check terms of use!", GenrePicker.getGenreItem(driver, 0,0));
	}
}
