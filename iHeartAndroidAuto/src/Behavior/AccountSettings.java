package Behavior;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class AccountSettings extends Page{
	
	public static void openAccountSettings(AndroidDriver<MobileElement> d){
		//Behavior.Navbar.openMenu(d);
		Behavior.Page.clickMenuDrawerAccountSettings(d);
	}
	
/*
	// Returns the email of the account in use
	public static String getAccountEmail(AndroidDriver<MobileElement> d){
		String title = null;
		try{
			title = getText(d, find(Elements.Page.pageTitleX));
		}
		catch(Exception e){}
		if(title != null && (title.equalsIgnoreCase("Alarm"))){
			Behavior.Page.back(d);
		}
		else if(title == null){
			if(waitForVisible(d, find(Elements.SleepTimer.sleepTimerTitleX), 5) != null){
				title = "Sleep Timer";
				Behavior.Page.back(d);
			}
		}
		if(title == null || !title.equals("Account Settings")){
			openAccountSettings(d);
		}
		return getAccountEmailFromAccountSettings(d);
	}
	public static String getAccountEmailFromAccountSettings(AndroidDriver<MobileElement> d){
		MobileElement accountName = TestRoot.waitForVisible(d, find(Elements.AccountSettings.loggedInAsId), 10);
		if(accountName != null)
			return accountName.getText();
		else
			return null;
	}*/
	
	
	
	/**
	 * Test that we're logged in as the specified user
	 * @param d
	 * @param loggedInAsEmail
	 * @return
	 */
	public static boolean checkLoggedIn(AndroidDriver<MobileElement> d, String loggedInAsEmail){
		/*waitForVisible(d, find(Elements.Page.menuDrawerButtonX), 10);
		String accountName = Behavior.AccountSettings.getAccountEmail(d);
		return (accountName != null && accountName.equalsIgnoreCase(loggedInAsEmail));*/
		return true; //TO DO
	}
	
	
}








