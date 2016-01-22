package Behavior;

import org.openqa.selenium.By;

import Utilities.TestRoot;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public abstract class Page extends TestRoot{
	
	/**
	 * Taps the hardware (or overlayed) Android system back button
	 * @param d
	 */
	public static void back(AndroidDriver<MobileElement> d){
		d.navigate().back();
	}
	
	public static void hideKey(AndroidDriver<MobileElement> d){
		d.hideKeyboard();
	}
	
	/**
	 * Log in from the first launch
	 * @param d
	 */
	
	
	/**
	 * Creates a throwaway account from boot
	 * @param d
	 */
	//public static void signUpFromBoot(AndroidDriver<MobileElement> d){
		//Behavior.Gate.clickSignUp(d);
		//Behavior.Account.createThrowawayAccount(d);
		//Behavior.GenreGame.selectGenre(d, 1);
		//Behavior.GenreGame.clickDone(d);
	//}
	
	

	/** 
	 * Takes a string that is either an ID or an Xpath to pass to other testClick methods
	 * @param d
	 * @param elementLocator
	 * @return
	 */
	public static boolean testClick(AndroidDriver<MobileElement> d, String elementLocator){
		return testClick(d, find(elementLocator));
	}
	/**
	 * Returns true if the element was clicked
	 * @param d
	 * @param by
	 * @return
	 */
	public static boolean testClick(AndroidDriver<MobileElement> d, By by){
		return testClick(d, waitForVisible(d, by, 15));
	}
	/**
	 * Returns true if the element was clicked
	 * @param d
	 * @param by
	 * @return
	 */
	public static boolean testClick(AndroidDriver<MobileElement> d, MobileElement e){
		if(e != null){
			click(d, e);
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
	
	
	// Menu bar options
	public static void clickMenuDrawerItem(AndroidDriver<MobileElement> d, int item){
		waitToClick(d, find(Elements.Page.getMenuDrawerItem(item)), 5);
	}
	public static void clickMenuDrawerHome(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerHomeX), 5);
	}
	public static void clickMenuDrawerLiveRadio(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerLiveRadioX), 5);
	}
	public static void clickMenuDrawerArtistRadio(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerArtistRadioX), 5);
	}
	public static void clickMenuDrawerPodcasts(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerPodcastsX), 5);
	}
	public static void clickMenuDrawerPerfectFor(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerPerfectForX), 5);
	}
	public static void clickMenuDrawerListeningHistory(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerListeningHistoryX), 5);
	}
	public static void clickMenuDrawerAlarmClock(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerAlarmClockX), 5);
	}
	public static void clickMenuDrawerSleepTimer(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerSleepTimerX), 5);
	}
	public static void clickMenuDrawerAccountSettings(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerAccountSettingsX), 5);
	}
	public static void clickMenuDrawerExitApp(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.Page.menuDrawerExitAppX), 5);
	}
	
	
	/**
	 * Old method, use tapListItemOption(AndroidDriver<MobileElement> d, int listItem, int optionNumber) instead
	 * @param d
	 * @param listItem
	 * @param optionNumber
	 * @param optionButton
	 */
	public static void tapListItemOptionOLD(AndroidDriver<MobileElement> d, int listItem, int optionNumber, MobileElement optionButton){
		tapListItemOptionOLD(d, listItem, optionNumber, optionButton.getLocation().getY());
	}
	/**
	 * Tap a selected option from a drop-down button. 
	 * 		Example: Add to Favorites for a station in a "For You" list (the 3 dot button)
	 * @param d AndroidDriver<MobileElement> 
	 * @param listItem	int (the item in the list)
	 * @param optionNumber	int (which item from the popup option box) --STARTS AT 0
	 * @param optionButton MobileElement (The button that had to be clicked)
	 */
	public static void tapListItemOptionOLD(AndroidDriver<MobileElement> d, int listItem, int optionNumber, int buttonY){
		/*
		 * This is slightly different than the specialized method for list history, 
		 * F: Final Y location of first option
		 * Fx: The Xth item under the first option (if two options, this would be the second, X value of 1)
		 * B: Button Y value
		 * H: Height
		 * Ro: Constant, distance between options as a ratio to height
		 * Rd: Constant, distance between button and first option as a ratio to height
		 * Ro = 0.071875 OR: 0.078125
		 * Rd = 0.09375 OR: 0.08828125
		 * 
		 * FORMULA:
		 * F = B + (Rd * H)
		 * Fx = F + x(Ro * H)
		 * 
		 * If at the bottom of the screen, a different formula is used, counting up from bottom:
		 * L: Last item Y value
		 * L1: Item above that (Lx)
		 * RL: Constant, 0.953125 location of the option as a ratio to the height
		 * 
		 * FORMULA:
		 * L = RL * H
		 * Lx = L - x(Ro * H)
		 * 
		 * X VALUE: 0.56111111 * App Width)
		 */
		int phoneHeight = getAppHeight(d);
//		int buttonY = optionButton.getCenter().getY();
		double distanceBetweenOptions = 0.078125 * phoneHeight; // Ro
		double distanceBetweenOptionAndButton = 0.08828125 * phoneHeight;// Rd
		double lastOptionY = 0.953125 * phoneHeight;
		double minimumLimitForThreeOptions = 0.671875 * phoneHeight;
		double minimumLimitForTwoOptions = 0.78125 * phoneHeight;
		
		// Sleep since we don't have a "wait for maybe visible"
		sleep(2000);
		
		boolean threeOptions = false;
		if(optionNumber > 1){
			// assume you know what you're doing and skip it all
			threeOptions = true;
		}
		
		int selectedOptionY = 0;
		int minimum = (int) minimumLimitForTwoOptions;
		
		if(threeOptions){
			minimum = (int) minimumLimitForThreeOptions;
		}
		
		if(buttonY < minimum){
			/* Use normal formula
			 * F = B + (Rd * H)
			 * Fx = F + x(Ro * H)
	 		 */
			if(optionNumber == 0){
				selectedOptionY = (int) (buttonY + distanceBetweenOptionAndButton);
			}
			else{
				int firstOption = (int) (buttonY + distanceBetweenOptionAndButton);
				selectedOptionY = (int) (firstOption + distanceBetweenOptions * optionNumber);
			}
		}
		else{
			// Use lower limit logic
			if(optionNumber == 1){
				selectedOptionY = (int) lastOptionY;
			}
			else{
				selectedOptionY = (int) (lastOptionY - distanceBetweenOptions * optionNumber);
			}
		}
		
		
		// Click 
		int clickX = (int) (0.56111112 * getAppWidth(d));
		try{
			d.tap(1, clickX, selectedOptionY, 200);
		}
		catch(Exception e){}
	}

}
