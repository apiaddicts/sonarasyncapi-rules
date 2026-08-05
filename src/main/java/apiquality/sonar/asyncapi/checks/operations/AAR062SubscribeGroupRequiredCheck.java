package apiquality.sonar.asyncapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;

import java.util.Map;
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
        for (Map.Entry<String, JsonNode> entry : channelsNode.propertyMap().entrySet()) {
            JsonNode channel = entry.getValue();
            if (isAbsent(channel)) continue;
            JsonNode subscribe = channel.get("subscribe");
            if (isAbsent(subscribe)) continue;
            if (!isAbsent(subscribe.get("$ref"))) continue; // unresolved $ref operation: group lives at the ref target
            if (!hasGroup(subscribe)) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), subscribe.key());
            }
        }
    }

    private void checkV3(JsonNode rootNode) {
        JsonNode operationsNode = rootNode.get("operations");
        if (isAbsent(operationsNode)) return;
        for (Map.Entry<String, JsonNode> entry : operationsNode.propertyMap().entrySet()) {
            JsonNode operation = entry.getValue();
            if (isAbsent(operation)) continue;
            JsonNode actionNode = operation.get("action");
            if (isAbsent(actionNode)) continue;
            if (!"receive".equals(actionNode.stringValue())) continue;
            if (!hasGroup(operation)) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), operation.key());
            }
        }
    }

    private boolean hasGroup(JsonNode operation) {
        JsonNode groupNode = operation.get(GROUP_EXTENSION);
        if (!isAbsent(groupNode)) {
            String value = groupNode.stringValue();
            if (value != null && !value.trim().isEmpty()) return true;
        }

        JsonNode bindings = operation.get("bindings");
        if (!isAbsent(bindings)) {
            JsonNode kafka = bindings.get("kafka");
            if (!isAbsent(kafka)) {
                JsonNode groupId = kafka.get("groupId");
                if (!isAbsent(groupId)) {
                    if (groupId.isObject() || groupId.isArray()) return true;
                    String gid = groupId.stringValue();
                    if (gid != null && !gid.trim().isEmpty()) return true;
                }
            }
        }

        return false;
    }

    private static boolean isAbsent(JsonNode node) {
        return node == null || node.isMissing() || node.isNull();
    }
}
