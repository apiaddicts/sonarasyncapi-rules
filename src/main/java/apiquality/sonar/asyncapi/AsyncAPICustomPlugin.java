package apiquality.sonar.asyncapi;

import org.sonar.api.Plugin;

/**
 * Entry point of your plugin containing your custom rules.
 */
public class AsyncAPICustomPlugin implements Plugin {

	@Override
	public void define(Context context) {
		context.addExtensions(
				// server extensions -> objects are instantiated during server start
				AsyncAPICustomProfileDefinition.class,
				AsyncAPICustomRulesDefinition.class,
				// batch extensions -> objects are instantiated during code analysis
				AsyncAPICustomRuleRepository.class
		);
	}

}
