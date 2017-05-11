package tests.sanity;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import testUtilities.CategoryInterfaces.S7PR;
import testUtilities.CategoryInterfaces.S7StableSanity;
import testUtilities.CategoryInterfaces.Sanity;
import testUtilities.CategoryInterfaces.StableSanity;
import testUtilities.TestUtilities;

public class TestInstallationAndFUX extends TestUtilities{
	
	@Test
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testInstall () {
		Assert.assertTrue("Cannot tap sign up button!", Pages.SignUpLogInGate.tapSignUpButton(driver).noErrors());
		Assert.assertTrue("Cannot find confirmation e-mail edit text!", isVisible(Pages.SignUp.getEmailConfirmEditText(driver)));
		Assert.assertTrue("Cannot tap cancel button!", Pages.SignUp.tapCancelButton(driver).noErrors());
		 
		Assert.assertTrue("Cannot tap log in button!", Pages.SignUpLogInGate.tapLogInButton(driver).noErrors());
		Assert.assertTrue("Cannot find password edit text!", isVisible(Pages.LogIn.getPasswordEditText(driver)));
		Assert.assertTrue("Cannot tap cancel button!", Pages.LogIn.tapCancelButton(driver).noErrors());
		
		Assert.assertTrue("Cannot tap maybe later button!", Pages.SignUpLogInGate.tapMaybeLaterButton(driver).noErrors());
		Assert.assertTrue("Cannot find continue button!", isVisible(Pages.GenrePicker.getContinueButton(driver)));
	}
	
	@Test
	@Category({Sanity.class, S7StableSanity.class, S7PR.class})
	public void testGenreGameStartUpFlow () {
		Assert.assertTrue("Cannot tap maybe later button!", Pages.SignUpLogInGate.tapMaybeLaterButton(driver).noErrors());
		
		int row = 0;
		for (int column = 0; column <= 1; column++) {
			Assert.assertTrue(String.format("Cannot select genre item with row: %d and column: %d.", row, column), 
				Pages.GenrePicker.selectGenre(driver, row, column).noErrors());
			Assert.assertTrue(String.format("Error! Genre item with row: %d and column: %d is not selected!", row, column),
				Pages.GenrePicker.isGenreItemSelected(driver, row, column));
		}
		
		Assert.assertTrue("Cannot tap continue!", Pages.GenrePicker.tapContinueButton(driver).noErrors());
		
	}
	
	
	
}
