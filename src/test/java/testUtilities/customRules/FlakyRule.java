package testUtilities.customRules;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import testUtilities.Flaky;


/**
 * Reruns tests with @Flaky annotation that failed. 
 * @author kinpi
 */

public class FlakyRule implements TestRule {

	private AtomicInteger retryCount;
	
	public FlakyRule (int retries) {
		super();
		retryCount = new AtomicInteger(retries);
	}
	
	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement () {

			@Override
			public void evaluate() throws Throwable {
				Throwable caughtThrowable = null;
				
				while (retryCount.get() >= 0) {
					try {
						base.evaluate();
						return;
					}
					catch (Throwable t) {
						if (retryCount.getAndDecrement() > 0 && description.getAnnotation(Flaky.class) != null) {
							caughtThrowable = t;
							System.err.println(description.getDisplayName() + ": Failed.");
							System.err.println(retryCount.toString() + " retries remaining.");
						}
						else {
							throw caughtThrowable;
						}
					}
				}
				
			}
			
		};
	}
	
}