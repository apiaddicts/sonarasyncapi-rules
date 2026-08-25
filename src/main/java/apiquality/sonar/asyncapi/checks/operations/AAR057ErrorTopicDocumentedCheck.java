/*
 * SonarQube AsyncAPI Plugin
 * Copyright (C) 2018-2019 Societe Generale
 * vincent.girard-reydet AT socgen DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package apiquality.sonar.asyncapi.checks.operations;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR057ErrorTopicDocumentedCheck.CHECK_KEY)
public class AAR057ErrorTopicDocumentedCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR057";
    private static final String ERROR_KEY = "AAR057.error";


    private static final Pattern ERROR_TOPIC_PATTERN = Pattern.compile(
            "^[a-z0-9]{1,63}(?:-[a-z0-9]{1,63}){0,10}(?:\\.[a-z0-9]{1,63}(?:-[a-z0-9]{1,63}){0,10}){0,20}\\.error\\.\\d{1,10}$");

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        JsonNode channelsNode = rootNode.get("channels");
        if (channelsNode == null || channelsNode.isMissing() || channelsNode.isNull()) {
            JsonNode anchor = channelsNode == null ? rootNode : channelsNode;
            addIssue(CHECK_KEY, translate(ERROR_KEY), anchor.key());
            return;
        }

        boolean isV3 = AsyncAPIVersionDetector.isVersion3Plus(rootNode);

        for (Map.Entry<String, JsonNode> entry : channelsNode.propertyMap().entrySet()) {
            String topicName = resolveTopicName(entry, isV3);
            if (topicName != null && ERROR_TOPIC_PATTERN.matcher(topicName).matches()) {
                return;
            }
        }

        addIssue(CHECK_KEY, translate(ERROR_KEY), channelsNode.key());
    }

    private String resolveTopicName(Map.Entry<String, JsonNode> entry, boolean isV3) {
        if (!isV3) {
            return entry.getKey();
        }
        JsonNode addressNode = entry.getValue().get("address");
        if (addressNode == null || addressNode.isMissing() || addressNode.isNull()) {
            return null;
        }
        return addressNode.stringValue();
    }
}
