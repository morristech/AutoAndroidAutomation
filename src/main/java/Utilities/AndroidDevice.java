package Utilities;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import testCommons.TestDevice;

public class AndroidDevice extends TestDevice {
	
	private String appPackage;
	private String appActivity;
	private boolean testOD = true;

	public AndroidDevice(){
		super();
	}
	
	/**
	 * Creates a new Android device
	 * @param u url
	 * @param p port
	 * @param d device
	 * @param v OS version 
	 * @param m Model
	 * @param appPath
	 * @param appPack
	 * @param appAct
	 * @param e emulator/simulator
	 * @param tod testOD
	 */
	public AndroidDevice(String url, String port, String device, String version, 
			String model, String appPath, String appPack, String appAct, boolean tod, boolean e){
		super(url, port, device, "ANDROID", version, model, appPath, e);
		this.appPackage = appPack;
		this.appActivity = appAct;
		this.testOD = tod;
	}
	
	// Getters
	public String getPackage(){
		return this.appPackage;
	}
	
	public String getAppActivity(){
		return this.appActivity;
	}
	
	public Boolean getTestOD(){
		return this.testOD;
	}
	
	// Setters
	public void setPackage(String ap){
		this.appPackage = ap;
	}
	
	public void setAppActivity(String aa){
		this.appActivity = aa;
	}
	
	public void setTestOD(Boolean tod){
		this.testOD = tod;
	}
	
	// Other methods
	
	/**
	 * Returns the desired capabilities for this Android devices
	 * @return
	 */
	public DesiredCapabilities getCapabilities(){
		DesiredCapabilities capabilities = new DesiredCapabilities();
		
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, this.device);
		capabilities.setCapability(MobileCapabilityType.UDID, this.device);
		if (this.emulator){
			capabilities.setCapability("avd", this.device);
		}
		capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, this.version);
		capabilities.setCapability(MobileCapabilityType.APP, this.appPath);
		capabilities.setCapability("appPackage", this.appPackage);
		capabilities.setCapability("appWaitPackage", this.appPackage);
		capabilities.setCapability("appWaitActivity", this.appActivity);
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 1200);
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
		
		return capabilities;
	}
	
	public AndroidDriver<MobileElement> getDriver(DesiredCapabilities capabilities) throws Exception{
		// Create the URL
		URL appiumUrl = null;
		try{
			appiumUrl = new URL("http://" + this.url + ":" + this.port + "/wd/hub");
		}
		catch(Exception e){
			e.printStackTrace();
			try{
				appiumUrl = new URL("http://127.0.0.1:4723/wd/hub");
			}
			catch(MalformedURLException mue){
				mue.printStackTrace(); // Driver creation will fail
			}
		}
		
		return new AndroidDriver<MobileElement>(appiumUrl, capabilities);
	}
	
	public boolean isMarshmallow(){
		String[] majorMinor = this.version.split("\\.");
		if (majorMinor != null && majorMinor.length > 0){
			return Integer.parseInt(majorMinor[0]) >= 6;
		}
		return false;
	}
}
