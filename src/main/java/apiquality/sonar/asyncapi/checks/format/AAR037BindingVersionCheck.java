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
package apiquality.sonar.asyncapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Map;
import java.util.Set;

@Rule(key = AAR037BindingVersionCheck.CHECK_KEY)
public class AAR037BindingVersionCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR037";

    private static final String BINDINGS_KEY = "bindings";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode root) {
        processServers(root.get("servers"));
        processChannels(root.get("channels"));
        processComponents(root.get("components"));
    }

    private void processServers(JsonNode servers) {
        if (isNotPresent(servers)) return;

        for (JsonNode server : servers.propertyMap().values()) {
            checkBindings(server.get(BINDINGS_KEY));
        }
    }

    private void processChannels(JsonNode channels) {
        if (isNotPresent(channels)) return;

        for (JsonNode channel : channels.propertyMap().values()) {
            checkBindings(channel.get(BINDINGS_KEY));
            checkOperationBindings(channel.get("subscribe"));
            checkOperationBindings(channel.get("publish"));
        }
    }

    private void processComponents(JsonNode components) {
        if (isNotPresent(components)) return;

        JsonNode messages = components.get("messages");
        if (isNotPresent(messages)) return;

        for (JsonNode message : messages.propertyMap().values()) {
            checkBindings(message.get(BINDINGS_KEY));
        }
    }

    private void checkOperationBindings(JsonNode operation) {
        if (isNotPresent(operation)) return;

        checkBindings(operation.get(BINDINGS_KEY));
        JsonNode message = operation.get("message");
        if (!isNotPresent(message)) {
            checkBindings(message.get(BINDINGS_KEY));
        }
    }

    private void checkBindings(JsonNode bindingsNode) {
        if (isNotPresent(bindingsNode)) return;

        for (Map.Entry<String, JsonNode> entry : bindingsNode.propertyMap().entrySet()) {
            JsonNode bindingNode = entry.getValue();
            if (bindingNode == null || bindingNode.isMissing()) {
                continue;
            }
            JsonNode versionNode = bindingNode.get("bindingVersion");
            if (isNotPresent(versionNode)) {
                addIssue(CHECK_KEY, translate("AAR037.error"), bindingNode.key());
            }
        }
    }

    private boolean isNotPresent(JsonNode node) {
        return node == null || node.isMissing() || node.isNull();
    }
}