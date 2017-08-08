package Tests;

import org.junit.runner.RunWith;
import com.googlecode.junittoolbox.SuiteClasses;
import com.googlecode.junittoolbox.WildcardPatternSuite;


/**
 * Runs all test suites (Just once)
 * -A band-aid due to a bug in Gradle 3.3
 * 
 * Run from command line with:
 * "gradle -Dtest.single=RunAllTests build test"
 * 
 * @author daniellegolinsky
 *
 */

@RunWith(WildcardPatternSuite.class)
@SuiteClasses({ "**/Test*.class" })

public class RunAllTests {
}
