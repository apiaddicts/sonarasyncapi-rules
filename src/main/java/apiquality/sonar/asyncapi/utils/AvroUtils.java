package apiquality.sonar.asyncapi.utils;

import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.List;

public class AvroUtils {

    public static final String AVRO_SCHEMA_FORMAT_PREFIX = "application/vnd.apache.avro";
    public static final String AVRO_SCHEMA_REF_SUFFIX = "/schema";

    private static final List<String> AVRO_ROOT_TYPES = Arrays.asList("record", "enum", "fixed");

    private AvroUtils() {
    }

    /**
     * Returns true when the message declares an Avro schemaFormat, handling
     * both AsyncAPI versions:
     *   v2: schemaFormat is directly inside the message object
     *   v3: schemaFormat is inside message.payload (Multi-Format Schema Object)
     */
    public static boolean isAvroMessage(JsonNode messageNode) {
        if (messageNode == null || messageNode.isMissing() || messageNode.isNull()) {
            return false;
        }
        // v2: message.schemaFormat
        if (hasAvroSchemaFormat(messageNode)) {
            return true;
        }
        // v3: message.payload.schemaFormat  (Multi-Format Schema Object)
        JsonNode payload = messageNode.get("payload");
        return payload != null && !payload.isMissing() && !payload.isNull()
                && hasAvroSchemaFormat(payload);
    }

    /**
     * Returns the actual Avro schema node from a message, regardless of version:
     *   v2: message.payload  (raw Avro schema)
     *   v3: message.payload.schema  (inside Multi-Format Schema Object)
     */
    public static JsonNode getAvroSchemaFromMessage(JsonNode messageNode) {
        if (messageNode == null || messageNode.isMissing() || messageNode.isNull()) {
            return null;
        }
        JsonNode payload = messageNode.get("payload");
        if (payload == null || payload.isMissing() || payload.isNull()) {
            return null;
        }
        // v3: payload is a Multi-Format Schema Object
        if (hasAvroSchemaFormat(payload)) {
            JsonNode schema = payload.get("schema");
            return (schema != null && !schema.isMissing()) ? schema : null;
        }
        // v2: payload IS the Avro schema
        return payload;
    }

    private static boolean hasAvroSchemaFormat(JsonNode node) {
        JsonNode schemaFormat = node.get("schemaFormat");
        if (schemaFormat == null || schemaFormat.isMissing() || schemaFormat.isNull()) {
            return false;
        }
        String value = schemaFormat.getTokenValue();
        return value != null && value.contains(AVRO_SCHEMA_FORMAT_PREFIX);
    }

    public static boolean isAvroSchema(JsonNode schemaNode) {
        if (schemaNode == null || schemaNode.isMissing() || schemaNode.isNull()) {
            return false;
        }
        JsonNode typeNode = schemaNode.get("type");
        if (typeNode == null || typeNode.isMissing() || typeNode.isNull()) {
            return false;
        }
        String type = typeNode.getTokenValue();
        return type != null && AVRO_ROOT_TYPES.contains(type);
    }

    public static boolean isAvroComponentSchema(JsonNode componentSchemaNode) {
        if (componentSchemaNode == null || componentSchemaNode.isMissing() || componentSchemaNode.isNull()) {
            return false;
        }
        JsonNode schemaFormat = componentSchemaNode.get("schemaFormat");
        if (schemaFormat == null || schemaFormat.isMissing() || schemaFormat.isNull()) {
            return false;
        }
        String value = schemaFormat.getTokenValue();
        return value != null && value.contains(AVRO_SCHEMA_FORMAT_PREFIX);
    }

    public static boolean hasAvroSchemaSuffix(String refValue) {
        return refValue != null && refValue.endsWith(AVRO_SCHEMA_REF_SUFFIX);
    }

    public static boolean isMissingAvroSchemaSuffix(String refValue) {
        if (refValue == null) {
            return false;
        }
        if (!refValue.startsWith("#/components/schemas/")) {
            return false;
        }
        return !refValue.endsWith(AVRO_SCHEMA_REF_SUFFIX);
    }
}
