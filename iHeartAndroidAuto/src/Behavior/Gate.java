package Behavior;

import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Gate extends Page {
		public static boolean clickSignUp(AndroidDriver<MobileElement> d){
			waitToClick(d, MobileBy.id(Elements.Gate.gateSignUpId), 30);
			// Ensure the email login has displayed, but not signup information
			return waitForVisible(d, MobileBy.id(Elements.Account.emailId), 15) != null
					&& isNotVisible(d, MobileBy.id(Elements.Account.zipId));
		
	}
	
	public static boolean clickLogIn(AndroidDriver<MobileElement> d){
		waitToClick(d, MobileBy.id(Elements.Gate.gateLogInId), 30);
		// Ensure the email login has displayed, but not signup information
		return waitForVisible(d, MobileBy.id(Elements.Account.emailId), 15) != null
				&& isNotVisible(d, MobileBy.id(Elements.Account.zipId));
	}
	
	
	
	// Maybe later
	
	public static boolean clickMaybeLater(AndroidDriver<MobileElement> d){
		waitToClick(d, MobileBy.id(Elements.Gate.maybeLaterId), 30);
		// Ensure the email login has displayed, but not signup information
		return waitForVisible(d, MobileBy.id(Elements.Account.emailId), 15) != null
				&& isNotVisible(d, MobileBy.id(Elements.Account.zipId));
	}
}
