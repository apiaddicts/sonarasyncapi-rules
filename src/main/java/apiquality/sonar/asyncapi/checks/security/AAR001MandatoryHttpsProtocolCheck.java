package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;

import apiquality.sonar.asyncapi.checks.BaseCheck;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;

@Rule(key = AAR001MandatoryHttpsProtocolCheck.KEY)
public class AAR001MandatoryHttpsProtocolCheck extends BaseCheck {

	public static final String KEY = "AAR001";

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(AsyncApiGrammar.ROOT);
	}

	@Override
	public void visitNode(JsonNode node) {
		if (node.getType() instanceof AsyncApiGrammar) {
			visitV2Node(node);
		} else {
			visitV3Node(node);
		}
	}

	private void visitV2Node(JsonNode node) {
		JsonNode schemesNode = node.get("schemes");
		if (schemesNode.isMissing() || schemesNode.isNull()) {
			addIssue(KEY, translate("OAR001.error-v2-schemes"), node.key());
		} else {
			List<JsonNode> protocolNodes = schemesNode.elements();
			boolean httpsDeclared = protocolNodes.stream().map(AstNode::getTokenValue).anyMatch("https"::equals);
			if (!httpsDeclared) addIssue(KEY, translate("OAR001.error-v2-https"), schemesNode.key());
		}
	}

	private void visitV3Node(JsonNode node) {
		JsonNode serversNode = node.get("servers");
		if (serversNode.isMissing() || serversNode.isNull()) {
			addIssue(KEY, translate("OAR001.error-v3-servers"), node.key());
		} else {
			List<JsonNode> protocolNodes = serversNode.elements();
			for (JsonNode jsonNode : protocolNodes) {
				JsonNode urlNode = jsonNode.get("url");
				String server = urlNode.getTokenValue();
				if (!server.matches("^(https)?://(.*)")) {
					addIssue(KEY, translate("OAR001.error-v3-https"), urlNode.key());
				}
			}
		}
	}
}
