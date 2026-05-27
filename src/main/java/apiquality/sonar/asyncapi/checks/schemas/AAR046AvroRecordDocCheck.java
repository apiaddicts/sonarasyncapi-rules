package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

@Rule(key = AAR046AvroRecordDocCheck.CHECK_KEY)
public class AAR046AvroRecordDocCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR046";
    private static final String ERROR_KEY = "AAR046.error";

    @Override
    protected void visitAvroRecord(JsonNode node) {
        JsonNode docNode = node.get("doc");
        if (docNode.isMissing() || docNode.isNull()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
        }
    }
}
