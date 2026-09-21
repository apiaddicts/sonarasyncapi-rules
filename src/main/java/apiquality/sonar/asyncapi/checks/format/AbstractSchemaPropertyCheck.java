package apiquality.sonar.asyncapi.checks.format;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.JsonNodeUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

public abstract class AbstractSchemaPropertyCheck extends BaseCheck {

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.SCHEMA, AsyncApiGrammar.PAYLOAD_SCHEMA);
    }

    @Override
    protected void visitNode(JsonNode node) {
        node = JsonNodeUtils.resolve(node);
        JsonNode properties = node.get("properties");
        if (properties == null || properties.isMissing() || properties.isNull()) {
            return;
        }
        for (Map.Entry<String, JsonNode> entry : properties.propertyMap().entrySet()) {
            JsonNode original = entry.getValue();
            JsonNode property = JsonNodeUtils.resolve(original);
            checkProperty(property, original);
        }
    }

    /**
     * @param property the resolved property schema (after following any $ref)
     * @param anchor   the original property node, used to anchor the issue at the
     *                 property's own key in the document
     */
    protected abstract void checkProperty(JsonNode property, JsonNode anchor);

    protected static String typeOf(JsonNode node) {
        JsonNode type = node.get("type");
        if (type == null || type.isMissing() || type.isNull()) {
            return null;
        }
        return type.stringValue();
    }

    protected static boolean present(JsonNode node, String key) {
        JsonNode child = node.get(key);
        return child != null && !child.isMissing() && !child.isNull();
    }

    protected static boolean hasNonEmptyEnum(JsonNode node) {
        JsonNode enumNode = node.get("enum");
        return enumNode != null && !enumNode.isMissing() && !enumNode.isNull()
                && enumNode.isArray() && !enumNode.elements().isEmpty();
    }
}
