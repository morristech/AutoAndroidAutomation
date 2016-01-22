package Behavior;

import java.util.Calendar;

import org.openqa.selenium.By;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class Account extends Page{
	
	public static void selectYear(AndroidDriver<MobileElement> d, int year){
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int modifier = 10;
		int earliestYear = currentYear - modifier;
		
		if(year > earliestYear && year <= currentYear){
			click(d, find(Elements.Account.birthYearId));
			click(d, d.findElement(By.name(String.valueOf(year))));
		}
		
		int displayedYear = currentYear;
		while(earliestYear > currentYear - 150
				&& earliestYear > year){
			if(!useEmulator){
				click(d, find(Elements.Account.birthYearId));
			}
			else{
				// Try a quick tap... or two... 
				int testTap = 0;
				MobileElement dropdown = waitForVisible(d, find(Elements.Account.birthYearId), 10);
				while(testTap < 2
						&& waitForVisible(d, By.name(String.valueOf(displayedYear - 1)),1) == null){
					testTap++;
					quickTap(d, dropdown);
				}
			}
			try{
				while(waitForVisible(d, By.name(String.valueOf(earliestYear)), 1) == null
						&& modifier > 0){
					// perhaps we were being too ambitious
					modifier -= 2; 
					earliestYear += 2;
				}
			}
			catch(Exception e){}
			if(modifier <= 0){
				System.err.println("Had to stop trying to select year while signing up!");
				break;
			}
			click(d, d.findElement(By.name(String.valueOf(earliestYear))));
			displayedYear = earliestYear;
			earliestYear -= modifier;
		}
		// Finally select the year, now that it's visible
		if(!useEmulator){
			click(d, find(Elements.Account.birthYearId));
		}
		else{
			// Try a quick tap... or two... 
			int testTap = 0;
			MobileElement dropdown = waitForVisible(d, find(Elements.Account.birthYearId), 10);
			while(testTap < 2
					&& waitForVisible(d, By.name(String.valueOf(displayedYear - 1)),1) == null){
				testTap++;
				quickTap(d, dropdown);
			}
		}
		click(d, d.findElement(By.name(String.valueOf(year))));
	}
	
	// Log in from start screen (gate)
	public static void signIn(AndroidDriver<MobileElement> d){
		waitToType(d, find(Elements.Account.emailId), Elements.AccountSettings.iHeartAccountEmail, 10);
		d.navigate().back();
		waitToType(d, find(Elements.Account.passwordId), Elements.AccountSettings.iHeartAccountPassword, 10);
		//type(d, find(Elements.Account.passwordId), Elements.AccountSettings.iHeartAccountPassword);
		d.navigate().back();
		sleep(100); // Just to permit the button to become clickable
		click(d, find(Elements.Account.logInId));
		
		//For temporary login
		click(d, find(Elements.Account.GenreContinueBtn));
		
		//ByPass AUX 
		driver.findElement(By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.Button[1]")).click();
	
	   //Accept Disclaimer
		driver.findElement(By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.ScrollView[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[2]/android.widget.Button[1]")).click();

		
	}

}
