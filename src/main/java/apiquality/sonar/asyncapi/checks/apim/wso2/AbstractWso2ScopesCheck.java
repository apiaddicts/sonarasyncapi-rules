package apiquality.sonar.asyncapi.checks.apim.wso2;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;

public abstract class AbstractWso2ScopesCheck extends BaseCheck {

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(AsyncApiGrammar.ROOT);
	}

	@Override
	public void visitNode(JsonNode node) {
		visitV2NV3Node(node);
	}

	private void visitV2NV3Node(JsonNode node) {
		JsonNode scopesNode = node.get("x-wso2-security").get("apim").get("x-wso2-scopes");
		visitScopesNode(scopesNode);
		if (scopesNode.isMissing() || scopesNode.isNull()) return;
		List<JsonNode> scopes = scopesNode.elements();
		visitScopes(scopes);
		scopes.forEach(this::visitScope);
	}

	protected void visitScopesNode(JsonNode scopesNode) {
		// Intentional blank
	}

	protected void visitScopes(List<JsonNode> scopes) {
		// Intentional blank
	}

	protected abstract void visitScope(JsonNode scope);
}
