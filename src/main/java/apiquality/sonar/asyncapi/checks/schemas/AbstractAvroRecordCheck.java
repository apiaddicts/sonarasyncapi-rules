package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AvroUtils;
import apiquality.sonar.asyncapi.utils.JsonNodeUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

public abstract class AbstractAvroRecordCheck extends BaseCheck {

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
        visitAvroRecord(node);
    }

    protected abstract void visitAvroRecord(JsonNode node);

    protected boolean isAvroRecord(JsonNode node) {
        JsonNode typeNode = node.get("type");
        if (typeNode.isMissing() || typeNode.isNull()) return false;
        return "record".equals(typeNode.stringValue());
    }
}
