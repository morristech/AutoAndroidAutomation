package suites;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import testUtilities.CategoryInterfaces.StablePR;

@RunWith(Categories.class)
@IncludeCategory(StablePR.class)
@Suite.SuiteClasses({
	// Keep it in alphabetical order please.
	tests.sanity.TestAnonymousUser.class,
	tests.sanity.TestArtistStation.class,
	tests.sanity.TestFavorites.class,
	tests.sanity.TestForYou.class,
	tests.sanity.TestInstallationAndFUX.class,
	tests.sanity.TestLiveStations.class,
	tests.sanity.TestPodcasts.class,
	tests.sanity.TestRecentStation.class,
	tests.sanity.TestSettings.class,
	tests.sanity.TestSignUpLogIn.class
})

public class StablePRTests {

}
