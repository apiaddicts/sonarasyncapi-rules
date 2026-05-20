package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

@Rule(key = AAR044AvroNamespaceCheck.CHECK_KEY)
public class AAR044AvroNamespaceCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR044";
    private static final String ERROR_KEY = "AAR044.error";

    @Override
    protected void visitAvroRecord(JsonNode node) {
        JsonNode namespaceNode = node.get("namespace");
        if (namespaceNode.isMissing() || namespaceNode.isNull()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
        }
    }
}
