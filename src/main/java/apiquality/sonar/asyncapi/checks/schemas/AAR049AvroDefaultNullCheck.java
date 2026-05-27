package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;

@Rule(key = AAR049AvroDefaultNullCheck.CHECK_KEY)
public class AAR049AvroDefaultNullCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR049";
    private static final String ERROR_KEY = "AAR049.error";

    @Override
    protected void visitAvroRecord(JsonNode node) {
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
