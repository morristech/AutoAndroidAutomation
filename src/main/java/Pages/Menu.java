package Pages;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;

import testCommons.Errors;

public class Menu extends Page {
	
	public enum MainMenuItem {
		FOR_YOU("For You"),
		FAVORITES("Favorites"),
		RECENT_STATIONS("Recent Stations"),
		LIVE_RADIO("Live Radio"),
		ARTIST_RADIO("Artist Radio"),
		PODCASTS("Podcasts");
		
		private String displayName;
		
		private MainMenuItem (String displayName) {
			this.displayName = displayName;
		}
		
		public String toString () {
			return displayName;
		}
	}
	
	public enum LiveRadioMenuItem {
		NEAR_YOU("Near You"),
		BY_LOCATION("By Location"),
		BY_GENRE("By Genre");
		
		private String displayName;
		
		private LiveRadioMenuItem (String displayName) {
			this.displayName = displayName;
		}
		
		public String toString () {
			return displayName;
		}
	}
	
	public enum LiveRadioByGenreMenuItem {
		ALTERNATIVE("Alternative"),
		CLASSIC_ROCK("Classic Rock"),
		COUNTRY("Country"),
		CHRISTIAN_AND_GOSPEL("Christian & Gospel"),
		CLASSICAL("Classical"),
		HIP_HOP_AND_RNB("Hip Hop and R&B"),
		JAZZ("Jazz"),
		NEWS_AND_TALK("News & Talk"),
		REGGAE_AND_ISLAND("Reggae & Island"),
		MIX_AND_VARIETY("Mix & Variety"),
		OLDIES("Oldies"),
		ROCK("Rock"),
		SOFT_ROCK("Soft Rock"),
		SPANISH("Spanish"),
		TOP_40_AND_POP("Top 40 & Pop"),
		DANCE("Dance"),
		SPORTS("Sports"),
		WORLD("World"),
		_80S_AND_90S_HITS("80s & 90s Hits"),
		PUBLIC_RADIO("Public Radio"),
		HOLIDAY("Holiday"),
		COMEDY("Comedy"),
		HOSTS_AND_DJS("Hosts and DJs"),
		COLLEGE_RADIO("College Radio"),
		MEXICO("Mexico"),
		EDM("EDM"),
		PERSONALITIES("Personalities"),
		KIDS_AND_FAMILY("Kids & Family");
		
		private String displayName;
		
		private LiveRadioByGenreMenuItem (String displayName) {
			this.displayName = displayName;
		}
		
		public String toString () {
			return displayName;
		}
	}
	
	public enum ArtistRadioByGenreMenuItem {
		POP("Pop"),
		RAP("Rap"),
		COUNTRY("Country"),
		ROCK("Rock"),
		R_AND_B("R&B"),
		LATIN("Latin"),
		BLUES("Blues"),
		JAZZ("Jazz"),
		WORLD("World"),
		ELECTRONIC("Electronic"),
		HOLIDAY("Holiday"),
		CHRISTIAN("Christian"),
		SOUNDTRACK("Soundtrack"),
		VOCAL("Vocal"),
		EASY_LISTENING("Easy Listening"),
		REGGAE("Reggae");
		
		private String displayName;
		
		private ArtistRadioByGenreMenuItem (String displayName) {
			this.displayName = displayName;
		}
		
		public String toString () {
			return displayName;
		}
	}
	
	/********************/
	/* *** Elements *** */
	/********************/
	
	private static String menuBackButton = Page.connectId + "menu_back_btn";
	private static String menuCloseButton = Page.connectId + "menu_close_btn";
	private static String menuItemTitle = Page.connectId + "title";
	
	
	/*******************/
	/* *** Getters *** */
	/*******************/
	
	public static AndroidElement getMenuBackButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(menuBackButton), 7);
	}
	
	public static AndroidElement getMenuCloseButton (AndroidDriver<MobileElement> d) {
		return waitForVisible(d, By.id(menuCloseButton), 7);
	}
	
	public static AndroidElement getItem (AndroidDriver<MobileElement> d, int position) {
		return getCardItem(d, position, 0);
	}
	
	public static AndroidElement getItem (AndroidDriver<MobileElement> d, int row, int column) {
		return getCardItem(d, column, row);
	}
	
	public static AndroidElement getItem (AndroidDriver<MobileElement> d, String title) {
		return (AndroidElement) findElements(d, By.id(menuItemTitle)).stream()
		                                                             .filter(menuItem -> getText(menuItem).equalsIgnoreCase(title))
		                                                             .findFirst()
		                                                             .orElse(null);
	}
	
	public static AndroidElement getMenuItem (AndroidDriver<MobileElement> d, Enum<?> item) {
		return getItem(d, item.toString());
	}
	
	/***************************************/
	/* *** Individual Element Behavior *** */
	/***************************************/
	
	public static Errors tapMenuBackButton (AndroidDriver<MobileElement> d) {
		return click(d, getMenuBackButton(d), "Cannot tap menu back button!", "tapMenuBackButton");
	}
	
	public static Errors tapMenuCloseButton (AndroidDriver<MobileElement> d) {
		return click(d, getMenuCloseButton(d), "Cannot tap menu close button!", "tapMenuCloseButton");
	}
	
	public static Errors tapItem (AndroidDriver<MobileElement> d, int position) {
		String errorMessage = String.format("Cannot tap item with position: %d.", position);
		return click(d, getItem(d, position), errorMessage, "tapItem");
	}
	
	public static Errors tapItem (AndroidDriver<MobileElement> d, int row, int column) {
		String errorMessage = String.format("Cannot tap item with row position %d and column posiiton %d.", row, column);
		return click(d, getItem(d, row, column), errorMessage, "tapItem");
	}
	
	public static Errors tapItem (AndroidDriver<MobileElement> d, String item) {
		String errorMessage = String.format("Cannot find item: %s.", item);
		return click(d, getItem(d, item), errorMessage,"tapItem");
	}
	
	public static Errors tapMenuItem (AndroidDriver<MobileElement> d, Enum<?> item) {
		String errorMessage = String.format("Cannot tap menu item: %s.", item.toString());
		return click(d, getMenuItem(d, item), errorMessage, "tapMenuItem");
	}
	
	/*******************/
	/* *** Utility *** */
	/*******************/
	
	public static String getItemTitle (AndroidDriver<MobileElement> d, int position) {
		return getText(findChildElement(d, getItem(d, position), By.id(menuItemTitle)));
	}
	
	public static String getItemTitle (AndroidDriver<MobileElement> d, int row, int column) {
		return getText(findChildElement(d, getItem(d, row, column), By.id(menuItemTitle)));
	}
	
	public static List<String> getAllItemTextOnScreen (AndroidDriver<MobileElement> d) {
		return findElements(d, By.id(menuItemTitle)).stream()
		                                            .map(item -> getText(item))
		                                            .collect(Collectors.toList());
	}
	
	private static List<String> getMenuItemTextList (Class<? extends Enum> clazz) {
		return (List<String>) EnumSet.allOf(clazz)
		                             .stream()
		                             .map(menuItem -> menuItem.toString())
		                             .collect(Collectors.toList()); 
	}

	public static List<String> getLiveRadioMenuItemTextList () {
		return getMenuItemTextList(LiveRadioMenuItem.class);
	}
	
	public static List<String> getMainMenuItemTextList () {
		return getMenuItemTextList(MainMenuItem.class);
	}
	
	public static List<String> getLiveRadioByGenreMenuItemTextList() {
		return getMenuItemTextList(LiveRadioByGenreMenuItem.class);
	}
	
}
