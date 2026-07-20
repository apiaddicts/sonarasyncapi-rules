package apiquality.sonar.asyncapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Rule(key = AAR054ClassificationValidValuesCheck.CHECK_KEY)
public class AAR054ClassificationValidValuesCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR054";
    private static final String ERROR_KEY = "AAR054.error";

    public static final String DEFAULT_VALID_VALUES = "cdc,cmd,sys";

    @RuleProperty(
        key = "validValues",
        description = "Comma-separated list of allowed values for the channel name's classification segment (2nd segment)",
        defaultValue = DEFAULT_VALID_VALUES)
    public String validValuesStr = DEFAULT_VALID_VALUES;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        Set<String> validValues = new HashSet<>(Arrays.asList(validValuesStr.split(",")));

        JsonNode channelsNode = rootNode.get("channels");
        if (channelsNode == null || channelsNode.isMissing() || channelsNode.isNull()) {
            return;
        }

        for (Map.Entry<String, JsonNode> channelEntry : channelsNode.propertyMap().entrySet()) {
            JsonNode channelNode = channelEntry.getValue();
            JsonNode addressNode = channelNode.get("address");

            String channelName;
            JsonNode issueLocation;
            if (addressNode != null && !addressNode.isMissing()) {
                if (addressNode.isNull()) {
                    continue;
                }
                channelName = addressNode.stringValue();
                issueLocation = addressNode;
            } else {
                channelName = channelEntry.getKey();
                issueLocation = channelNode.key();
            }

            if (channelName == null) {
                continue;
            }

            String[] segments = channelName.split("\\.");
            String classification = segments.length > 1 ? segments[1] : null;

            if (classification == null || !validValues.contains(classification)) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), issueLocation);
            }
        }
    }
}
