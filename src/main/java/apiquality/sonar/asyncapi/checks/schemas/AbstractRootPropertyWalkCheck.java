package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

public abstract class AbstractRootPropertyWalkCheck extends BaseCheck {

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        visit(rootNode);
    }

    private void visit(JsonNode node) {
        if (node == null || node.isMissing() || node.isNull()) {
            return;
        }
        if (node.isArray()) {
            for (JsonNode element : node.elements()) {
                visit(element);
            }
            return;
        }
        if (!node.isObject()) {
            return;
        }
        for (Map.Entry<String, JsonNode> entry : node.propertyMap().entrySet()) {
            visitProperty(entry.getKey(), entry.getValue());
            visit(entry.getValue());
        }
    }

    protected abstract void visitProperty(String key, JsonNode value);
}
