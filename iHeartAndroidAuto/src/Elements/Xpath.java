package Elements;

/**
 * This class is used to help shorten Xpaths with easier to edit (and shorter) sections
 * @author ChitraSelvaraj
 */
public class Xpath {
	// //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.support.v4.widget.DrawerLayout[1]/android.widget.RelativeLayout[1]/android.widget.FrameLayout[1]/android.view.View[1]/android.support.v7.widget.LinearLayoutCompat[1]/android.widget.TextView[1]

	/**
	 * Base starts after the 2nd frame framelayout item. 
	 */
	public static String base = "/" + "/android.widget.LinearLayout[1]" + "/android.widget.FrameLayout[1]";
	
	// Private xpaths, use methods to retrieve XXXXX is replaced by number
	private static String linear = "/android.widget.LinearLayout[XXXXX]";
	private static String frame = "/android.widget.FrameLayout[XXXXX]";
	private static String drawer = "/android.support.v4.widget.DrawerLayout[XXXXX]";
	private static String relative = "/android.widget.RelativeLayout[XXXXX]";
	private static String view = "/android.view.View[XXXXX]";
	private static String scroll = "/android.widget.ScrollView[XXXXX]";
	private static String horizScroll = "/android.widget.HorizontalScrollView[XXXXX]";
	private static String linearCompat = "/android.support.v7.widget.LinearLayoutCompat[XXXXX]";
	private static String text = "/android.widget.TextView[XXXXX]";
	private static String image = "/android.widget.ImageView[XXXXX]";
	private static String imageButton = "/android.widget.ImageButton[XXXXX]";
	private static String viewPager = "/android.support.v4.view.ViewPager[XXXXX]";
	private static String list = "/android.widget.ListView[XXXXX]";
	private static String editText = "/android.widget.EditText[XXXXX]";
	private static String button = "/android.widget.Button[XXXXX]";
	
			
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
		return view.replace("[XXXXX]", "[" + i + "]");
	}
	public static String scroll(int i){
		return scroll.replace("[XXXXX]", "[" + i + "]");
	}
	public static String horizScroll(int i){
		return horizScroll.replace("[XXXXX]", "[" + i + "]");
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
	public static String editText(int i){
		return editText.replace("[XXXXX]", "[" + i + "]");
	}
	public static String button(int i){
		return button.replace("[XXXXX]", "[" + i + "]");
	}
	
}
