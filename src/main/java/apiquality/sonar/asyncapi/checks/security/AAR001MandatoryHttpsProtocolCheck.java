package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

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
            return;
        }

        Map<String, JsonNode> serverNodes = serversNode.propertyMap();

        for (Map.Entry<String, JsonNode> entry : serverNodes.entrySet()) {
            JsonNode protocolNode = entry.getValue().get("protocol");

            if (protocolNode != null && !protocolNode.isMissing() && !protocolNode.isNull()) {
                String protocol = protocolNode.getTokenValue();

                if (protocol != null && !"kafka".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                    addIssue(KEY, translate("AAR001.error-v2-https"), protocolNode.key());
                }
            }
        }
    }
}
