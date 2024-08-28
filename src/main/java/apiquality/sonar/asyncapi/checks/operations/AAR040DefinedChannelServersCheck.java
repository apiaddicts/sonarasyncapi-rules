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
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;
import java.util.Map;

@Rule(key = AAR040DefinedChannelServersCheck.CHECK_KEY)
public class AAR040DefinedChannelServersCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR040";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.CHANNEL);
    }

    @Override
    protected void visitNode(JsonNode channelNode) {
        // Navigate to the root and get the 'servers' object
        JsonNode rootNode = getRootNode(channelNode);
        JsonNode serversNode = rootNode.get("servers");

        if (serversNode.isMissing() || serversNode.isNull()) {
            return; // No servers defined at the root level, nothing to check against
        }

        // Access the servers referenced by the channel
        JsonNode channelServers = channelNode.get("servers");
        if (channelServers.isMissing() || channelServers.isNull()) {
            return; // No specific servers defined for this channel, no check needed
        }

        // Check each server reference in the channel against the defined servers
        for (JsonNode server : channelServers.elements()) {
            String serverName = server.stringValue();
            if (!serversNode.propertyMap().containsKey(serverName)) {
                addIssue(CHECK_KEY, translate("AAR040.error"), server.key());
            }
        }
    }

    private JsonNode getRootNode(JsonNode node) {
        JsonNode parentNode = node;
        while (parentNode.getParent() != null) {
            parentNode = (JsonNode) parentNode.getParent();
        }
        return parentNode;
    }
}
