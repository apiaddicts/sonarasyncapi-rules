package apiquality.sonar.asyncapi;

import org.sonar.api.ExtensionPoint;
import org.sonar.api.scanner.ScannerSide;
import org.apiaddicts.apitools.dosonarapi.api.AsyncApiCustomRuleRepository;
import org.sonarsource.api.sonarlint.SonarLintSide;

import apiquality.sonar.asyncapi.checks.RulesLists;

import static apiquality.sonar.asyncapi.AsyncAPICustomRulesDefinition.REPOSITORY_KEY;

import java.util.List;

/**
 * Makes the rules visible to the AsyncAPI scanner sensor,
 * hence adds to the classes that are going to be executed during source code analysis.
 * <p>
 * This class is a batch extension by implementing the {@link AsyncApiCustomRuleRepository}
 */
@SonarLintSide
@ScannerSide
@ExtensionPoint
public class AsyncAPICustomRuleRepository implements AsyncApiCustomRuleRepository {
	@Override
	public String repositoryKey() {
		return REPOSITORY_KEY;
	}

	@Override
    public List<Class<?>> checkClasses() {
		return RulesLists.getAllChecks();
    }
}
