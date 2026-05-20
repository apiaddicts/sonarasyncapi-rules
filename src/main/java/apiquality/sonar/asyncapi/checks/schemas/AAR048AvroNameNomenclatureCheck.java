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

@Rule(key = AAR048AvroNameNomenclatureCheck.CHECK_KEY)
public class AAR048AvroNameNomenclatureCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR048";
    private static final String ERROR_KEY = "AAR048.error";
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*$");

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
        JsonNode nameNode = node.get("name");
        if (!nameNode.isMissing() && !nameNode.isNull()) {
            String name = nameNode.stringValue();
            if (name != null && !NAME_PATTERN.matcher(name).matches()) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), node.key());
            }
        }
        JsonNode fieldsNode = node.get("fields");
        if (fieldsNode.isMissing() || fieldsNode.isNull()) return;
        final JsonNode recordNode = node;
        fieldsNode.elements().forEach(field -> {
            JsonNode fieldNameNode = field.get("name");
            if (!fieldNameNode.isMissing() && !fieldNameNode.isNull()) {
                String fieldName = fieldNameNode.stringValue();
                if (fieldName != null && !NAME_PATTERN.matcher(fieldName).matches()) {
                    addIssue(CHECK_KEY, translate(ERROR_KEY), recordNode.key());
                }
            }
        });
    }

    private boolean isAvroRecord(JsonNode node) {
        JsonNode typeNode = node.get("type");
        if (typeNode.isMissing() || typeNode.isNull()) return false;
        return "record".equals(typeNode.stringValue());
    }
}
