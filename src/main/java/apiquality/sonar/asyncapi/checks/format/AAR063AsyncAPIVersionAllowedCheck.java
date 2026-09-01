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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v4.AsyncApiGrammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;

import apiquality.sonar.asyncapi.checks.BaseCheck;

@Rule(key = AAR063AsyncAPIVersionAllowedCheck.CHECK_KEY)
public class AAR063AsyncAPIVersionAllowedCheck extends BaseCheck {

    public static final String CHECK_KEY = "AAR063";
    private static final String MESSAGE = "AAR063.error";
    private static final String DEFAULT_ALLOWED_VERSIONS = "2.6.0";

    @RuleProperty(
        key = "allowedVersions",
        description = "Comma-separated list of allowed asyncapi versions",
        defaultValue = DEFAULT_ALLOWED_VERSIONS
    )
    public String allowedVersions = DEFAULT_ALLOWED_VERSIONS;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT);
    }

    @Override
    protected void visitNode(JsonNode rootNode) {
        JsonNode asyncapiNode = rootNode.get("asyncapi");
        if (asyncapiNode.isMissing() || asyncapiNode.isNull()) {
            return;
        }

        String version = asyncapiNode.stringValue();
        if (version == null || version.trim().isEmpty()) {
            return;
        }
        version = version.trim();

        Set<String> allowed = Arrays.stream(allowedVersions.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        if (!allowed.contains(version)) {
            addIssue(CHECK_KEY, translate(MESSAGE), asyncapiNode.key());
        }
    }
}
