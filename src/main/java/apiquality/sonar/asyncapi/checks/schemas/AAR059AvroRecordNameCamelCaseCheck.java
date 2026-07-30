package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.regex.Pattern;

@Rule(key = AAR059AvroRecordNameCamelCaseCheck.CHECK_KEY)
public class AAR059AvroRecordNameCamelCaseCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR059";
    private static final String ERROR_KEY = "AAR059.error";
    private static final Pattern CAMEL_CASE_PATTERN =
            Pattern.compile("^[A-Z][a-z0-9]+(?:[A-Z][a-z0-9]*)*$");

    @Override
    protected void visitAvroRecord(JsonNode node) {
        checkRecord(node);
    }

    private void checkRecord(JsonNode node) {
        JsonNode nameNode = node.get("name");
        if (!nameNode.isMissing() && !nameNode.isNull()) {
            String name = nameNode.stringValue();
            if (name != null && !CAMEL_CASE_PATTERN.matcher(name).matches()) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), nameNode.key());
            }
        }
        JsonNode fieldsNode = node.get("fields");
        if (fieldsNode.isMissing() || fieldsNode.isNull()) return;
        fieldsNode.elements().forEach(field -> checkFieldType(field.get("type")));
    }

    private void checkFieldType(JsonNode typeNode) {
        if (typeNode == null || typeNode.isMissing() || typeNode.isNull()) return;
        if (typeNode.isArray()) {
            typeNode.elements().forEach(this::checkFieldType);
            return;
        }
        if (!typeNode.isObject()) return;
        JsonNode nestedTypeNode = typeNode.get("type");
        if (nestedTypeNode.isMissing() || nestedTypeNode.isNull()) return;
        String nestedType = nestedTypeNode.stringValue();
        if ("record".equals(nestedType)) {
            checkRecord(typeNode);
        } else if ("array".equals(nestedType)) {
            checkFieldType(typeNode.get("items"));
        } else if ("map".equals(nestedType)) {
            checkFieldType(typeNode.get("values"));
        }
    }
}
