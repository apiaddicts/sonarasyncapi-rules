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
package apiquality.sonar.asyncapi.checks.security;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import apiquality.sonar.asyncapi.checks.BaseCheck;

@Rule(key = AAR018SecuritySchemasCheck.CHECK_KEY)
public class AAR018SecuritySchemasCheck extends BaseCheck {

    public static final String CHECK_KEY = "AAR018";
    private static final String MESSAGE = "AAR018.error";

    private static final String SECURITY_SCHEMES = "oauth2, apiKey";

    @RuleProperty(
        key = "expected-security-scheme",
        description = "Expected security schemes",
        defaultValue = SECURITY_SCHEMES
    )
    private String expectedSecurityScheme = SECURITY_SCHEMES;

    private Set<String> expectedSecuritySchemes;
    private boolean hasGlobalSecurity = false;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(
            AsyncApiGrammar.ROOT,
            AsyncApiGrammar.SECURITY_SCHEME
        );
    }

    @Override
    protected void visitFile(JsonNode root) {
        JsonNode security = root.get("security");
        hasGlobalSecurity = !(security.isMissing() || security.isNull() || security.elements().isEmpty());

        expectedSecuritySchemes = Arrays.stream(expectedSecurityScheme.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        super.visitFile(root);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (node.is(AsyncApiGrammar.SECURITY_SCHEME)) {
            visitSecuritySchemeNode(node);
        }
    }

    private void visitSecuritySchemeNode(JsonNode node) {
        if (hasGlobalSecurity) return;

        JsonNode securityTypeNode = node.get("type");
        if (securityTypeNode.isMissing() || securityTypeNode.isNull()) {
            addIssue(CHECK_KEY, translate(MESSAGE), node.key());
            return;
        }

        String securityType = securityTypeNode.getTokenValue();
        if (!expectedSecuritySchemes.contains(securityType)) {
            addIssue(CHECK_KEY, translate(MESSAGE), node.key());
        }
    }
}
