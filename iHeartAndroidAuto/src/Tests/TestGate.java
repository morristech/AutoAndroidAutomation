package Tests;

import org.testng.Assert;
import org.testng.annotations.*;

import Utilities.TestRoot;

public class TestGate extends TestRoot{
	@BeforeTest
	public void before(){
		if(!setup()){
			Assert.fail("Could not load driver");
		}
	}
	@AfterTest
	public void after(){
		quit();
	}
	
	@Test
	public void testSignUpButton(){
		Assert.assertTrue(Behavior.Gate.clickSignUp(driver), "Could not click Sign Up button");
	
	}
	
	
	@Test
	public void testLogInButton(){
		Assert.assertTrue(Behavior.Gate.clickLogIn(driver), "Could not click Log In button");
	}
	
	@Test
	public void testMaybeLaterButton(){
		Assert.assertTrue(Behavior.Gate.clickMaybeLater(driver), "Could not click 'Maybe Later' button");
	}
	
}
