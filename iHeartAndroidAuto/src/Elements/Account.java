package Elements;

/**
 * Elements used in the sign in or sign up process
 * @author ChitraSelvaraj
 *
 */
public class Account extends Page{
	// Sign up or login
	public static String emailId = Page.connectId + "email_text";
	public static String passwordId = Page.connectId + "password_text";

	// Sign up or log in w/ facebook or G+
	public static String withFacebookId = Page.connectId + "login_facebook";
	public static String withGooglePlusId = Page.connectId + "login_google_plus";
	
	// Sign up specific elements
	public static String zipId = Page.connectId + "zipcode";
	public static String birthYearId = Page.connectId + "birthyear";
	public static String maleId = Page.connectId + "male";
	public static String femaleId = Page.connectId + "female";
	public static String tosId = Page.connectId + "tos";
	public static String signUpId = Page.connectId + "sign_up";
	// Birt year selector
	private static String birthYearX = "/" + Xpath.list(1) + "/android.widget.TextView[XXXXX]";
	public static String getBirthYearItemX(int i){
		return birthYearX.replace("[XXXXX]", "[" + i + "]");
	}
	// Log in specific elements
	public static String logInId = Page.connectId + "next_btn"; //Login button name on UI-Automator
	public static String logInEmailButtonId = Page.connectId + "next_btn"; 
	public static String forgotId = Page.connectId + "forgot_password_link";
	
	//Temporary Continue button from Genre Game
	public static String GenreContinueBtn = Page.connectId + "continue_button";
	
}
