package apiquality.sonar.asyncapi.checks.schemas;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.JsonNodeUtils;

import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR060ContentTypeAvroCheck.CHECK_KEY)
public class AAR060ContentTypeAvroCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR060";
    private static final String ERROR_KEY = "AAR060.error";
    private static final Pattern AVRO_CONTENT_TYPE =
            Pattern.compile("^application/.{1,255}\\+avro$");

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT, AsyncApiGrammar.MESSAGE);
    }

    @Override
    protected void visitNode(JsonNode node) {
        JsonNode asyncapiNode = node.get("asyncapi");
        if (!asyncapiNode.isMissing() && !asyncapiNode.isNull()) {
            checkContentType(node.get("defaultContentType"));
        } else {
            JsonNode resolved = JsonNodeUtils.resolve(node);
            checkContentType(resolved.get("contentType"));
        }
    }

    private void checkContentType(JsonNode contentTypeNode) {
        if (contentTypeNode.isMissing() || contentTypeNode.isNull()) return;
        String value = contentTypeNode.stringValue();
        if (value != null && !AVRO_CONTENT_TYPE.matcher(value).matches()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), contentTypeNode.key());
        }
    }
}
