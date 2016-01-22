package Tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import Behavior.GenreGame;
import Utilities.TestRoot;

public class TestGenreGame extends TestRoot{
	
	@Before
	public void before(){
		if(!setup()){
			Assert.fail("Could not load driver");
		}
	}
	@After
	public void after(){
		quit();
	}
	
	// Test anonymous user logging in and using app
	@Test
	public void testMultipleGenreSelection(){
		// Click maybe later
		Assert.assertTrue("Could not click 'Maybe Later'", Behavior.AccountSettings.clickMaybeLater(driver));
		// Select genres
		int[] genres = {1, 4, 9};
		Assert.assertTrue("Could not select genres", Behavior.GenreGame.selectGenres(driver, genres));
		Assert.assertTrue("Could not click done after selecting genres", Behavior.GenreGame.clickDone(driver));
	}
	
	@Test
	public void testSingleGenreSelection(){
		// Click maybe later
		Assert.assertTrue("Could not click 'Maybe Later'", Behavior.AccountSettings.clickMaybeLater(driver));
		// Select a single genre, assert we can click done
		Assert.assertTrue("Could not select genres", Behavior.GenreGame.selectGenre(driver, 1));
		Assert.assertTrue("Could not click done after selecting genres", Behavior.GenreGame.clickDone(driver));
	}
	
	@Test 
	public void testNewUserGenreGame(){
		// Test account: iHeartTester1@gmail.com (gmail password: automationTester)
		Behavior.Gate.clickSignUp(driver);
		Behavior.Account.createThrowawayAccount(driver);
		Assert.assertTrue("Could not select a genre", GenreGame.selectGenre(driver, 1));
		Assert.assertTrue("Could not click done after selecting a genre", Behavior.GenreGame.clickDone(driver));
	}
	
	
	@Test
	public void testCurrentUserImproveRecommendations(){
		// Log in
		Behavior.Gate.clickLogIn(driver);
		Behavior.Account.signIn(driver);
		// Scroll down
		Behavior.Home.scrollDownAndClickImproveRecomendations(driver);
		Assert.assertTrue("Could not scroll down", waitForVisible(driver, find(Elements.GenreGame.getGenreGameItemX(1)), 1) != null);
		// Make sure we can't click done unless a change is made
		Assert.assertTrue("Was able to click done with no new changes, should not have been possible.", Behavior.GenreGame.clickDisabledDone(driver));
		
		// Deselected the only genre we had
		Assert.assertTrue("Could not deselect genre", Behavior.GenreGame.deselectGenre(driver, 1)); 
		// Assert that if nothing is clicked, we can't click done
		// This has been changed
//		Assert.assertTrue("Was able to click done with no new changes, should not have been possible.", Behavior.GenreGame.clickDisabledDone(driver));
		
		// Make sure we can make selections and tap done
		int[] genres = {1, 4};
		Behavior.GenreGame.selectGenresAndClickDone(driver, genres);
		
		// Go back in and remove added genre
		// Scroll down
		Behavior.Home.scrollDownAndClickImproveRecomendations(driver);
		Assert.assertTrue("Could not deselect genre we added to improve recommendations", Behavior.GenreGame.deselectGenreAndClickDone(driver, 4)); // Deselected the genre we added
	}
	
}
