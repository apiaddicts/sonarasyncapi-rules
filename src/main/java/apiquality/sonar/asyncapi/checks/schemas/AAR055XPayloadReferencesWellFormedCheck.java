package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR055XPayloadReferencesWellFormedCheck.CHECK_KEY)
public class AAR055XPayloadReferencesWellFormedCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR055";
    private static final String ERROR_NOT_ARRAY_KEY = "AAR055.error-not-array";
    private static final String ERROR_ITEM_NOT_OBJECT_KEY = "AAR055.error-item-not-object";
    private static final String ERROR_MISSING_FIELD_KEY = "AAR055.error-missing-field";
    private static final String ERROR_INVALID_SUBJECT_KEY = "AAR055.error-invalid-subject";
    private static final String ERROR_INVALID_REF_KEY = "AAR055.error-invalid-ref";
    private static final String ERROR_INVALID_REFERENCE_NAME_KEY = "AAR055.error-invalid-reference-name";

    private static final String EXTENSION_KEY = "x-payload-references";
    private static final String FIELD_SUBJECT = "subject";
    private static final String FIELD_REF = "ref";
    private static final String FIELD_REFERENCE_NAME = "referenceName";
    private static final List<String> REQUIRED_FIELDS = Arrays.asList(FIELD_SUBJECT, FIELD_REF, FIELD_REFERENCE_NAME);

    private static final Pattern SUBJECT_PATTERN = Pattern.compile("^[:a-zA-Z0-9_.-]+$");
    private static final Pattern REF_PATTERN =
            Pattern.compile("^(?:https?|svn|svn\\+ssh|file)://[^\\s?@]+\\.avsc(?:[?@]\\S*)?$");
    private static final Pattern REFERENCE_NAME_PATTERN =
            Pattern.compile("^[a-z][a-z0-9_]{0,62}(?:\\.[a-z][a-z0-9_]{0,62}){0,20}\\.[A-Z]\\w{0,62}$");

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
            if (EXTENSION_KEY.equals(entry.getKey())) {
                validateReferences(entry.getValue());
            }
            visit(entry.getValue());
        }
    }

    private void validateReferences(JsonNode referencesNode) {
        if (!referencesNode.isArray()) {
            addIssue(CHECK_KEY, translate(ERROR_NOT_ARRAY_KEY), referencesNode.key());
            return;
        }

        for (JsonNode item : referencesNode.elements()) {
            validateItem(item);
        }
    }

    private void validateItem(JsonNode item) {
        if (!item.isObject()) {
            addIssue(CHECK_KEY, translate(ERROR_ITEM_NOT_OBJECT_KEY), item);
            return;
        }

        Map<String, JsonNode> props = item.propertyMap();
        for (String field : REQUIRED_FIELDS) {
            validateField(props, field, item);
        }
    }

    private void validateField(Map<String, JsonNode> props, String field, JsonNode item) {
        JsonNode value = props.get(field);
        if (value == null) {
            addIssue(CHECK_KEY, translate(ERROR_MISSING_FIELD_KEY, field), item);
            return;
        }
        if (isInvalidValue(value)) {
            addIssue(CHECK_KEY, translate(ERROR_MISSING_FIELD_KEY, field), value.key());
            return;
        }
        if (!patternFor(field).matcher(value.stringValue()).matches()) {
            addIssue(CHECK_KEY, translate(formatErrorKeyFor(field)), value.key());
        }
    }

    private static Pattern patternFor(String field) {
        switch (field) {
            case FIELD_REF:
                return REF_PATTERN;
            case FIELD_REFERENCE_NAME:
                return REFERENCE_NAME_PATTERN;
            default:
                return SUBJECT_PATTERN;
        }
    }

    private static String formatErrorKeyFor(String field) {
        switch (field) {
            case FIELD_REF:
                return ERROR_INVALID_REF_KEY;
            case FIELD_REFERENCE_NAME:
                return ERROR_INVALID_REFERENCE_NAME_KEY;
            default:
                return ERROR_INVALID_SUBJECT_KEY;
        }
    }

    private boolean isInvalidValue(JsonNode value) {
        if (value.isNull() || value.isObject() || value.isArray()) {
            return true;
        }
        return value.stringValue().trim().isEmpty();
    }
}
