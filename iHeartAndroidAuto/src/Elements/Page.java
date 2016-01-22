package Elements;

public class Page 
{
	
	// Base IDs or Resource id 
	
	public static String connectId = "com.clearchannel.iheartradio.connect:id/";

	
	// Common elements
	//public static String backButtonX = Xpath.base + Xpath.relative(1) + Xpath.frame(1) + Xpath.view(1) + Xpath.imageButton(1);
	//public static String pageTitleX = Xpath.base + Xpath.relative(1) + Xpath.frame(1) + Xpath.view(1) + Xpath.text(1);
	//public static String iHeartLogoX = Xpath.base + Xpath.drawer(1) + Xpath.relative(1) + Xpath.frame(1) + Xpath.view(1) + Xpath.image(1);
	//public static String listHeaderTitleId = controllerId + "list_header_title";
	//public static String bigLogoId = controllerId + "logo_image";
	//public static String loadingBarId = controllerId + "progress_bar_container";
	
	// Modals
	/*public static String alertConfirmId = "android:id/button1";
	public static String alertCancelId = "android:id/button2";
	public static String alertHeadingId = "android:id/alertTitle";
	public static String alertMessageId = "android:id/message";*/
	
	// Menu drawer items
	//TODO Get someone to give these buttons ID's!!! XPaths are NOT stable. Our tests WILL break.	
	private static String menuDrawerItemBaseX = Xpath.base + Xpath.drawer(1) + Xpath.frame(1) + Xpath.scroll(1) + Xpath.linear(1) + "/android.widget.LinearLayout[XXXXX]";
	public static String menuDrawerLogoX = getMenuDrawerItem(1) + Xpath.image(1);
	public static String menuDrawerHomeX = getMenuDrawerItem(2);
	public static String menuDrawerLiveRadioX = getMenuDrawerItem(3);
	public static String menuDrawerArtistRadioX = getMenuDrawerItem(4);
	public static String menuDrawerPodcastsX = getMenuDrawerItem(5);
	public static String menuDrawerPerfectForX = getMenuDrawerItem(6);
	public static String menuDrawerListeningHistoryX = getMenuDrawerItem(7);
	public static String menuDrawerAlarmClockX = getMenuDrawerItem(8);
	public static String menuDrawerSleepTimerX = getMenuDrawerItem(9);
	public static String menuDrawerAccountSettingsX = getMenuDrawerItem(10);
	public static String menuDrawerExitAppX = getMenuDrawerItem(11);
	
	public static String popupConfirmId = "android:id/button1";
	public static String popupCancelId = "android:id/button2";
	
	// If static definitions above aren't working due to a smaller screen, use this method
	public static String getMenuDrawerItem(int i){
		return menuDrawerItemBaseX.replace("[XXXXX]", "[" + i + "]");
	}
	
	// Navigation elements present on most (if not all) pages
	public static String menuDrawerButtonX = Xpath.base + Xpath.drawer(1) + Xpath.relative(1) + Xpath.frame(1) + Xpath.view(1) + Xpath.imageButton(1);
	public static String searchButtonX = Xpath.base + Xpath.drawer(1) + Xpath.relative(1) + Xpath.frame(1) + Xpath.view(1) + Xpath.linearCompat(1) + Xpath.text(1);
	//public static String searchBarId = controllerId + "search_src_text";
	//public static String chromecastButtonId = controllerId + "chromecast_button";
	public static String nowPlayingButtonId = connectId + "now_playing_view";
	
	// Copyright info, TOS, and Privacy policy
	public static String copyrightId = connectId + "copyright_item";
	//public static String tosId = controllerId + "tos_link";
	public static String privacyPolicyId = connectId + "privacy_link";
	
	
	// Overlay options from tapping an option button
	private static String overlayOptionButtonX = "/" + Xpath.linear(1) + Xpath.list(1) + "/android.widget.RelativeLayout[XXXXX]" + Xpath.text(1);
	public static String getOverlayOptionButtonX(int i){
		return overlayOptionButtonX.replace("[XXXXX]", "[" + i + "]");
	}
	
	public static String likeiHeartRadioTitle = Page.connectId + "title";
	public static String likeiHeartThumbUp = Page.connectId + "thumb_up";
	public static String likeiHeartThumbDown = Page.connectId + "thumb_down";
	public static String likeiHeartDismiss = Page.connectId + "dismiss_dialog";
}
