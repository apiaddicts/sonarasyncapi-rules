package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.regex.Pattern;

@Rule(key = AAR045AvroNamespaceNamingCheck.CHECK_KEY)
public class AAR045AvroNamespaceNamingCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR045";
    private static final String ERROR_KEY = "AAR045.error";
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^[a-z][a-z0-9]*+(\\.[a-z][a-z0-9]*+)*+$");

    @Override
    protected void visitAvroRecord(JsonNode node) {
        JsonNode namespaceNode = node.get("namespace");
        if (namespaceNode.isMissing() || namespaceNode.isNull()) return;
        String namespace = namespaceNode.stringValue();
        if (namespace == null || !NAMESPACE_PATTERN.matcher(namespace).matches()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
        }
    }
}
