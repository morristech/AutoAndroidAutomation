package Pages;

import org.openqa.selenium.By;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import testCommons.Errors;

public class GenrePicker extends Page {
	
	/* README: There's a weird bug that sometimes (not always) occurs when you select, deselect and select the same genre item.
	 * During the second select, Appium sometimes detect the check image even though it should not be there after the deselect.
	 * This occurs even though we visually cannot see the check image. I think the problem is with Appium because the failure is inconsistent.
	 */

	private enum Mode {
		SELECT,
		DESELECT
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String genreItemNameId = Page.connectId + "item_name";
	private static String genreItemCheckId = Page.connectId + "item_check";
	private static String continueButtonId = Page.connectId + "continue_button";
	private static String cancelButtonId = Page.connectId + "cancel_button";
	
	/*******************/
	/* *** Getters *** */
	/*******************/

	public static AndroidElement getGenreItem (AndroidDriver<MobileElement> d, int row, int column) {
		return getCardItem(d, row, column);
	}
	
	public static AndroidElement getGenreItemCheck (AndroidDriver<MobileElement> d, int row, int column) {
		AndroidElement genreItem = getGenreItem(d, row, column);
		return findChildElement(d, genreItem, By.id(genreItemCheckId));
	}
	
	public static AndroidElement getContinueButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(continueButtonId), 7);
	}
	
	public static AndroidElement getCancelButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(cancelButtonId), 7);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	/********************* Select Or Deselect A Single Genre *********************/
	
	private static Errors selectOrDeselectGenre (AndroidDriver<MobileElement> d, int row, int column, Mode mode) {
		Errors errs = new Errors();
		boolean shouldClick = true;
		
		switch (mode) {
			case SELECT: 
				shouldClick = !isGenreItemSelected(d, row, column);
				break;
			case DESELECT:
				shouldClick = isGenreItemSelected(d, row, column);
				break;
		}
		
		if (shouldClick) {
			String errorMessage = String.format("Could not %s genre element with row: %d and column: %d!", mode.toString(), row, column);
			errs.add(d, click(d, getGenreItem(d, row, column), errorMessage, "selectOrDeselectGenre"));
		}
		
		return errs;
	}
	
	public static Errors selectGenre (AndroidDriver<MobileElement> d, int row, int column) {
		return selectOrDeselectGenre(d, row, column, Mode.SELECT);
	}
	
	public static Errors deselectGenre (AndroidDriver<MobileElement> d, int row, int column) {	
		return selectOrDeselectGenre(d, row, column, Mode.DESELECT);
	}
	
	public static Errors tapContinueButton (AndroidDriver<MobileElement> d) {
		return click(d, getContinueButton(d), "Cannot tap on continue button!", "tapContinueButton");
	}

	public static Errors tapCancelButton (AndroidDriver<MobileElement> d) {
		return click(d, getCancelButton(d), "Cannot tap on cancel button!", "tapCancelButton");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getGenreItemName (AndroidDriver<MobileElement> d, int row, int column) {
		AndroidElement genreItem = getGenreItem(d, row, column);
		AndroidElement nameTextView = findChildElement(d, genreItem, By.id(genreItemNameId));
		return getText(d, nameTextView);
	}
	
	public static boolean isGenreItemSelected (AndroidDriver<MobileElement> d, int row, int column) {
		return isVisible(getGenreItemCheck(d, row, column));
	}
	
	public static Errors selectFirstGenreItemAndContinue (AndroidDriver<MobileElement> d) {
		Errors err = new Errors();
		err.add(d, selectGenre(d, 0, 0));
		err.add(d, tapContinueButton(d));
		return err;
	}
}
