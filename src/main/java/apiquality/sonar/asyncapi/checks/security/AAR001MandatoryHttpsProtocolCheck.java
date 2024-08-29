package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Map;
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
        JsonNode serversNode = node.get("servers");
        if (serversNode.isMissing() || serversNode.isNull()) {
        } else {
            	Map<String, JsonNode> serverNodes = serversNode.propertyMap();

				for (Map.Entry<String, JsonNode> entry : serverNodes.entrySet()) {
					JsonNode serverNode = entry.getValue();
					JsonNode protocolNode = serverNode.get("protocol");

					if (protocolNode.isMissing() || !protocolNode.getTokenValue().equals("https")) {
						addIssue(KEY, translate("AAR001.error-v2-https"), protocolNode.key());
					}
				}
		}
	}
}
