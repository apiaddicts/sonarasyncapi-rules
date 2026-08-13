package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AvroUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Rule(key = AAR056AvroSchemaFormatCheck.CHECK_KEY)
public class AAR056AvroSchemaFormatCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR056";
    private static final String ERROR_KEY = "AAR056.error";

    private static final String SCHEMA_FORMAT_KEY = "schemaFormat";
    private static final String EXPECTED_SCHEMA_FORMAT = "application/vnd.apache.avro;version=1.9.0";

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
            if (SCHEMA_FORMAT_KEY.equals(entry.getKey())) {
                validateSchemaFormat(entry.getValue());
            }
            visit(entry.getValue());
        }
    }

    private void validateSchemaFormat(JsonNode schemaFormatNode) {
        if (schemaFormatNode == null || schemaFormatNode.isMissing() || schemaFormatNode.isNull()) {
            return;
        }
        if (schemaFormatNode.isObject() || schemaFormatNode.isArray()) {
            return;
        }

        String value = schemaFormatNode.stringValue();
        if (value == null || !value.toLowerCase(Locale.ROOT).contains(AvroUtils.AVRO_SCHEMA_FORMAT_PREFIX)) {
            return;
        }

        if (!EXPECTED_SCHEMA_FORMAT.equals(value)) {
            addIssue(CHECK_KEY, translate(ERROR_KEY, value, EXPECTED_SCHEMA_FORMAT), schemaFormatNode.key());
        }
    }
}
