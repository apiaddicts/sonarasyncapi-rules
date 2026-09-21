package apiquality.sonar.asyncapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR053ChannelNamingConventionCheck.CHECK_KEY)
public class AAR053ChannelNamingConventionCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR053";
    private static final String ERROR_KEY = "AAR053.error";

    public static final String DEFAULT_PATTERN =
        "^[a-z0-9]+(?:-[a-z0-9]+)*(?:\\.[a-z0-9]+(?:-[a-z0-9]+)*){4}(?:\\.[a-z0-9]+(?:-[a-z0-9]+)*)?$";

    @RuleProperty(
        key = "pattern",
        description = "Regular expression the channel name must match: "
            + "<cod_poaps>.<classification>.<domain>.<origin>.<scope>[.<version>] "
            + "(lowercase alphanumeric segments separated by dots; hyphens allowed only within a segment)",
        defaultValue = DEFAULT_PATTERN)
    public String patternStr = DEFAULT_PATTERN;

    private Pattern pattern;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        pattern = Pattern.compile(patternStr);

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

            if (channelName == null || !pattern.matcher(channelName).matches()) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), issueLocation);
            }
        }
    }
}
