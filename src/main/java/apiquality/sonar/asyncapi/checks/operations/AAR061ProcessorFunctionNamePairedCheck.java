package apiquality.sonar.asyncapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Rule(key = AAR061ProcessorFunctionNamePairedCheck.CHECK_KEY)
public class AAR061ProcessorFunctionNamePairedCheck extends BaseCheck {

    public static final String CHECK_KEY = "AAR061";
    private static final String ERROR_KEY = "AAR061.error";
    private static final String FUNCTION_NAME = "x-scs-function-name";

    private static final class FunctionGroup {
        private boolean hasProduce = false;
        private boolean hasConsume = false;
        private final List<JsonNode> nodes = new ArrayList<>();
    }

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        Map<String, FunctionGroup> groups = new HashMap<>();

        if (AsyncAPIVersionDetector.isVersion3Plus(rootNode)) {
            collectV3(rootNode, groups);
        } else {
            collectV2(rootNode, groups);
        }

        for (FunctionGroup group : groups.values()) {
            if (!group.hasProduce || !group.hasConsume) {
                for (JsonNode functionNode : group.nodes) {
                    addIssue(CHECK_KEY, translate(ERROR_KEY, functionNode.stringValue()), functionNode.key());
                }
            }
        }
    }

    private void collectV2(JsonNode rootNode, Map<String, FunctionGroup> groups) {
        JsonNode channelsNode = rootNode.get("channels");
        if (isAbsent(channelsNode)) return;
        for (Map.Entry<String, JsonNode> entry : channelsNode.propertyMap().entrySet()) {
            JsonNode channel = entry.getValue();
            if (isAbsent(channel)) continue;
            record(channel.get("publish"), true, groups);
            record(channel.get("subscribe"), false, groups);
        }
    }

    private void collectV3(JsonNode rootNode, Map<String, FunctionGroup> groups) {
        JsonNode operationsNode = rootNode.get("operations");
        if (isAbsent(operationsNode)) return;
        for (Map.Entry<String, JsonNode> entry : operationsNode.propertyMap().entrySet()) {
            JsonNode operation = entry.getValue();
            if (isAbsent(operation)) continue;
            JsonNode actionNode = operation.get("action");
            if (isAbsent(actionNode)) continue;
            String action = actionNode.stringValue();
            if ("send".equals(action)) {
                record(operation, true, groups);
            } else if ("receive".equals(action)) {
                record(operation, false, groups);
            }
        }
    }

    private void record(JsonNode operation, boolean isProduce, Map<String, FunctionGroup> groups) {
        if (isAbsent(operation)) return;
        JsonNode functionNode = operation.get(FUNCTION_NAME);
        if (isAbsent(functionNode)) return;
        String value = functionNode.stringValue();
        if (value == null || value.trim().isEmpty()) return;
        FunctionGroup group = groups.computeIfAbsent(value, key -> new FunctionGroup());
        if (isProduce) {
            group.hasProduce = true;
        } else {
            group.hasConsume = true;
        }
        group.nodes.add(functionNode);
    }

    private static boolean isAbsent(JsonNode node) {
        return node == null || node.isMissing() || node.isNull();
    }
}
