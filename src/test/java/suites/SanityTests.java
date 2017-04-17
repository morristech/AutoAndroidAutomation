package suites;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import testUtilities.CategoryInterfaces.Sanity;

@RunWith(Categories.class)
@IncludeCategory(Sanity.class)
@Suite.SuiteClasses({
	tests.sanity.TestInstallationAndFUX.class,
	tests.sanity.TestSettings.class,
	tests.sanity.TestSignUpLogIn.class,
	tests.sanity.TestLiveStations.class,
	tests.sanity.TestAnonymousUser.class,
	tests.sanity.TestArtistStation.class,
	tests.sanity.TestPodcasts.class
})

public class SanityTests {
	
}
