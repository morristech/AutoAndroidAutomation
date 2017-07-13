package tests.regression_specific;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import Pages.ConnectionGate;
import Pages.GenrePicker;
import Pages.Options;
import Pages.OptionsSubpage;
import Pages.Page;
import Pages.SignUpLogInGate;
import Pages.UpdateResetPassword;
import testCommons.Account;
import testCommons.AccountBuilder;
import testUtilities.TestUtilities;
import testUtilities.CategoryInterfaces.RegressionSpecific;

public class TestSettings extends TestUtilities {

	@Test
	@Category({RegressionSpecific.class})
	public void testCarConnections () {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		
		Assert.assertTrue("Unable to tap on car connections!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.CAR_CONNECTIONS).noErrors());
		String expectedTitle = "Car Connections";
		String actualTitle = OptionsSubpage.getOptionsTitle(driver);
		Assert.assertEquals(String.format("Expected: %s, Actual: %s", expectedTitle, actualTitle), expectedTitle, actualTitle);
		Assert.assertTrue("Unable to tap back button!", OptionsSubpage.tapOptionsBackButton(driver).noErrors());
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testUpdatePasswordErrorMessage () {
		Account account = new AccountBuilder().build();
		
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, account, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		
		// Change Password
		Assert.assertTrue("Unable to tap on update password!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.UPDATE_PASSWORD).noErrors());
		Assert.assertTrue("Unable to enter current password!", UpdateResetPassword.enterCurrentPassword(driver, account.password + "1").noErrors());
		
		String newPassword = account.password + "a";
		Assert.assertTrue("Unable to enter new password!", UpdateResetPassword.enterNewPassword(driver, newPassword).noErrors());
		Assert.assertTrue("Unable to enter new password confirmation!", UpdateResetPassword.enterConfirmNewPassword(driver, newPassword).noErrors());
		Assert.assertTrue("Unable to press update!", UpdateResetPassword.tapUpdatePasswordButton(driver).noErrors());
		
		String expectedErrorMessage = "The password entered doesn't match our records. Please retry.";
		String actualErrorMessage = Page.getCustomDialogText(driver);
		Assert.assertEquals(String.format("Expected: %s Actual: %s", expectedErrorMessage, actualErrorMessage), expectedErrorMessage, actualErrorMessage);	
	
		Assert.assertTrue("Unable to tap Reset Button!", Page.tapRedDialogButton(driver).noErrors());
		Assert.assertTrue("Unable to find new password confirmation edit text!", isVisible(UpdateResetPassword.getConfirmNewPasswordEditText(driver)));
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testMyGenres () {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		
		Assert.assertTrue("Unable to tap on my genres!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.MY_GENRE).noErrors());
		Assert.assertTrue("Unable to select genre item!", GenrePicker.selectGenre(driver, 1, 1).noErrors());
		Assert.assertTrue("Unable to tap continue button!", GenrePicker.tapContinueButton(driver).noErrors());
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testDeselectGenre () {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		
		Assert.assertTrue("Unable to tap on my genres!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.MY_GENRE).noErrors());
		for (int r = 0; r < 2; r++) {
			for (int c = 0; c < 3; c++) {
				Assert.assertTrue("Unable to deselect item!", GenrePicker.deselectGenre(driver, r, c).noErrors());
			}
		}
		Assert.assertTrue("Unable to tap continue button!", GenrePicker.tapContinueButton(driver).noErrors());
		
		String expectedErrorMessage = "Please select at least one genre to continue.";
		String actualErrorMessage = Page.getCustomDialogText(driver);
		Assert.assertEquals(String.format("Expected: %s, Actual: %s", expectedErrorMessage, actualErrorMessage), expectedErrorMessage, actualErrorMessage);
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testCancelGenreGameCard () {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		Assert.assertTrue("Unable to tap on my genres!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.MY_GENRE).noErrors());
		
		int row = 1;
		int column = 1;
		Assert.assertTrue("Genre Item selected for some reason!", !GenrePicker.isGenreItemSelected(driver, row, column));
		Assert.assertTrue("Unable to select item!", GenrePicker.selectGenre(driver, row, column).noErrors());
		Assert.assertTrue("Genre Item not selected for some reason!", GenrePicker.isGenreItemSelected(driver, row, column));
		Assert.assertTrue("Unable to tap cancel!", GenrePicker.tapCancelButton(driver).noErrors());
		
		Assert.assertTrue("Unable to tap on my genres!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.MY_GENRE).noErrors());
		Assert.assertTrue("Genre Item selected for some reason!", !GenrePicker.isGenreItemSelected(driver, row, column));
	}
	
	@Test
	@Category({RegressionSpecific.class})
	public void testCancelSignUpLogIn () {
		Assert.assertTrue("Unable to sign up!", SignUpLogInGate.signUp(driver, false).noErrors());
		Assert.assertTrue("Unable to tap on Options!", ConnectionGate.tapOptionsButton(driver).noErrors());
		Assert.assertTrue("Unable to tap on Logged In As!", Options.scrollAndTapOptionItem(driver, UP, Options.OptionItem.LOGGED_IN_AS).noErrors());
		Assert.assertTrue("Unable to tap cancel!", Page.tapWhiteDialogButton(driver).noErrors()); // Cancel Button
		Assert.assertTrue("Unable to tap done!", Options.tapDoneButton(driver).noErrors());
		Assert.assertTrue("Options button not visible!", isVisible(ConnectionGate.getOptionsButton(driver)));
	}
}
