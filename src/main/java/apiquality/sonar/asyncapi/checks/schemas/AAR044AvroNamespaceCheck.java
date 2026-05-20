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

@Rule(key = AAR044AvroNamespaceCheck.CHECK_KEY)
public class AAR044AvroNamespaceCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR044";
    private static final String ERROR_KEY = "AAR044.error";

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
        if (namespaceNode.isMissing() || namespaceNode.isNull()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
        }
    }

    private boolean isAvroRecord(JsonNode node) {
        JsonNode typeNode = node.get("type");
        if (typeNode.isMissing() || typeNode.isNull()) return false;
        return "record".equals(typeNode.stringValue());
    }
}
