package apiquality.sonar.asyncapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = AAR008DefinedServerCheck.KEY)
public class AAR008DefinedServerCheck extends BaseCheck {

    public static final String KEY = "AAR008";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.ROOT);
    }

    @Override
    public void visitNode(JsonNode node) {
        AsyncAPIVersionDetector.AsyncAPIVersion version = AsyncAPIVersionDetector.detectVersion(node);
        JsonNode serversNode = node.get("servers");

        if (version == AsyncAPIVersionDetector.AsyncAPIVersion.V2) {
            if (serversNode.isMissing() || serversNode.isNull()) {
                addIssue(KEY, translate("AAR008.error-v2-servers"), serversNode.key());
            }
        } else if (AsyncAPIVersionDetector.isVersion3Plus(node) && (serversNode.isMissing() || serversNode.isNull())) {
            addIssue(KEY, translate("AAR008.error-v3-servers"), serversNode.key());
        }
    }
}
