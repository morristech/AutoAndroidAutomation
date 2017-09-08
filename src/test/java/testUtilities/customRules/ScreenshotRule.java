package testUtilities.customRules;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import Utilities.TestRoot;
import testCommons.Errors;
import testCommons.OutputFormatter;

/**
 * Screenshot Rule
 * Runs after every test. 
 * If the test passed, this runs the usual shutdown method
 * If it failed, it takes a screenshot, then runs the usual shutdown method
 * This replaces @after with the following:
 * @Rule
 * public ScreenshotRule screenshot = new ScreenshotRule();
 * 
 */

public class ScreenshotRule implements MethodRule{
	
	@Override
	/**
	 * Tests run within this. Once it ends, it takes a screenshot for failures, but ends regardless
	 */
	public Statement apply(final Statement statement, final FrameworkMethod method, Object target) {
		return new Statement() {
			@Override
			/**
			 * Run the test, if it fails, catch the failure (an exception/throwable)
			 * 		and take a screenshot, before allowing the failure to continue.
			 * Then, fail the test. 		
			 */
			public void evaluate() throws Throwable {
				String status = "FAILED";
				try{
					statement.evaluate();
					status = "PASSED";
					TestRoot.testRootLog.logInfo("TEST PASSED");
				}
				catch(Throwable t){
					// Ensure status is set to failed
					status = "FAILED";
					
					// Catch the failure, take the screenshot, then pass the failure on
					if(TestRoot.driver != null){
						String errorMethod = "assertFrom_" + method.getName();
						Errors.captureScreenshot(TestRoot.driver, errorMethod, TestRoot.SCREENSHOT_DIRECTORY, TestRoot.SCREENSHOT_URL);
					}
					// Make sure we quit
					TestRoot.quit();
					// Throw the original error
					throw t;
				}
				finally{
					// Quit even if it did not fail
					// Make sure the last message is slightly different than the others
					// 	for easier readability 
					if (status != null && TestRoot.testRootLog != null && status.contains("FAILED")){
						System.out.println(TestRoot.testRootLog.getActions());
						TestRoot.testRootLog.logError("TEST FAILED");
					}
					OutputFormatter.printConsoleHeader("Test Finished. Status: " + status);
					TestRoot.quit();
				}
			}
		};
	}
}
