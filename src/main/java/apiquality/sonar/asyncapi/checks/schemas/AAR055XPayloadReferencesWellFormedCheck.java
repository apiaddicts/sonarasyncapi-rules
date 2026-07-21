package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Rule(key = AAR055XPayloadReferencesWellFormedCheck.CHECK_KEY)
public class AAR055XPayloadReferencesWellFormedCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR055";
    private static final String ERROR_NOT_ARRAY_KEY = "AAR055.error-not-array";
    private static final String ERROR_ITEM_NOT_OBJECT_KEY = "AAR055.error-item-not-object";
    private static final String ERROR_MISSING_FIELD_KEY = "AAR055.error-missing-field";

    private static final String EXTENSION_KEY = "x-payload-references";
    private static final List<String> REQUIRED_FIELDS = Arrays.asList("subject", "ref", "referenceName");

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        visit(rootNode);
    }

    private void visit(JsonNode node) {
        if (node == null || node.isMissing() || node.isNull()) {
            return;
        }
        if (node.isArray()) {
            for (JsonNode element : node.elements()) {
                visit(element);
            }
            return;
        }
        if (!node.isObject()) {
            return;
        }
        for (Map.Entry<String, JsonNode> entry : node.propertyMap().entrySet()) {
            if (EXTENSION_KEY.equals(entry.getKey())) {
                validateReferences(entry.getValue());
            }
            visit(entry.getValue());
        }
    }

    private void validateReferences(JsonNode referencesNode) {
        if (!referencesNode.isArray()) {
            addIssue(CHECK_KEY, translate(ERROR_NOT_ARRAY_KEY), referencesNode.key());
            return;
        }

        for (JsonNode item : referencesNode.elements()) {
            validateItem(item);
        }
    }

    private void validateItem(JsonNode item) {
        if (!item.isObject()) {
            addIssue(CHECK_KEY, translate(ERROR_ITEM_NOT_OBJECT_KEY), item);
            return;
        }

        Map<String, JsonNode> props = item.propertyMap();
        for (String field : REQUIRED_FIELDS) {
            validateField(props, field, item);
        }
    }

    private void validateField(Map<String, JsonNode> props, String field, JsonNode item) {
        JsonNode value = props.get(field);
        if (value == null) {
            addIssue(CHECK_KEY, translate(ERROR_MISSING_FIELD_KEY, field), item);
        } else if (isInvalidValue(value)) {
            addIssue(CHECK_KEY, translate(ERROR_MISSING_FIELD_KEY, field), value.key());
        }
    }

    private boolean isInvalidValue(JsonNode value) {
        if (value.isNull() || value.isObject() || value.isArray()) {
            return true;
        }
        return value.stringValue().trim().isEmpty();
    }
}
