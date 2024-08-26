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

import java.util.Set;

@Rule(key = AAR036BadDescriptionCheck.CHECK_KEY)
public class AAR036BadDescriptionCheck extends BaseCheck {
    public static final String CHECK_KEY = "AAR036";
    private static final String MESSAGE = "AAR036.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return Sets.newHashSet(AsyncApiGrammar.ROOT, AsyncApiGrammar.CHANNEL, AsyncApiGrammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (AsyncApiGrammar.ROOT.equals(node.getType())) {
            checkInfoDescription(node);
        }
        if (AsyncApiGrammar.CHANNEL.equals(node.getType())) {
            checkDescriptionFormat(node.get("description"));
        }
        if (AsyncApiGrammar.OPERATION.equals(node.getType())) {
            checkDescriptionFormat(node.get("description"));
        }
    }

    private void checkInfoDescription(JsonNode rootNode) {
        JsonNode infoNode = rootNode.get("info");
        if (infoNode != null) {
            checkDescriptionFormat(infoNode.get("description"));
        }
    }

    private void checkDescriptionFormat(JsonNode descriptionNode) {
        if (descriptionNode == null || descriptionNode.isMissing()) {
            return;
        }
    
        String description = descriptionNode.getTokenValue();
        description = description == null ? "" : description.trim();
    
        if (description.isEmpty()) {
            addIssue(CHECK_KEY, translate(MESSAGE), descriptionNode);
            return;
        }
    
        if (!Character.isUpperCase(description.charAt(0)) || !description.endsWith(".")) {
            addIssue(CHECK_KEY, translate(MESSAGE), descriptionNode);
        }
    }
}
