package testUtilities.customRules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class DriverFailureRule implements TestRule{
	int retries = 1; // default
	public DriverFailureRule(){
		this.retries = 1;
	}
	public DriverFailureRule(int r){
		this.retries = r;
	}
	
	@Override
	public Statement apply(Statement base, Description description) {
		return statement(base, description);
	}
	
	private Statement statement(final Statement base, final Description description){
		return new Statement(){
			@Override
			public void evaluate() throws Throwable {
				// Save the exception for the end of the retries
				Throwable caughtThrowable = null;
				
				for (int i = 0; i < retries; i++){
					try{
						base.evaluate();
						return; // End
					}
					catch (Throwable t){
						System.out.println(t);
						caughtThrowable = t;
						// Only retry if it was a driver issue, not an assertion
						if(!(t instanceof java.lang.AssertionError)
								&& t.getMessage().contains("session is either terminated or not started")){
							System.err.println("\n\nRun #" + (i + 1) + " failed, may retry.");
						}
						else{
							throw t;
						}
					}
				}
				if (caughtThrowable != null){
					throw caughtThrowable;
				}
			}
		};
	}
}
