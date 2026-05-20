package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AvroUtils;
import apiquality.sonar.asyncapi.utils.JsonNodeUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;

@Rule(key = AAR049AvroDefaultNullCheck.CHECK_KEY)
public class AAR049AvroDefaultNullCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR049";
    private static final String ERROR_KEY = "AAR049.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.SCHEMA, AsyncApiGrammar.PAYLOAD_SCHEMA);
    }

    @Override
    protected void visitNode(JsonNode node) {
        node = JsonNodeUtils.resolve(node);
        if (AvroUtils.isAvroComponentSchema(node)) {
            JsonNode inner = node.get("schema");
            if (inner == null || inner.isMissing() || inner.isNull()) return;
            node = inner;
        }
        if (!isAvroRecord(node)) return;
        JsonNode fieldsNode = node.get("fields");
        if (fieldsNode.isMissing() || fieldsNode.isNull()) return;
        final JsonNode recordNode = node;
        fieldsNode.elements().forEach(field -> {
            JsonNode typeNode = field.get("type");
            if (typeNode.isMissing() || typeNode.isNull()) return;
            if (!isNullableUnion(typeNode)) return;
            JsonNode defaultNode = field.get("default");
            boolean hasNullDefault = !defaultNode.isMissing() && defaultNode.isNull();
            if (!hasNullDefault) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), recordNode.key());
            }
        });
    }

    private boolean isAvroRecord(JsonNode node) {
        JsonNode typeNode = node.get("type");
        if (typeNode.isMissing() || typeNode.isNull()) return false;
        return "record".equals(typeNode.stringValue());
    }

    private boolean isNullableUnion(JsonNode typeNode) {
        List<JsonNode> elements = typeNode.elements();
        if (elements == null || elements.isEmpty()) return false;
        return elements.stream().anyMatch(e -> {
            if (e.isNull()) return true;
            String tv = e.getTokenValue();
            return "null".equals(tv) || "\"null\"".equals(tv);
        });
    }
}
