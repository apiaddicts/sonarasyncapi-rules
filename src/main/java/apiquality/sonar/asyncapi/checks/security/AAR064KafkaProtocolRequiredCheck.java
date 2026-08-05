package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Rule(key = AAR064KafkaProtocolRequiredCheck.CHECK_KEY)
public class AAR064KafkaProtocolRequiredCheck extends BaseCheck {

    public static final String CHECK_KEY = "AAR064";
    private static final String MESSAGE = "AAR064.error";

    private static final Set<String> ALLOWED_PROTOCOLS = new HashSet<>(Arrays.asList("kafka", "kafka-ssl"));

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

        if (serversNode.isArray()) {
            for (JsonNode server : serversNode.elements()) {
                checkServer(server);
            }
        } else {
            for (JsonNode server : serversNode.propertyMap().values()) {
                checkServer(server);
            }
        }
    }

    private void checkServer(JsonNode server) {
        JsonNode protocolNode = server.get("protocol");
        if (protocolNode == null || protocolNode.isMissing() || protocolNode.isNull()) {
            return;
        }
        String protocol = protocolNode.getTokenValue();
        if (protocol == null) {
            return;
        }
        if (!ALLOWED_PROTOCOLS.contains(protocol)) {
            addIssue(CHECK_KEY, translate(MESSAGE), protocolNode.key());
        }
    }
}
