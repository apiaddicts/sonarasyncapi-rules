package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AvroUtils;
import apiquality.sonar.asyncapi.utils.JsonNodeUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR045AvroNamespaceNamingCheck.CHECK_KEY)
public class AAR045AvroNamespaceNamingCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR045";
    private static final String ERROR_KEY = "AAR045.error";
    private static final Pattern NAMESPACE_PATTERN = Pattern.compile("^[a-z][a-z0-9]*(\\.[a-z][a-z0-9]*)*$");

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
        JsonNode namespaceNode = node.get("namespace");
        if (namespaceNode.isMissing() || namespaceNode.isNull()) return;
        String namespace = namespaceNode.stringValue();
        if (namespace == null || !NAMESPACE_PATTERN.matcher(namespace).matches()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
        }
    }

    private boolean isAvroRecord(JsonNode node) {
        JsonNode typeNode = node.get("type");
        if (typeNode.isMissing() || typeNode.isNull()) return false;
        return "record".equals(typeNode.stringValue());
    }
}
