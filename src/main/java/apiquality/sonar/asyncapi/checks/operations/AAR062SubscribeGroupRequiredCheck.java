package apiquality.sonar.asyncapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;

import java.util.Set;

@Rule(key = AAR062SubscribeGroupRequiredCheck.CHECK_KEY)
public class AAR062SubscribeGroupRequiredCheck extends BaseCheck {

    public static final String CHECK_KEY = "AAR062";
    private static final String ERROR_KEY = "AAR062.error";
    private static final String GROUP_EXTENSION = "x-scs-group";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        if (AsyncAPIVersionDetector.isVersion3Plus(rootNode)) {
            checkV3(rootNode);
        } else {
            checkV2(rootNode);
        }
    }

    private void checkV2(JsonNode rootNode) {
        JsonNode channelsNode = rootNode.get("channels");
        if (isAbsent(channelsNode)) return;
        for (JsonNode channel : channelsNode.propertyMap().values()) {
            if (!isAbsent(channel)) {
                checkConsumer(channel.get("subscribe"));
            }
        }
    }

    private void checkV3(JsonNode rootNode) {
        JsonNode operationsNode = rootNode.get("operations");
        if (isAbsent(operationsNode)) return;
        for (JsonNode operation : operationsNode.propertyMap().values()) {
            if (isReceiveOperation(operation)) {
                checkConsumer(operation);
            }
        }
    }

    private boolean isReceiveOperation(JsonNode operation) {
        if (isAbsent(operation)) return false;
        JsonNode actionNode = operation.get("action");
        return !isAbsent(actionNode) && "receive".equals(actionNode.stringValue());
    }

    private void checkConsumer(JsonNode operation) {
        if (isAbsent(operation)) return;
        if (!isAbsent(operation.get("$ref"))) return;
        if (!hasGroup(operation)) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), operation.key());
        }
    }

    private boolean hasGroup(JsonNode operation) {
        return hasScsGroup(operation) || hasKafkaGroupId(operation);
    }

    private boolean hasScsGroup(JsonNode operation) {
        JsonNode groupNode = operation.get(GROUP_EXTENSION);
        if (isAbsent(groupNode)) return false;
        String value = groupNode.stringValue();
        return value != null && !value.trim().isEmpty();
    }

    private boolean hasKafkaGroupId(JsonNode operation) {
        JsonNode bindings = operation.get("bindings");
        if (isAbsent(bindings)) return false;
        JsonNode kafka = bindings.get("kafka");
        if (isAbsent(kafka)) return false;
        JsonNode groupId = kafka.get("groupId");
        if (isAbsent(groupId)) return false;
        if (groupId.isObject() || groupId.isArray()) return true;
        String gid = groupId.stringValue();
        return gid != null && !gid.trim().isEmpty();
    }

    private static boolean isAbsent(JsonNode node) {
        return node == null || node.isMissing() || node.isNull();
    }
}
