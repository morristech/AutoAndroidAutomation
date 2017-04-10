package Pages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.function.Predicate;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import Utilities.Errors;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

public class OptionsSubpage extends Page {
	
	public enum OptionsOnOff {
		ON, OFF
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String optionsBackButtonId = Page.connectId + "option_back_btn";
	private static String optionsSubpageOnButtonId = Page.connectId + "custom_toggle_on";
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getOptionsBackButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(optionsBackButtonId), 7);
	}
	
	public static AndroidElement getOptionsSubpageOnButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(optionsSubpageOnButtonId), 7);
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapOptionsBackButton (AndroidDriver<MobileElement> d) {
		return click(d, getOptionsBackButton(d), "Cannot tap options back button!", "tapOptionsBackButton");
	}
	
	public static Errors tapOptionsSubpageOnButton (AndroidDriver<MobileElement> d) {
		return click(d, getOptionsSubpageOnButton(d), "Cannot tap on button!", "tapOptionsSubpageOnButton");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static Errors toggleOnOff (AndroidDriver<MobileElement> d, OptionsOnOff option) {
		Errors err = new Errors();
		if ( (option == OptionsOnOff.OFF && isOn(d)) || (option == OptionsOnOff.ON && isOff(d)) ) {
			err.add(d, tapOptionsSubpageOnButton(d));
		}
		return err;
	}
	
	public static boolean isOff (AndroidDriver<MobileElement> d) {
		return !isOn(d);
	}
	
	public static boolean isOn (AndroidDriver<MobileElement> d){
		AndroidElement button = getOptionsSubpageOnButton(d);
		boolean answer = false;
		
		if (isVisible(button)) {
			int x = button.getLocation().getX();
			int y = button.getLocation().getY();
			int width = button.getSize().getWidth();
			int height = button.getSize().getHeight();
			
			int screenHeight = d.manage().window().getSize().getHeight();
			
			int xTestValue = width/10;
			int yTestValue = height/10;
			
			BufferedImage img = null;
			
			sleep(500); // Wait a while before taking the screenshot because tapping the button can be a bit slow.
			// Create the file and store the screenshot
			File tempScreenshot = ((TakesScreenshot) d).getScreenshotAs(OutputType.FILE);
			
			try {
				img = ImageIO.read(tempScreenshot);
				
				/* 
				 * Subimage method requires the x and y values for the upper-left corner.
				 * However, because the auto app is locked in landscape mode only and also because the screenshot taken
				 * is always in portrait mode, we need new coordinates to reflect this change.
				 * 
				 * The landscape bottom-left corner will be the portrait mode's top-right corner.
				 * 
				 * Example:
				 * 
				 * Landscape Mode:
				 * 
				 *    0 1 2 3 4 5 6 7             (X,Y)
				 *  0 X X X X X X A X         A = (6,0)
				 *  1 X X X X X X B X         B = (6,1)
				 *  2 X X X X X X C X         C = (6,2)
				 *  3 X X X X X X X X
				 *  
				 *  Portrait Mode:
				 *  
				 *    0 1 2 3 
				 *  0 X X X X
				 *  1 X X X X 
				 *  2 X X X X
				 *  3 X X X X                     (X,Y)
				 *  4 X X X X                 A = (3,6)
				 *  5 X X X X                 B = (2,6)
				 *  6 X C B A                 C = (1,6)
				 *  7 X X X X
				 *  
				 *  We need an algorithm that when given A(6,0) in landscape will give us C(1,6) in portrait.
				 *  In our example, 
				 *  screenheight (landscape) = 4
				 *  height of object (landscape) = 3.
				 *  A's X/width = 6
				 *  A's Y/height = 0
				 *  
				 *  C's X (in portrait) = 4 - (0 + 3) = 1
				 *  C's Y (in portrait) = A's X (in landscape) = 6
				 *  The height becomes the new width and vice versa because of the rotation.
				 */
				img = img.getSubimage(screenHeight - (y + height), x, height, width);
				
				Predicate<Color> isGray = (c) -> c.getRed() > 180 && c.getGreen() > 180 && c.getBlue() > 180;
				int rgb = img.getRGB(xTestValue, yTestValue);
				
				if (isGray.test(new Color(rgb))) {
					answer = true;
				}
				else {
					answer = false;
				}
			
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			tempScreenshot.delete();

		}
		
		return answer;
	}
}
