package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Rule(key = AAR001MandatoryHttpsProtocolCheck.KEY)
public class AAR001MandatoryHttpsProtocolCheck extends BaseCheck {

    public static final String KEY = "AAR001";

    private static final Set<String> SECURE_PROTOCOLS = new HashSet<>(Arrays.asList(
        "https", "wss", "amqps", "mqtts", "mqtt+tls", "mqtt+ssl",
        "kafka", "kafka-ssl", "kafka-secure", "jms"));

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

        if (AsyncAPIVersionDetector.isVersion3Plus(node)) {
            // v3+: servers is an array
            for (JsonNode server : serversNode.elements()) {
                checkServer(server, false);
            }
        } else {
            // v2: servers is a map
            for (Map.Entry<String, JsonNode> entry : serversNode.propertyMap().entrySet()) {
                checkServerV2(entry.getValue());
            }
        }
    }

    private void checkServer(JsonNode server, boolean isV2) {
        JsonNode protocolNode = server.get("protocol");
        if (protocolNode == null || protocolNode.isMissing() || protocolNode.isNull()) return;
        String protocol = protocolNode.getTokenValue();
        if (protocol != null && !SECURE_PROTOCOLS.contains(protocol.toLowerCase())) {
            // For array items, server.key() resolves to line 1
            addIssue(KEY, translate("AAR001.error"), server.key());
        }
    }

    private void checkServerV2(JsonNode server) {
        JsonNode protocolNode = server.get("protocol");
        if (protocolNode == null || protocolNode.isMissing() || protocolNode.isNull()) return;
        String protocol = protocolNode.getTokenValue();
        if (protocol != null && !"kafka".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
            addIssue(KEY, translate("AAR001.error-v2-https"), protocolNode.key());
        }
    }
}
