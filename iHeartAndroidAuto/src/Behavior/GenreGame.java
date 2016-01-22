package Behavior;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

public class GenreGame extends Page {

	public static boolean selectGenre(AndroidDriver<MobileElement> d, int genre){
		waitToClick(d, find(Elements.GenreGame.getGenreGameItemX(genre)), 20);
		return (waitForVisible(d, find(Elements.GenreGame.getGenreSelectedX(genre)), 20) != null);
	}
	
	public static boolean clickDone(AndroidDriver<MobileElement> d){
		waitToClick(d, find(Elements.GenreGame.genreGameDoneId), 10);
		return waitForVisible(d, find(Elements.Page.iHeartLogoX), 15) != null;
	}
	/**
	 * Use when you know it's disabled and don't want to wait too long
	 * @param d
	 * @return
	 */
	public static boolean clickDisabledDone(AndroidDriver<MobileElement> d){
		MobileElement doneButton = waitForVisible(d, find(Elements.GenreGame.genreGameDoneId), 2);
		if(doneButton != null){
			if(doneButton.isEnabled()){
				doneButton.click();
				return false;
			}
			else{
				return true;
			}
		}
		else{
			return true;
		}
	}
	
	public static boolean selectGenres(AndroidDriver<MobileElement> d, int[] genres){
		boolean genresSelected = true;
		
		// Tap all of the requested genres (only from visible genres, scroll if more are required)
		for(int g : genres){
			if(g < 20){
				if(!selectGenre(d, g)){
					genresSelected = false;
				}
			}
		}		
		return genresSelected;
	}
	
	public static boolean selectGenreAndClickDone(AndroidDriver<MobileElement> d, int genre){
		return selectGenre(d, genre)
				&& clickDone(d);
	}
	
	public static boolean selectGenresAndClickDone(AndroidDriver<MobileElement> d, int[] genres){
		return selectGenres(d, genres) 
				&& clickDone(d);
	}
	
	public static boolean deselectGenre(AndroidDriver<MobileElement> d, int genre){
		waitToClick(d, find(Elements.GenreGame.getGenreGameItemX(genre)), 20);
		return (waitForVisible(d, find(Elements.GenreGame.getGenreSelectedX(genre)), 1) == null);
	}
	public static boolean deselectGenres(AndroidDriver<MobileElement> d, int[] genres){
		boolean genresDeselected = true;
		
		// Tap all of the requested genres (only from visible genres, scroll if more are required)
		for(int g : genres){
			if(g < 20){
				if(!deselectGenre(d, g)){
					genresDeselected = false;
				}
			}
		}		
		return genresDeselected;
	}
	
	public static boolean deselectGenreAndClickDone(AndroidDriver<MobileElement> d, int genre){
		return deselectGenre(d, genre) 
				&& clickDone(d);
	}
	public static boolean deselectGenresAndClickDone(AndroidDriver<MobileElement> d, int[] genres){
		return deselectGenres(d, genres) 
				&& clickDone(d);
	}
}
