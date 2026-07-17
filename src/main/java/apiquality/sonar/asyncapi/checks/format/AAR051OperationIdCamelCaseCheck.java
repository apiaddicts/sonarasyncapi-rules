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
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.snakeyaml.parser.Tokens;

import java.util.Set;
import java.util.regex.Pattern;

@Rule(key = AAR051OperationIdCamelCaseCheck.CHECK_KEY)
public class AAR051OperationIdCamelCaseCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR051";
    private static final Pattern CAMEL_CASE = Pattern.compile("^[a-z][a-zA-Z0-9]*$");

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.OPERATION);
    }

    @Override
    protected void visitNode(JsonNode node) {
        JsonNode operationIdNode = node.at("/operationId");

        if (operationIdNode.isMissing()) {
            addIssue(CHECK_KEY, translate("AAR051.error"), node.key());
            return;
        }

        if (operationIdNode.isNull()) {
            addIssue(CHECK_KEY, translate("AAR051.error"), operationIdNode);
            return;
        }

        if (operationIdNode.getToken().getType() != Tokens.STRING) {
            addIssue(CHECK_KEY, translate("AAR051.error"), operationIdNode);
            return;
        }

        String operationId = operationIdNode.getTokenValue();
        if (operationId == null || !CAMEL_CASE.matcher(operationId).matches()) {
            addIssue(CHECK_KEY, translate("AAR051.error"), operationIdNode);
        }
    }
}
