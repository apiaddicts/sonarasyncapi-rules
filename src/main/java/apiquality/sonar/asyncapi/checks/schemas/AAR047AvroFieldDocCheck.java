package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

@Rule(key = AAR047AvroFieldDocCheck.CHECK_KEY)
public class AAR047AvroFieldDocCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR047";
    private static final String ERROR_KEY = "AAR047.error";

    @Override
    protected void visitAvroRecord(JsonNode node) {
        JsonNode fieldsNode = node.get("fields");
        if (fieldsNode.isMissing() || fieldsNode.isNull()) return;
        final JsonNode recordNode = node;
        fieldsNode.elements().forEach(field -> {
            JsonNode docNode = field.get("doc");
            if (docNode.isMissing() || docNode.isNull()) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), recordNode.key());
            }
        });
    }
}
