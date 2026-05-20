package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.regex.Pattern;

@Rule(key = AAR048AvroNameNomenclatureCheck.CHECK_KEY)
public class AAR048AvroNameNomenclatureCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR048";
    private static final String ERROR_KEY = "AAR048.error";
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z_]\\w*$");

    @Override
    protected void visitAvroRecord(JsonNode node) {
        JsonNode nameNode = node.get("name");
        if (!nameNode.isMissing() && !nameNode.isNull()) {
            String name = nameNode.stringValue();
            if (name != null && !NAME_PATTERN.matcher(name).matches()) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
            }
        }
        JsonNode fieldsNode = node.get("fields");
        if (fieldsNode.isMissing() || fieldsNode.isNull()) return;
        final JsonNode recordNode = node;
        fieldsNode.elements().forEach(field -> {
            JsonNode fieldNameNode = field.get("name");
            if (!fieldNameNode.isMissing() && !fieldNameNode.isNull()) {
                String fieldName = fieldNameNode.stringValue();
                if (fieldName != null && !NAME_PATTERN.matcher(fieldName).matches()) {
                    addIssue(CHECK_KEY, translate(ERROR_KEY), recordNode.key());
                }
            }
        });
    }
}
