package org.sonar.samples.asyncapi;

import org.junit.Test;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.api.server.rule.RulesDefinition.Repository;

import apiquality.sonar.asyncapi.AsyncAPICustomRulesDefinition;
import apiquality.sonar.asyncapi.I18nContext;
import apiquality.sonar.asyncapi.checks.RulesLists;

import static org.assertj.core.api.Assertions.assertThat;

public class AsyncAPICustomRulesDefinitionTest {

	@Test
	public void testRepository() {
		I18nContext.setLang("en");
		AsyncAPICustomRulesDefinition rulesDefinition = new AsyncAPICustomRulesDefinition();
		RulesDefinition.Context context = new RulesDefinition.Context();
		rulesDefinition.define(context);
		Repository repository = context.repository(AsyncAPICustomRulesDefinition.REPOSITORY_KEY);
		assertThat(repository.name()).isEqualTo("AsyncAPI Custom");
		assertThat(repository.language()).isEqualTo("asyncapi");
		assertThat(repository.rules()).hasSize(RulesLists.getAllChecks().size());
	}
}
