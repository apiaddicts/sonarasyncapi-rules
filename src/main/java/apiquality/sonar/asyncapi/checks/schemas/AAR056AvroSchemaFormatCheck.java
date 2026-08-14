package apiquality.sonar.asyncapi.checks.schemas;

import org.sonar.check.Rule;
import apiquality.sonar.asyncapi.utils.AvroUtils;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Locale;

@Rule(key = AAR056AvroSchemaFormatCheck.CHECK_KEY)
public class AAR056AvroSchemaFormatCheck extends AbstractRootPropertyWalkCheck {
    public static final String CHECK_KEY = "AAR056";
    private static final String ERROR_KEY = "AAR056.error";

    private static final String SCHEMA_FORMAT_KEY = "schemaFormat";
    private static final String EXPECTED_SCHEMA_FORMAT = "application/vnd.apache.avro;version=1.9.0";

    @Override
    protected void visitProperty(String key, JsonNode value) {
        if (SCHEMA_FORMAT_KEY.equals(key)) {
            validateSchemaFormat(value);
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
