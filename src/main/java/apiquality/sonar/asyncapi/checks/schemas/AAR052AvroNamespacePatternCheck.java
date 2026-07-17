package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import java.util.regex.Pattern;

@Rule(key = AAR052AvroNamespacePatternCheck.CHECK_KEY)
public class AAR052AvroNamespacePatternCheck extends AbstractAvroRecordCheck {
    public static final String CHECK_KEY = "AAR052";
    private static final String ERROR_KEY = "AAR052.error";

    public static final String DEFAULT_PATTERN =
        "^org\\.madrid\\.(common\\.[a-z0-9_-]+|[a-z0-9_-]+\\.[a-z0-9_-]+\\.[a-z0-9_-]+)$";

    @RuleProperty(
        key = "pattern",
        description = "Regular expression the Avro namespace must match (application schemas: "
            + "org.madrid.<cod_poaps>.<classification>.<domain>; common schemas: org.madrid.common.<domain>)",
        defaultValue = DEFAULT_PATTERN)
    public String patternStr = DEFAULT_PATTERN;

    private Pattern pattern;

    @Override
    protected void visitFile(JsonNode root) {
        pattern = Pattern.compile(patternStr);
        super.visitFile(root);
    }

    @Override
    protected void visitAvroRecord(JsonNode node) {
        JsonNode namespaceNode = node.get("namespace");
        if (namespaceNode.isMissing() || namespaceNode.isNull()) return;
        String namespace = namespaceNode.stringValue();
        if (namespace == null || !pattern.matcher(namespace).matches()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
        }
    }
}
