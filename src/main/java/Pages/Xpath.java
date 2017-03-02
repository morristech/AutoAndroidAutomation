package Pages;

import Utilities.TestRoot;

/**
 * This class is used to help shorten Xpaths with easier to edit (and shorter) sections
 * @author daniellegolinsky
 */
public class Xpath {

	/**
	 * Base starts after the 2nd frame framelayout item. 
	 */
	public static String base = "/" + "/android.widget.LinearLayout[1]" + "/android.widget.FrameLayout[1]" + "/android.widget.LinearLayout[1]" + "/android.widget.FrameLayout[1]";
	
	// Private xpaths, use methods to retrieve XXXXX is replaced by number
	private static String linear = "/android.widget.LinearLayout[XXXXX]";
	private static String frame = "/android.widget.FrameLayout[XXXXX]";
	private static String drawer = "/android.support.v4.widget.DrawerLayout[XXXXX]";
	private static String relative = "/android.widget.RelativeLayout[XXXXX]";
	private static String view = "/android.view.View[XXXXX]";
	private static String viewGroup = "/android.view.ViewGroup[XXXXX]";
	private static String scroll = "/android.widget.ScrollView[XXXXX]";
	private static String horizScroll = "/android.widget.HorizontalScrollView[XXXXX]";
	private static String grid = "/android.widget.GridView[XXXXX]";
	private static String linearCompat = "/android.support.v7.widget.LinearLayoutCompat[XXXXX]";
	private static String text = "/android.widget.TextView[XXXXX]";
	private static String image = "/android.widget.ImageView[XXXXX]";
	private static String imageButton = "/android.widget.ImageButton[XXXXX]";
	private static String viewPager = "/android.support.v4.view.ViewPager[XXXXX]";
	private static String list = "/android.widget.ListView[XXXXX]";
	private static String spinner = "/android.widget.Spinner[XXXXX]";
	private static String editText = "/android.widget.EditText[XXXXX]";
	private static String web = "/android.webkit.WebView[XXXXX]";
	private static String button = "/android.widget.Button[XXXXX]";
	private static String tableLayout = "/android.widget.TableLayout[XXXXX]";
	private static String tableRow = "/android.widget.TableRow[XXXXX]";
	private static String recyclerview = "/android.support.v7.widget.RecyclerView[XXXXX]";	
	private static String checkedview = "/android.widget.CheckedTextView[XXXXX]";
	private static String actionBarTab = "/android.support.v7.app.ActionBar.Tab[XXXXX]";
	
	/**
	 * Useful for partial xpaths when trying to locate a sub element. 
	 * @param path
	 * @return
	 */
	public static String subXpath(String path){
		if (!path.startsWith("./"))
			return "./" + path;
		else
			return path;
	}
	
	// Public methods for forming xpath definitions
	public static String linear(int i){
		return linear.replace("[XXXXX]", "[" + i + "]");
	}
	public static String frame(int i){
		return frame.replace("[XXXXX]", "[" + i + "]");
	}
	public static String drawer(int i){
		return drawer.replace("[XXXXX]", "[" + i + "]");
	}
	public static String relative(int i){
		return relative.replace("[XXXXX]", "[" + i + "]");
	}
	public static String view(int i){
		if(TestRoot.ISMARSHMALLOW)
			return viewGroup(i);
		return view.replace("[XXXXX]", "[" + i + "]");
	}
	// 6.0+ devices use viewgroups
	public static String viewGroup(int i){
		return viewGroup.replace("[XXXXX]", "[" + i + "]");
	}
	public static String scroll(int i){
		return scroll.replace("[XXXXX]", "[" + i + "]");
	}
	public static String horizScroll(int i){
		return horizScroll.replace("[XXXXX]", "[" + i + "]");
	}
	public static String recyclerview(int i){
		return recyclerview.replace("[XXXXX]", "[" + i + "]");
	}
	public static String grid(int i){
		return grid.replace("[XXXXX]", "[" + i + "]");
	}
	public static String linearCompat(int i){
		return linearCompat.replace("[XXXXX]", "[" + i + "]");
	}
	public static String text(int i){
		return text.replace("[XXXXX]", "[" + i + "]");
	}
	public static String image(int i){
		return image.replace("[XXXXX]", "[" + i + "]");
	}
	public static String imageButton(int i){
		return imageButton.replace("[XXXXX]", "[" + i + "]");
	}
	public static String viewPager(int i){
		return viewPager.replace("[XXXXX]", "[" + i + "]");
	}
	public static String list(int i){
		return list.replace("[XXXXX]", "[" + i + "]");
	}
	public static String spinner(int i){
		return spinner.replace("[XXXXX]", "[" + i + "]");
	}
	public static String editText(int i){
		return editText.replace("[XXXXX]", "[" + i + "]");
	}
	public static String web(int i){
		return web.replace("[XXXXX]", "[" + i + "]");
	}
	public static String button(int i){
		return button.replace("[XXXXX]", "[" + i + "]");
	}
	public static String tableLayout(int i){
		return tableLayout.replace("[XXXXX]", "[" + i + "]");
	}
	public static String tableRow(int i){
		return tableRow.replace("[XXXXX]", "[" + i + "]");
	}
	public static String checkedText(int i){
		return checkedview.replace("[XXXXX]", "[" + i + "]");
	}
	public static String actionBarTab(int i){
		return actionBarTab.replace("[XXXXX]", "[" + i + "]");
	}
}
