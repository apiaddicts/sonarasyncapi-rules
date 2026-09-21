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

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import apiquality.sonar.asyncapi.checks.BaseCheck;
import apiquality.sonar.asyncapi.utils.AsyncAPIVersionDetector;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.snakeyaml.parser.Tokens;

import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR051OperationIdCamelCaseCheck.CHECK_KEY)
public class AAR051OperationIdCamelCaseCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR051";
    private static final String ERROR_KEY = "AAR051.error";
    private static final Pattern CAMEL_CASE = Pattern.compile("^[a-z][a-zA-Z0-9]*$");
    private static final String OPERATION_ID = "operationId";
    private static final String EXTENSION_PREFIX = "x-";
    private static final Set<String> UNRESOLVED_NULL_SPELLINGS = Sets.newHashSet("~", "Null", "NULL");

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        JsonNode components = rootNode.get("components");
        if (AsyncAPIVersionDetector.isVersion3Plus(rootNode)) {
            checkOperations(rootNode.get("operations"));
            if (isObject(components)) {
                checkOperations(components.get("operations"));
            }
        } else {
            checkChannels(rootNode.get("channels"));
            if (isObject(components)) {
                checkChannels(components.get("channels"));
            }
        }
    }

    private void checkOperations(JsonNode operationsNode) {
        if (!isObject(operationsNode)) return;
        for (JsonNode operation : operationsNode.properties()) {
            JsonNode keyNode = operation.key();
            if (!isObject(operation) || isExtension(keyNode)) continue;
            String operationKey = keyNode.stringValue();
            if (operationKey == null || !CAMEL_CASE.matcher(operationKey).matches()) {
                addIssue(CHECK_KEY, translate(ERROR_KEY), keyNode);
            }
        }
    }

    private void checkChannels(JsonNode channelsNode) {
        if (!isObject(channelsNode)) return;
        for (JsonNode channel : channelsNode.properties()) {
            if (!isObject(channel) || isExtension(channel.key())) continue;
            checkOperation(channel.get("publish"));
            checkOperation(channel.get("subscribe"));
            checkCallbacks(channel.get("callbacks"));
        }
    }

    private void checkCallbacks(JsonNode callbacksNode) {
        if (!isObject(callbacksNode)) return;
        for (JsonNode callback : callbacksNode.properties()) {
            if (!isObject(callback) || isExtension(callback.key())) continue;
            checkOperation(callback.get("publish"));
            checkOperation(callback.get("subscribe"));
        }
    }

    private void checkOperation(JsonNode operation) {
        if (!isObject(operation)) return;

        JsonNode operationIdNode = operation.get(OPERATION_ID);

        if (operationIdNode.isMissing() || isNullValue(operationIdNode)) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), operation.key());
            return;
        }

        if (operationIdNode.getToken().getType() != Tokens.STRING) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), operationIdNode.key());
            return;
        }

        String operationId = operationIdNode.getTokenValue();
        if (operationId == null || !CAMEL_CASE.matcher(operationId).matches()) {
            addIssue(CHECK_KEY, translate(ERROR_KEY), operationIdNode.key());
        }
    }

    private static boolean isNullValue(JsonNode node) {
        if (node.isNull()) {
            return true;
        }
        return node.isScalar() && UNRESOLVED_NULL_SPELLINGS.contains(node.getTokenValue());
    }

    private static boolean isObject(JsonNode node) {
        return node != null && !node.isMissing() && !node.isNull() && node.isObject();
    }

    private static boolean isExtension(JsonNode keyNode) {
        String key = keyNode.stringValue();
        return key != null && key.startsWith(EXTENSION_PREFIX);
    }
}
